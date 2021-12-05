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

        log.info("Запускается метод 'getById' с параметром 'id' - {}", id);

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfo = authService.findById(id);

        log.info("Создан 'AuthenticationInfoDto' - {} с запуском 'authService'", authenticationInfo);

        if (!authenticationInfo.isPresent()) {

            log.warn("Не удалось создать 'AuthenticationInfoDto'" +
                    ", так как 'AuthenticationInfo' с id - {} не существует", id);

            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Client with id " + id + " not found";

        }

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfo);

        log.info("Создан новый 'ResponseDto  - {} с 'isSuccess' - {}", response.getData(), response.isSuccess());
        log.info("Завершен метод 'getById' с параметром 'id' - {}", id);

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAuthenticationInfo(@RequestBody AuthenticationInfoDto authenticationInfo) {

        log.info("Запускается метод 'createAuthenticationInfo' с параметром 'AuthenticationInfoDto' - {} ", authenticationInfo);
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

        log.info("Создан новый 'ResponseDto' - {} с 'isSuccess - {}", response.getData(), response.isSuccess());
        log.info("Завершается метод 'createAuthenticationInfo' с параметром 'AuthenticationInfoDto' - {}", authenticationInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAuthenticationInfo(@PathVariable("id") Long id,
                                                                           @RequestBody AuthenticationInfoDto authenticationInfo) {

        log.info("Запускается метод 'updateAuthenticationInfo'" +
                " с параметрами 'id' - {} и 'AuthenticationInfoDto' - {}", id, authenticationInfo);

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.updateAuthenticationInfo(id, authenticationInfo);

        log.info("Попытка обновить 'authenticationInfo' с 'AuthenticationInfoDto' - {} с запуском 'authService'", authenticationInfoDto);

        if (!authenticationInfoDto.isPresent()) {

            log.warn("Не удалось создать 'AuthenticationInfoDto'"  +
                                        ", так как 'AuthenticationInfo' с id - {} не существует", id);

            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't update. Client with id " + id + " not found";
        }

        log.info("Успешно");

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfo);

        log.info("Создан новый 'ResponseDto' - {} с 'isSuccess' - {}", response.getData(), response.isSuccess());
        log.info("Завершается метод 'updateAuthenticationInfo'" +
                " с параметрами 'id' - {} и 'AuthenticationInfoDto' - {}", id, authenticationInfo);

        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> deleteAuthenticationInfo(@PathVariable("id") Long id) {

        log.info("Запускается метод 'deleteAuthenticationInfo' с параметром 'id' - {}", id);

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AuthenticationInfoDto> authenticationInfoDto = authService.deleteAuthenticationInfo(id);

        log.info("Попытка деактивировать 'authenticationInfo' c 'AuthenticationInfoDto' - {} с запуском 'authService'", authenticationInfoDto);

        if (!authenticationInfoDto.isPresent()) {

            log.warn("Не удалось создать 'AuthenticationInfoDto'"  +
                    ", так как 'AuthenticationInfo' с id - {} не существует", id);

            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't delete. Client with id " + id + " not found";
        }

        log.info("Успешно");

        ResponseDto response = ResponseDto.buildResponse(success, errorMessage, authenticationInfoDto);

        log.info("Создан новый 'ResponseDto' - {} с 'isSuccess' - {}", response.getData(), response.isSuccess());
        log.info("Завершается метод 'deleteAuthenticationInfo' с параметром 'id' - {}", id);

        return ResponseEntity.status(status).body(response);
    }
}
