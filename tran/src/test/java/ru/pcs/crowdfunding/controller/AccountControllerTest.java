package ru.pcs.crowdfunding.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;
import ru.pcs.crowdfunding.tran.services.AccountService;

import java.io.*;
import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
class AccountControllerTest {
    static final Long TEST_CLIENT_ID = 10L;
    static final String TEST_CLIENT_FIRST_NAME = "Ivan";
    static final String TEST_CLIENT_LAST_NAME = "Ivanov";
    static final String TEST_CLIENT_COUNTRY = "Russia";
    static final String TEST_CLIENT_CITY = "Elabuga";
    static final String TEST_CLIENT_EMAIL = "testEmail@testEmail.com";
    static final String TEST_CLIENT_PASSWORD = "testPassword!";
    static final Long TEST_ACCOUNT_ID = 50L;
    static final Instant TIME_CREATED = Instant.parse("2017-02-03T11:25:30.00Z");
    static final Instant TIME_MODIFIED = Instant.parse("2018-07-12T11:25:30.00Z");
    static final Instant TIME_OPERATION = Instant.parse("2013-12-03T11:25:30.00Z");




    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountsRepository accountsRepositoryMock;

    @MockBean
    private OperationsRepository operationsRepositoryMock;

    @BeforeEach
    public void beforeEach(){
        accountsRepositoryMock.save(Account.builder()
                .id(1l)
                .createdAt(TIME_CREATED)
                .isActive(true)
                .modifiedAt(TIME_MODIFIED)
                .build());
        accountsRepositoryMock.save(Account.builder()
                .id(2l)
                .createdAt(TIME_MODIFIED)
                .isActive(true)
                .modifiedAt(TIME_CREATED)
                .build());

        operationsRepositoryMock.save(Operation.builder()
                        .creditAccount(Account.builder()
                                .id(1l)
                                .createdAt(TIME_CREATED)
                                .isActive(true)
                                .modifiedAt(TIME_MODIFIED)
                                .build())
                        .debitAccount(Account.builder()
                                .id(2l)
                                .createdAt(TIME_MODIFIED)
                                .isActive(true)
                                .modifiedAt(TIME_CREATED)
                                .build())
                        .id(1l)
                        .datetime(TIME_OPERATION)
                        .initiator(1l)
                        .operationType(Operation.builder().build().getOperationType())
                .build());

        Optional<AccountDto> testAuthInfo = Optional.of(AccountDto.builder()
                .id(1l)
                        .isActive(true)
                        .createdAt(TIME_CREATED)
                        .modifiedAt(TIME_MODIFIED)
                .build());
        // имитируем возврат данных существующего пользователя
        when(accountService.findById(1L)).thenReturn(testAuthInfo);
        // имитируем возврат данных несуществующего пользователя

    }

    @Test
    void getAccount() throws Exception{
        mockMvc.perform(get("/api/account/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign Up Page")));
    }



    @Test
    void whenFormIsCorrect_thenSignUpAndRedirect () throws Exception {
       MvcResult mvcResult =  mockMvc.perform(post("/signUp")
                    .param("firstName", TEST_CLIENT_FIRST_NAME)
                    .param("lastName", TEST_CLIENT_LAST_NAME)
                    .param("country", TEST_CLIENT_COUNTRY)
                    .param("city", TEST_CLIENT_CITY)
                    .param("email", TEST_CLIENT_EMAIL)
                    .param("password", TEST_CLIENT_PASSWORD))
                .andDo(print())
               .andExpect(status().isFound())
               .andExpect(redirectedUrl("/clients/" + TEST_CLIENT_ID))
               .andReturn();

    }
}