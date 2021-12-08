package ru.pcs.crowdfunding.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.dto.GetBalanceResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class TransactionServiceRestTemplateClient extends RestTemplateClient implements TransactionServiceClient {

    private static final String API_ACCOUNT_URL = "/api/account";
    private static final String GET_BALANCE_URL = API_ACCOUNT_URL + "/{accountId}/balance?date={date}";

    @Autowired
    public TransactionServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${api.transaction-service.remote-address}") String remoteAddress,
            @Value ("${api.transaction-service.token}")String token,
            ObjectMapper objectMapper) {
        super(restTemplateBuilder, remoteAddress, token, objectMapper);
    }

    @Override
    public CreateAccountResponse createAccount() {
        return get(API_ACCOUNT_URL, CreateAccountResponse.class);
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        GetBalanceResponseDto balance = get(GET_BALANCE_URL, GetBalanceResponseDto.class, accountId, Instant.now().getEpochSecond());
        return balance.getBalance();
    }


}
