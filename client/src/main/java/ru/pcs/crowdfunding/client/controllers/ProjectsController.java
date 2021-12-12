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
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.security.JwtTokenProvider;
import ru.pcs.crowdfunding.client.services.ClientsService;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Controller
/** А почему не RestController? */
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectsController {

    private final ProjectsService projectsService;
    private final JwtTokenProvider tokenProvider;

    @GetMapping(value = "/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        log.info("Starting 'get /projects/{id}': get 'id' = {}", id);

        Optional<ProjectDto> project = projectsService.findById(id);
        if (!project.isPresent()) {
            log.error("Project with 'id' - {} didn't found", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id " + id + " not found");
        }

        log.debug("Finishing 'get /projects/{id}': result = {}", project.get());

        model.addAttribute("project", project.get());
        return "projectCard";
    }

    @GetMapping(value = "/create")
    public String getProjectCreatePage(Model model) {
        log.info("Starting 'get /projects/create'");
        model.addAttribute("projectCreatedForm", new ProjectForm());
        return "createProject";
    }

    @PostMapping(value = "/create")
    public String createProject(@Valid ProjectForm form, BindingResult result, Model model, HttpServletRequest request,
                                @RequestParam("file") MultipartFile file) {
        log.info("Starting 'post /projects/create': post 'form' - {}, 'result' - {}", form.toString(), result.toString());
        if (result.hasErrors()) {
            log.error("Can't create new project, 'result' has error(s) - {}", result.getAllErrors());
            model.addAttribute("projectCreatedForm", form);
            return "createProject";
        }

        Long clientId;

        try {
            String token = getTokenFromCookie(request);
            clientId = tokenProvider.getClientIdFromToken(token);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "createProject";
        }

        form.setClientId(clientId);

        Optional<Long> projectId = projectsService.createProject(form, file);
        if (!projectId.isPresent()) {
            log.error("Unable to create project");
            throw new IllegalStateException("Unable to create project");
        }
        log.info("Finishing 'post /projects/create': with 'id' - {}", projectId.get());
        return "redirect:/projects/" + projectId.get();
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
        /** Хочу видеть логи тут о том что планируется сделать с входящими параметрами */
        model.addAttribute("projectUpdatedForm", new ProjectForm());
        Optional<ProjectDto> currentProject = projectsService.findById(id);
        if(!currentProject.isPresent()){
            return "createProject";
        }
        ProjectDto project = currentProject.get();
        model.addAttribute("id", id);
        model.addAttribute("project_title", project.getTitle());
        model.addAttribute("project_description", project.getDescription());
        model.addAttribute("project_money_goal", project.getMoneyGoal().toString());

        String finishDate = project.getFinishDate().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        model.addAttribute("finish_date", finishDate);
        return "updateProject";
    }

    @PostMapping(value = "/update/{id}")
    public String updateProject(@Valid ProjectForm form, @PathVariable("id") Long id, BindingResult result, Model model,
                                @RequestParam("file") MultipartFile file) {
        if (result.hasErrors()) {
            model.addAttribute("projectUpdatedForm", form);
        }
        ProjectDto updatedProject = projectsService.updateProject(id, form, file);

        updatedProject.setMoneyCollected(projectsService.getMoneyCollectedByProjectId(id));
        updatedProject.setContributorsCount(projectsService.getContributorsCountByProjectId(id));

        model.addAttribute("project", updatedProject);
        return "redirect:/projects/" + id;
    }


    //Временный метод до запуска Spring Security и получения id пользователя из контекста
    private String getTokenFromCookie(HttpServletRequest request) throws IllegalAccessException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SignUpController.TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        throw new IllegalAccessException("Not enough rights for this operation");
    }
}