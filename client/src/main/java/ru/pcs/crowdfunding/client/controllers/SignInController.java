package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pcs.crowdfunding.client.dto.SignInForm;
import ru.pcs.crowdfunding.client.services.ClientsService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signIn")
public class SignInController {

    private final ClientsService clientsService;

    @GetMapping
    public String getSignInPage(Model model) {
        model.addAttribute("signInForm", new SignInForm());
        return "signIn";
    }

    @PostMapping
    public String signIn(SignInForm form) {

        String email = form.getEmail();
        String password = form.getPassword();

        return "redirect:/clients/{id}";

        // if ()
    }

}
