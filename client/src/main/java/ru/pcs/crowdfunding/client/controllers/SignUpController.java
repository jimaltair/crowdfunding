package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.services.SignUpService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signUp")
public class SignUpController {

    private static final String TOKEN_COOKIE_NAME = "accessToken";

    private final SignUpService signUpService;

    @GetMapping()
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "signUp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signUp(@Valid SignUpForm form, BindingResult bindingResult, Model model,
                         HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "signUp";
        }

        form = signUpService.signUp(form);

        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, form.getAccessToken());
        response.addCookie(cookie);

        return "redirect:/clients/" + form.getId();
    }
}
