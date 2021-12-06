package ru.pcs.crowdfunding.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorizationInfoDto {

    private Long userId;
    private Role.RoleEnum role;
    private Status.StatusEnum status;
    private String accessToken;

    public static AuthorizationInfoDto from(AuthorizationInfo authorizationInfo, Role role, Status status) {
        return AuthorizationInfoDto.builder()
                .userId(authorizationInfo.getUserId())
                .role(role.getName())
                .status(status.getName())
                .accessToken(authorizationInfo.getAccessToken())
                .build();
    }
}
