package ru.pcs.crowdfunding.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.dto.SignInForm;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

import java.util.Arrays;

@RequestMapping("/api/signIn")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SignInController {

    private final AuthenticationService authenticationService;
    private final AuthenticationInfosRepository authenticationInfosRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> signIn(@RequestBody SignInForm signInForm) {
        log.info("Starting 'post /api/signIn/': post 'signInForm' - {}", signInForm.toString());
        AuthenticationInfoDto authenticationInfoDto = authenticationService.signInAuthentication(signInForm);
        ResponseDto response;

        log.info("Check existence email {}", signInForm.getEmail());

        if (!authenticationInfosRepository.findByEmail(signInForm.getEmail()).isPresent()) { // temp
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Email not exists","ERROR MESSAGE"))
                    .build();
            ResponseEntity<ResponseDto> responseBody = ResponseEntity.badRequest().body(response);
            return responseBody;
        }
        response = ResponseDto.builder()
                .data(authenticationInfoDto)
                .success(true)
                .build();

        ResponseEntity<ResponseDto> responseBody = ResponseEntity.ok().body(response);
        return responseBody;
    }

}