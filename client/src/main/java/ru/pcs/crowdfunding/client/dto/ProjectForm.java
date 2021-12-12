package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.ProjectImage;

import javax.validation.constraints.*;
import java.math.BigDecimal;

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
    @NotBlank
    @Size(min = 2, max = 255)
    private String title;

    @NotBlank
    @Size(min = 2, max = 4096)
    private String description;

    @NotBlank
    private String finishDate;

    @NotNull
    @Positive
    private BigDecimal moneyGoal;

    private ProjectImage image;
}
