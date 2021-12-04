package ru.pcs.crowdfunding.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ru.pcs.crowdfunding.client.dto.CreateAccountRequest;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;

@Component
public class TransactionServiceRestTemplateClient extends RestTemplateClient implements TransactionServiceClient {

    private static final String CREATE_ACCOUNT_URL = "/api/account";
    private static final String GET_BALANCE = "/balance";

    @Autowired
    public TransactionServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${api.transaction-service.remote-address}") String remoteAddress,
            ObjectMapper objectMapper) {
        super(restTemplateBuilder, remoteAddress, objectMapper);
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        return post(CREATE_ACCOUNT_URL, request, CreateAccountResponse.class);
    }

    @Override
    public ResponseEntity<ResponseDto> getBalance(Long accountId) {
        ResponseEntity<ResponseDto> response = restTemplate.getForEntity(remoteAddress + CREATE_ACCOUNT_URL + "/" + accountId + GET_BALANCE, ResponseDto.class);
        Object data = response.getBody().getData();

    }
}
