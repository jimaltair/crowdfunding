package ru.pcs.crowdfunding.auth.services;

import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;

public interface AuthenticationService {
    void signUpAuthentication(AuthenticationInfoDto client);
}
