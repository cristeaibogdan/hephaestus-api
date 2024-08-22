package org.personal.washingmachine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserApplicationServiceIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @Nested
//    class testIsValidRegistrationCode {
//
//        @Test
//        void should_ReturnTrue() throws Exception {
//            // GIVEN
//            String registrationCode = "I will pass";
//            Boolean expected = true;
//
//            given(userApplicationServiceMock.isValidRegistrationCode(registrationCode))
//                    .willReturn(true);
//
//            // WHEN
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/users/{registrationCode}", registrationCode));
//
//            // THEN
//            then(userApplicationServiceMock)
//                    .should(times(1))
//                    .isValidRegistrationCode(registrationCode);
//
//            resultActions
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(expected.toString()));
//        }
//
//        @Test
//        void should_ReturnFalse() throws Exception {
//            // GIVEN
//            String registrationCode = "I will fail";
//            Boolean expected = false;
//
//            given(userApplicationServiceMock.isValidRegistrationCode(registrationCode))
//                    .willReturn(false);
//
//            // WHEN
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/v1/users/{registrationCode}", registrationCode));
//
//            // THEN
//            then(userApplicationServiceMock)
//                    .should(times(1))
//                    .isValidRegistrationCode(registrationCode);
//
//            resultActions
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(expected.toString()));
//        }
//    }

    @Nested
    class testGetOrganizationAndCountry {

        @Test
        void should_ReturnOrganizationAndCountryDTO() throws Exception {
            // GIVEN
            String registrationCode = "RX1001";
            OrganizationAndCountryDTO expected = new OrganizationAndCountryDTO(
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
//
//    @Nested
//    class testRegister {
//
//        @Test
//        void should_ReturnHttpStatusCreated() throws Exception {
//            // GIVEN
//            UserDTO userDTO = new UserDTO(
//                    "RX1001",
//                    "OLX",
//                    "Romania",
//                    "bogdan@gmail.com",
//                    "bogdan",
//                    "123456");
//
//            // WHEN
//            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(userDTO)));
//
//            // THEN
//            then(userApplicationServiceMock)
//                    .should(times(1))
//                    .register(userDTO);
//
//            resultActions
//                    .andExpect(status().isCreated())
//                    .andExpect(content().string(""));
//        }
//
//        @Test
//        void should_ThrowCustomException_When_EmailIsAlreadyTaken() throws Exception {
//            // GIVEN
//            UserDTO userDTO = new UserDTO(
//                    "RX1001",
//                    "OLX",
//                    "Romania",
//                    "bogdan@gmail.com",
//                    "bogdan",
//                    "123456");
//
//            willThrow(new CustomException(ErrorCode.INVALID_REGISTRATION_CODE))
//                    .given(userApplicationServiceMock).register(userDTO);
//
//            // WHEN
//            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/register")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(userDTO)));
//
//            // THEN
//            then(userApplicationServiceMock)
//                    .should(times(1))
//                    .register(userDTO);
//
//            resultActions
//                    .andExpect(status().isIAmATeapot());
//        }
//
//    }
//
//    @Nested
//    class testLogin {
//
//        @Test
//        void should_ReturnUserDTO() throws Exception {
//            // GIVEN
//            UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO(
//                    "bogdan",
//                    "123456");
//
//            UserDTO expected = new UserDTO(
//                    "RX1001",
//                    "OLX",
//                    "Romania",
//                    "bogdan@gmail.com",
//                    "bogdan",
//                    "123456");
//
//            given(userApplicationServiceMock.login(userCredentialsDTO))
//                    .willReturn(expected);
//
//            // WHEN
//            ResultActions resultActions = mockMvc.perform(post("/api/v1/users/login")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(userCredentialsDTO)));
//
//            // THEN
//            then(userApplicationServiceMock)
//                    .should(times(1))
//                    .login(userCredentialsDTO);
//
//            resultActions
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(
//                            objectMapper.writeValueAsString(expected)
//                    ));
//        }
//    }
}