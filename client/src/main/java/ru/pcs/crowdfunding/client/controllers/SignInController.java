package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.dto.AuthSignInRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignInResponse;
import ru.pcs.crowdfunding.client.dto.SignInForm;


@RequiredArgsConstructor
@Controller
@RequestMapping("/signIn")
public class SignInController {

    private final AuthorizationServiceClient authorizationServiceClient;

    @GetMapping
    public String getSignInPage(Model model) {
        model.addAttribute("signInForm", new SignInForm());
        return "signIn";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signIn(SignInForm form) {

        AuthSignInRequest request = AuthSignInRequest.builder()
                .email(form.getEmail())
                .password(form.getPassword())
                .build();
        AuthSignInResponse response = authorizationServiceClient.signIn(request);
        return "redirect:/clients/1"; //пока захардкодила, правильный id не добрасывается

    }

}
