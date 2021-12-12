package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {

    public enum Type {
        PAYMENT,
        REFUND,
        TOP_UP,
        WITHDRAW
    }

    private Long id;
    private Long initiatorId;
    private Instant datetime;
    private Type operationType;
    private Long debitAccountId;
    private Long creditAccountId;
    private BigDecimal sum;

}