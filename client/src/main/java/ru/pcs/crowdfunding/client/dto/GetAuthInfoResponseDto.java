package ru.pcs.crowdfunding.client.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAuthInfoResponseDto {

    private Long userId;

    private String email;

    private String password;

    private String accessToken;
    private String refreshToken;
    private Boolean isActive;

}