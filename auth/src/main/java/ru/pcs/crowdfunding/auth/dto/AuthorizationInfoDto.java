package ru.pcs.crowdfunding.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorizationInfoDto {

    private Long userId;
    private AuthorizationInfo.Role role;
    private AuthorizationInfo.Status status;

    public static AuthorizationInfoDto from(AuthorizationInfo authorizationInfo) {
        return AuthorizationInfoDto.builder()
                .userId(authorizationInfo.getUserId())
                .role(authorizationInfo.getRole())
                .status(authorizationInfo.getStatus())
                .build();
    }

    public static List<AuthorizationInfoDto> from(Collection<AuthorizationInfo> authorizationInfo) {
        return authorizationInfo.stream()
                .map(AuthorizationInfoDto::from)
                .collect(Collectors.toList());
    }
}
