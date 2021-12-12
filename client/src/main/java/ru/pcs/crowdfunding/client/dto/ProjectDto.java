package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Project;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private Long id;

    private String authorName;

    private String title;
    private String description;

    private Instant createdAt;
    private Instant finishDate;

    private BigDecimal moneyGoal;
    private BigDecimal moneyCollected;
    private Long contributorsCount;

    private List<Long> imagesIds;

    private String status;
    private Long accountId;

    public static ProjectDto from(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .authorName(project.getAuthor().getFirstName() + " " + project.getAuthor().getLastName())
                .title(project.getTitle())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .finishDate(project.getFinishDate())
                .moneyGoal(project.getMoneyGoal())
                .status(project.getStatus().getStatus().toString())
                .accountId(project.getAccountId())
                .build();
    }

    public static List<ProjectDto> from(List<Project> projects) {
        return projects.stream().map(ProjectDto::from).collect(Collectors.toList());
    }

}