package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WashingMachineService {
	private final WashingMachineRepository repository;

	public WashingMachine findBySerialNumber(String serialNumber) {
		return repository
				.findBySerialNumber(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	public void create(WashingMachine washingMachine) {
		boolean existingSerialNumber = repository.existsBySerialNumber(washingMachine.getSerialNumber());
		if (existingSerialNumber) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_ALREADY_TAKEN);
		}

		repository.save(washingMachine);
	}
}
