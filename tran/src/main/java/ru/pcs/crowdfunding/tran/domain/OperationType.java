package ru.pcs.crowdfunding.tran.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "operation_type")
public class OperationType {

    public enum Type {
        PAYMENT,
        REFUND,
        TOP_UP,
        WITHDRAW
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "description", length = 4096)
    private String description;

}