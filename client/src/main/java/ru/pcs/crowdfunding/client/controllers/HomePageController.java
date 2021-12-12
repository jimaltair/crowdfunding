package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import java.util.List;

@Slf4j
@Controller
/** А почему не RestController? */
@RequestMapping("/")
@RequiredArgsConstructor

public class HomePageController {

    private final ProjectsService projectsService;

    @Value(value = "${uriSignUp}")
    private String uriSignUpPage;

    @Value(value = "${uriSignInPage}")
    private String uriSignInPage;

    @Value(value = "${uriLogout}")
    private String uriLogout;

    @Value(value = "${uriProjects}")
    private String uriProjects;

    @RequestMapping()
    public String getHomePage(Model model) {
        /** Хочу видеть логи тут о том что планируется сделать с входящими параметрами */
        List<ProjectDto> page = projectsService.getConfirmedProjects();

        model.addAttribute("ListProject", page);
        model.addAttribute("SignUpUri", uriSignUpPage);
        model.addAttribute("SignInUri", uriSignInPage);
        model.addAttribute("ProjectsUri", uriProjects);
        model.addAttribute("Logout", uriLogout);
        /** Хочу видеть логи тут о том что сделано */
        return "homePage";
    }

}