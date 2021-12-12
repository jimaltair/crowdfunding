package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignUpResponse;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final ClientsRepository clientsRepository;
    private final AuthorizationServiceClient authorizationServiceClient;
    private final TransactionServiceClient transactionServiceClient;

    @Transactional
    @Override
    public SignUpForm signUp(SignUpForm form) {
        /** А мы не хотим отвалидировать сначала входные данные? */
        Client newClient = Client.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .country(form.getCountry())
                .city(form.getCity())
                .build();

        // сохраняем заготовку клиента локально, чтобы для него уже создался id.
        // id понадобится для походов в другие сервисы.
        newClient = clientsRepository.save(newClient);
        log.info("Saved 'newClient' - {} into 'clientsRepository'", newClient);
        // ходим  в остальные сервисы и обновляем поля клиента
        String accessToken = createAuthenticationAndGetAccessToken(
                newClient.getId(), form.getEmail(), form.getPassword());
        newClient.setAccountId(createAccount());

        // сохраняем дополненного клиента
        newClient = clientsRepository.save(newClient);
        log.debug("Changed 'newClient' - {} in 'clientsRepository'", newClient);
        SignUpForm result = SignUpForm.from(newClient);
        result.setAccessToken(accessToken);
        log.info("Result of method 'signUp': 'result' - {}", result);
        return result;
    }

    private String createAuthenticationAndGetAccessToken(Long userId, String email, String password) {
        AuthSignUpRequest request = AuthSignUpRequest.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .build();

        AuthSignUpResponse response = authorizationServiceClient.signUp(request);
        log.info("Result of method 'createAuthenticationAndGetAccessToken': {}", response.getAccessToken());
        return response.getAccessToken();
    }

    private Long createAccount() {
        CreateAccountResponse response = transactionServiceClient.createAccount();
        log.info("Result of method 'createAccount': {}", response.getId());
        return response.getId();
    }

}