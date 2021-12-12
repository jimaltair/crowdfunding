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
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.repositories.ClientImagesRepository;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectsRepository;

import java.io.*;
import java.util.Optional;

import static ru.pcs.crowdfunding.client.dto.ClientDto.from;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientsServiceImpl implements ClientsService {
    private final ClientsRepository clientsRepository;
    private final ProjectsRepository projectsRepository;

    private final ClientImagesRepository clientImagesRepository;
    private final TransactionServiceClient transactionServiceClient;
    private final AuthorizationServiceClient authorizationServiceClient;

    @Override
    public Optional<ClientDto> findById(Long id) {

        Optional<Client> client = clientsRepository.findById(id);
        if (!client.isPresent()) {
            log.warn("Client with 'id' = {} not found", id);
            return Optional.empty();
        }
        log.debug("Created 'client' - {} with 'clientsRepository'", client);
        ClientDto clientDto = from(client.get());

        clientDto.setEmail(authorizationServiceClient.getAuthInfo(client.get().getId()).getEmail());
        clientDto.setSumAccount(transactionServiceClient.getBalance(client.get().getAccountId()));
        return Optional.of(clientDto);
    }

    @Override
    public Optional<Client> findByProject(ProjectDto projectDto) {
        Optional<Project> project = projectsRepository.findById(projectDto.getId());
        if(!project.isPresent()) {
            log.error("Project didn't found");
            throw new IllegalArgumentException("Project didn't found");
        }
        return clientsRepository.findById(project.get().getAuthor().getId());
    }
    @Override
    public Optional<ImageDto> getImageById(Long id) {
        return clientImagesRepository.findById(id)
                .map(image -> ImageDto.builder()
                        .format(FilenameUtils.getExtension(image.getName()))
                        .content(image.getContent())
                        .build());
    }

    @Override
    public ClientForm updateClient(Long clientId, ClientForm form, MultipartFile file) {
        Client client = clientsRepository.getById(clientId);

        client.setFirstName(form.getFirstName());
        client.setLastName(form.getLastName());
        client.setCountry(form.getCountry());
        client.setCity(form.getCity());

        // Если передали новое изображение, то обновляем/создаем
        if (!file.isEmpty()) {
            if (client.getImage() == null) {
                client.setImage(ClientImage.builder()
                        .client(client)
                        .build());
            }

            updateImageContent(client.getImage(), file);

            clientImagesRepository.save(client.getImage());
        }

        clientsRepository.save(client);

        ClientForm clientForm = ClientForm.from(client);
        clientForm.setEmail(getEmail(clientId));
        return clientForm;
    }

    private void updateImageContent(ClientImage clientImage, MultipartFile file) {
        try {
            clientImage.setName(file.getOriginalFilename());
            clientImage.setContent(file.getBytes());
        } catch (IOException e) {
            log.error("Can't update image content {}", file.getOriginalFilename(), e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Long getAccountIdByClientId(Long clientId) throws IllegalAccessException {
        Long accountId;
        Optional<ClientDto> optionalClient = findById(clientId);
        if(!optionalClient.isPresent()) {
            throw new IllegalAccessException("Client not found");
        }
        accountId = optionalClient.get().getAccountId();
        return accountId;
    }

    private String getEmail(Long idClient) {
        return authorizationServiceClient.getAuthInfo(idClient).getEmail();
    }

}
