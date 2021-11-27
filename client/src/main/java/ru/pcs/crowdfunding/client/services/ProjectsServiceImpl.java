package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.repositories.ProjectsRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectsRepository projectsRepository;

    @Override
    public Optional<Project> findById(Long id) {
        Optional<Project> project = projectsRepository.findById(id);
        if (!project.isPresent()) {
            log.warn("project with id = {} not found", id);
        }
        return project;
    }
}
