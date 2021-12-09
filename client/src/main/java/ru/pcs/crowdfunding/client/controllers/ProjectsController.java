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
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import javax.validation.Valid;
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

    @GetMapping(value = "/create")
    public String getProjectCreatePage(Model model) {
        model.addAttribute("projectCreatedForm", new ProjectForm());
        return "createProject";
    }

    @PostMapping(value = "/create")
    public String createProject(@Valid ProjectForm form, BindingResult result, Model model,
                                @RequestParam("file") MultipartFile file) {

        if (result.hasErrors()) {
            model.addAttribute("projectCreatedForm", form);
            return "createProject";
        }

        Optional<Long> projectId = projectsService.createProject(form, file);
        if (!projectId.isPresent()) {
            throw new IllegalStateException("Unable to create project");
        }

        return "redirect:/projects/" + projectId.get();
    }

    @GetMapping(value = "/update/{id}")
    public String getProjectUpdatePage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("projectUpdatedForm", new ProjectForm());
        Optional<ProjectDto> currentProject = projectsService.findById(id);
        if(!currentProject.isPresent()){
            // если по запрошенному id-шнику ничего нет, то возвращаю страницу создания проекта -
            // пока ничего лучше в голову не пришло
            return "createProject";
        }
        ProjectDto project = currentProject.get();
        model.addAttribute("id", id);
        model.addAttribute("project_title", project.getTitle());
        model.addAttribute("project_description", project.getDescription());
        model.addAttribute("project_money_goal", project.getMoneyGoal().toString());
        model.addAttribute("finish_date", project.getFinishDate().toString());
        return "updateProject";
    }

    @PostMapping(value = "/update/{id}")
    public String updateProject(@Valid ProjectForm form, @PathVariable("id") Long id, BindingResult result, Model model,
                                @RequestParam("file") MultipartFile file) {
        if (result.hasErrors()) {
            model.addAttribute("projectUpdatedForm", form);
        }
        projectsService.updateProject(id, form, file);
        // здесь должна прилететь страница проекта, когда будет готова
        return "projectCard";
    }
}
