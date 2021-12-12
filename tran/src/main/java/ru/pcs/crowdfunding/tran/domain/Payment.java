package ru.pcs.crowdfunding.tran.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString()
@EqualsAndHashCode()
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "sum_payment", nullable = false)
    private BigDecimal sum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @Column(name = "date_time", nullable = false)
    private Instant datetime;

}