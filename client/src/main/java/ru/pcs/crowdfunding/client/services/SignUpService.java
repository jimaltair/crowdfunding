package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.SignUpForm;

public interface SignUpService {
    SignUpForm signUp(SignUpForm form);
}
