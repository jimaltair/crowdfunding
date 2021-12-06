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
        log.info("Запускается метод 'getAccount' с параметром 'id' - {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> accountDto = accountService.findById(id);

        log.info("Попытка получения 'AccountDto' - {} с запуском 'accountService'", accountDto);

        if (!accountDto.isPresent()) {

            log.warn("'AccountDto' не удалось создать, так как 'Account' не существует");

            status = HttpStatus.NOT_FOUND;

            log.warn("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Account with id " + id + " not found"))
                    .build();

            log.warn("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getError(), response.isSuccess());

        } else {

            log.info("Успешно создан 'AccountDto'");

            status = HttpStatus.ACCEPTED;

            log.info("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(true)
                    .data(accountDto.get())
                    .build();

            log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
        }

        log.info("Завершается метод 'getAccount' с параметром 'id' - {}", id);
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(value = "/{id}/balance")
    public ResponseEntity<ResponseDto> getBalanceById(@PathVariable("id") Long id,
                                                      @RequestParam("date")
                                                              Long epochSecondTimeStamp) {

        log.info("Запускается метод 'getBalanceById' с параметрами 'id' - {} и 'epochSecondTimeStamp' - {}", id, epochSecondTimeStamp);

        Instant balanceDateTime = Instant.ofEpochSecond(epochSecondTimeStamp);

        log.info("Создан новый 'Instant' - {}", balanceDateTime);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> optionalAccountDto = accountService.findById(id);

        log.info("Создан 'AccountDto' - {} с запуском 'accountService'", optionalAccountDto);

        if (!optionalAccountDto.isPresent()) {

            log.warn("'AccountDto' не удалось создать, так как 'Account' не существует");

            status = HttpStatus.NOT_FOUND;

            log.warn("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Account with id " + id + " not found"))
                    .build();

            log.warn("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getError(), response.isSuccess());

            return ResponseEntity.status(status).body(response);
        }

        AccountDto accountDto = optionalAccountDto.get();
        Account account = Account.builder()
                .id(accountDto.getId())
                .isActive(accountDto.getIsActive())
                .createdAt(accountDto.getCreatedAt())
                .modifiedAt(accountDto.getModifiedAt())
                .build();

        log.info("Создан новый 'Account' - {}", account);

        BigDecimal balance = accountService.getBalance(account, balanceDateTime);

        log.info("Получен 'balance' - {} для 'account' - {} c запуском 'accountService'", balance, account);

        BalanceDto balanceDto = BalanceDto.builder()
                .accountId(id)
                .balance(balance != null ? balance : BigDecimal.valueOf(0))
                .build();

        log.info("Создан новый 'BalanceDto' - {}", balanceDto);
        log.info("Успешно получен баланс");

        status = HttpStatus.ACCEPTED;

        log.info("Создан новый 'HttpStatus' - {}", status);

        response = ResponseDto.builder()
                .success(true)
                .data(balanceDto)
                .build();

        log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
        log.info("Завершается метод 'getBalanceById' с параметрами 'id' - {} и 'date' - {}", id, epochSecondTimeStamp);

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAccount(@RequestBody AccountDto newAccountDto) {

        log.info("Запускается метод 'createAccount' с параметром 'newAccountDto' - {}", newAccountDto);

        AccountDto accountDto = accountService.createAccount(newAccountDto);

        log.info("Создан новый 'AccountDto' - {} c запуском 'accountService'", accountDto);

        ResponseDto response = ResponseDto.builder()
            .success(true)
            .data(accountDto)
            .build();

        log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
        log.info("Завершается метод 'createAccount' с параметром 'AccountDto' - {}", newAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAccount(@PathVariable("id") Long id,
                                                     @RequestBody AccountDto updateAccountDto) {

        log.info("Запускается метод 'updateAccount' с параметрами 'id' - {} и 'updateAccountDto' - {}", id, updateAccountDto);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> accountDto = accountService.updateAccount(id, updateAccountDto);

        log.info("Попытка обновить 'account' с 'AccountDto' - {} с 'id' - {} c запуском 'accountService' ", accountDto, id);

        if (!accountDto.isPresent()) {

            log.warn("'AccountDto' не удалось создать, так как 'Account' не существует");

            status = HttpStatus.NOT_FOUND;

            log.warn("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Can't update. Account with id " + id + " not found"))
                    .build();

            log.warn("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getError(), response.isSuccess());

        } else {

            log.info("Аккаунт успешно обновлен");

            status = HttpStatus.ACCEPTED;

            log.info("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(true)
                    .data(accountDto.get())
                    .build();

            log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());

        }

        log.info("Завершается метод 'updateAccount' с параметрами 'id' - {} и 'AccountDto' - {}", id, updateAccountDto);

        return ResponseEntity.status(status).body(response);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> deleteAccount(@PathVariable("id") Long id) {

        log.info("Запускается метод 'deleteAccount' с параметром 'id' - {}", id);

        ResponseDto response;
        HttpStatus status;

        Optional<AccountDto> accountDto = accountService.deleteAccount(id);

        log.info("Попытка деактивировать 'AccountDto' - {} с запуском 'accountService'", accountDto);


        if (!accountDto.isPresent()) {

            log.warn("'AccountDto' не удалось создать, так как 'Account' не существует");

            status = HttpStatus.NOT_FOUND;

            log.warn("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(false)
                    .error(Arrays.asList("Can't delete. Account with id " + id + " not found"))
                    .build();

            log.warn("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getError(), response.isSuccess());

        } else {

            log.info("Аккаунт успешно деактивирован");

            status = HttpStatus.ACCEPTED;

            log.info("Создан новый 'HttpStatus' - {}", status);

            response = ResponseDto.builder()
                    .success(true)
                    .data(accountDto.get())
                    .build();

            log.info("Создан новый 'ResponseDto' c содержимым - {} и 'isSuccess' - {}", response.getData(), response.isSuccess());
        }

        log.info("Завершается метод 'deleteAccount' с параметром 'id' - {}", id);

        return ResponseEntity.status(status).body(response);
    }

}
