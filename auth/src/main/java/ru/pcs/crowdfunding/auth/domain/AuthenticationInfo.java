package ru.pcs.crowdfunding.auth.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authentication_info")
public class AuthenticationInfo {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "refresh_token", length = 4096)
    private String refreshToken;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    private Status status;

}