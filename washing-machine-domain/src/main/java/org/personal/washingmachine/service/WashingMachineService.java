package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WashingMachineService {
	private final WashingMachineRepository washingMachineRepository;

	public Page<WashingMachine> loadPaginatedAndFiltered(BooleanBuilder booleanBuilder, PageRequest pageRequest) {

		Page<WashingMachine> responsePage = washingMachineRepository.findAll(booleanBuilder, pageRequest);

		if (responsePage.isEmpty()) {
			throw new CustomException(ErrorCode.EMPTY_PAGE);
		}

		return responsePage;
	}

	public WashingMachine loadExpanded(String serialNumber) {
		return washingMachineRepository
				.findBySerialNumber(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	public void save(WashingMachine washingMachine) {

		boolean existingSerialNumber = washingMachineRepository.existsBySerialNumber(washingMachine.getSerialNumber());
		if (existingSerialNumber) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_ALREADY_TAKEN);
		}

		washingMachineRepository.save(washingMachine);
	}

	public boolean isSerialNumberInUse(String serialNumber) {
		return washingMachineRepository.existsBySerialNumber(serialNumber);
	}
}
