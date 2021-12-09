package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.Project;
import ru.pcs.crowdfunding.client.domain.ProjectImage;
import ru.pcs.crowdfunding.client.domain.ProjectStatus;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.dto.ProjectDto;
import ru.pcs.crowdfunding.client.dto.ProjectForm;
import ru.pcs.crowdfunding.client.dto.ProjectImageDto;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectImagesRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectStatusesRepository;
import ru.pcs.crowdfunding.client.repositories.ProjectsRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectsRepository projectsRepository;

    private final ClientsRepository clientsRepository;

    private final ProjectStatusesRepository projectStatusesRepository;

    private final ProjectImagesRepository projectImagesRepository;

    private final TransactionServiceClient transactionServiceClient;

    @Override
    public Optional<ProjectDto> findById(Long id) {
        Optional<Project> project = projectsRepository.findById(id);
        if (!project.isPresent()) {
            log.warn("project with id = {} not found", id);
            return Optional.empty();
        }

        Long accountId = project.get().getAccountId();
        BigDecimal balance = transactionServiceClient.getBalance(accountId);
        Long donorsCount = transactionServiceClient.getContributorsCount(accountId);
        List<Long> imagesIds = project.get().getImages().stream()
                .map(ProjectImage::getId)
                .collect(Collectors.toList());

        ProjectDto projectDto = ProjectDto.from(project.get());
        projectDto.setMoneyCollected(balance);
        projectDto.setContributorsCount(donorsCount);
        projectDto.setImagesIds(imagesIds);
        return Optional.of(projectDto);
    }

    @Override
    public Optional<Long> createProject(ProjectForm form, MultipartFile file) {
        log.info("Try to create project from {}", form.toString());
        ProjectStatus projectStatus = projectStatusesRepository.getByStatus(ProjectStatus.Status.CONFIRMED);
        Project project = getProject(form, projectStatus);

        // создаём запрос в transaction-service на создание счёта для проекта
        log.info("Try to create account for project");
        CreateAccountResponse response = transactionServiceClient.createAccount();
        Long projectAccountId = response.getId();
        log.info("Was created new account for project with id={}", projectAccountId);
        project.setAccountId(projectAccountId);

        projectsRepository.save(project);

        if (!file.isEmpty()) {
            log.info("Try to save image with name={}", file.getOriginalFilename());
            ProjectImage projectImage = getImage(file, project);
            Long id = projectImagesRepository.save(projectImage).getId();
            log.info("Image was saved with id={}", id);
        }

        return Optional.of(project.getId());
    }

    @Override
    public void updateProject(Long id, ProjectForm form, MultipartFile file) {
        log.info("Try to update project with id={}", id);
        Optional<Project> project = projectsRepository.getProjectById(id);
        if (!project.isPresent()) {
            log.error("Project with id={} was not found", id);
            throw new IllegalArgumentException("Project was not found");
        }
        Project existedProject = project.get();
        log.info("The existed {}", existedProject);
        log.info("The new data from {}", form.toString());
        if (form.getTitle() != null) {
            existedProject.setTitle(form.getTitle());
        }
        if (form.getDescription() != null) {
            existedProject.setDescription(form.getDescription());
        }
        if (form.getFinishDate() != null) {
            existedProject.setFinishDate(LocalDateTime.parse(form.getFinishDate()).toInstant(ZoneOffset.UTC));
        }
        projectsRepository.save(existedProject);
        log.info("Project data was updated successfully");

        if (!file.isEmpty()) {
            log.info("Try to update project image with new image with name={}", file.getOriginalFilename());
//            ProjectImage projectImage = projectImagesRepository.findProjectImageByProject(existedProject);
            // по идее, здесь должна обновиться фотка проекта, нужно тестировать
            ProjectImage newProjectImage = getImage(file, existedProject);
            projectImagesRepository.save(newProjectImage);
            log.info("Project image was updated successfully");
        }

    }

    @Override
    public Optional<ProjectImageDto> getImageById(Long id) {
        return projectImagesRepository.findById(id)
                .map(image -> ProjectImageDto.builder()
                        .format(FilenameUtils.getExtension(image.getName()))
                        .content(image.getContent())
                        .build());
    }

    private ProjectImage getImage(MultipartFile file, Project project) {
        try {
            return ProjectImage.builder()
                    .content(file.getBytes())
                    .name(file.getOriginalFilename())
                    .project(project)
                    .build();
        } catch (IOException e) {
            log.error("Can't save image {}", file.getOriginalFilename());
            throw new IllegalStateException(e);
        }
    }

    /**
     * @deprecated в текущей реализации (сохранение картинки в базу) данный метод не используется
     */
    private void createDirectoryIfNotExists(String path) {
        if (Files.notExists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path).toAbsolutePath().normalize());
            } catch (IOException e) {
                log.error("Can't create directory {}", path);
                throw new IllegalArgumentException(e);
            }
        }
    }

    private Project getProject(ProjectForm form, ProjectStatus projectStatus) {
        if (form == null || projectStatus == null) {
            log.error("Can't create project - ProjectForm is null or ProjectStatus is null");
            throw new IllegalArgumentException("Can't create project");
        }

        // получаем автора проекта через временный метод, пока не работает функционал,
        // позволяющий создать проект уже зарегистрированному пользователю
        Client userForTesting = getUserForTesting();
        log.info("Use the test {} to create project", userForTesting.toString());

        return Project.builder()
                .author(userForTesting) // временно используем тестового пользователя в качестве автора
                .title(form.getTitle())
                .description(form.getDescription())
                .createdAt(Instant.now())
                .finishDate(LocalDateTime.parse(form.getFinishDate()).toInstant(ZoneOffset.UTC))
                .moneyGoal(form.getMoneyGoal())
                .status(projectStatus)
                .build();
    }

    /**
     * Временный метод, создающий пользователя в базе для последующего использования в качестве автора проекта,
     * если в базе на данный момент пусто. В противном случае возвращает первого по порядку пользователя.
     *
     * @return Client - сущность, представляющую пользователя данного сервиса.
     */
    private Client getUserForTesting() {
        List<Client> users = clientsRepository.findAll();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return clientsRepository.save(Client.builder()
                    .firstName("Иван")
                    .lastName("Иванов")
                    .city("Москва")
                    .country("Россия")
                    .build());
        }
    }
}
