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
import org.personal.washingmachine.dto.WashingMachineReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
	public Page<GetWashingMachineSimpleResponseDTO> loadPaginatedAndFiltered(SearchWashingMachineRequestDTO searchWashingMachineRequestDTO) {
		PageRequest pageRequest = PageRequest.of(
				searchWashingMachineRequestDTO.pageIndex(),
				searchWashingMachineRequestDTO.pageSize(),
				Sort.by(washingMachine.createdAt.getMetadata().getName()).descending());

		BooleanBuilder booleanBuilder = new BooleanBuilder()
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.identificationMode, searchWashingMachineRequestDTO.identificationMode()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.manufacturer, searchWashingMachineRequestDTO.manufacturer()))

				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.model, searchWashingMachineRequestDTO.model()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.type, searchWashingMachineRequestDTO.type()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.serialNumber, searchWashingMachineRequestDTO.serialNumber()))

				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.returnType, searchWashingMachineRequestDTO.returnType()))
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.damageType, searchWashingMachineRequestDTO.damageType()))
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.recommendation, searchWashingMachineRequestDTO.recommendation()))

				.and(QueryDSLUtils.addTimestampEqualCondition(washingMachine.createdAt, parseToLocalDate(searchWashingMachineRequestDTO.createdAt())));

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
	public GetWashingMachineExpandedResponseDTO loadExpanded(String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return WashingMachineMapper.toExpandedDTO(washingMachine);
	}

	@Override
	public void save(CreateWashingMachineRequestDTO createWashingMachineRequestDTO, List<MultipartFile> imageFiles) {

		WashingMachine washingMachine = WashingMachineMapper.toEntity(createWashingMachineRequestDTO);

		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = WashingMachineImageMapper.toEntity(image);
			washingMachine.addImage(washingMachineImage);
		});

		service.save(washingMachine);
	}

	@Override
	public Recommendation getRecommendation(WashingMachineDetailDTO washingMachineDetailDTO) {
		WashingMachineDetail detail = WashingMachineDetailMapper.toEntity(washingMachineDetailDTO);
		return damageCalculator.getRecommendation(detail);
	}

	@Override
	public WashingMachineReportDTO getReport(String serialNumber) {
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
