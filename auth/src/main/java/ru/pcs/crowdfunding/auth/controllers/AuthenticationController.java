package ru.pcs.crowdfunding.auth.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

@RestController
@RequestMapping("/signUp ?????????")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST, value = "/signUp ????????")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(Long userId, String password, String email, BindingResult result, Model model) {
        if(userId == null || email == null || password == null){
            return "signUp";
        }

        AuthenticationInfoDto authenticationInfoDto = AuthenticationInfoDto.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .build();

        if (result.hasErrors()) {
            model.addAttribute("authenticationInfoDto",authenticationInfoDto);
            return "signUp";
        }

        authenticationService.signUpAuthentication(authenticationInfoDto);
        return "redirect:/login";
    }
}
