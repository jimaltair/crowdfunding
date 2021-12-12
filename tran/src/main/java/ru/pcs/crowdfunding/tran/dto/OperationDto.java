package ru.pcs.crowdfunding.tran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.tran.domain.Operation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDto {

    private Long id;
    private Long initiatorId;
    private Instant datetime;
    private String operationType;
    private Long debitAccountId;
    private Long creditAccountId;
    private BigDecimal sum;

    public static OperationDto from (Operation operation) {
        return OperationDto.builder()
                .id(operation.getId())
                .initiatorId(operation.getInitiator())
                .datetime(operation.getDatetime())
                .operationType(operation.getOperationType().getType().toString())
                .debitAccountId(operation.getDebitAccount().getId())
                .creditAccountId(operation.getCreditAccount().getId())
                .sum(operation.getSum())
                .build();
    }

    public static List<OperationDto> from (List<Operation> operations){
        return operations.stream()
                .map(OperationDto::from)
                .collect(Collectors.toList());
    }

}