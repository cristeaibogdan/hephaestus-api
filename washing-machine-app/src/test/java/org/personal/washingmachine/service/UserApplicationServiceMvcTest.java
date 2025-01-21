package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
import org.personal.washingmachine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApplicationService.class)
class UserApplicationServiceMvcTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper jackson;

    @MockBean UserService userServiceMock;

    @Nested
    class testGetOrganizationAndCountry {

        @Test
        void should_ReturnOrganizationAndCountryDTO_When_ValidCode() throws Exception {
            // GIVEN
            String registrationCode = "RX1001";
            GetOrganizationAndCountryResponse expected = new GetOrganizationAndCountryResponse(
                    "ZEOS",
                    "SLOVENIA");

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/{registrationCode}/organization-and-country", registrationCode));

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(jackson.writeValueAsString(expected)));
        }
    }

    @Nested
    class testRegister {

        @Test
        void should_ReturnHttpStatusCreated() throws Exception {
            // GIVEN
            LoginUserResponse loginUserResponse = new LoginUserResponse(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan");

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.writeValueAsString(loginUserResponse)));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .register(any()); //TODO: Should use matchers or not?

            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(content().string(""));
        }

        @Test
        void should_ThrowCustomException_When_EmailIsAlreadyTaken() throws Exception {
            // GIVEN
            LoginUserResponse loginUserResponse = new LoginUserResponse(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan");

            willThrow(new CustomException(ErrorCode.EMAIL_ALREADY_TAKEN))
                    .given(userServiceMock).register(any());

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.writeValueAsString(loginUserResponse)));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .register(any());

            resultActions
                    .andExpect(status().isConflict());
        }

    }

    @Nested
    class testLogin {

        @Test
        void should_ReturnUserDTO() throws Exception {
            // GIVEN
            LoginUserRequest loginUserRequest = new LoginUserRequest(
                    "bogdan",
                    "123456");

            LoginUserResponse expected = new LoginUserResponse(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan");

            User user = new User(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan",
                    "123456");

            given(userServiceMock.login(loginUserRequest.username(), loginUserRequest.password()))
                    .willReturn(user);

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.writeValueAsString(loginUserRequest)));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .login(loginUserRequest.username(), loginUserRequest.password()); //TODO: Is this still useful? Stubbed methods are auto verified by default

            resultActions
                    .andExpect(status().isOk());
//                    .andExpect(content().string(
//                            objectMapper.writeValueAsString(expected)
//                    )); //TODO: Issues, as I do not return the password
        }
    }
}