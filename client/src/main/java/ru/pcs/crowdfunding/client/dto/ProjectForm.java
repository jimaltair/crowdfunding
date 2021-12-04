package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.ProjectImage;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 28.11.2021
 * Crowdfunding
 *
 * @author Nikolay Ponomarev
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectForm {

    public static final String PROJECT_IMAGE_PATH = "./src/main/resources/static/upload";

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String finishDate;

    private BigDecimal moneyGoal;

    private ProjectImage image;

}
