package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.SignUpForm;

public interface SignUpService {

    /**
     * есть предложение использовать над каждым методом @Retryable(value = Exception.class)
     */
    SignUpForm signUp(SignUpForm form);

}