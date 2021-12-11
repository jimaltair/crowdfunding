package ru.pcs.crowdfunding.auth.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;

import java.util.Arrays;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
@DisplayName("AuthController")
class AuthControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationInfosRepository authenticationInfosRepository;

    private String techAuthToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6Ik1TX0NMSUVOVCIsInN0YXR1cyI6IkFDVElWRSIsImV4cCI6MjIxNjIzOTAyMn0.Aj-UHmdBosUrf12BrXqn3dsGtXwn0QgBF-q6KP-LvpI";
    private String otherTechAuthToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjQxNTc5Nzc2fQ.zaAgZjCMUEzML_W-px8al2DQsSOIqemMxDjoRHlQ7MQ";

    @BeforeEach
    void setUp() {
        authenticationInfosRepository.save(AuthenticationInfo.builder()
            .userId(1L)
            .email("email@email.com")
            .password("111!")
            .refreshToken("refresh_test_token")
            .isActive(true)
            .build());
    }

    @AfterEach
    public void tearDown() {
        authenticationInfosRepository.deleteAll();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("GET /api/auth/{id}")
    class GetAuthTest {

        @Test
        void when_getById_thenStatus202andCreatedReturns() throws Exception {
            mockMvc.perform(get("/api/auth/1")
                    .header("Authorization", techAuthToken)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$['success']", is(true)))
                .andExpect(jsonPath("$['error']", nullValue(null)))
                .andExpect(jsonPath("$['data'].userId", is(1)))
                .andExpect(jsonPath("$['data'].email", is("email@email.com")))
                .andExpect(jsonPath("$['data'].password", is("111!")))
                .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
                .andExpect(jsonPath("$['data'].isActive", is(true)));
        }

        @Test
        void when_getNotExistedUser_thenStatus404AndErrorMessageReturns() throws Exception {
            mockMvc.perform(get("/api/auth/100")
                    .header("Authorization", techAuthToken)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['success']", is(false)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Client with id 100 not found"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

        @Test()
        void when_techAuthTokenOther_thenStatus403AndErrorMessageReturns() throws Exception {
            mockMvc.perform(get("/api/auth/1")
                    .header("Authorization", otherTechAuthToken)
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("DELETE /api/auth/{id}")
    class DeleteAuthTest {

        @Test
        void when_deleteById_then_Status202_and_ResponseReturnsAuthenticationInfo() throws Exception {
            mockMvc.perform(delete("/api/auth/1")
                    .header("Authorization", techAuthToken)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$['success']", is(true)))
                .andExpect(jsonPath("$['error']", nullValue(null)))
                .andExpect(jsonPath("$['data'].userId", is(1)))
                .andExpect(jsonPath("$['data'].email", is("email@email.com")))
                .andExpect(jsonPath("$['data'].password", is("111!")))
                .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
                .andExpect(jsonPath("$['data'].isActive", is(false)));
        }

        @Test
        void when_deleteNotExistedUser_then_Status404_and_ErrorMessageReturns() throws Exception {
            mockMvc.perform(delete("/api/auth/3")
                    .header("Authorization", techAuthToken)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['success']", is(false)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Can't delete. Client with id 3 not found"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

        @Test()
        void when_techAuthTokenOther_thenStatus403AndErrorMessageReturns() throws Exception {
            mockMvc.perform(delete("/api/auth/3")
                    .header("Authorization", otherTechAuthToken)
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }
}
