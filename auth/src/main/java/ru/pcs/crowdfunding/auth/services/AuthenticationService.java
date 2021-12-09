package ru.pcs.crowdfunding.auth.services;

import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.SignInForm;

public interface AuthenticationService {
    AuthenticationInfoDto signUpAuthentication(AuthenticationInfoDto client);
    boolean signInAuthentication(SignInForm client);
    boolean existEmailInDb(AuthenticationInfoDto client);
}
