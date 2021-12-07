package ru.pcs.crowdfunding.client.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.*;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;
import ru.pcs.crowdfunding.client.services.SignUpService;

import java.time.Instant;

import java.util.Optional;

import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
class SignUpServiceImplTest {

    static final Long TEST_CLIENT_ID = 10L;
    static final String TEST_CLIENT_FIRST_NAME = "Ivan";
    static final String TEST_CLIENT_LAST_NAME = "Ivanov";
    static final String TEST_CLIENT_COUNTRY = "Russia";
    static final String TEST_CLIENT_CITY = "Elabuga";
    static final String TEST_CLIENT_EMAIL = "testEmail@testEmail.com";
    static final String TEST_CLIENT_PASSWORD = "testPassword";
    static final Long TEST_ACCOUNT_ID = 50L;


    @MockBean
    private ClientsRepository clientsRepository;

    @MockBean
    private AuthorizationServiceClient authorizationServiceClient;

    @MockBean
    private TransactionServiceClient transactionServiceClient;

    @Autowired
    private SignUpService signUpService;

    @BeforeEach
    public void beforeEach(){
        when(clientsRepository.save(Client.builder()
                                        .id(TEST_CLIENT_ID)
                                        .firstName(TEST_CLIENT_FIRST_NAME)
                                        .lastName(TEST_CLIENT_LAST_NAME)
                                        .country(TEST_CLIENT_COUNTRY)
                                        .city(TEST_CLIENT_CITY)
                                        .accountId(TEST_ACCOUNT_ID)
                                        .build()))
                .thenReturn(Client.builder()
                                    .id(TEST_CLIENT_ID)
                                    .firstName(TEST_CLIENT_FIRST_NAME)
                                    .lastName(TEST_CLIENT_LAST_NAME)
                                    .country(TEST_CLIENT_COUNTRY)
                                    .city(TEST_CLIENT_CITY)
                                    .accountId(TEST_ACCOUNT_ID)
                            .build());

        when(clientsRepository.findById(TEST_CLIENT_ID))
                .thenReturn(Optional.of(
                        Client.builder()
                                .id(TEST_CLIENT_ID)
                                .firstName(TEST_CLIENT_FIRST_NAME)
                                .lastName(TEST_CLIENT_LAST_NAME)
                                .country(TEST_CLIENT_COUNTRY)
                                .city(TEST_CLIENT_CITY)
                                .accountId(TEST_ACCOUNT_ID)
                                .build()));

        when(authorizationServiceClient.signUp(AuthSignUpRequest.builder()
                .userId(TEST_CLIENT_ID)
                .email(TEST_CLIENT_EMAIL)
                .password(TEST_CLIENT_PASSWORD)
                .build()))
                .thenReturn(AuthSignUpResponse.builder()
                        .userId(TEST_CLIENT_ID)
                        .isActive(true)
                        .email(TEST_CLIENT_EMAIL)
                        .password("HASH_PASSWORD")
                        .accessToken("Access token")
                        .refreshToken("Refresh token")
                        .build());

        when(transactionServiceClient.createAccount(CreateAccountRequest.builder()
                        .isActive(true)
                        .createdAt(Instant.ofEpochSecond(100000L))
                        .modifiedAt(Instant.ofEpochSecond(100000L))
                        .build()))
                .thenReturn(CreateAccountResponse.builder()
                        .id(TEST_ACCOUNT_ID)
                        .createdAt(Instant.ofEpochSecond(100000L))
                        .modifiedAt(Instant.ofEpochSecond(100000L))
                        .isActive(true)
                        .build());

    }

    @Test
    void signUp_integration_test() {

        SignUpForm form = signUpService.signUp(SignUpForm.builder()
                        .firstName(TEST_CLIENT_FIRST_NAME)
                        .lastName(TEST_CLIENT_LAST_NAME)
                        .country(TEST_CLIENT_COUNTRY)
                        .city(TEST_CLIENT_CITY)
                        .email(TEST_CLIENT_EMAIL)
                        .password(TEST_CLIENT_PASSWORD)
                        .build());

        assertEquals(form.getFirstName(),TEST_CLIENT_FIRST_NAME);
        assertEquals(form.getLastName(), TEST_CLIENT_LAST_NAME);
        assertEquals(form.getCountry(), TEST_CLIENT_COUNTRY);
        assertEquals(form.getCity(), TEST_CLIENT_CITY);
        assertEquals(form.getEmail(), TEST_CLIENT_EMAIL);

        Client client = clientsRepository.findById(TEST_CLIENT_ID).get();

        assertEquals(client.getAccountId(), TEST_ACCOUNT_ID);

    }
}