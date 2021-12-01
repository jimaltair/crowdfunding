package ru.pcs.crowdfunding.client.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDto {
    private Long initiatorId;
    private Instant datetime;
    private Type type;
    private Long debitAccountId;
    private Long creditAccountId;
    private BigDecimal sum;

    public enum Type {
        PAYMENT,
        REFUND,
        TOP_UP,
        WITHDRAW
    }
}
