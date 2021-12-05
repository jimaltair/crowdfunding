package ru.pcs.crowdfunding.auth.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString()
@EqualsAndHashCode()
@Entity
@Table(name = "authorization_info")
public class AuthorizationInfo {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "access_token", length = 4096)
    private String accessToken;


}
