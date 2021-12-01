package ru.pcs.crowdfunding.auth.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.services.AuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        Optional<AuthenticationInfoDto> testAuthInfo = Optional.of(AuthenticationInfoDto.builder()
                .userId(1L)
                .email("email@email.com")
                .password("test_pass")
                .accessToken("access_test_token")
                .refreshToken("refresh_test_token")
                .isActive(true)
                .build());
        when(authService.findById(1L)).thenReturn(testAuthInfo);
    }


    @Test
    void when_getById_thenStatus202andCreatedReturns() throws Exception {
        mockMvc.perform(get("/auth/1"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$['success']", is(true)))
//                .andExpect(jsonPath("$['error']", is(null))); throws NPE
                .andExpect(jsonPath("$['data'].userId", is(1)))
                .andExpect(jsonPath("$['data'].email", is("email@email.com")))
                .andExpect(jsonPath("$['data'].password", is("test_pass")))
                .andExpect(jsonPath("$['data'].accessToken", is("access_test_token")))
                .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
                .andExpect(jsonPath("$['data'].isActive", is(true)));
    }
}