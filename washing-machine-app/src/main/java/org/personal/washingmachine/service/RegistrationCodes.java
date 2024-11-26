package org.personal.washingmachine.service;

import lombok.Getter;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>This is a simple implementation of the Singleton design pattern, ensuring
 * that only one instance of the class is created. The instance can be accessed
 * via the {@link #getInstance()} method, and the map of registration codes is
 * lazily initialized and returned by the {@link #getCodes()} method.
 * </p>
 *
 * <p>Note: This implementation does not provide multithreading support.
 * In a multithreaded environment, additional synchronization
 * would be needed to ensure thread safety.</p>
 *
 * <p>Usage Example:</p>
 * {@code
 * Map<GetOrganizationAndCountryResponse, List<String>> codes =
 *     RegistrationCodes.getInstance().getCodes();
 * }
 */
@Getter
public class RegistrationCodes {

	private static RegistrationCodes instance;
	/**
	 * This map contains registration codes associated with organizations and countries.
	 * It is initialized in the constructor when the instance is created.
	 */
	private final Map<GetOrganizationAndCountryResponse, List<String>> codes;

	/**
	 * Private constructor to prevent instantiation from outside the class.
	 * Initializes the map of registration codes.
	 */
	private RegistrationCodes() {
		this.codes = new HashMap<>();
		codes.put(new GetOrganizationAndCountryResponse("ZEOS", "SLOVENIA"), List.of("RX1000", "RX1001", "RX1002", "RX1003"));
		codes.put(new GetOrganizationAndCountryResponse("GORENJE", "SLOVENIA"), List.of("RX2000", "RX2001", "RX2002", "RX2003"));
		codes.put(new GetOrganizationAndCountryResponse("BOSCH", "GERMANY"), List.of("RX3000", "RX3001", "RX3002", "RX3003"));
		codes.put(new GetOrganizationAndCountryResponse("SMEG", "ITALY"), List.of("RX4000", "RX4001", "RX4002", "RX4003"));
		codes.put(new GetOrganizationAndCountryResponse("ORIGIN", "ROMANIA"), List.of("RX5000", "RX5001", "RX5002", "RX5003"));
	}

	/**
	 * Returns the Singleton instance of the {@code RegistrationCodes} class.
	 * If the instance does not exist, it will be created lazily.
	 */
	public static RegistrationCodes getInstance() {
		if(instance == null) {
			instance = new RegistrationCodes();
		}
		return instance;
	}
}