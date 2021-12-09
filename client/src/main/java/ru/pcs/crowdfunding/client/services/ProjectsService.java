package ru.pcs.crowdfunding.client.services;

import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.dto.ProjectImageDto;

import java.util.Optional;

public interface ProjectsService {
    Optional<ProjectDto> findById(Long id);

    Optional<Long> createProject(ProjectForm form, MultipartFile file);

    Optional<ProjectImageDto> getImageById(Long id);

    void updateProject(Long id, ProjectForm form, MultipartFile file);
}
