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
        log.info("Starting 'post /api/operation': get 'operationDto' = {}", newOperationDto.toString());

        OperationDto operationDto;
        try {
            operationDto = operationService.createOperation(newOperationDto);
        } catch (IllegalArgumentException e) {
            log.error("Can't create operation", e);
            ResponseDto response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(e.getMessage()))
                .build();

            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            log.info("Finishing 'post /api/operation': 'response': {}", responseBody);
            return responseBody;
        }
        ResponseDto response = ResponseDto.builder()
            .success(true)
            .data(operationDto)
            .build();
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
        log.info("Finishing 'post /api/operation': 'response': {}", responseBody);
        return responseBody;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getOperation(@PathVariable("id") Long id) {
        log.info("Starting 'get /api/operation/{id}': 'id' = {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<OperationDto> operationDto = operationService.findById(id);

        if (!operationDto.isPresent()) {
            log.error("Operation with 'id' - {} didn't found", id);
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
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'get /api/operation/{id}': 'response': {}", responseBody);
        return responseBody;
    }
}