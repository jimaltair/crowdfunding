package ru.pcs.crowdfunding.auth.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

import javax.validation.Valid;
import java.util.Arrays;

@RequestMapping("/api/signUp")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> signUp(@RequestBody @Valid AuthenticationInfoDto authenticationInfoDto) {
        log.info("Starting 'post /api/signUp/': post 'authenticationInfoDto' - {}", authenticationInfoDto.toString());
        ResponseDto response;

        if (!authenticationService.existEmailInDb(authenticationInfoDto)) {
            authenticationInfoDto = authenticationService.signUpAuthentication(authenticationInfoDto);
            response = ResponseDto.builder()
                    .data(authenticationInfoDto)
                    .success(true)
                    .build();
            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
            log.info("Finishing 'post /api/signUp/': 'responseBody' - 'status':{}, 'body': {} "
                    , responseBody.getStatusCode(), responseBody.getBody().getData());
            return responseBody;
        }

        log.error("Error 'email' - {} already exists", authenticationInfoDto.getEmail());
        response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList("Email already exists","ERROR MESSAGE"))
                .build();
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        log.info("Finishing 'post /api/signUp/': 'responseBody' - {} , {} ", responseBody.getStatusCode(), responseBody.getBody().getError());
        return responseBody;
    }

}