package ru.pcs.crowdfunding.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public AuthenticationInfoDto getById(@PathVariable("id") Long id) {
        log.info("get AuthenticationInfo by id {}", id);
        Optional<AuthenticationInfoDto> authenticationInfo = authService.findById(id);
        if (!authenticationInfo.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
        }
        log.debug("result = {}", authenticationInfo.get());
        return authenticationInfo.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationInfoDto addAuthenticationInfo(@RequestBody AuthenticationInfoDto authenticationInfo) {
        return authService.addAuthenticationInfo(authenticationInfo);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthenticationInfoDto updateAuthenticationInfo(@PathVariable("id") Long id, @RequestBody AuthenticationInfoDto authenticationInfo){
        return authService.updateAuthenticationInfo(id, authenticationInfo);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthenticationInfoDto deleteAuthenticationInfo(@PathVariable("id") Long id){
        return authService.deleteAuthenticationInfo(id);
    }
}
