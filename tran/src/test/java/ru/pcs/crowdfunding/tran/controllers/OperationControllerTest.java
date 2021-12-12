package ru.pcs.crowdfunding.tran.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.tran.domain.Account;
import ru.pcs.crowdfunding.tran.domain.Operation;
import ru.pcs.crowdfunding.tran.domain.OperationType;
import ru.pcs.crowdfunding.tran.repositories.AccountsRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationTypesRepository;
import ru.pcs.crowdfunding.tran.repositories.OperationsRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase
@DisplayName("OperationController")
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OperationsRepository operationsRepository;

    @Autowired
    private OperationTypesRepository operationTypesRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    private String techTranToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6Ik1TX0NMSUVOVCIsInN0YXR1cyI6IkFDVElWRSIsImV4cCI6MjIxNjIzOTAyMn0.Aj-UHmdBosUrf12BrXqn3dsGtXwn0QgBF-q6KP-LvpI";
    private String otherTechTranToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjQxNTc5Nzc2fQ.zaAgZjCMUEzML_W-px8al2DQsSOIqemMxDjoRHlQ7MQ";

    @BeforeEach
    void setUp() {


        //сетишь в БД типы операции
        operationTypesRepository.save(OperationType.builder()
                .id(1L)
                .description("adding funds to your account")
                .type(OperationType.Type.TOP_UP)
                .build()
        );
        operationTypesRepository.save(OperationType.builder()
                .id(2L)
                .description("transfer of funds within the platform")
                .type(OperationType.Type.PAYMENT)
                .build()
        );
        operationTypesRepository.save(OperationType.builder()
                .id(3L)
                .description("refund within the platform")
                .type(OperationType.Type.REFUND)
                .build()
        );
        operationTypesRepository.save(OperationType.builder()
                .id(4L)
                .description("withdrawal of funds from the platform")
                .type(OperationType.Type.WITHDRAW)
                .build()
        );

        accountsRepository.save(Account.builder()
            .isActive(true)
            .id(1l)
            .createdAt(Instant.parse("2017-02-03T11:25:30.00Z"))
            .modifiedAt(Instant.parse("2018-02-03T11:25:30.00Z"))
            .build());


        OperationType testType = OperationType.builder()
            .id(2L)
            .description("transfer of funds within the platform")
            .type(OperationType.Type.PAYMENT)
            .build();



//        operationsRepository.save(Operation.builder()
//                .operationType(OperationType.builder().type(testType.getType()).build())
//                .initiator(1l)
//                .datetime(Instant.parse("2017-02-03T11:25:30.00Z"))
//                .debitAccount(Account.builder()
//                        .isActive(true)
//                        .id(1l)
//                        .createdAt(Instant.parse("2017-02-03T11:25:30.00Z"))
//                        .modifiedAt(Instant.parse("2018-02-03T11:25:30.00Z"))
//                        .build())
//                .creditAccount(Account.builder()
//                        .isActive(true)
//                        .id(2l)
//                        .createdAt(Instant.parse("2018-02-03T11:25:30.00Z"))
//                        .modifiedAt(Instant.parse("2019-02-03T11:25:30.00Z"))
//                        .build())
//                .id(1l)
//                .sum(BigDecimal.valueOf(500))
//                .build());
//
//
//
//        operationsRepository.save(Operation.builder()
//                .operationType(OperationType.builder().type(testType.getType()).build())
//                .initiator(2l)
//                .datetime(Instant.parse("2017-02-03T11:25:30.00Z"))
//                .debitAccount(Account.builder()
//                        .isActive(true)
//                        .id(2l)
//                        .createdAt(Instant.parse("2018-02-03T11:25:30.00Z"))
//                        .modifiedAt(Instant.parse("2019-02-03T11:25:30.00Z"))
//                        .build())
//                .creditAccount(Account.builder()
//                        .isActive(true)
//                        .id(1l)
//                        .createdAt(Instant.parse("2017-02-03T11:25:30.00Z"))
//                        .modifiedAt(Instant.parse("2018-02-03T11:25:30.00Z"))
//                        .build())
//                .id(2l)
//                .sum(BigDecimal.valueOf(500))
//                .build());
    }
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("POST /api/operation")
    class CreateOperationTest {

        @Test
        void when_create_Operation_then_Status201_and_ResponseReturnsAccount_Info() throws Exception {
            mockMvc.perform(post("/api/operation")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", techTranToken)
                            .content("{\"initiatorId\": 1, \"operationType\": \"TOP_UP\", \"debitAccountId\": \"1\", \"sum\": \"200.00\"}")
                    )
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$['success']", is(true)))
                    .andExpect(jsonPath("$['error']",  nullValue(null)));
        }
    }
}