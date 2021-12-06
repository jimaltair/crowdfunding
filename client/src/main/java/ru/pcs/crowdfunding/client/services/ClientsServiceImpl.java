package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.omg.IOP.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceRestTemplateClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.*;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.repositories.ClientImagesRepository;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static ru.pcs.crowdfunding.client.dto.ClientDto.from;
import static ru.pcs.crowdfunding.client.dto.ClientForm.CLIENTS_IMAGE_PATH;
import static ru.pcs.crowdfunding.client.dto.ProjectForm.PROJECT_IMAGE_PATH;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientsServiceImpl implements ClientsService {

    private final ClientsRepository clientsRepository;
    private final ClientImagesRepository clientImagesRepository;
    private final TransactionServiceClient transactionServiceClient;
    private final AuthorizationServiceClient authorizationServiceClient;

    @Override
    public Optional<ClientDto> getById(Long id) {

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
// MultipartFile проверять на isEmpty
        client.setFirstName(form.getFirstName());
        client.setLastName(form.getLastName());
        client.setCountry(form.getCountry());
        client.setCity(form.getCity());
        client.setAvatarImagePath(form.getImage().getPath());

        clientsRepository.save(client);

        if (!file.isEmpty()) {
            ClientImage image = getImage(file, client);
            try {
                log.info("Try to save client's image {}", image.getPath());
                Files.copy(file.getInputStream(), Paths.get(image.getPath()));
            } catch (IOException e) {
                log.error("Can't save client's image {}", image.getPath());
                throw new IllegalArgumentException(e);
            }
            clientImagesRepository.save(image);
        }
        //надо вернуть снова ClientForm, как из Client сделать ClientForm??
        return null;
    }


    private ClientImage getImage(MultipartFile file, Client client) {
        if (file == null || client == null) {
            throw new IllegalArgumentException("Can't upload image");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        createDirectoryIfNotExists(CLIENTS_IMAGE_PATH);
        return ClientImage.builder()
                .clientId(client.getId())
                .path(CLIENTS_IMAGE_PATH + UUID.randomUUID() + "." + extension)
                .build();
    }


}
