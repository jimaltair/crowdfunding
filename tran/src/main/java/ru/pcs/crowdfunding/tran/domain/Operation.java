package ru.pcs.crowdfunding.tran.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_type", nullable = false)
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account_id", nullable = false)
    private Account debitAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account_id", nullable = false)
    private Account creditAccount;

    @Column(name = "sum_payment", nullable = false)
    private BigDecimal sum;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operation")
    private List<Payment> payments;
}
