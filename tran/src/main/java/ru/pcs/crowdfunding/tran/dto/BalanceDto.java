package ru.pcs.crowdfunding.tran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BalanceDto {
    private Long accountId;
    private BigDecimal balance;
}
