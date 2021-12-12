package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pcs.crowdfunding.client.dto.AuthSignInRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignInResponse;
import ru.pcs.crowdfunding.client.services.SignInService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@RequiredArgsConstructor
@Controller
/** А почему не RestController? */
@Slf4j
@RequestMapping("/signIn")
public class SignInController {

    private final SignInService signInService;
    private static final String TOKEN_COOKIE_NAME = "accessToken";

    @GetMapping
    public String getSignInPage(Model model) {
        model.addAttribute("authSignInRequest", new AuthSignInRequest());
        return "signIn";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signIn(@Valid AuthSignInRequest authSignInRequest,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authSignInRequest", authSignInRequest);
            log.error("Incorrect data sign in, 'bindingResult' has error(s) - {}", bindingResult.getAllErrors());
            return "signIn";
        }

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