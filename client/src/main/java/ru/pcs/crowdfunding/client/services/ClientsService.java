package ru.pcs.crowdfunding.client.services;

import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;

import java.util.Optional;

public interface ClientsService {
    Optional<ClientDto> findById(Long id);

    ClientForm updateClient(Long clientId, ClientForm form, MultipartFile file);
}
