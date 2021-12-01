package ru.pcs.crowdfunding.client.clients;

import org.springframework.http.ResponseEntity;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

public interface TransactionServiceClient {
    ResponseEntity<ResponseDto> createAccount();
}