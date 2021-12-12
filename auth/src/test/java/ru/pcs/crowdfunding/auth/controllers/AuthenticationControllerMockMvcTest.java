package ru.pcs.crowdfunding.auth.controllers;

import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.dto.AuthorizationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.RolesRepository;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
@DisplayName("AuthenticationController")
public class AuthenticationControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationInfosRepository authenticationInfosRepository;

    @Autowired
    private RolesRepository rolesRepository;

    private String techAuthToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6Ik1TX0NMSUVOVCIsInN0YXR1cyI6IkFDVElWRSIsImV4cCI6MjIxNjIzOTAyMn0.Aj-UHmdBosUrf12BrXqn3dsGtXwn0QgBF-q6KP-LvpI";
    private String otherTechAuthToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjQxNTc5Nzc2fQ.zaAgZjCMUEzML_W-px8al2DQsSOIqemMxDjoRHlQ7MQ";
    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzOCIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjQxOTE1MDU1fQ.orwbNoIQ9EBImDXdbUc8e9j3_o0Qi_AOLwh45dwrM34";

    @BeforeEach
    void setUp() {
        authenticationInfosRepository.save(AuthenticationInfo.builder()
            .userId(55L)
            .email("email@email.com")
            .password("111!")
            .refreshToken("refresh_test_token")
            .isActive(true)
            .build());

        rolesRepository.save(Role.builder()
            .roleId(1L)
            .name(Role.RoleEnum.USER)
            .build());
    }
    @AfterEach
    public void tearDown() {
        authenticationInfosRepository.deleteAll();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("POST /api/signUp")
    class GetSignUpTest {

        @Test
        void when_usedУEmail_then_Status409_and_ResponseReturnsAuthenticationInfo() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 2, \"email\": \"email@email.com\", \"password\": \"1111111!\"}")
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$['success']", is(false)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Email already exists", "ERROR MESSAGE"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

        @Test
        void when_newEmail_then_Status201_and_ResponseReturnsAuthenticationInfo() throws Exception {

            String email = RandomStringUtils.randomAlphanumeric(10).toLowerCase() + "@email.com";

            mockMvc.perform(post("/api/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 42, \"email\": \""+ email +"\", \"password\": \"1111111!\"}")
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['success']", is(true)))
                .andExpect(jsonPath("$['error']", nullValue(null)))
                .andExpect(jsonPath("$['data'].userId", notNullValue()))
                .andExpect(jsonPath("$['data'].email", is(email)))
                .andExpect(jsonPath("$['data'].password", notNullValue()))
                .andExpect(jsonPath("$['data'].refreshToken", nullValue(null)))
                .andExpect(jsonPath("$['data'].accessToken", notNullValue()))
                .andExpect(jsonPath("$['data'].isActive", is(true)));
        }

        @Test
        void when_passwordIsLess7CharactersLong_thenStatus400() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 1, \"email\": \"email@email.com\", \"password\": \"11!\"}")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

        @Test
        void when_techAuthTokenOther_thenStatus403AndErrorMessageReturns() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", otherTechAuthToken)
                    .content("{\"userId\": 1, \"email\": \"email@email.com\", \"password\": \"11!\"}")
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$['error']", is("User not found with token")));
        }
    }
}
