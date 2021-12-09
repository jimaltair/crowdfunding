package ru.pcs.crowdfunding.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.dto.SignInForm;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

import java.util.Arrays;

@RestController
@RequestMapping("/api/signIn")
@RequiredArgsConstructor
@Slf4j
public class SignInController {
    private final AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> signIn(@RequestBody SignInForm signInForm) {
        log.info("Starting 'post /api/signIn/': post 'signInForm' - {}", signInForm.toString());
        ResponseDto response;

        // пока проверяет только наличие email в базе, т.е. запрос добрасывается

        if (!authenticationService.signInAuthentication(signInForm)) {
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Email not exists","ERROR MESSAGE"))
                    .build();
            ResponseEntity<ResponseDto> responseBody = ResponseEntity.badRequest().body(response);
            return responseBody;
        }

        response = ResponseDto.builder()
                .data(signInForm)
                .success(true)
                .build();

        ResponseEntity<ResponseDto> responseBody = ResponseEntity.ok().body(response);

        return responseBody;
    }
}
