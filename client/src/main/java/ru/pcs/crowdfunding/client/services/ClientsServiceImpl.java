package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omg.IOP.TransactionService;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceRestTemplateClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.util.Optional;

import static ru.pcs.crowdfunding.client.dto.ClientDto.from;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientsServiceImpl implements ClientsService {

    private final ClientsRepository clientsRepository;
    private final TransactionServiceClient transactionServiceClient;
    private final AuthorizationServiceClient authorizationServiceClient;

    @Override
    public Optional<ClientDto> getById(Long id) {

        Optional<Client> client = clientsRepository.findById(id);
        if (!client.isPresent()) {
            log.warn("Client with 'id' = {} not found", id);
            return Optional.empty();
        }
        log.debug("Created 'client' - {} with 'clientsRepository'", client);
        ClientDto clientDto = from(client.get());

        clientDto.setEmail(authorizationServiceClient.getAuthInfo(client.get().getId()).getEmail());
        clientDto.setSumAccount(transactionServiceClient.getBalance(client.get().getAccountId()));
        log.info("Result of method 'getById':" +
                        " created 'clientDto' - {} with 'clientsRepository', 'authorizationServiceClient', 'transactionServiceClient'"
        , clientDto);
        return Optional.of(clientDto);
    }
}
