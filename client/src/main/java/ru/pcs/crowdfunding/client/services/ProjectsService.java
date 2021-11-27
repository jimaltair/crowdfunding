package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.ProjectDto;

import java.util.Optional;

public interface ProjectsService {
    Optional<ProjectDto> findById(Long id);
}
