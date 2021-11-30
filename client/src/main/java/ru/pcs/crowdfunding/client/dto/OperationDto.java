package ru.pcs.crowdfunding.client.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDto {
    Long userId;
    BigDecimal sumOperation;
    Long accountIdToTransfer;
}
