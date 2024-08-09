package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.dtos.PageRequestDTO;
import org.personal.washingmachine.entity.dtos.WashingMachineDTO;
import org.personal.washingmachine.entity.dtos.WashingMachineExpandedDTO;
import org.personal.washingmachine.entity.dtos.WashingMachineSimpleDTO;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.utils.QueryDSLUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;
import static org.personal.washingmachine.entity.dtos.Mapper.WashingMachineDetailsMapper.WashingMachineMapper;

@Service
@RequiredArgsConstructor
public class WashingMachineService {
	private final WashingMachineRepository washingMachineRepository;

	public Page<WashingMachineSimpleDTO> getPaginatedAndFilteredWashingMachines(PageRequestDTO pageRequestDTO) {
		PageRequest pageRequest = PageRequest.of(
				pageRequestDTO.pageIndex(),
				pageRequestDTO.pageSize(),
				Sort.by(washingMachine.createdAt.getMetadata().getName()).descending());

		BooleanBuilder booleanBuilder = new BooleanBuilder()
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.category, pageRequestDTO.category()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.manufacturer, pageRequestDTO.manufacturer()))

				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.damageType, pageRequestDTO.damageType()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.returnType, pageRequestDTO.returnType()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.identificationMode, pageRequestDTO.identificationMode()))

				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.serialNumber, pageRequestDTO.serialNumber()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.model, pageRequestDTO.model()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.type, pageRequestDTO.type()))
				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.recommendation, pageRequestDTO.recommendation()))

				.and(QueryDSLUtils.addIntegerEqualCondition(washingMachine.damageLevel, pageRequestDTO.damageLevel()))

				.and(QueryDSLUtils.addTimestampEqualCondition(washingMachine.createdAt, pageRequestDTO.createdAt()));

		Page<WashingMachine> responsePage = washingMachineRepository.findAll(booleanBuilder, pageRequest);

		if (responsePage.isEmpty()) {
			throw new CustomException(ErrorCode.EMPTY_PAGE);
		}

		return new PageImpl<WashingMachineSimpleDTO>(
				responsePage.getContent().stream()
						.map(washingMachine -> WashingMachineMapper.toSimpleDTO(washingMachine))
						.toList(),
				responsePage.getPageable(),
				responsePage.getTotalElements()
		);
	}

	public WashingMachineExpandedDTO getWashingMachineExpanded(String serialNumber) {
		return washingMachineRepository
				.findBySerialNumber(serialNumber)
				.map(washingMachine -> WashingMachineMapper.toExpandedDTO(washingMachine))
				.orElseThrow(() -> new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber));
	}

	public void saveWashingMachine(WashingMachineDTO washingMachineDTO, List<MultipartFile> imageFiles) {

		boolean existingSerialNumber = washingMachineRepository.existsBySerialNumber(washingMachineDTO.serialNumber());
		if (existingSerialNumber) {
			throw new CustomException(ErrorCode.SERIAL_NUMBER_ALREADY_TAKEN);
		}

		WashingMachine washingMachine = WashingMachineMapper.toEntity(washingMachineDTO);
		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = getWashingMachineImage(image);
			washingMachine.addImage(washingMachineImage);
		});

		washingMachineRepository.save(washingMachine);
	}

	public boolean isSerialNumberInUse(String serialNumber) {
		return washingMachineRepository.existsBySerialNumber(serialNumber);
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
