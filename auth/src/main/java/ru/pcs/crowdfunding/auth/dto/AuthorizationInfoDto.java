package ru.pcs.crowdfunding.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorizationInfoDto {

    private Long userId;
    private Role.RoleEnum role;
    private Status.StatusEnum status;
    private String accessToken;

    public static AuthorizationInfoDto from(AuthorizationInfo authorizationInfo) {
        return AuthorizationInfoDto.builder()
                .userId(authorizationInfo.getUserId())
                .role(Role.RoleEnum.USER)
                .status(Status.StatusEnum.CONFIRMED)
                .accessToken(authorizationInfo.getAccessToken())
                .build();
    }

    public static List<AuthorizationInfoDto> from(Collection<AuthorizationInfo> authorizationInfo) {
        return authorizationInfo.stream()
                .map(AuthorizationInfoDto::from)
                .collect(Collectors.toList());
    }
}
