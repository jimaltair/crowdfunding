package ru.pcs.crowdfunding.client.services;

import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.ClientDto;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.dto.ImageDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProjectsService {
    Optional<ProjectDto> findById(Long id);

    Optional<Long> createProject(ProjectForm form, MultipartFile file);

    Optional<ImageDto> getImageById(Long id);

    ProjectDto updateProject(Long id, ProjectForm form, MultipartFile file);

    BigDecimal getMoneyCollectedByProjectId(Long projectId);

    Long getContributorsCountByProjectId(Long projectId);

    List<ProjectDto> getConfirmedProjects();

    List<ProjectDto> getProjectsFromClient(ClientDto client);

    Long getAccountIdByProjectId(Long projectId) throws IllegalAccessException;
}
