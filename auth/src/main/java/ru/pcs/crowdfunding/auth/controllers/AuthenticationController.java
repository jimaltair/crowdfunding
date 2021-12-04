package ru.pcs.crowdfunding.auth.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/api/signUp")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> signUp(@RequestBody @Valid AuthenticationInfoDto authenticationInfoDto) {
        if (!authenticationService.existEmailInDb(authenticationInfoDto)) {
            authenticationInfoDto = authenticationService.signUpAuthentication(authenticationInfoDto);
            return ResponseEntity.ok(ResponseDto.builder()
                    .data(authenticationInfoDto)
                    .success(true)
                    .build());
        }
        return ResponseEntity.badRequest().body(ResponseDto.builder()
                .success(false)
                .error(Arrays.asList("Email already exists","ERROR MESSAGE"))
                .build());
    }
}
