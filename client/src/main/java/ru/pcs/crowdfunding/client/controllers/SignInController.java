package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pcs.crowdfunding.client.dto.SignInForm;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.services.ClientsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @RequestMapping(method = RequestMethod.POST)
    public String signIn(SignInForm form, BindingResult bindingResult, Model model,
                         HttpServletResponse response) {


        return "redirect:/clients/" + form.getId();

        // if ()
    }

}
