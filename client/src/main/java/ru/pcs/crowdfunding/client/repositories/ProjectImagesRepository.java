package ru.pcs.crowdfunding.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.domain.ProjectImage;

@Repository
public interface ProjectImagesRepository extends JpaRepository<ProjectImage, Long> {

    ProjectImage findProjectImageByProject(Project project);
}
