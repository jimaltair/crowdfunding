package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.services.SignUpService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signUp")
public class SignUpController {
    private final SignUpService signUpService;

    @GetMapping()
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "signUp";
    }

    @PostMapping
    public String signUp(@Valid SignUpForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpForm", form);
            return "signUp";
        }

        form = signUpService.signUp(form);

        return "redirect:/clients/" + form.getId();
    }
}
