package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.services.ClientsService;

import java.util.Optional;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientsService clientsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable Long id, Model model) {
        log.info("Starting 'get /clients/{id}': get 'id' - {}", id);

        Optional<ClientDto> client = clientsService.getById(id);
        if (!client.isPresent()) {
            log.error("Client with 'id' - {} didn't found", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }

        log.info("Finishing 'get /clients/{id}': result = {}", client.get());
        model.addAttribute("clientDto", client.get());

        return "profile_page";
    }


}
