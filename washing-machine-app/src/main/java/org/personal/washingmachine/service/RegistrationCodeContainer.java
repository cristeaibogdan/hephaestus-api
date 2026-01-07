package org.personal.washingmachine.service;

import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>This is a simple implementation of the Singleton design pattern, ensuring
 * that only one instance of the class is created. The instance can be accessed
 * via the {@link #getInstance()} method.
 * </p>
 *
 * <p>Note: This implementation does not provide multithreading support.
 * In a multithreaded environment, additional synchronization
 * would be needed to ensure thread safety.</p>
 */
final class RegistrationCodeContainer {

	/** Singleton instance of the {@code RegistrationCodeContainer} class. */
	private static RegistrationCodeContainer instance;

	/**
	 * A map containing registration codes associated with organizations and countries.
	 * The keys are {@link GetOrganizationAndCountryResponse} objects representing
	 * organization and country pairs, and the values are lists of registration codes.
	 */
	private final Map<GetOrganizationAndCountryResponse, List<String>> codes = new HashMap<>();

	/**
	 * Private constructor to prevent instantiation from outside the class.
	 * Initializes the {@code codes} map with predefined registration codes.
	 */
	private RegistrationCodeContainer() {
		codes.put(new GetOrganizationAndCountryResponse("ZEOS", "SLOVENIA"), List.of("RX1000", "RX1001", "RX1002", "RX1003"));
		codes.put(new GetOrganizationAndCountryResponse("GORENJE", "SLOVENIA"), List.of("RX2000", "RX2001", "RX2002", "RX2003"));
		codes.put(new GetOrganizationAndCountryResponse("BOSCH", "GERMANY"), List.of("RX3000", "RX3001", "RX3002", "RX3003"));
		codes.put(new GetOrganizationAndCountryResponse("SMEG", "ITALY"), List.of("RX4000", "RX4001", "RX4002", "RX4003"));
		codes.put(new GetOrganizationAndCountryResponse("ORIGIN", "ROMANIA"), List.of("RX5000", "RX5001", "RX5002", "RX5003"));
	}

	/**
	 * Returns the Singleton instance of the {@code RegistrationCodeContainer} class.
	 * If the instance does not exist, it will be created lazily.
	 */
	public static RegistrationCodeContainer getInstance() {
		if(instance == null) {
			instance = new RegistrationCodeContainer();
		}
		return instance;
	}

	/**
	 * Checks whether a given registration code exists in the {@code codes} map.
	 *
	 * @param registrationCode the registration code to search for.
	 * @return {@code true} if the registration code exists, {@code false} otherwise.
	 */
	public boolean exists(String registrationCode) {
		return codes.entrySet().stream()
				.anyMatch(entry -> entry.getValue().contains(registrationCode));
	}

	/**
	 * Retrieves the organization and country associated with a given registration code.
	 *
	 * @param registrationCode the registration code to search for.
	 * @return a {@link GetOrganizationAndCountryResponse} object representing the
	 *         organization and country associated with the given registration code.
	 * @throws CustomException if the registration code is invalid or not found.
	 */
	public GetOrganizationAndCountryResponse getOrganizationAndCountry(String registrationCode) {
		return codes.entrySet().stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst()
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_REGISTRATION_CODE));
	}
}