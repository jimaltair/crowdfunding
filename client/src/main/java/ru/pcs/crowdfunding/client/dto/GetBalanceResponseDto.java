package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetBalanceResponseDto {

    private Long accountId;
    private BigDecimal balance;

}