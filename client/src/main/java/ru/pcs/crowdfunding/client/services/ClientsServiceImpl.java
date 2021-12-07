package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.*;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.repositories.ClientImagesRepository;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static ru.pcs.crowdfunding.client.dto.ClientDto.from;
import static ru.pcs.crowdfunding.client.dto.ClientForm.CLIENTS_IMAGE_PATH;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientsServiceImpl implements ClientsService {

    private final ClientsRepository clientsRepository;
    private final ClientImagesRepository clientImagesRepository;
    private final TransactionServiceClient transactionServiceClient;
    private final AuthorizationServiceClient authorizationServiceClient;

    @Override
    public Optional<ClientDto> findById(Long id) {

        Optional<Client> client = clientsRepository.findById(id);
        if (!client.isPresent()) {
            log.warn("client with id = {} not found", id);
            return Optional.empty();
        }

        ClientDto clientDto = from(client.get());

        clientDto.setEmail(authorizationServiceClient.getAuthInfo(client.get().getId()).getEmail());
        clientDto.setSumAccount(transactionServiceClient.getBalance(client.get().getAccountId()));

        return Optional.of(clientDto);
    }

    /**
     * @deprecated в текущей реализации (сохранение картинки в базу) данный метод не используется
     */
    private void createDirectoryIfNotExists(String path) {
        if (Files.notExists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path).toAbsolutePath().normalize());
            } catch (IOException e) {
                log.error("Can't create directory {}", path);
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public ClientForm updateClient(Long clientId, ClientForm form, MultipartFile file) {
        Client client = clientsRepository.getById(clientId);

        client.setFirstName(form.getFirstName());
        client.setLastName(form.getLastName());
        client.setCountry(form.getCountry());
        client.setCity(form.getCity());
        client.setImage(form.getImage()); // не уверена в корректности

        clientsRepository.save(client);

        if (!file.isEmpty()) {
            log.info("Try to save image with name={}", file.getOriginalFilename());
            ClientImage clientImage = getImage(file, client);
            Long id = clientImagesRepository.save(clientImage).getId();
            log.info("Image was saved with id={}", id);
        }
        //надо вернуть снова ClientForm, как из Client сделать ClientForm??
        return client;
    }

    private ClientImage getImage(MultipartFile file, Client client) {
        try {
            return ClientImage.builder()
                    .name(file.getOriginalFilename())
                    .client(client)
                    .content(file.getBytes())
                    .build();
        } catch (IOException e) {
            log.error("Can't save image {}", file.getOriginalFilename());
            throw new IllegalStateException(e);
        }
    }
}
