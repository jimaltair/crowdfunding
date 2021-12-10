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

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomePageController {

    private final ProjectsService projectsService;

    @Value(value = "${uriSignUp}")
    private String uriSignUpPage;

    @Value(value = "${uriSignInPage}")
    private String uriSignInPage;

    @Value(value = "${uriLogout}")
    private String uriLogout;

    @RequestMapping()
    public String getHomePage(Model model) {

        List<ProjectDto> page = projectsService.getConfirmedProjects();

        model.addAttribute("ListProject", page);
        model.addAttribute("SignUpUri", uriSignUpPage);
        model.addAttribute("SignInUri", uriSignInPage);
        model.addAttribute("Logout", uriLogout);

        return "homePage";
    }
}
