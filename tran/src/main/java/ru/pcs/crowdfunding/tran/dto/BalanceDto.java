package ru.pcs.crowdfunding.tran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDto {

    private Long accountId;
    private BigDecimal balance;

}