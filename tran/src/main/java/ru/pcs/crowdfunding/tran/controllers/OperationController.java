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
        log.info("get newOperationDto {}", newOperationDto.toString());

        OperationDto operationDto = null;
        try {
            operationDto = operationService.createOperation(newOperationDto);
        } catch (IllegalArgumentException e) {

            ResponseDto response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(e.getMessage()))
                .data(newOperationDto)
                .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        ResponseDto response = ResponseDto.builder()
            .success(true)
            .data(operationDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getOperation(@PathVariable("id") Long id) {

        log.info("get Operation by id {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<OperationDto> operationDto = operationService.findById(id);

        if (!operationDto.isPresent()) {
            status = HttpStatus.NOT_FOUND;
            response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList("Operation with id " + id + " not found"))
                .build();
        } else {
            status = HttpStatus.ACCEPTED;
            response = ResponseDto.builder()
                .success(true)
                .data(operationDto.get())
                .build();
        }
        return ResponseEntity.status(status).body(response);
    }
}