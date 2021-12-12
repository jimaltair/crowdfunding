package ru.pcs.crowdfunding.auth.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id", nullable = false, unique = true)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public enum RoleEnum {
        USER,
        MODERATOR,
        ADMIN
    }

}