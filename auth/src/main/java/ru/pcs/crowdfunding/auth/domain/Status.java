package ru.pcs.crowdfunding.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
