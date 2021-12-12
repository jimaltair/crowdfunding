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
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.security.CrowdfundingUtils;
import ru.pcs.crowdfunding.client.security.JwtTokenProvider;
import ru.pcs.crowdfunding.client.services.ClientsService;
import ru.pcs.crowdfunding.client.exceptions.ImageProcessingError;
import ru.pcs.crowdfunding.client.exceptions.DateMustBeFutureError;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectsController {

    private final ProjectsService projectsService;
    private final JwtTokenProvider tokenProvider;
    private final ClientsService clientsService;

    @GetMapping(value = "/{id}")
    public String getProjectPageById(@PathVariable("id") Long id, Model model) {
        log.info("Starting 'get /projects/{id}': get 'id' = {}", id);

        Optional<ProjectDto> project = projectsService.findById(id);
        if (!project.isPresent()) {
            log.error("Project with 'id' - {} didn't found", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id " + id + " not found");
        }

        Optional<Client> clientProjectAuthor = clientsService.findByProject(project.get());
        if (!clientProjectAuthor.isPresent()) {
            log.error("Client didn't found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client didn't found");
        }

        long percent = getPercentForProgressBar(project);

        LocalDate startDate = project.get().getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate();
        int finish = getDaysLeft(project);

        Optional<Long> clientId = CrowdfundingUtils.getClientIdFromRequestContext();
        if (!clientId.isPresent()) {
            log.warn("Can't get user id from RequestContext");
            return "newCreateProject";
        }
        log.info("Get clientId = {} from secutity context", clientId.get());
        Long tokenClientId = clientId.get();

        log.debug("Finishing 'get /projects/{id}': result = {}", project.get());

        model.addAttribute("project", project.get());
        model.addAttribute("client", clientProjectAuthor.get());
        model.addAttribute("size", clientProjectAuthor.get().getProjects().size());
        model.addAttribute("percent", percent);
        model.addAttribute("startDate", startDate);
        model.addAttribute("finishDate", finish);
        model.addAttribute("userId", tokenClientId);
        return "newProjectCard";
    }

    private int getDaysLeft(Optional<ProjectDto> project) {
        LocalDate finishDate = project.get().getFinishDate().atZone(ZoneOffset.UTC).toLocalDate();
        int finish = finishDate.compareTo(LocalDate.now());
        return finish;
    }

    private long getPercentForProgressBar(Optional<ProjectDto> project) {
        double moneyCollected = project.get().getMoneyCollected().doubleValue();
        double moneyGoal = project.get().getMoneyGoal().doubleValue();
        long percent = (long) ((moneyCollected / moneyGoal) * 100);
        return percent;
    }

    @GetMapping(value = "/create")
    public String getProjectCreatePage(Model model) {
        log.info("Starting 'get /projects/create'");
        model.addAttribute("projectForm", new ProjectForm());
        return "newCreateProject";
    }

    @PostMapping(value = "/create")
    public String createProject(@Valid ProjectForm form, BindingResult result, Model model,
                                @RequestParam("file") MultipartFile file) {
        log.info("Starting 'post /projects/create': post 'form' - {}, 'result' - {}", form.toString(), result.toString());
        if (result.hasErrors()) {
            log.error("Can't create new project, 'result' has error(s) - {}", result.getAllErrors());
            model.addAttribute("projectForm", form);
            return "newCreateProject";
        }

        Optional<Long> clientId = CrowdfundingUtils.getClientIdFromRequestContext();
        if (!clientId.isPresent()) {
            log.warn("Can't get user id from RequestContext");
            return "createProject";
        }
        log.info("Get clientId = {} from secutity context", clientId.get());
        form.setClientId(clientId.get());

        try {
            Optional<Long> projectId = projectsService.createProject(form, file);
            if (!projectId.isPresent()) {
                log.error("Unable to create project");
                throw new IllegalStateException("Unable to create project");
            }
            log.info("Finishing 'post /projects/create': with 'id' - {}", projectId.get());
            return "redirect:/projects/" + projectId.get();
        } catch (ImageProcessingError e) {
            log.warn("Caught ImageProcessingError exception");
            model.addAttribute("imageProcessingError", Boolean.TRUE);
        } catch (DateMustBeFutureError e) {
            log.warn("Caught MustBeFutureError exception");
            model.addAttribute("dateMustBeFutureError", Boolean.TRUE);
        }

        model.addAttribute("projectForm", form);
        return "newCreateProject";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") Long id) {
        log.info("get project image by id {}", id);

        Optional<ImageDto> imageDto = projectsService.getImageById(id);
        if (!imageDto.isPresent()) {
            log.error("project image with id {} not found", id);
            return ResponseEntity.notFound().build();
        }

        log.info("found project image with id {}, format {} and content length {}",
                id, imageDto.get().getFormat(), imageDto.get().getContent().length);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/" + imageDto.get().getFormat()));
        headers.setContentLength(imageDto.get().getContent().length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageDto.get().getContent());
    }

    @GetMapping(value = "/update/{id}")
    public String getProjectUpdatePage(@PathVariable("id") Long id, Model model) {
        Optional<ProjectDto> currentProject = projectsService.findById(id);
        if(!currentProject.isPresent()){
            model.addAttribute("projectForm", new ProjectForm());
            return "newCreateProject";
        }

        ProjectDto project = currentProject.get();
        model.addAttribute("id", id);

        ProjectForm projectForm = ProjectForm.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .moneyGoal(project.getMoneyGoal().toString())
                .finishDate(project.getFinishDate()
                        .atOffset(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
        model.addAttribute("projectForm", projectForm);

        return "newUpdateProject";
    }

    @PostMapping(value = "/update/{id}")
    public String updateProject(@PathVariable("id") Long id, @Valid ProjectForm form, BindingResult result, Model model,
                                @RequestParam("file") MultipartFile file) {
        try {
            if (!result.hasErrors()) {
                projectsService.updateProject(id, form, file);
                return "redirect:/projects/" + id;
            }
        } catch (ImageProcessingError e) {
            log.warn("Caught ImageProcessingError exception");
            model.addAttribute("imageProcessingError", Boolean.TRUE);
        } catch (DateMustBeFutureError e) {
            log.warn("Caught DateMustBeFutureError exception");
            model.addAttribute("dateMustBeFutureError", Boolean.TRUE);
        }

        model.addAttribute("id", id);
        model.addAttribute("projectForm", form);
        return "newUpdateProject";
    }
}
