package ru.pcs.crowdfunding.tran.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.domain.OperationType;
import ru.pcs.crowdfunding.tran.dto.AccountDto;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;
import ru.pcs.crowdfunding.tran.repositories.PaymentsRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
//@AutoConfigureTestDatabase
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountsRepository accountsRepository;

    private String techTranToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6Ik1TX0NMSUVOVCIsInN0YXR1cyI6IkFDVElWRSIsImV4cCI6MjIxNjIzOTAyMn0.Aj-UHmdBosUrf12BrXqn3dsGtXwn0QgBF-q6KP-LvpI";
    private String otherTechTranToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjQxNTc5Nzc2fQ.zaAgZjCMUEzML_W-px8al2DQsSOIqemMxDjoRHlQ7MQ";

    @BeforeEach
    void setUp() {
        accountsRepository.save(Account.builder()
                .isActive(true)
                .id(1l)
                .createdAt(Instant.parse("2017-02-03T11:25:30.00Z"))
                .modifiedAt(Instant.parse("2018-02-03T11:25:30.00Z"))
                .build());
        accountsRepository.save(Account.builder()
                .isActive(true)
                .id(2l)
                .createdAt(Instant.parse("2018-02-03T11:25:30.00Z"))
                .modifiedAt(Instant.parse("2019-02-03T11:25:30.00Z"))
                .build());
        AccountDto updateDto = AccountDto.builder()
                .isActive(true)
                .modifiedAt(Instant.parse("2021-02-03T11:25:30.00Z"))
                .build();


    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("GET /api/account/{id}")
    class GetAccountTest {

        @Test
        void when_getById_thenStatus202andCreatedReturns() throws Exception {
            mockMvc.perform(get("/api/account/1")
                            .header("Authorization", techTranToken))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$['success']", is(true)))
                    .andExpect(jsonPath("$['error']", nullValue(null)))
                    .andExpect(jsonPath("$['data'].id", is(1)))
                    .andExpect(jsonPath("$['data'].createdAt", is("2017-02-03T11:25:30Z")))
                    .andExpect(jsonPath("$['data'].modifiedAt", is("2018-02-03T11:25:30Z")))
                    .andExpect(jsonPath("$['data'].isActive", is(true)));
        }

        @Test
        void when_accountDoesNotExists_thenStatus404() throws Exception {
            mockMvc.perform(get("/api/account/3")
                            .header("Authorization", techTranToken))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['success']", is(false)));
        }

        @Test
        void when_getById_thenStatus403() throws Exception {
            mockMvc.perform(get("/api/account/1")
                            .header("Authorization", otherTechTranToken))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("GET /api/account/{id}/balance")
    class getBalanceByIdTest {

        @Test
        void when_getById_thenStatus202andCreatedReturns() throws Exception {
            mockMvc.perform(get("/api/account/1/balance?date=1638806337")
                            .header("Authorization", techTranToken))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$['success']", is(true)))
                    .andExpect(jsonPath("$['error']", nullValue(null)))
                    .andExpect(jsonPath("$['data'].accountId", is(1)))
                    .andExpect(jsonPath("$['data'].balance", is(0)));
        }

        @Test
        void when_accountDoesNotExists_thenStatus404() throws Exception {
            mockMvc.perform(get("/api/account/3/balance?date=1638806337")
                            .header("Authorization", techTranToken))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['success']", is(false)));
        }

        @Test
        void when_getById_thenStatus403() throws Exception {
            mockMvc.perform(get("/api/account/1/balance?date=1638806337")
                            .header("Authorization", otherTechTranToken))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("GET /api/account/{id}/contributorsCount")
    class getContributorsCount {

        @Test
        void when_getById_thenStatus200() throws Exception {
            mockMvc.perform(get("/api/account/1/contributorsCount?date=1638806337")
                            .header("Authorization", techTranToken))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$['success']", is(true)))
                    .andExpect(jsonPath("$['error']", nullValue(null)))
                    .andExpect(jsonPath("$['data']", is(0)));
        }

        @Test
        void when_accountDoesNotExists_thenStatus404() throws Exception {
            mockMvc.perform(get("/api/account/3/contributorsCount?date=1638806337")
                            .header("Authorization", techTranToken))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['success']", is(false)));
        }

        @Test
        void when_getById_thenStatus403() throws Exception {
            mockMvc.perform(get("/api/account/3/contributorsCount?date=1638806337")
                            .header("Authorization", otherTechTranToken))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("POST /api/account")
    class CreateAccountTest {

        @Test
        void when_create_Account_then_Status201_and_ResponseReturnsAccountionInfo() throws Exception {
            mockMvc.perform(get("/api/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", techTranToken)
                    )
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$['success']", is(true)))
                    .andExpect(jsonPath("$['error']",  nullValue(null)));
        }

        @Test
        void when_create_Account_with_Bad_Token_then_Status201_and_ResponseReturnsAccount_Info() throws Exception {
            mockMvc.perform(get("/api/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", otherTechTranToken)

                    )
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }
}