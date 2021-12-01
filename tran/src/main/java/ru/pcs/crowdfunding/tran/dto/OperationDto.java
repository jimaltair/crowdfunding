package ru.pcs.crowdfunding.tran.dto;

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
    private String operationType;
    private Long debitAccountId;
    private Long creditAccountId;
    private BigDecimal sum;

    public static OperationDto from (Operation operation) {
        return new OperationDto(); //временно
    }
}
