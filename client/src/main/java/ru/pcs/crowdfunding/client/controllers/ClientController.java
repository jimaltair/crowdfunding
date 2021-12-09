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
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.services.ClientsService;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientsService clientsService;
    private final ProjectsService projectsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable Long id, Model model) {
        log.info("get by id = {}", id);

        Optional<ClientDto> client = clientsService.getById(id);
        if (!client.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }
        List<Project> projects = client.get().getProjects();
        List<ProjectDto> projectDtos = projects.stream().map(project -> {
            Optional<ProjectDto> projectDto = projectsService.findById(project.getId());
            if (!projectDto.isPresent()) {
                log.error("Project didn't found");
                throw new IllegalArgumentException("Project didn't found");
            }
            return projectDto.get();}).collect(Collectors.toList());
        log.debug("result = {}", client.get());

        model.addAttribute("clientDto", client.get());
        model.addAttribute("projectsDtos", projectDtos);

        return "profile_page";
    }


}
