package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.services.ClientsService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientsService clientsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable Long id, Model model) {
        log.info("get by id = {}", id);

        Optional<ClientDto> client = clientsService.getById(id);
        if (!client.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }
        log.debug("result = {}", client.get());

        model.addAttribute("clientDto", client.get());

        return "profile_page";
    }

    @PutMapping(value = "/{id}/update")
    public String update(@PathVariable Long id, @Valid ClientForm form, Model model,
                             @RequestParam("file") MultipartFile file) {
        clientsService.updateClient(id, form, file);

        model.addAttribute("clientDto", form);

        return "profile_page";
    }

}
