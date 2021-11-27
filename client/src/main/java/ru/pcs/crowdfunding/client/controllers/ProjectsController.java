package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import java.util.Optional;

import static ru.pcs.crowdfunding.client.dto.ProjectDto.from;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectsController {

    private final ProjectsService projectsService;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        log.info("id = {}", id);

        Optional<Project> project = projectsService.findById(id);
        if (project.isPresent()) {
            ProjectDto projectDto = from(project.get());
            model.addAttribute("project", projectDto);

            log.debug("result = {}", projectDto);
        } else {
            log.error("project with id = {} not found", id);
        }

        return "projectCard";
    }
}
