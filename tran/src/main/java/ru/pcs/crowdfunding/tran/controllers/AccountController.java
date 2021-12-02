package ru.pcs.crowdfunding.tran.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.dto.BalanceDto;
import ru.pcs.crowdfunding.tran.dto.ResponseDto;
import ru.pcs.crowdfunding.tran.services.AccountService;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Optional;


@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable("id") Long id) {
        log.info("get Account by id {}", id);

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

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(value = "/{id}/balance")
    public ResponseEntity<ResponseDto> getBalanceById(@PathVariable("id") Long id,
                                                      @RequestParam("date")
                                                      Long epochSecondTimeStamp) {

        Instant balanceDateTime = Instant.ofEpochSecond(epochSecondTimeStamp);

        log.info("get balance by Account id {} at {}", id, balanceDateTime);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> optionalAccountDto = accountService.findById(id);


        if (!optionalAccountDto.isPresent()) {
            status = HttpStatus.NOT_FOUND;
            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Account with id " + id + " not found"))
                    .build();

            return ResponseEntity.status(status).body(response);
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

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAccount(@RequestBody AccountDto newAccountDto) {
        AccountDto accountDto = accountService.createAccount(newAccountDto);

        ResponseDto response = ResponseDto.builder()
                                        .success(true)
                                        .data(accountDto)
                                        .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAccount(@PathVariable("id") Long id,
                                                                @RequestBody AccountDto updateAccountDto) {
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

        return ResponseEntity.status(status).body(response);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> deleteAccount(@PathVariable("id") Long id) {
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

        return ResponseEntity.status(status).body(response);
    }

}
