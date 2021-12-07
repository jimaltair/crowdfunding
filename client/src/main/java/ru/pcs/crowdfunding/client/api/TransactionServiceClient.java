package ru.pcs.crowdfunding.client.api;

import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;

import java.math.BigDecimal;

public interface TransactionServiceClient {

    CreateAccountResponse createAccount();

    BigDecimal getBalance(Long accountId);


}