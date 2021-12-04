package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.ResponseDto;
import ru.pcs.crowdfunding.client.dto.SignUpForm;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.util.Map;


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
        newClient = callAuthorizationService(form, newClient);
        newClient = callTransactionService(newClient);

        // сохраняем дополненного клиента
        newClient = clientsRepository.save(newClient);

        return SignUpForm.from(newClient);
    }

    private Client callAuthorizationService(SignUpForm form, Client client) {
        AuthSignUpRequest authSignUpRequest = AuthSignUpRequest.builder()
                .userId(client.getId())
                .email(form.getEmail())
                .password(form.getPassword())
                .build();

        ResponseEntity<ResponseDto> responseEntity = authorizationServiceClient.signUp(authSignUpRequest);
        ResponseDto responseDto = responseEntity.getBody();
        if (responseEntity.getStatusCode().isError() || !responseDto.isSuccess()) {
            // FIXME: handle this properly
            throw new IllegalStateException("AuthorizationServiceClient.signUp() failed");
        }

        return client;
    }

    private Client callTransactionService(Client client) {
        ResponseEntity<ResponseDto> responseEntity = transactionServiceClient.createAccount();
        ResponseDto responseDto = responseEntity.getBody();
        if (responseEntity.getStatusCode().isError() || !responseDto.isSuccess()) {
            // FIXME: handle this properly
            throw new IllegalStateException("TransactionServiceClient.signUp() failed");
        }

        client.setAccountId(extractAccountId(responseDto));
        return client;
    }

    private Long extractAccountId(ResponseDto responseDto) {
        Map<?, ?> map = (Map<?, ?>) responseDto.getData();
        Integer id = (Integer) map.get("id");
        return id.longValue();
    }
}
