package ru.pcs.crowdfunding.client.services;

import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.dto.ImageDto;

import java.util.Optional;

public interface ProjectsService {
    Optional<ProjectDto> findById(Long id);

    Optional<Long> createProject(ProjectForm form, MultipartFile file);

    Optional<ImageDto> getImageById(Long id);
}
