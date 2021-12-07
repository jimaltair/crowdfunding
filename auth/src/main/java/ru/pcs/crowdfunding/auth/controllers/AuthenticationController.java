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
@RequestMapping("/api/signUp")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> signUp(@RequestBody @Valid AuthenticationInfoDto authenticationInfoDto) {
        log.info("Запускается метод 'signUp' с параметром 'authenticationInfo' - {}", authenticationInfoDto);

        ResponseDto response;
        log.info("Проверка на 'authenticationService.existEmailInDb(authenticationInfoDto)' - {}"
                , authenticationService.existEmailInDb(authenticationInfoDto));

        if (!authenticationService.existEmailInDb(authenticationInfoDto)) {
            authenticationInfoDto = authenticationService.signUpAuthentication(authenticationInfoDto);
            log.info("Получение измененного 'authenticationInfoDto' - {} с запуском 'authenticationService'"
                    , authenticationInfoDto);
            response = ResponseDto.builder()
                    .data(authenticationInfoDto)
                    .success(true)
                    .build();
            log.info("Создан новый 'ResponseDto c содержимым 'data' - {}, 'isSuccess' - {}"
                    , response.getData(), response.isSuccess());

            return ResponseEntity.ok(response);
        }

        log.warn("Ошибка! Email - {} уже существует", authenticationInfoDto.getEmail());
        response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList("Email already exists","ERROR MESSAGE"))
                .build();
        log.warn("Создан новый 'ResponseDto c содержимым 'error' - {}, 'isSuccess' - {}"
                , response.getError(), response.isSuccess());

        log.info("Завершается метод 'signUp' с параметром 'authenticationInfo' - {}", authenticationInfoDto);
        return ResponseEntity.badRequest().body(response);
    }
}
