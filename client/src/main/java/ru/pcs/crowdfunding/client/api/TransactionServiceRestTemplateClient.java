package ru.pcs.crowdfunding.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.pcs.crowdfunding.client.dto.CreateAccountRequest;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.dto.ResponseDto;
//import ru.pcs.crowdfunding.tran.dto.BalanceDto;

import java.math.BigDecimal;

@Component
public class TransactionServiceRestTemplateClient extends RestTemplateClient implements TransactionServiceClient {
    /*
    // предложение изменить на API_ACCOUNT_URL, потому что передавать в метод getBalance() переменнную с именем
    CREATE_ACCOUNT_URL не очень как-то
     */
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
    public BigDecimal getBalance(Long accountId) {
        ResponseEntity<ResponseDto> response = get(CREATE_ACCOUNT_URL + "/" + accountId + "/" + GET_BALANCE, ResponseEntity.class);
        if (response.getBody().getData() != null) {
            BigDecimal result = (BigDecimal) response.getBody().getData();
            return result;
        } else {
            return null; //возвращаем null только если такого кошелька не существует
        }
    }
}
