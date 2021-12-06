package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.ProjectImage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

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


    public static final String PROJECT_IMAGE_PATH = "./client/src/main/resources/static/project_images/";

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String finishDate;

    @NotNull
    @PositiveOrZero
    private BigDecimal moneyGoal;

    private ProjectImage image;

}
