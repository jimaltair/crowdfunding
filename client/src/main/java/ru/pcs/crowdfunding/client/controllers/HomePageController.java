package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.services.ProjectsService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
@Slf4j
public class HomePageController {

    private final ProjectsService projectsService;

    @RequestMapping()
    public String getHomePage(Model model) {
//        List<Project> page = projectsService.getConfirmedProjects().stream().collect(Collectors.toList());
//        model.addAttribute("ListProject", page);
        return "homePage";
    }
}
