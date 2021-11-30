package ru.pcs.crowdfunding.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.services.AuthService;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<AuthenticationInfoDto> getById(@PathVariable("id") Long id) {
        log.info("get AuthenticationInfo by id {}", id);
        Optional<AuthenticationInfoDto> authenticationInfo = authService.findById(id);
        if (!authenticationInfo.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }
        log.debug("result = {}", authenticationInfo.get());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticationInfo.get());
    }

    @PostMapping
    public ResponseEntity<AuthenticationInfoDto> addAuthenticationInfo(@RequestBody AuthenticationInfoDto authenticationInfo) {
        AuthenticationInfoDto authenticationInfoDto = authService.addAuthenticationInfo(authenticationInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationInfoDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AuthenticationInfoDto> updateAuthenticationInfo(@PathVariable("id") Long id, @RequestBody AuthenticationInfoDto authenticationInfo) {
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.updateAuthenticationInfo(id, authenticationInfo);
        if (!authenticationInfoDto.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't update. Client with id " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticationInfoDto.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AuthenticationInfoDto> deleteAuthenticationInfo(@PathVariable("id") Long id) {
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.deleteAuthenticationInfo(id);
        if (!authenticationInfoDto.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't delete. Client with id " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticationInfoDto.get());
    }
}
