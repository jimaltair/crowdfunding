package ru.pcs.crowdfunding.client.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.pcs.crowdfunding.client.api.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.api.TransactionServiceClient;
import ru.pcs.crowdfunding.client.domain.Client;
import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignUpResponse;
import ru.pcs.crowdfunding.client.dto.CreateAccountResponse;
import ru.pcs.crowdfunding.client.repositories.ClientsRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
class SignUpControllerTest {

    static final Long TEST_CLIENT_ID = 10L;
    static final String TEST_CLIENT_FIRST_NAME = "Ivan";
    static final String TEST_CLIENT_LAST_NAME = "Ivanov";
    static final String TEST_CLIENT_COUNTRY = "Russia";
    static final String TEST_CLIENT_CITY = "Elabuga";
    static final String TEST_CLIENT_EMAIL = "testEmail@testEmail.com";
    static final String TEST_CLIENT_PASSWORD = "testPassword!";
    static final Long TEST_ACCOUNT_ID = 50L;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientsRepository clientsRepositoryMock;

    @MockBean
    private AuthorizationServiceClient authorizationServiceClientMock;

    @MockBean
    private TransactionServiceClient transactionServiceClientMock;


    @BeforeEach
    public void beforeEach(){

        //Mock the repository for case "Save client first time without account_id"
        when(clientsRepositoryMock.save(Client.builder()
                .firstName(TEST_CLIENT_FIRST_NAME)
                .lastName(TEST_CLIENT_LAST_NAME)
                .country(TEST_CLIENT_COUNTRY)
                .city(TEST_CLIENT_CITY)
                .build()))
                .thenReturn(Client.builder()
                        .id(TEST_CLIENT_ID)
                        .firstName(TEST_CLIENT_FIRST_NAME)
                        .lastName(TEST_CLIENT_LAST_NAME)
                        .country(TEST_CLIENT_COUNTRY)
                        .city(TEST_CLIENT_CITY)
                        .build());

        //Mock the repository for case "Save client when hi has account_id"
        when(clientsRepositoryMock.save(Client.builder()
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


        //Mock the repository, getting client by client_id
        when(clientsRepositoryMock.findById(TEST_CLIENT_ID))
                .thenReturn(Optional.of(
                        Client.builder()
                                .id(TEST_CLIENT_ID)
                                .firstName(TEST_CLIENT_FIRST_NAME)
                                .lastName(TEST_CLIENT_LAST_NAME)
                                .country(TEST_CLIENT_COUNTRY)
                                .city(TEST_CLIENT_CITY)
                                .accountId(TEST_ACCOUNT_ID)
                                .build()));

        //Mock the authorisationService
        when(authorizationServiceClientMock.signUp(AuthSignUpRequest.builder()
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

        when(transactionServiceClientMock.createAccount())
                .thenReturn(CreateAccountResponse.builder()
                        .id(TEST_ACCOUNT_ID)
                        .isActive(true)
                        .build());

    }

    @AfterEach
    public void tearDown() {
        clientsRepositoryMock.deleteAll();
    }

    @Test
    void getSignUpPage() throws Exception{
        mockMvc.perform(get("/signUp"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Заполните форму регистрации:")));
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