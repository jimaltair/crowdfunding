package ru.pcs.crowdfunding.auth.security.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pcs.crowdfunding.auth.domain.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenContent {
    private Long userId;
    private Role role;
}
