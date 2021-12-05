package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.domain.ProjectImage;
import ru.pcs.crowdfunding.client.domain.ProjectStatus;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectImagesRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectStatusesRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static ru.pcs.crowdfunding.client.dto.ProjectDto.from;
import static ru.pcs.crowdfunding.client.dto.ProjectForm.PROJECT_IMAGE_PATH;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectsServiceImpl implements ProjectsService {

    private static final Long CLIENT_FOR_TEST = 2L;

    private final ProjectsRepository projectsRepository;

    private final ClientsRepository clientsRepository;

    private final ProjectStatusesRepository projectStatusesRepository;

    private final ProjectImagesRepository projectImagesRepository;

    @Override
    public Optional<ProjectDto> findById(Long id) {
        Optional<Project> project = projectsRepository.findById(id);
        if (!project.isPresent()) {
            log.warn("project with id = {} not found", id);
            return Optional.empty();
        }
        return Optional.of(from(project.get()));
    }

    @Override
    public void createProject(ProjectForm form, MultipartFile file) {
        log.info("Try to create project from {}", form.toString());
        ProjectStatus projectStatus = getProjectStatus();
        projectStatusesRepository.save(projectStatus);
        Project project = getProject(form, projectStatus);
        projectsRepository.save(project);

        if (!file.isEmpty()) {
            ProjectImage image = getImage(file, project);
            try {
                log.info("Try to save project image {}", image.getPath());
                Files.copy(file.getInputStream(), Paths.get(image.getPath()));
            } catch (IOException e) {
                log.error("Can't save project image {}", image.getPath());
                throw new IllegalArgumentException(e);
            }
            projectImagesRepository.save(image);
        }
    }

    private ProjectImage getImage(MultipartFile file, Project project) {
        if (file == null || project == null) {
            throw new IllegalArgumentException("Can't upload image");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        createDirectoryIfNotExists(PROJECT_IMAGE_PATH);
        return ProjectImage.builder()
                .project(project)
                .path(PROJECT_IMAGE_PATH + UUID.randomUUID() + "." + extension)
                .build();
    }

    private void createDirectoryIfNotExists(String path) {
        if (Files.notExists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("Can't create directory %s", path));
            }
        }
    }

    private Project getProject(ProjectForm form, ProjectStatus projectStatus) {
        if (form == null || projectStatus == null) {
            log.warn("Can't create project - ProjectForm is null or ProjectStatus is null");
            throw new IllegalArgumentException("Can't create project");
        }
        return Project.builder()
                //клиента забиваю в базу в ручную
                // TODO: сделать как-то чтобы тестовый клиент инсертился автоматом
                .author(clientsRepository.getById(CLIENT_FOR_TEST))
                .title(form.getTitle())
                .description(form.getDescription())
                .createdAt(Instant.now())
                .finishDate(LocalDateTime.parse(form.getFinishDate()).toInstant(ZoneOffset.UTC))
                .moneyGoal(form.getMoneyGoal())
                .status(projectStatus)
                .build();
    }

    private ProjectStatus getProjectStatus() {
        return ProjectStatus.builder()
                .description("Simple description")
                .status(ProjectStatus.Status.CONFIRMED)
                .build();
    }
}
