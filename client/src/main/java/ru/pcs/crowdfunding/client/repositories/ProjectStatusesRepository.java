package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.client.domain.ProjectStatus;

public interface ProjectStatusesRepository extends JpaRepository<ProjectStatus, Long> {
    ProjectStatus getByStatus(ProjectStatus.Status name);
}
