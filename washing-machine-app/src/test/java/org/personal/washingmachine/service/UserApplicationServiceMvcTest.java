package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.UserDTO;
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

    @MockBean
    UserService userServiceMock;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class testIsValidRegistrationCode {

        @Test
        void should_ReturnTrue_When_ValidCode() throws Exception {
            // GIVEN
            String registrationCode = "RX2000";
            boolean expected = true;

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/{registrationCode}", registrationCode));

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(Boolean.toString(expected)));
        }

        @Test
        void should_ReturnFalse_When_InvalidCode() throws Exception {
            // GIVEN
            String registrationCode = "I will fail";
            boolean expected = false;

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/{registrationCode}", registrationCode));

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(Boolean.toString(expected)));
        }
    }

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
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));
        }
    }

    @Nested
    class testRegister {

        @Test
        void should_ReturnHttpStatusCreated() throws Exception {
            // GIVEN
            UserDTO userDTO = new UserDTO(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan",
                    "123456");

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTO)));

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
            UserDTO userDTO = new UserDTO(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan",
                    "123456");

            willThrow(new CustomException(ErrorCode.EMAIL_ALREADY_TAKEN))
                    .given(userServiceMock).register(any());

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTO)));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .register(any());

            resultActions
                    .andExpect(status().is4xxClientError());
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

            UserDTO expected = new UserDTO(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan",
                    "123456");

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
                    .content(objectMapper.writeValueAsString(loginUserRequest)));

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