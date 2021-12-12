package ru.pcs.crowdfunding.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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