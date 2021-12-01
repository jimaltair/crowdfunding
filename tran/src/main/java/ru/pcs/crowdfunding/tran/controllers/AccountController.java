package ru.pcs.crowdfunding.tran.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.dto.ResponseDto;
import ru.pcs.crowdfunding.tran.services.AccountService;

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

        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AccountDto> accountDto = accountService.findById(id);

        if (!accountDto.isPresent()) {
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Account with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, status, errorMessage, accountDto);
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAccount(@RequestBody AccountDto newAccountDto) {
        AccountDto accountDto = accountService.createNewAccount(newAccountDto);

        ResponseDto response = ResponseDto.buildResponse(true, HttpStatus.CREATED,
                null, accountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> updateAccount(@PathVariable("id") Long id,
                                                                @RequestBody AccountDto updateAccountDto) {
        boolean success = true;
        HttpStatus status = HttpStatus.ACCEPTED;
        String errorMessage = null;
        Optional<AccountDto> accountDto = accountService.updateAccount(id, updateAccountDto);

        if (!accountDto.isPresent()) {
            success = false;
            status = HttpStatus.NOT_FOUND;
            errorMessage = "Can't update. Account with id " + id + " not found";
        }

        ResponseDto response = ResponseDto.buildResponse(success, status, errorMessage, accountDto);

        return ResponseEntity.status(status).body(response);
    }

}
