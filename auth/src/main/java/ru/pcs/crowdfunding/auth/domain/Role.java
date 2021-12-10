package ru.pcs.crowdfunding.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public static Role getRoleFromString(String input) {
        Role role;
        switch (input) {
            case "USER":
                role = new Role(1L, RoleEnum.USER);
                break;
            case "MODERATOR":
                role = new Role(2L, RoleEnum.MODERATOR);
                break;
            case "ADMIN":
                role = new Role(3L, RoleEnum.ADMIN);
                break;
            default:
                throw new IllegalArgumentException("Unexpected role: " + input);
        }
        return role;
    }
}
