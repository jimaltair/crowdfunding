package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.*;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import javax.transaction.Transactional;
import java.time.Instant;


@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final ClientsRepository clientsRepository;
    private final AuthorizationServiceClient authorizationServiceClient;
    private final TransactionServiceClient transactionServiceClient;

    @Transactional
    @Override
    public SignUpForm signUp(SignUpForm form) {
        Client newClient = Client.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .country(form.getCountry())
                .city(form.getCity())
                .build();

        // сохраняем заготовку клиента локально, чтобы для него уже создался id.
        // id понадобится для походов в другие сервисы.
        newClient = clientsRepository.save(newClient);

        // ходим в остальные сервисы и обновляем поля клиента
        String accessToken = createAuthenticationAndGetAccessToken(
                newClient.getId(), form.getEmail(), form.getPassword());
        newClient.setAccountId(createAccount());

        // сохраняем дополненного клиента
        newClient = clientsRepository.save(newClient);

        SignUpForm result = SignUpForm.from(newClient);
        result.setAccessToken(accessToken);
        return result;
    }

    private String createAuthenticationAndGetAccessToken(Long userId, String email, String password) {
        AuthSignUpRequest request = AuthSignUpRequest.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .build();

        AuthSignUpResponse response = authorizationServiceClient.signUp(request);
        return response.getAccessToken();
    }

    private Long createAccount() {
        final Instant now = Instant.now();
        CreateAccountRequest request = CreateAccountRequest.builder()
                .isActive(true)
                .createdAt(now)
                .modifiedAt(now)
                .build();

        CreateAccountResponse response = transactionServiceClient.createAccount(request);
        return response.getId();
    }
}
