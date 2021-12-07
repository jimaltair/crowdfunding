package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.client.domain.Image;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String finishDate;

    @NotNull
    @PositiveOrZero
    private BigDecimal moneyGoal;

    private Image image;

}
