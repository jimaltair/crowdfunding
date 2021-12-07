package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.*;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final ClientsRepository clientsRepository;
    private final AuthorizationServiceClient authorizationServiceClient;
    private final TransactionServiceClient transactionServiceClient;

    @Override
    public SignUpForm signUp(SignUpForm form) {
        Client newClient = Client.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .country(form.getCountry())
                .city(form.getCity())
                .build();

        // сохраняем заготовку клиента локально
        newClient = clientsRepository.save(newClient);

        // ходим в остальные сервисы и обновляем поля клиента
        String accessToken = callAuthorizationService(form, newClient);
        callTransactionService(newClient);

        // сохраняем дополненного клиента
        newClient = clientsRepository.save(newClient);

        SignUpForm result = SignUpForm.from(newClient);
        result.setAccessToken(accessToken);
        return result;
    }

    private String callAuthorizationService(SignUpForm form, Client client) {
        AuthSignUpRequest request = AuthSignUpRequest.builder()
                .userId(client.getId())
                .email(form.getEmail())
                .password(form.getPassword())
                .build();

        AuthSignUpResponse response = authorizationServiceClient.signUp(request);
        return response.getAccessToken();
    }

    private void callTransactionService(Client client) {
        final Instant now = Instant.now();
        CreateAccountRequest request = CreateAccountRequest.builder()
                .isActive(true)
                .createdAt(now)
                .modifiedAt(now)
                .build();

        CreateAccountResponse response = transactionServiceClient.createAccount(request);

        client.setAccountId(response.getId());
    }
}
