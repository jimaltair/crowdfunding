package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.client.dto.OperationDto;
import ru.pcs.crowdfunding.client.services.OperationService;


import java.math.BigDecimal;


@Controller
@RequestMapping("/operation")
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/top_up")
    public String createTopUpOperation(@RequestHeader(value = "referer", required = false) final String referer,
                                       @RequestParam("sum") BigDecimal sumTopUp,
                                       @RequestParam("client_id") Long clientId,
                                       @RequestParam("account_id") Long accountId) {
        log.info("post /api/operation/top_up: post operation TOP_UP with " +
                "clientId = {}, accountId = {}, sum = {}", clientId, accountId, sumTopUp);

       OperationDto operationDto = OperationDto.builder()
               .operationType(OperationDto.Type.TOP_UP)
               .sum(sumTopUp)
               .initiatorId(clientId)
               .debitAccountId(accountId)
               .build();

        try {
            operationDto = operationService.operate(operationDto);
        } catch (IllegalArgumentException e) {

        }

//            ResponseDto response = ResponseDto.builder()
//                .success(false)
//                .error(Arrays.asList(e.getMessage()))
//                .build();
//
//            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            log.info("Finishing 'post /api/operation': 'response': 'status' - {}, 'body' - {}"
//                    , responseBody.getStatusCode(), responseBody.getBody().getData());
//            return responseBody;
//        }
//        ResponseDto response = ResponseDto.builder()
//            .success(true)
//            .data(operationDto)
//            .build();
//        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
//        log.info("Finishing 'post /api/operation': 'response': 'status' - {}, 'body' - {}"
//                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return referer;
    }

}