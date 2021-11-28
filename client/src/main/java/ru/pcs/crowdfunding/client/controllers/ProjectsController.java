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
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import java.util.Optional;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectsController {

    private final ProjectsService projectsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        log.info("get by id = {}", id);

        Optional<ProjectDto> project = projectsService.findById(id);
        if (!project.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id " + id + " not found");
        }

        log.debug("result = {}", project.get());

        model.addAttribute("project", project.get());
        return "projectCard";
    }
}
