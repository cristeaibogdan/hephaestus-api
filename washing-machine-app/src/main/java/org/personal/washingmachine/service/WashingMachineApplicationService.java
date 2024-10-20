package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.personal.shared.clients.ProductClient;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.utils.QueryDSLUtils;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static org.personal.washingmachine.dto.Mapper.*;
import static org.personal.washingmachine.dto.Mapper.WashingMachineMapper;
import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;

@Service
@RestController
@RequiredArgsConstructor
public class WashingMachineApplicationService implements IWashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineDamageCalculator damageCalculator;
	private final WashingMachineReportGenerator reportGenerator;

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

		return responsePage.map(wm -> WashingMachineMapper.toSimpleDTO(wm));
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

	@Override
	public GetWashingMachineExpandedResponse loadExpanded(String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return WashingMachineMapper.toExpandedDTO(washingMachine);
	}

	@Override
	public void save(CreateWashingMachineRequest createWashingMachineRequest, List<MultipartFile> imageFiles) {

		WashingMachine washingMachine = WashingMachineMapper.toEntity(createWashingMachineRequest);

		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = WashingMachineImageMapper.toEntity(image);
			washingMachine.addImage(washingMachineImage);
		});

		Recommendation recommendation = damageCalculator.getRecommendation(washingMachine.getWashingMachineDetail());
		washingMachine.setRecommendation(recommendation);

		service.save(washingMachine);
	}

	@Override
	public Recommendation getRecommendation(@PathVariable String serialNumber) {
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

	@Override
	public List<String> getManufacturers(String category) {
		return productClient.getManufacturers(category);
	}

	@Override
	public boolean isSerialNumberInUse(String serialNumber) {
		return repository.existsBySerialNumber(serialNumber);
	}
}
