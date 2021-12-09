package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.*;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.repositories.ClientImagesRepository;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

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

    private String getEmail(Long idClient) {
        return authorizationServiceClient.getAuthInfo(idClient).getEmail();
    }

    @Override
    public ClientForm updateClient(Long clientId, ClientForm form, MultipartFile file) {
        Client client = clientsRepository.getById(clientId);

        client.setFirstName(form.getFirstName());
        client.setLastName(form.getLastName());
        client.setCountry(form.getCountry());
        client.setCity(form.getCity());

        clientsRepository.save(client);

        if (!file.isEmpty()) {
            clientImagesRepository.deleteAll();

            log.info("Try to save image with name={}", file.getOriginalFilename()); //сделать имя уникальным
            ClientImage clientImage = createClientImage(file, client);
            Long id = clientImagesRepository.save(clientImage).getId();
            log.info("Image was saved with id={}", id);

            FileOutputStream fout;


            {
                try {
                    fout = new FileOutputStream(CLIENTS_IMAGE_PATH);
                    writeFile(clientImage, fout);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        ClientForm clientForm = ClientForm.from(client);
        clientForm.setEmail(getEmail(clientId));
//        clientForm.setImage(getImage(clientId));
        return clientForm;
    }

    @Override
    public ClientImage getImage(Long clientId) {

        return ClientImage.builder()
                .path(CLIENTS_IMAGE_PATH)
                .build();
    }

    @Override
    public void writeFile(ClientImage clientImage, OutputStream outputStream) {
        try {
            byte[] mas = clientImagesRepository.getBytesImage(clientImage.getClient().getId());
//            Files.copy(Paths.get(CLIENTS_IMAGE_PATH), outputStream);
            //Files.copy(mas, outputStream);
            outputStream.write(mas);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private ClientImage createClientImage(MultipartFile file, Client client) {
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
