package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.personal.shared.clients.ProductClient;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.*;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.utils.QueryDSLUtils;
import org.personal.washingmachine.vo.WashingMachineReportVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.personal.washingmachine.dto.Mapper.WashingMachineMapper;
import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;

@Service
@RestController
@RequiredArgsConstructor
public class WashingMachineApplicationService implements IWashingMachineApplicationService {
	private final WashingMachineService service;
	private final WashingMachineRepository repository;
	private final WashingMachineDamageCalculator damageCalculator;

	@Override
	public Page<WashingMachineSimpleDTO> loadPaginatedAndFiltered(PageRequestDTO pageRequestDTO) {
		PageRequest pageRequest = PageRequest.of(
				pageRequestDTO.pageIndex(),
				pageRequestDTO.pageSize(),
				Sort.by(washingMachine.createdAt.getMetadata().getName()).descending());

		BooleanBuilder booleanBuilder = new BooleanBuilder()
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.category, pageRequestDTO.category()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.manufacturer, pageRequestDTO.manufacturer()))

				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.damageType, pageRequestDTO.damageType()))
				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.returnType, pageRequestDTO.returnType()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.identificationMode, pageRequestDTO.identificationMode()))

				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.serialNumber, pageRequestDTO.serialNumber()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.model, pageRequestDTO.model()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.type, pageRequestDTO.type()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.recommendation, pageRequestDTO.recommendation()))

				.and(QueryDSLUtils.addIntegerEqualCondition(washingMachine.damageLevel, pageRequestDTO.damageLevel()))

				.and(QueryDSLUtils.addTimestampEqualCondition(washingMachine.createdAt, pageRequestDTO.createdAt()));

		Page<WashingMachine> responsePage = repository.findAll(booleanBuilder, pageRequest);

		return responsePage.map(wm -> WashingMachineMapper.toSimpleDTO(wm));
	}

	@Override
	public WashingMachineExpandedDTO loadExpanded(String serialNumber) {
		WashingMachine washingMachine = service.findBySerialNumber(serialNumber);
		return WashingMachineMapper.toExpandedDTO(washingMachine);
	}

	@Override
	public void save(WashingMachineDTO washingMachineDTO, List<MultipartFile> imageFiles) {

		WashingMachine washingMachine = WashingMachineMapper.toEntity(washingMachineDTO);
		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = getWashingMachineImage(image);
			washingMachine.addImage(washingMachineImage);
		});

		service.save(washingMachine);
	}

	@Override
	public WashingMachineEvaluationDTO getDamageEvaluation(WashingMachineDetailsDTO washingMachineDetailsDTO) {
		return damageCalculator.getDamageEvaluation(washingMachineDetailsDTO);
	}

	@Override
	public WashingMachineReportVO getReport(String serialNumber) {
		return service.getReport(serialNumber);
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

//*********************************************************************************************
//******************** HELPER METHODS
//*********************************************************************************************

	private WashingMachineImage getWashingMachineImage(MultipartFile imageFile) {

		byte[] image;

		try {
			image = imageFile.getBytes();
		} catch (IOException e) {
			throw new CustomException("Could not extract bytes from image: " + imageFile.getName(), e, ErrorCode.GENERAL);
		}

		String imagePrefix = "data:image/" + getImageExtension(imageFile) + ";base64,";

		return new WashingMachineImage(imagePrefix, image);
	}

	private String getImageExtension(MultipartFile imageFile) {
		String extension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());

		return switch (extension.toLowerCase()) {
			case "png" -> "png";
			case "jpg" -> "jpg";
			case "jpeg" -> "jpeg";
			case "bmp" -> "bmp";
			default -> throw new CustomException("Invalid image extension: " + extension, ErrorCode.GENERAL);
		};
	}
}
