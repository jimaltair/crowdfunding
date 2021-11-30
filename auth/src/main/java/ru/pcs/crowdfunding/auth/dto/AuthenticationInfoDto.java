package ru.pcs.crowdfunding.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationInfoDto {

    private Long userId;
    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;
    private Boolean isActive;

    public static AuthenticationInfoDto from(AuthenticationInfo authenticationInfo) {
        return AuthenticationInfoDto.builder()
                .userId(authenticationInfo.getUserId())
                .email(authenticationInfo.getEmail())
                .password(authenticationInfo.getPassword())
                .accessToken(authenticationInfo.getAccessToken())
                .refreshToken(authenticationInfo.getRefreshToken())
                .isActive(true)
                .build();
    }

    public static List<AuthenticationInfoDto> from(Collection<AuthenticationInfo> authenticationInfo) {
        return authenticationInfo.stream()
                .map(AuthenticationInfoDto::from)
                .collect(Collectors.toList());
    }
}
