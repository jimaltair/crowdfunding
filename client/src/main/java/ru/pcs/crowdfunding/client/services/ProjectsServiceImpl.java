package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pcs.crowdfunding.client.domain.Client;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static ru.pcs.crowdfunding.client.dto.ProjectDto.from;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectsServiceImpl implements ProjectsService {

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

        Project project = Project.builder()
                .author(clientsRepository.getById(2L))
                .title(form.getTitle())
                .description(form.getDescription())
                .createdAt(Instant.now())
                .finishDate(Instant.now())
                .moneyGoal(BigDecimal.ZERO)
                .status(projectStatusesRepository.getById(1L))
                .build();


        ProjectImage image = ProjectImage.builder()
                .project(project)
                .path("C:\\Users\\isave\\Desktop\\ponomarev_nikolay_java_pcs_21_01_homeworks\\Crowdfunding\\client\\src\\main\\resources\\static\\upload"
                        + UUID.randomUUID() + file.getOriginalFilename())
                .build();


        try {
            log.debug(image.getPath());
            log.info(image.getPath());
            Files.copy(file.getInputStream(), Paths.get(image.getPath()));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        projectsRepository.save(project);
        projectImagesRepository.save(image);
    }
}
