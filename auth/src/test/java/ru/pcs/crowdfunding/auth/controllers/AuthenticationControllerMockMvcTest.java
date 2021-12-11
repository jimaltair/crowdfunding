package ru.pcs.crowdfunding.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.services.AuthenticationService;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@DisplayName("AuthenticationController")
public class AuthenticationControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationInfosRepository authenticationInfosRepository;

    @MockBean
    private AuthenticationService authenticationService;

    private String techAuthToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6Ik1TX0NMSUVOVCIsInN0YXR1cyI6IkFDVElWRSIsImV4cCI6MjIxNjIzOTAyMn0.Aj-UHmdBosUrf12BrXqn3dsGtXwn0QgBF-q6KP-LvpI";
    private String otherTechAuthToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjQxNTc5Nzc2fQ.zaAgZjCMUEzML_W-px8al2DQsSOIqemMxDjoRHlQ7MQ";
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        //region POST
        when(authenticationService.existEmailInDb(
            AuthenticationInfoDto.builder()
                .userId(2L)
                .email("email@email.com")
                .password("1111111!")
                .accessToken(null)
                .refreshToken(null)
                .isActive(null)
                .build()
        )).thenReturn(true);

        AuthenticationInfoDto authInfoDto = AuthenticationInfoDto.builder()
            .userId(1L)
            .email("email@email.com")
            .password("1111111!")
            .accessToken(null)
            .refreshToken(null)
            .isActive(null)
            .build();

        when(authenticationService.existEmailInDb(authInfoDto)).thenReturn(false);

        AuthenticationInfo authInfo = AuthenticationInfo.builder()
            .email("email@email.com".toLowerCase())
            .password("1111111!")
            .userId(1L)
            .refreshToken("refresh_test_token")
            .isActive(true)
            .roles(Arrays.asList(Role.builder().name(Role.RoleEnum.USER).build()))
            .status(Status.builder().name(Status.StatusEnum.CONFIRMED).build())
            .build();

        when(authenticationInfosRepository.findByEmail("email@email.com")).thenReturn(Optional.of(authInfo));

        when(authenticationService.signUpAuthentication(authInfoDto))
            .thenReturn(
                AuthenticationInfoDto.builder()
                    .userId(1L)
                    .email("email@email.com")
                    .password("1111111!")
                    .accessToken("eyJ0eXAiOiJKV1Q")
                    .refreshToken("refresh_test_token")
                    .isActive(true)
                    .build()
            );
        when(authenticationInfosRepository.save(authInfo)).thenReturn(authInfo);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("POST /api/signUp")
    class GetSignUpTest {

        @Test
        void when_usedУEmail_then_Status201_and_ResponseReturnsAuthenticationInfo() throws Exception {
            mockMvc.perform(post("/api/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", techAuthToken)
                    .content("{\"userId\": 2, \"email\": \"email@email.com\", \"password\": \"1111111!\"}")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['success']", is(false)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Email already exists", "ERROR MESSAGE"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

        @Test
        void when_newEmail_then_Status201_and_ResponseReturnsAuthenticationInfo() throws Exception {

            String json = objectMapper.writeValueAsString(
                AuthenticationInfoDto.builder()
                    .userId(1L)
                    .email("email@email.com")
                    .password("1111111!")
                    .accessToken(null)
                    .refreshToken(null)
                    .isActive(null)
                    .build());


            mockMvc.perform(post("/api/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", techAuthToken)
                    .content(json)
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
