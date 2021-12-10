package ru.pcs.crowdfunding.client.api;

import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.dto.OperationDto;

import java.math.BigDecimal;

public interface TransactionServiceClient {

    CreateAccountResponse createAccount();

    BigDecimal getBalance(Long accountId);

    Long getContributorsCount(Long accountId);

    OperationDto operate(OperationDto operationDto);
}