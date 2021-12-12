package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.exceptions.ImageProcessingError;
import ru.pcs.crowdfunding.client.services.ClientsService;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientsService clientsService;
    private final ProjectsService projectsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable Long id, Model model) {
        log.info("Starting 'get /clients/{id}': get 'id' - {}", id);

        Optional<ClientDto> client = clientsService.findById(id);
        if (!client.isPresent()) {
            log.error("Client with 'id' - {} didn't found", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }
        List<ProjectDto> projectDtos = projectsService.getProjectsFromClient(client.get());
        log.info("Finishing 'get /clients/{id}': result = {}", client.get());
        model.addAttribute("clientDto", client.get());
        model.addAttribute("projectDtos", projectDtos);

        // добавляем атрибут "clientForm" с данными из "clientDto",
        // чтобы заполнились нужные поля во вкладке обновления клиента.
        model.addAttribute("clientForm", ClientForm.builder()
                .firstName(client.get().getFirstName())
                .lastName(client.get().getLastName())
                .country(client.get().getCountry())
                .city(client.get().getCity())
                .build());

        return "profile_page";
    }

    @PostMapping(value = "/{id}")
    public String update(@PathVariable Long id, @Valid ClientForm form, BindingResult bindingResult, Model model,
                         @RequestParam("file") MultipartFile file) {
        log.info("update by id = {}", id);

        try {
            if (!bindingResult.hasErrors()) {
                clientsService.updateClient(id, form, file);
                return "redirect:/clients/" + id;
            }
        } catch (ImageProcessingError e) {
            log.warn("Caught ImageProcessingError exception");
            model.addAttribute("imageProcessingError", Boolean.TRUE);
        }

        Optional<ClientDto> client = clientsService.findById(id);
        if (client.isPresent()) {
            model.addAttribute("clientDto", client.get());
            model.addAttribute("projectDtos", projectsService.getProjectsFromClient(client.get()));
        }
        model.addAttribute("clientForm", form);
        return "profile_page";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") Long id) {
        log.info("get client image by id {}", id);

        Optional<ImageDto> imageDto = clientsService.getImageById(id);
        if (!imageDto.isPresent()) {
            log.error("client image with id {} not found", id);
            return ResponseEntity.notFound().build();
        }

        log.info("found client image with id {}, format {} and content length {}",
                id, imageDto.get().getFormat(), imageDto.get().getContent().length);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/" + imageDto.get().getFormat()));
        headers.setContentLength(imageDto.get().getContent().length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageDto.get().getContent());
    }

}
