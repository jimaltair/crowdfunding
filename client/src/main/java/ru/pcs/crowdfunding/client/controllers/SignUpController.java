package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.dto.ResponseDto;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.services.SignUpService;

import javax.validation.Valid;
import java.util.Arrays;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signUp")
public class SignUpController {
    private final SignUpService signUpService;

    @RequestMapping()
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "signUp";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> signUp(@Valid SignUpForm form) {
        if (form == null) {
            return ResponseEntity.badRequest().body(ResponseDto.builder()
                    .data(form)
                    .success(false)
                    .error(Arrays.asList("Registration form is empty"))
                    .build());
        }
        signUpService.signUp(form);
        return ResponseEntity.ok(ResponseDto.builder()
                .data(form)
                .success(true)
                .build());
    }
}
