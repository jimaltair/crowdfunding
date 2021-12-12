package ru.pcs.crowdfunding.auth.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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