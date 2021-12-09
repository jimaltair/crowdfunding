package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.ImageDto;

import java.util.Optional;

public interface ImageService {
    Optional<ImageDto> getProjectImageById(Long id);
}
