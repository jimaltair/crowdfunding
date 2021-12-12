package ru.pcs.crowdfunding.client.services;

import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ClientForm;
import ru.pcs.crowdfunding.client.dto.ImageDto;

import java.util.Optional;

public interface ClientsService {
    /**
     * есть предложение использовать над каждым методом @Retryable(value = Exception.class)
     */
    Optional<ClientDto> findById(Long id);

    /** есть предложение использовать над каждым методом @Retryable(value = Exception.class) */
    ClientForm updateClient(Long clientId, ClientForm form, MultipartFile file);

    /** есть предложение использовать над каждым методом @Retryable(value = Exception.class) */
    Optional<ImageDto> getImageById(Long id);

    Long getAccountIdByClientId(Long clientId) throws IllegalAccessException;
}
