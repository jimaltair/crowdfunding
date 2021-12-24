package ru.pcs.crowdfunding.tran.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.dto.BalanceDto;
import ru.pcs.crowdfunding.tran.dto.ResponseDto;
import ru.pcs.crowdfunding.tran.services.AccountService;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.Arrays;
import java.util.Optional;


@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getAccount(@PathVariable("id") Long id) {
        log.info("get /api/account/{id}: id = {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> accountDto = accountService.findById(id);

        if (!accountDto.isPresent()) {
            status = HttpStatus.NOT_FOUND;
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Account with id " + id + " not found"))
                    .build();
        } else {
            status = HttpStatus.ACCEPTED;
            response = ResponseDto.builder()
                    .success(true)
                    .data(accountDto.get())
                    .build();
        }
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'get /api/account/{id}': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @GetMapping(value = "/{id}/balance")
    public ResponseEntity<ResponseDto> getBalanceById(@PathVariable("id") Long id,
                                                      @RequestParam("date")
                                                      Long epochSecondTimeStamp) {

        Instant balanceDateTime = Instant.now();
        log.info("get /api/account/{id}/balance: id = {}, date = {}", id, balanceDateTime);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> optionalAccountDto = accountService.findById(id);

        if (!optionalAccountDto.isPresent()) {
            status = HttpStatus.NOT_FOUND;
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Account with id " + id + " not found"))
                    .build();
            ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
            log.info("Finishing 'get /api/account/{id}/balance': 'response': 'status - {}, 'body' - {}"
                    , responseBody.getStatusCode(), responseBody.getBody().getData());
            return responseBody;
        }

        AccountDto accountDto = optionalAccountDto.get();
        Account account = Account.builder()
                .id(accountDto.getId())
                .isActive(accountDto.getIsActive())
                .createdAt(accountDto.getCreatedAt())
                .modifiedAt(accountDto.getModifiedAt())
                .build();
        BigDecimal balance = accountService.getBalance(account, balanceDateTime);

        BalanceDto balanceDto = BalanceDto.builder()
                .accountId(id)
                .balance(balance != null ? balance : BigDecimal.valueOf(0))
                .build();

        status = HttpStatus.ACCEPTED;
        response = ResponseDto.builder()
                .success(true)
                .data(balanceDto)
                .build();

        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'get /api/account/{id}/balance': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @GetMapping(value = "/{id}/contributorsCount")
    public ResponseEntity<ResponseDto> getContributorsCount(@PathVariable("id") Long id,
                                                            @RequestParam("date") Long epochSecondTimeStamp) {
        Instant dateTime = Instant.now();

        HttpStatus httpStatus;
        ResponseDto responseDto;

        Optional<Long> donorsCount = accountService.getContributorsCount(id, dateTime);
        if (donorsCount.isPresent()) {
            httpStatus = HttpStatus.OK;
            responseDto = ResponseDto.buildSuccess(donorsCount.get());
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            responseDto = ResponseDto.buildError("Account with id " + id + " not found");
        }

        return ResponseEntity.status(httpStatus).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> createAccount() {
        AccountDto accountDto = accountService.createAccount();
        ResponseDto response = ResponseDto.builder()
            .success(true)
            .data(accountDto)
            .build();
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(HttpStatus.CREATED).body(response);
        log.info("Finishing 'get /api/account/': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAccount(@PathVariable("id") Long id,
                                                     @RequestBody AccountDto updateAccountDto) {
        log.info("put /api/account/{id}: id = {}, updateAccountDto={}", id, updateAccountDto);
        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> accountDto = accountService.updateAccount(id, updateAccountDto);

        if (!accountDto.isPresent()) {
            status = HttpStatus.NOT_FOUND;
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Can't update. Account with id " + id + " not found"))
                    .build();
        } else {
            status = HttpStatus.ACCEPTED;
            response = ResponseDto.builder()
                    .success(true)
                    .data(accountDto.get())
                    .build();
        }
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'put /api/account/{id}': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> deleteAccount(@PathVariable("id") Long id) {
        log.info("delete /api/account/{id}: id = {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> accountDto = accountService.deleteAccount(id);

        if (!accountDto.isPresent()) {
            status = HttpStatus.NOT_FOUND;
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Can't delete. Account with id " + id + " not found"))
                    .build();
        } else {
            status = HttpStatus.ACCEPTED;
            response = ResponseDto.builder()
                    .success(true)
                    .data(accountDto.get())
                    .build();
        }
        ResponseEntity<ResponseDto> responseBody = ResponseEntity.status(status).body(response);
        log.info("Finishing 'put /api/account/{id}': 'response': 'status' - {}, 'body' - {}"
                , responseBody.getStatusCode(), responseBody.getBody().getData());
        return responseBody;
    }
}
