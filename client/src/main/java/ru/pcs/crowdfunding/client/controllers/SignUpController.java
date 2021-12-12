package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.exceptions.EmailAlreadyExistsError;
import ru.pcs.crowdfunding.client.services.SignUpService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signUp")
@Slf4j
public class SignUpController {

    public static final String TOKEN_COOKIE_NAME = "accessToken";

    private final SignUpService signUpService;

    @GetMapping()
    public String getSignUpPage(Model model) {
        log.info("Starting 'get /signUp'");
        model.addAttribute("signUpForm", new SignUpForm());
        return "newSignUp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signUp(@Valid SignUpForm form, BindingResult bindingResult, Model model,
                         HttpServletResponse response) {
        log.info("Starting 'post /signUp': post 'form' - {}, 'bindingResult' - {}, 'response' - {}"
                ,form.toString(), bindingResult.toString(), response.getStatus());
        if (bindingResult.hasErrors()) {
            log.error("Can't create new account, 'bindingResult' has error(s) - {}", bindingResult.getAllErrors());
            model.addAttribute("signUpForm", form);
            return "newSignUp";
        }

        try {
            form = signUpService.signUp(form);

            Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, form.getAccessToken());
            response.addCookie(cookie);
            log.info("Finishing 'post /signUp': post 'form' - {}, 'cookie' - {}", form, cookie);
            return "redirect:/clients/" + form.getId();
        } catch (EmailAlreadyExistsError e) {
            log.info("Caught EmailAlreadyExistsError exception");

            model.addAttribute("signUpForm", form);
            model.addAttribute("emailAlreadyExists", Boolean.TRUE);

            return "newSignUp";
        }
    }
}
