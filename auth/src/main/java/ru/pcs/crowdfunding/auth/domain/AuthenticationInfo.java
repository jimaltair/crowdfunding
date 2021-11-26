package ru.pcs.crowdfunding.auth.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString()
@EqualsAndHashCode()
@Entity
@Table(name = "authentication_info")
public class AuthenticationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "access_token")
    @Lob
    private String accessToken;

    @Column(name = "refresh_token")
    @Lob
    private String refreshToken;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
