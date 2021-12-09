package ru.pcs.crowdfunding.auth.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationController")
public class AuthenticationControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private String techAuthToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6Ik1TX0NMSUVOVCIsInN0YXR1cyI6IkFDVElWRSIsImV4cCI6MjIxNjIzOTAyMn0.Aj-UHmdBosUrf12BrXqn3dsGtXwn0QgBF-q6KP-LvpI";
    private String otherTechAuthToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjQxNTc5Nzc2fQ.zaAgZjCMUEzML_W-px8al2DQsSOIqemMxDjoRHlQ7MQ";

    @BeforeEach
    void setUp() {

        //region POST
        when(authenticationService.existEmailInDb(
            AuthenticationInfoDto.builder()
                .userId(1L)
                .email("email@email.com")
                .password("1111111!")
                .build()
        )).thenReturn(true);

        when(authenticationService.signUpAuthentication(
            AuthenticationInfoDto.builder()
                .userId(1L)
                .email("email@email.com")
                .password("1111111!")
                .build()
        )).thenReturn(
            AuthenticationInfoDto.builder()
                .userId(1L)
                .email("email@email.com")
                .password("1111111!")
                .accessToken("eyJ0eXAiOiJKV1Q")
                .refreshToken("refresh_test_token")
                .isActive(true)
                .build()
        );
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("POST /api/signUp")
    class GetSignUpTest {

        @Disabled
        @Test
        void when_usedУEmail_then_Status201_and_ResponseReturnsAuthenticationInfo() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .header("Content-Type", "application/json")
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 1, \"email\": \"email@email.com\", \"password\": \"1111111!\"}")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['false']", is(true)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Email already exists", "ERROR MESSAGE"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

        @Disabled
        @Test
        void when_newEmail_then_Status201_and_ResponseReturnsAuthenticationInfo() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .header("Content-Type", "application/json")
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 1, \"email\": \"email@email.com\", \"password\": \"1111111!\"}")
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['success']", is(true)))
                .andExpect(jsonPath("$['error']", nullValue(null)))
                .andExpect(jsonPath("$['data'].userId", is(1)))
                .andExpect(jsonPath("$['data'].email", is("email@email.com")))
                .andExpect(jsonPath("$['data'].password", is("1111111!")))
                .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
                .andExpect(jsonPath("$['data'].accessToken", notNullValue()))
                .andExpect(jsonPath("$['data'].isActive", is(true)));
        }

        @Test
        void when_passwordIsLess7CharactersLong_thenStatus400() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .header("Content-Type", "application/json")
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 1, \"email\": \"email@email.com\", \"password\": \"11!\"}")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

        @Test
        void when_techAuthTokenOther_thenStatus403AndErrorMessageReturns() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .header("Content-Type", "application/json")
                    .header("Authorization", otherTechAuthToken)
                    .content("{\"userId\": 1, \"email\": \"email@email.com\", \"password\": \"11!\"}")
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }
}
