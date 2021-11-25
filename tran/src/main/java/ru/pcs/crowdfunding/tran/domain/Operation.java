package ru.pcs.crowdfunding.tran.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "initiator", nullable = false)
    private Long initiator;

    @Column(name = "date_time", nullable = false)
    private Instant datetime;

    @ManyToOne()
    @JoinColumn(name = "operation_type", nullable = false)
    private OperationType operationType;

    @ManyToOne()
    @JoinColumn(name = "debit_account_id", nullable = false)
    private Account debitAccount;

    @ManyToOne()
    @JoinColumn(name = "credit_account_id", nullable = false)
    private Account creditAccount;

    @Column(name = "sum_payment", nullable = false)
    private BigDecimal sum;
}
