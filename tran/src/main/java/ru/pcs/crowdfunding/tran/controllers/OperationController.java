package ru.pcs.crowdfunding.tran.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        log.info("post /api/operation: get operationDto = {}", newOperationDto.toString());

        OperationDto operationDto;
        try {
            operationDto = operationService.createOperation(newOperationDto);
        } catch (IllegalArgumentException e) {

            ResponseDto response = ResponseDto.builder()
                .success(false)
                .error(Arrays.asList(e.getMessage()))
                .build();

            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            log.info("Finishing 'post /api/operation': 'response': 'status' - {}, 'body' - {}"
                    , responseBody.getStatusCode(), responseBody.getBody().getData());
            return responseBody;
        }
        ResponseDto response = ResponseDto.builder()
            .success(true)
            .data(operationDto)
            .build();
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
        log.info("Finishing 'post /api/operation': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getOperation(@PathVariable("id") Long id) {
        log.info("get /api/operation/{id}, id = {}", id);

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
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'get /api/operation/{id}': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

}