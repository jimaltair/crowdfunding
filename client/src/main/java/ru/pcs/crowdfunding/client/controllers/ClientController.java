package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.services.ClientsService;

import javax.validation.Valid;
import java.util.Optional;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны, например
 *
 * @Slf4j
 * @Controller
 * @RequiredArgsConstructor
 * @RequestMapping("/clients")
 */
@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientsService clientsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable Long id, Model model) {
        log.info("Starting 'get /clients/{id}': get 'id' - {}", id);

        Optional<ClientDto> client = clientsService.findById(id);
        if (!client.isPresent()) {
            log.error("Client with 'id' - {} didn't found", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found"); /** Есть предположение что использовать канкатенацию плохой подход, лучше как минимум String.format() */
        }

        log.info("Finishing 'get /clients/{id}': result = {}", client.get());
        model.addAttribute("clientDto", client.get());

        return "profile_page";
    }

    @PostMapping(value = "/{id}")
    public String update(@PathVariable Long id, @Valid ClientForm form, Model model,
                         @RequestParam("file") MultipartFile file) {
        log.info("update by id = {}", id);

        ClientForm newForm = clientsService.updateClient(id, form, file);

        model.addAttribute("clientDto", newForm);

        return "redirect:/clients/" + id;
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