package ru.pcs.crowdfunding.client.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pcs.crowdfunding.client.dto.AccountDto;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

import java.time.Instant;

@Component
public class TransactionServiceRestTemplateClient implements TransactionServiceClient {

    private static final String CREATE_ACCOUNT_URL = "/api/account";
    private static final String GET_BALANCE = "/balance";

    private final RestTemplate restTemplate;
    private final String remoteAddress;

    @Autowired
    public TransactionServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${api.transaction-service.remote-address}") String remoteAddress) {
        this.restTemplate = restTemplateBuilder.build();
        this.remoteAddress = remoteAddress;
    }

    @Override
    public ResponseEntity<ResponseDto> createAccount() {
        final Instant now = Instant.now();
        AccountDto accountDto = AccountDto.builder()
                .createdAt(now)
                .modifiedAt(now)
                .isActive(true)
                .build();
        return restTemplate.postForEntity(remoteAddress + CREATE_ACCOUNT_URL, accountDto, ResponseDto.class);
    }

    @Override
    public ResponseEntity<ResponseDto> getBalance(Long accountId) {
        ResponseEntity<ResponseDto> response = restTemplate.getForEntity(remoteAddress + CREATE_ACCOUNT_URL + "/" + accountId + GET_BALANCE, ResponseDto.class);
        Object data = response.getBody().getData();

    }
}
