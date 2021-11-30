package ru.pcs.crowdfunding.auth.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private Boolean success;
    private List<String> error;
    private Object data;
}
