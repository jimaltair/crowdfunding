package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.client.domain.Project;

public interface ProjectsRepository extends JpaRepository<Project, Long> {
}
