package ru.pcs.crowdfunding.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.services.AuthService;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable("id") Long id) {

        log.info("Starting 'get /api/auth/{id}: get 'id' - {}", id);
        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfo = authService.findById(id);

        if (!authenticationInfo.isPresent()) {
            log.error("Client with 'id' - {} didn't found", id);
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Client with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfo);
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'get/api/auth/{id}': 'responseBody' - 'status':{}, 'body': {} "
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAuthenticationInfo(@RequestBody AuthenticationInfoDto authenticationInfo) {
        log.info("Starting 'post /api/auth': post 'authenticationInfo' - {} ", authenticationInfo.toString());
        Optional<AuthenticationInfo> authenticationInfoDto;

        try {
            authenticationInfoDto = authService.createAuthenticationInfo(authenticationInfo);
        } catch (IllegalArgumentException e) {

            ResponseDto response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(e.getMessage()))
                .build();

            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            return responseBody;
        }
        ResponseDto response = ResponseDto.buildResponse(true, null, authenticationInfoDto);
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
        log.info("Finishing 'post /api/auth/': 'responseBody' - 'status':{}, 'body': {} "
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAuthenticationInfo(@PathVariable("id") Long id,
                                                                           @RequestBody AuthenticationInfoDto authenticationInfo) {
        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.updateAuthenticationInfo(id, authenticationInfo);
        if (!authenticationInfoDto.isPresent()) {
            log.error("Can't update. Client with 'id' - {} didn't found", id);
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't update. Client with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfo);
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'put /api/auth/{id}': 'responseBody' - 'status':{}, 'body': {} "
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> deleteAuthenticationInfo(@PathVariable("id") Long id) {
        log.info("Starting 'delete /api/auth/{id}': delete 'id' - {}", id);

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.deleteAuthenticationInfo(id);

        if (!authenticationInfoDto.isPresent()) {
            log.error("Can't delete. Client with 'id' - {} didn't found", id);
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't delete. Client with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfoDto);
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'delete /api/auth/{id}': 'responseBody' - 'status':{}, 'body': {} "
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }
}
