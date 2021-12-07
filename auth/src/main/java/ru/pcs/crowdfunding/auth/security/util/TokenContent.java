package ru.pcs.crowdfunding.auth.security.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenContent {
    private Long userId;
    // TODO: add roles
}
