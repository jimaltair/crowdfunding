package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.domain.Project;

import java.util.Optional;

public interface ProjectsService {
    Optional<Project> findById(Long id);
}
