package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.entity.dtos.OrganizationAndCountryDTO;
import org.personal.washingmachine.entity.dtos.UserCredentialsDTO;
import org.personal.washingmachine.entity.dtos.UserDTO;
import org.personal.washingmachine.exception.CustomException;
import org.personal.washingmachine.exception.ErrorCode;
import org.personal.washingmachine.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.personal.washingmachine.entity.dtos.Mapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private Map<OrganizationAndCountryDTO, List<String>> initializeRegistrationCodes() {
        Map<OrganizationAndCountryDTO, List<String>> registerCodes = new HashMap<>();
        registerCodes.put(new OrganizationAndCountryDTO("ZEOS", "SLOVENIA"), List.of("RX1000", "RX1001", "RX1002", "RX1003"));
        registerCodes.put(new OrganizationAndCountryDTO("GORENJE", "SLOVENIA"), List.of("RX2000", "RX2001", "RX2002", "RX2003"));
        registerCodes.put(new OrganizationAndCountryDTO("BOSCH", "GERMANY"), List.of("RX3000", "RX3001", "RX3002", "RX3003"));
        registerCodes.put(new OrganizationAndCountryDTO("SMEG", "ITALY"), List.of("RX4000", "RX4001", "RX4002", "RX4003"));
        registerCodes.put(new OrganizationAndCountryDTO("ORIGIN", "ROMANIA"), List.of("RX5000", "RX5001", "RX5002", "RX5003"));

        return registerCodes;
    }

// ************************************************************
// *** REGISTRATION
// ************************************************************

    public boolean isValidRegistrationCode(String registrationCode) {
        Optional<OrganizationAndCountryDTO> response = initializeRegistrationCodes().entrySet().stream()
                .filter(entry -> entry.getValue().contains(registrationCode))
                .map(entry -> entry.getKey())
                .findFirst();

        return response.isPresent();
    }

    public OrganizationAndCountryDTO getOrganizationAndCountry(String registrationCode) {
        return initializeRegistrationCodes().entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(registrationCode))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.E_1001, "Code not found"));
    }

    public void register(UserDTO userDTO) {
        // Guard clauses
        boolean existingEmail = userRepository.existsByEmail(userDTO.email());
        if (existingEmail) {
            throw new CustomException(ErrorCode.E_1002, "Email is taken");
        }

        boolean existingUsername = userRepository.existsByUsername(userDTO.username());
        if (existingUsername) {
            throw new CustomException(ErrorCode.E_1003, "Username is taken");
        }

        // Convert to entity and save
        User user = UserMapper.toEntity(userDTO);
        userRepository.save(user);
    }

// ************************************************************
// *** LOGIN
// ************************************************************

    public UserDTO login(UserCredentialsDTO userCredentialsDTO) {
        return userRepository
                .findByUsernameAndPassword(
                        userCredentialsDTO.username(),
                        userCredentialsDTO.password())
                .map(user -> UserMapper.toDTO(user))
                .orElseThrow(() -> new CustomException(ErrorCode.E_1004, "Invalid username or password"));
    }
}
