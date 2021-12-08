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

@RestController
@RequestMapping("/api/signIn")
@RequiredArgsConstructor
@Slf4j
public class SignInController {
    private final AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> signIn(@RequestBody @Valid AuthenticationInfoDto authenticationInfoDto) {
        log.info("Starting 'post /api/signIn/': post 'authenticationInfoDto' - {}", authenticationInfoDto.toString());
        ResponseDto response;

        if (authenticationService.existEmailInDb(authenticationInfoDto)) {
            authenticationInfoDto = authenticationService.signUpAuthentication(authenticationInfoDto);
            response = ResponseDto.builder()
                    .data(authenticationInfoDto)
                    .success(true)
                    .build();
            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
            log.info("Finishing 'post /api/signIn/': 'responseBody' - 'status':{}, 'body': {} "
                    , responseBody.getStatusCode(), responseBody.getBody().getData());
            return responseBody;
        }

        response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList("Email not exists","ERROR MESSAGE"))
                .build();
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.badRequest().body(response);
        log.info("Finishing 'post /api/signIn/': 'responseBody' - {} , {} ", responseBody.getStatusCode(), responseBody.getBody().getError());
        return responseBody;
    }
}
