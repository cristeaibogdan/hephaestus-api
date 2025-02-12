package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.personal.shared.clients.ProductClient;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.utils.QueryDSLUtils;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.personal.washingmachine.dto.WashingMachineMapper;
import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;

@Service
@RestController
@RequiredArgsConstructor
public class WashingMachineApplicationService implements IWashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineDamageCalculator damageCalculator;
	private final WashingMachineReportGenerator reportGenerator;

	private final WashingMachineImageMapper washingMachineImageMapper;
	private final WashingMachineMapper washingMachineMapper;

	@Override
	public Page<GetWashingMachineSimpleResponse> loadPaginatedAndFiltered(SearchWashingMachineRequest searchWashingMachineRequest) {
		PageRequest pageRequest = PageRequest.of(
				searchWashingMachineRequest.pageIndex(),
				searchWashingMachineRequest.pageSize(),
				Sort.by(washingMachine.createdAt.getMetadata().getName()).descending());

		BooleanBuilder booleanBuilder = new BooleanBuilder()
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.identificationMode, searchWashingMachineRequest.identificationMode()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.manufacturer, searchWashingMachineRequest.manufacturer()))

				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.model, searchWashingMachineRequest.model()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.type, searchWashingMachineRequest.type()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.serialNumber, searchWashingMachineRequest.serialNumber()))

				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.returnType, searchWashingMachineRequest.returnType()))
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.damageType, searchWashingMachineRequest.damageType()))
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.recommendation, searchWashingMachineRequest.recommendation()))

				.and(QueryDSLUtils.addTimestampEqualCondition(washingMachine.createdAt, parseToLocalDate(searchWashingMachineRequest.createdAt())));

		Page<WashingMachine> responsePage = repository.findAll(booleanBuilder, pageRequest);

		return responsePage.map(wm -> washingMachineMapper.toGetWashingMachineSimpleResponse(wm));
	}

	private Optional<LocalDate> parseToLocalDate(String dateString) {
		try {
			return Optional.ofNullable(dateString)
					.filter(s -> StringUtils.isNotBlank(s))
					.map(s -> LocalDate.parse(s.trim()));
		} catch (DateTimeParseException e) {
			throw new CustomException("Invalid date provided", ErrorCode.INVALID_DATE, e);
		}
	}

	/**
	 * @deprecated
	 * <p> <b>In the context of</b> providing an API for retrieving a single {@link org.personal.washingmachine.dto.GetWashingMachineFullResponse} by serial number, </p>
	 * <p> <b>facing</b> the concern that clients might call this method in a loop to retrieve multiple DTOs, </p>
	 * <p> <b>we decided</b> to deprecate this method and introduce {@link org.personal.washingmachine.service.WashingMachineApplicationService#loadMany} </p>
	 * <p> <b>to achieve</b> improved performance by reducing the number of network requests, </p>
	 * <p> <b>accepting</b> that clients need to wrap single serial numbers in a list. </p>
	 * <p> Use {@link org.personal.washingmachine.service.WashingMachineApplicationService#loadMany} instead.
	 */
	@Deprecated(since = "2024/11/21")
	@Override
	public GetWashingMachineFullResponse load(String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return washingMachineMapper.toGetWashingMachineFullResponse(washingMachine);
	}

	@Override
	public void save(CreateWashingMachineRequest createWashingMachineRequest, List<MultipartFile> imageFiles) {

		WashingMachine washingMachine = washingMachineMapper.toEntity(createWashingMachineRequest);

		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = washingMachineImageMapper.toEntity(image);
			washingMachine.addImage(washingMachineImage);
		});

		Recommendation recommendation = damageCalculator.getRecommendation(washingMachine.getWashingMachineDetail());
		washingMachine.setRecommendation(recommendation);

		service.save(washingMachine);
	}

	@Override
	public Recommendation getRecommendation(String serialNumber) {
		return repository.getRecommendation(serialNumber)
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	@Override
	public GetWashingMachineReportResponse getReport(String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return reportGenerator.getReport(washingMachine);
	}

	// *****************************************
	// Exception Propagation Test Endpoint
	// *****************************************
	//TODO: To be deleted
	private final ProductClient productClient;

//	@Override
//	public List<String> getManufacturers(String category) {
//		return productClient.getManufacturers(category);
//	}

	@Override
	public boolean isSerialNumberInUse(String serialNumber) {
		return repository.existsBySerialNumber(serialNumber);
	}

	@Override
	public Map<String, GetWashingMachineFullResponse> loadMany(Set<String> serialNumbers) {

		List<WashingMachine> foundWashingMachines = repository.findAllBySerialNumberIn(serialNumbers);
		if (foundWashingMachines.isEmpty()) {
			throw new CustomException(ErrorCode.SERIAL_NUMBERS_NOT_FOUND, serialNumbers);
		}

		return buildResponseMap(foundWashingMachines, serialNumbers);
	}

	private Map<String, GetWashingMachineFullResponse> buildResponseMap(List<WashingMachine> foundWashingMachines, Set<String> serialNumbers) {
		Map<String, GetWashingMachineFullResponse> result = foundWashingMachines.stream()
				.map(wm -> washingMachineMapper.toGetWashingMachineFullResponse(wm))
				.collect(Collectors.toMap(
						wm -> wm.serialNumber(),
						wm -> wm
				));

		serialNumbers.forEach(sn -> result.putIfAbsent(sn, null));
		return result;
	}
}
