package org.personal.washingmachine.controller;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.facade.dtos.OrganizationAndCountryDTO;
import org.personal.washingmachine.facade.dtos.UserCredentialsDTO;
import org.personal.washingmachine.facade.dtos.UserDTO;
import org.personal.washingmachine.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
class UserController {

    private final UserService userService;

// ************************************************************
// *** REGISTRATION
// ************************************************************

    @GetMapping("/{registrationCode}")
    boolean isValidRegistrationCode(@PathVariable String registrationCode) {
        return userService.isValidRegistrationCode(registrationCode);
    }

    @GetMapping("/{registrationCode}/organization-and-country")
    OrganizationAndCountryDTO getOrganizationAndCountry(@PathVariable String registrationCode) {
        return userService.getOrganizationAndCountry(registrationCode);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
    }

// ************************************************************
// *** LOGIN
// ************************************************************

    @PostMapping("/login")
    UserDTO login(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        return userService.login(userCredentialsDTO);
    }
}
