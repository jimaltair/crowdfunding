package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pcs.crowdfunding.client.domain.ProjectImage;

public interface ProjectImagesRepository extends JpaRepository<ProjectImage, Long> {
}
