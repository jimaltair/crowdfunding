package ru.pcs.crowdfunding.client.api;

import org.springframework.http.ResponseEntity;
import ru.pcs.crowdfunding.client.dto.CreateAccountRequest;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

import java.math.BigDecimal;

public interface TransactionServiceClient {

    CreateAccountResponse createAccount(CreateAccountRequest request);

    BigDecimal getBalance(Long accountId);


}