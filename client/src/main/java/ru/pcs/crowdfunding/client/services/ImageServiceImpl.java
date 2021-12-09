package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.dto.ImageDto;
import ru.pcs.crowdfunding.client.repositories.ProjectImagesRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ProjectImagesRepository projectImagesRepository;

    @Override
    public Optional<ImageDto> getProjectImageById(Long id) {
        return projectImagesRepository.findById(id)
                .map(image -> ImageDto.builder()
                        .fileName(image.getName())
                        .content(image.getContent())
                        .build());
    }
}
