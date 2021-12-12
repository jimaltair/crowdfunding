package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.domain.ProjectImage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectForm {

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

    private Long clientId;
}
