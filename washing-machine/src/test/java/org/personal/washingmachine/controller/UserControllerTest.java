package org.personal.washingmachine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.entity.dtos.OrganizationAndCountryDTO;
import org.personal.washingmachine.entity.dtos.UserCredentialsDTO;
import org.personal.washingmachine.entity.dtos.UserDTO;
import org.personal.washingmachine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userServiceMock;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Nested
    class testIsValidRegistrationCode {

        @Test
        void should_ReturnTrue() throws Exception {
            // GIVEN
            String registrationCode = "I will pass";
            Boolean expected = true;

            given(userServiceMock.isValidRegistrationCode(registrationCode))
                    .willReturn(true);

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/{registrationCode}", registrationCode));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .isValidRegistrationCode(registrationCode);

            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(expected.toString()));
        }

        @Test
        void should_ReturnFalse() throws Exception {
            // GIVEN
            String registrationCode = "I will fail";
            Boolean expected = false;

            given(userServiceMock.isValidRegistrationCode(registrationCode))
                    .willReturn(false);

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/{registrationCode}", registrationCode));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .isValidRegistrationCode(registrationCode);

            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(expected.toString()));
        }
    }

    @Nested
    class testGetOrganizationAndCountry {

        @Test
        void should_ReturnOrganizationAndCountryDTO() throws Exception {
            // GIVEN
            String registrationCode = "RX1001";
            OrganizationAndCountryDTO expected = new OrganizationAndCountryDTO(
                    "Organization",
                    "United Kingdom");

            given(userServiceMock.getOrganizationAndCountry(registrationCode))
                    .willReturn(expected);

            // WHEN
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/users/{registrationCode}/organization-and-country", registrationCode));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .getOrganizationAndCountry(registrationCode);

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
                    .register(userDTO);

            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(content().string(""));
        }

        @Test
        void should_ThrowCustomException_When_EmailIsTaken() throws Exception {
            // GIVEN
            UserDTO userDTO = new UserDTO(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan",
                    "123456");

            willThrow(CustomException.class).given(userServiceMock).register(userDTO);

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTO)));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .register(userDTO);

            resultActions
                    .andExpect(status().isIAmATeapot());
            //.andExpect(content().);
            // How can I check if my Custom ErrorResponse is passed?
            // It also has a LocalDateTime.now() which might cause problems?
        }

    }

    @Nested
    class testLogin {

        @Test
        void should_ReturnUserDTO() throws Exception {
            // GIVEN
            UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO(
                    "bogdan",
                    "123456");

            UserDTO expected = new UserDTO(
                    "RX1001",
                    "OLX",
                    "Romania",
                    "bogdan@gmail.com",
                    "bogdan",
                    "123456");

            given(userServiceMock.login(userCredentialsDTO))
                    .willReturn(expected);

            // WHEN
            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCredentialsDTO)));

            // THEN
            then(userServiceMock)
                    .should(times(1))
                    .login(userCredentialsDTO);

            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                            objectMapper.writeValueAsString(expected)
                    ));
        }
    }
}