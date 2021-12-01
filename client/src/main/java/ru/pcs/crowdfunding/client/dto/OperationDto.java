package ru.pcs.crowdfunding.client.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDto {
    private Long id;
    private Long clientId;
    private Instant datetime;
    private OperationType operationType;
    private Account debitAccount;
    private Account creditAccount;
    private BigDecimal sum;
    private List<Payment> payments;
}


