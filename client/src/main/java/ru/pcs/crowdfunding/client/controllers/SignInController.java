package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pcs.crowdfunding.client.dto.AuthSignInRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignInResponse;
import ru.pcs.crowdfunding.client.dto.SignInForm;
import ru.pcs.crowdfunding.client.services.SignInService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/signIn")
public class SignInController {

    private final SignInService signInService;
    private static final String TOKEN_COOKIE_NAME = "accessToken";

    @GetMapping
    public String getSignInPage(Model model) {
        model.addAttribute("signInForm", new SignInForm());
        return "signIn";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signIn(AuthSignInRequest authSignInRequest, HttpServletResponse response) {
        log.info("Введен email {} ", authSignInRequest.getEmail());

        AuthSignInResponse responseForm = signInService.signIn(authSignInRequest);
        log.info("UserId is {}, accessToken {} ", responseForm.getUserId(), responseForm.getAccessToken());
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, responseForm.getAccessToken());
        response.addCookie(cookie);

        if (responseForm.getUserId() == 0L) {
            return "redirect:/signIn?error";
        }
        return "redirect:/clients/" + responseForm.getUserId();

    }

}
