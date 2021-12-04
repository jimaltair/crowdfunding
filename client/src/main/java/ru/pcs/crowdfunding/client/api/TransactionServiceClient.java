package ru.pcs.crowdfunding.client.api;

import ru.pcs.crowdfunding.client.dto.CreateAccountRequest;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;

public interface TransactionServiceClient {
    CreateAccountResponse createAccount(CreateAccountRequest request);
}