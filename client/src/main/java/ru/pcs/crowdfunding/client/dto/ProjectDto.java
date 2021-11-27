package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Project;

import java.math.BigDecimal;
import java.time.Instant;

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

    private String status;

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
                .build();
    }
}
