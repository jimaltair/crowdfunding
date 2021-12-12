package ru.pcs.crowdfunding.client.dto;

import lombok.*;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAuthInfoResponseDto {

    private Long userId;

    private String email;

    private String password;

    private String accessToken;
    private String refreshToken;
    private Boolean isActive;

}