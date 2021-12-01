package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Override
    public Optional<ClientDto> findById(Long id) {

        Optional<Client> client = clientsRepository.findById(id);
        if (!client.isPresent()) {
            log.warn("client with id = {} not found", id);
            return Optional.empty();
        }
        return Optional.of(from(client.get()));
    }
}
