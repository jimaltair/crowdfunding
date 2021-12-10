package ru.pcs.crowdfunding.auth.dto;

import lombok.*;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationInfoDto {

    @NotNull
    private Long userId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 7, max = 20)
    @ValidPassword
    private String password;

    private String accessToken;
    private String refreshToken;
    private Boolean isActive;

    public static AuthenticationInfoDto from(AuthenticationInfo authenticationInfo) {
        return AuthenticationInfoDto.builder()
                .userId(authenticationInfo.getUserId())
                .email(authenticationInfo.getEmail())
                .password(authenticationInfo.getPassword())
                .refreshToken(authenticationInfo.getRefreshToken())
                .isActive(authenticationInfo.isActive())
                .build();
    }

    public static List<AuthenticationInfoDto> from(Collection<AuthenticationInfo> authenticationInfo) {
        return authenticationInfo.stream()
                .map(AuthenticationInfoDto::from)
                .collect(Collectors.toList());
    }
}
