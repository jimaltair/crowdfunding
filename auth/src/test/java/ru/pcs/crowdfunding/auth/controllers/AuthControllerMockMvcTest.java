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
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.services.AuthService;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthController:")
class AuthControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private String techAuthToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOiJBRE1JTiIsImV4cCI6MTY0MTYxMjQ5Niwic3RhdHVzIjoiQ09ORklSTUVEIn0.PaZbpZ_SC-6ktPaZ2_wEKvtPNMxyR39YseZA5BdEET0";

    @BeforeEach
    void setUp() {

        //region Get
        when(authService.findById(1L)).thenReturn(
            Optional.of(AuthenticationInfoDto.builder()
                .userId(1L)
                .email("email@email.com")
                .password("111!")
                .refreshToken("refresh_test_token")
                .isActive(true)
                .build())
        );
        when(authService.findById(100L)).thenReturn(Optional.empty());
        //endregion

        //region POST
        when(authService.createAuthenticationInfo(
            AuthenticationInfoDto.builder()
                .userId(3L)
                .email("email@email.com")
                .password("111!")
                .build()
        )).thenReturn(
            Optional.of(
                AuthenticationInfo.builder()
                    .userId(3L)
                    .email("email@email.com")
                    .password("111!")
                    .isActive(true)
                    .refreshToken("refresh_token")
                    .status(Status.builder().name(Status.StatusEnum.CONFIRMED).build())
                    .roles(Arrays.asList(Role.builder().name(Role.RoleEnum.USER).build()))
                    .build()
            )
        );

        when(authService.createAuthenticationInfo(
            AuthenticationInfoDto.builder()
                .userId(99L)
                .email("email@email.com")
                .password("111!")
                .build()
        )).thenReturn(
            Optional.empty()
        );
        //endregion

        //region PUT

        //endregion

        //region DELETE
        when(authService.deleteAuthenticationInfo(1L)).thenReturn(
            Optional.of(AuthenticationInfoDto.builder()
                .userId(1L)
                .email("email@email.com")
                .password("111!")
                .refreshToken("refresh_test_token")
                .isActive(false)
                .build())
        );

        when(authService.deleteAuthenticationInfo(3L)).thenReturn(Optional.empty());
        //endregion
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("GET /api/auth/{id}")
    class GetAuthTest {

        @Test
        void when_getById_thenStatus202andCreatedReturns() throws Exception {
            mockMvc.perform(get("/api/auth/1")
//                    .header("Authorization", techAuthToken) //TODO здесь наверное должен использоваться технический токен?
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$['success']", is(true)))
                .andExpect(jsonPath("$['error']", nullValue(null)))
                .andExpect(jsonPath("$['data'].userId", is(1)))
                .andExpect(jsonPath("$['data'].email", is("email@email.com")))
                .andExpect(jsonPath("$['data'].password", is("111!"))) //TODO зачем мы пароль возвращаем?
                .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
                .andExpect(jsonPath("$['data'].isActive", is(true)));
        }

        @Test
        void when_getNotExistedUser_thenStatus404AndErrorMessageReturns() throws Exception {
            mockMvc.perform(get("/api/auth/100")
//                    .header("Authorization", techAuthToken)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['success']", is(false)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Client with id 100 not found"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

//        TODO после того как будет реализован технический токе, раскоментить хедеры и отредактировать кейсы
//         + раскоментить тест ниже
//
//        @Test()
//        @Disabled
//        void when_techAuthTokenOther_thenStatus403AndErrorMessageReturns() throws Exception {
//            mockMvc.perform(get("/api/auth/1")
//                    .header("Authorization", techAuthToken)
//                )
//                .andDo(print())
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$['error']", is("User not found with token")));
//        }
    }

//    @Nested
//    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//    @DisplayName("POST /api/auth")
//    class PostAuthTest {
//
//        @Test
//        void test1() throws Exception {
//            mockMvc.perform(post("/api/auth")
////                    .header("Authorization", techAuthToken)
//                    .header("Content-Type", "application/json")
//                    .content(
//                        "{\"userId\": 3, \"email\": \"email@email.com\", \"password\": \"111!\"}"
//                    )
//                )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$['success']", is(true)))
//                .andExpect(jsonPath("$['error']", nullValue(null)))
//                .andExpect(jsonPath("$['data'].userId", is(1)))
//                .andExpect(jsonPath("$['data'].email", is("email@email.com")))
//                .andExpect(jsonPath("$['data'].password", is("test_pass")))
//                .andExpect(jsonPath("$['data'].refreshToken", is("refresh_test_token")))
//                .andExpect(jsonPath("$['data'].status.name", is("CONFIRMED")))
//                .andExpect(jsonPath("$['data'].isActive", is(true)));
//        }
//
//        @Test
//        void test2() throws Exception {
//            mockMvc.perform(post("/api/auth")
//                    .header("Authorization", techAuthToken)
//                    .header("Content-Type", "application/json")
//                    .content(
//                        "{\"userId\": 99, \"email\": \"email@email.com\", \"password\": \"111!\"}"
//                    )
//                )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$['success']", is(false)))
//                .andExpect(jsonPath("$['error']", is(Arrays.asList("An account with email: email@email.com already exists."))))
//                .andExpect(jsonPath("$['data']", nullValue(null)));
//        }
//    }

//    @Nested
//    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//    @DisplayName("PUT /api/auth/{id}")
//    class PutAuthTest {
//
////        TODO список проверок:
////        - PUT /api/auth/{<user существует>} + header Authorization == techAuthToken + header Content-Type == application/json + valid body =>
////        202 + success == true, body == contains modified user, error == null
//
////        - PUT /api/auth/{<user существует>} + header == techAuthToken + header == Content-Type + valid body =>
////        202 + success == true, body == contains modified user, error == null
//
////        - PUT /api/auth/{<user НЕ существует>} + header == techAuthToken + valid body => 404 + success == false, body == null, error == <error>
////        - PUT /api/auth/{<user существует >} + header != techAuthToken + valid body => 403 + success == false, body == null, error == <error>
//
//
////        - PUT /api/auth/{<user существует>} + header Authorization == techAuthToken + header Content-Type == application/xml + valid body =>
////          202 + success == true, body == contains modified user, error == null
////        возможно избыточно
//
//
////        - PUT /api/auth/{<user существует>} + header == techAuthToken +  body (какие именно значения обязательны для отправки????) => 202 + success == true, body == contains modified user, error == null
////        накидать кейсов где нужных значений не будет
//
//    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("DELETE /api/auth/{id}")
    class DeleteAuthTest {

        @Test
        void when_deleteById_then_Status202_and_ResponseReturnsAuthenticationInfo() throws Exception {
            mockMvc.perform(delete("/api/auth/1")
//                    .header("Authorization", techAuthToken)
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
//                    .header("Authorization", techAuthToken)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['success']", is(false)))
                .andExpect(jsonPath("$['error']", is(Arrays.asList("Can't delete. Client with id 3 not found"))))
                .andExpect(jsonPath("$['data']", nullValue(null)));
        }

//        TODO добавить:
//        - DELETE /api/auth/{<user существует >} + header != techAuthToken => 403 + success == false, body == null, error == <error>
    }
}
