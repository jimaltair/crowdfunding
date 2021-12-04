package ru.pcs.crowdfunding.client.api;

import org.springframework.http.ResponseEntity;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

public interface TransactionServiceClient {
    ResponseEntity<ResponseDto> createAccount();

    ResponseEntity<ResponseDto> getBalance(Long accountId);


}