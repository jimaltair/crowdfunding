package ru.pcs.crowdfunding.tran.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private OperationsRepository operationsRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

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
        operationsRepository.save(Operation.builder()
                        .operationType(OperationType.builder().type(OperationType.Type.PAYMENT).build())
                        .initiator(1l)
                        .datetime(Instant.parse("2017-02-03T11:25:30.00Z"))
                        .debitAccount(Account.builder()
                                .isActive(true)
                                .id(1l)
                                .createdAt(Instant.parse("2017-02-03T11:25:30.00Z"))
                                .modifiedAt(Instant.parse("2018-02-03T11:25:30.00Z"))
                                .build())
                        .creditAccount(Account.builder()
                                .isActive(true)
                                .id(2l)
                                .createdAt(Instant.parse("2018-02-03T11:25:30.00Z"))
                                .modifiedAt(Instant.parse("2019-02-03T11:25:30.00Z"))
                                .build())
                        .id(1l)
                        .sum(BigDecimal.valueOf(500))
                .build());
        operationsRepository.save(Operation.builder()
                .operationType(OperationType.builder().type(OperationType.Type.REFUND).build())
                .initiator(2l)
                .datetime(Instant.parse("2017-02-03T11:25:30.00Z"))
                .debitAccount(Account.builder()
                        .isActive(true)
                        .id(2l)
                        .createdAt(Instant.parse("2018-02-03T11:25:30.00Z"))
                        .modifiedAt(Instant.parse("2019-02-03T11:25:30.00Z"))
                        .build())
                .creditAccount(Account.builder()
                        .isActive(true)
                        .id(1l)
                        .createdAt(Instant.parse("2017-02-03T11:25:30.00Z"))
                        .modifiedAt(Instant.parse("2018-02-03T11:25:30.00Z"))
                        .build())
                .id(2l)
                .sum(BigDecimal.valueOf(500))
                .build());
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("GET /api/account/{id}")
    class GetAccountTest {

        @Test
        void when_getById_thenStatus202andCreatedReturns() throws Exception {
            mockMvc.perform(get("/api/account/1")
                            .header("Transaction", techTranToken)
                    )
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$['success']", is(true)))
                    .andExpect(jsonPath("$['error']", nullValue(null)))
                    .andExpect(jsonPath("$['data'].userId", is(1)))
                    .andExpect(jsonPath("$['data'].createdAt", is("2017-02-03T11:25:30.00Z")))
                    .andExpect(jsonPath("$['data'].modifiedAt", is("2018-02-03T11:25:30.00Z")))
                    .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
                    .andExpect(jsonPath("$['data'].isActive", is(true)));
        }
    }
}