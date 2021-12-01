package ru.pcs.crowdfunding.tran.dto;

import lombok.*;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.domain.OperationType;
import ru.pcs.crowdfunding.tran.domain.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDto {
    private Long id;
    private Long initiator;
    private Instant datetime;
    private OperationType operationType;
    private Account debitAccount;
    private Account creditAccount;
    private BigDecimal sum;
    private List<Payment> payments;

    public static OperationDto from(Operation operation) {
        return OperationDto.builder()
                .id(operation.getId())
                .initiator(operation.getInitiator())
                .datetime(operation.getDatetime())
                .operationType(operation.getOperationType())
                .debitAccount(operation.getDebitAccount())
                .creditAccount(operation.getCreditAccount())
                .sum(operation.getSum())
                .payments(operation.getPayments())
                .build();
    }
}
