package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.washingmachine.entity.dtos.Mapper;
import org.personal.washingmachine.entity.dtos.OrganizationAndCountryDTO;
import org.personal.washingmachine.entity.dtos.UserCredentialsDTO;
import org.personal.washingmachine.entity.dtos.UserDTO;
import org.personal.washingmachine.exception.CustomException;
import org.personal.washingmachine.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService underTest;

    @Mock
    private UserRepository userRepositoryMock;


    @Nested
    class TestIsValidRegistrationCode {

        @ParameterizedTest(name = "{0} is a valid registration code")
        @ValueSource(strings = {
                "RX1000", "RX1001", "RX1002", "RX1003",
                "RX2000", "RX2001", "RX2002", "RX2003",
                "RX3000", "RX3001", "RX3002", "RX3003",
                "RX4000", "RX4001", "RX4002", "RX4003",
        })
        void should_ReturnTrue_When_RegistrationCodeIsValid(String registrationCode) {
            // GIVEN

            // WHEN
            Boolean actual = underTest.isValidRegistrationCode(registrationCode);

            // THEN
            assertThat(actual).isTrue();
        }

        @ParameterizedTest(name = "{0} is not a valid registration code")
        @ValueSource(strings = {"RX1234", "   ", "test", "something"})
        void should_ReturnFalse_When_RegistrationCodeIsInvalid(String registrationCode) {
            // GIVEN

            // WHEN
            Boolean actual = underTest.isValidRegistrationCode(registrationCode);

            // THEN
            assertThat(actual).isFalse();
        }
    }


    @Nested
    class TestGetOrganizationAndCountry {

        static Stream<Arguments> getOrganizationAndCountryTestCases() {
            return Stream.of(
                    arguments(new OrganizationAndCountryDTO("ZEOS", "SLOVENIA"), "RX1000"),
                    arguments(new OrganizationAndCountryDTO("GORENJE", "SLOVENIA"), "RX2001"),
                    arguments(new OrganizationAndCountryDTO("BOSCH", "GERMANY"), "RX3002"),
                    arguments(new OrganizationAndCountryDTO("SMEG", "ITALY"), "RX4003"),
                    arguments(new OrganizationAndCountryDTO("ORIGIN", "ROMANIA"), "RX5000")
            );
        }

        @ParameterizedTest(name = "Valid OrganizationAndCountryDTO for {1}")
        @MethodSource("getOrganizationAndCountryTestCases")
        void should_ReturnOrganizationAndCountryDTO_When_RegistrationCodeIsValid(
                OrganizationAndCountryDTO expected,
                String registrationCode) {
            // GIVEN

            // WHEN
            OrganizationAndCountryDTO actual = underTest.getOrganizationAndCountry(registrationCode);

            // THEN
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @ParameterizedTest(name = "{0} is not a valid registration code")
        @ValueSource(strings = {"RX1234", "   ", "test", "something"})
        void should_ThrowCustomException_When_RegistrationCodeIsInvalid(String registrationCode) {
            // GIVEN

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.getOrganizationAndCountry(registrationCode))
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    class TestRegister {

        @Test
        void should_ThrowCustomException_When_EmailIsAlreadyTaken() {
            // GIVEN
            UserDTO userDTO = UserDTO.builder()
                    .email("test@email.com")
                    .build();

            given(userRepositoryMock.existsByEmail(userDTO.email()))
                    .willReturn(true);

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.register(userDTO))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_ThrowCustomException_When_UsernameIsAlreadyTaken() {
            // GIVEN
            UserDTO userDTO = UserDTO.builder()
                    .username("testUsername")
                    .build();

            given(userRepositoryMock.existsByUsername(userDTO.username()))
                    .willReturn(true);

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.register(userDTO))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_SaveUser_When_ValidUserProvided() {
            // GIVEN
            UserDTO userDTO = new UserDTO(
                    "code",
                    "ORG",
                    "ROMANIA",
                    "email@yahoo.com",
                    "user123456",
                    "pass123456");

            // WHEN
            underTest.register(userDTO);

            // THEN
            then(userRepositoryMock)
                    .should(times(1))
                    .save(any());
        }
    }

    @Nested
    class TestLogin {

        @Test
        void should_ThrowCustomException_When_BadCredentials() {
            // GIVEN
            UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO(
                    "User",
                    "Pass");

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.login(userCredentialsDTO))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_ReturnUserDTO_When_GoodCredentials() {
            // GIVEN
            UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO(
                    "User",
                    "Pass");

            UserDTO expected = UserDTO.builder()
                    .username("usernameTest")
                    .password(null)
                    .build();

            given(userRepositoryMock.findByUsernameAndPassword(userCredentialsDTO.username(), userCredentialsDTO.password()))
                    .willReturn(Optional.of(Mapper.UserMapper.toEntity(expected)));

            // WHEN
            UserDTO actual = underTest.login(userCredentialsDTO);

            // THEN
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
