package ru.pcs.crowdfunding.auth.services;

import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;

public interface AuthenticationService {
    AuthenticationInfoDto signUpAuthentication(AuthenticationInfoDto client);

    boolean existEmailInDb(AuthenticationInfoDto client);
}
