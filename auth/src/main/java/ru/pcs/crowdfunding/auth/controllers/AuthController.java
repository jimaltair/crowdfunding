package ru.pcs.crowdfunding.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.services.AuthService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable("id") Long id) {
        log.info("get AuthenticationInfo by id {}", id);

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfo = authService.findById(id);

        if (!authenticationInfo.isPresent()) {
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Client with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfo);

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAuthenticationInfo(@RequestBody AuthenticationInfoDto authenticationInfo) {
        AuthenticationInfoDto authenticationInfoDto = authService.createAuthenticationInfo(authenticationInfo);

        ResponseDto response = ResponseDto.buildResponse(true, null, authenticationInfoDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAuthenticationInfo(@PathVariable("id") Long id,
                                                                @RequestBody AuthenticationInfoDto authenticationInfo) {
        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.updateAuthenticationInfo(id, authenticationInfo);

        if (!authenticationInfoDto.isPresent()) {
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't update. Client with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfo);

        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> deleteAuthenticationInfo(@PathVariable("id") Long id) {

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.deleteAuthenticationInfo(id);

        if (!authenticationInfoDto.isPresent()) {
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't delete. Client with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfoDto);

        return ResponseEntity.status(status).body(response);
    }
}
