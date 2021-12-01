package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.ClientDto;

import java.util.Optional;

public interface ClientsService {
    Optional<ClientDto> findById(Long id);
}
