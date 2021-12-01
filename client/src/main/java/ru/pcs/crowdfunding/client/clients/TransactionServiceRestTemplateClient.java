package ru.pcs.crowdfunding.client.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

@Component
public class TransactionServiceRestTemplateClient implements TransactionServiceClient {
    private static final String CREATE_ACCOUNT_URL = "/api/v0/createAccount";

    private final RestTemplate restTemplate;
    private final String remoteAddress;

    @Autowired
    public TransactionServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${clients.transaction-service.remote-address}") String remoteAddress) {
        this.restTemplate = restTemplateBuilder.build();
        this.remoteAddress = remoteAddress;
    }

    @Override
    public ResponseEntity<ResponseDto> createAccount() {
        return restTemplate.postForEntity(remoteAddress + CREATE_ACCOUNT_URL, null, ResponseDto.class);
    }
}
