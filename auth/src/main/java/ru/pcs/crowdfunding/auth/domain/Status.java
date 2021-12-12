package ru.pcs.crowdfunding.auth.domain;

import lombok.*;

import javax.persistence.*;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "status")
public class Status {
    @Id
    @Column(name = "status_id", nullable = false, unique = true)
    private Long statusId;

    @Enumerated(EnumType.STRING)
    private StatusEnum name;

    public enum StatusEnum {
        NOT_CONFIRMED,
        CONFIRMED,
        BANNED,
        DELETED
    }

}