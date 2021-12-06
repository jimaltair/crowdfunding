package ru.pcs.crowdfunding.tran.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pcs.crowdfunding.tran.dto.OperationDto;
import ru.pcs.crowdfunding.tran.dto.ResponseDto;
import ru.pcs.crowdfunding.tran.services.OperationService;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/operation")
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<ResponseDto> createOperation(@RequestBody OperationDto newOperationDto) {

        log.info("Запускается метод 'createOperation' с параметром 'newOperationDto' - {}", newOperationDto);

        OperationDto operationDto = null;
        log.info("Создан новый 'OperationDto' - {}", operationDto);

        try {
            operationDto = operationService.createOperation(newOperationDto);
            log.info("Получен измененный 'operationDto' - {} с вызовом 'operationService'", operationDto);
        } catch (IllegalArgumentException e) {
            log.error("Не удалось изменить 'operationDto'", new IllegalArgumentException(e));
            ResponseDto response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(e.getMessage()))
                .data(newOperationDto)
                .build();
            log.warn("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
            log.warn("Завершается метод 'createOperation' с параметром 'OperationDto' - {}", newOperationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        ResponseDto response = ResponseDto.builder()
            .success(true)
            .data(operationDto)
            .build();
        log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
        log.info("Завершается метод 'createOperation' с параметром 'OperationDto' - {}", newOperationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getOperation(@PathVariable("id") Long id) {

        log.info("Запускается метод 'getOperation' с параметром 'id' - {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<OperationDto> operationDto = operationService.findById(id);

        log.info("Попытка получения 'OperationDto' - {} с вызвовом 'operationService'", operationDto);

        if (!operationDto.isPresent()) {
            log.warn("Не удалось создать 'operationDto' потому что 'operation' с 'id' - {} не существует", id);
            status = HttpStatus.NOT_FOUND;
            log.warn("Создан новый 'HttpStatus' - {}", status);
            response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList("Operation with id " + id + " not found"))
                .build();
            log.warn("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getError(), response.isSuccess());
        } else {
            log.info("Успешно");
            status = HttpStatus.ACCEPTED;
            log.info("Создан новый 'HttpStatus' - {}", status);
            response = ResponseDto.builder()
                .success(true)
                .data(operationDto.get())
                .build();
            log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
        }
        return ResponseEntity.status(status).body(response);
    }
}