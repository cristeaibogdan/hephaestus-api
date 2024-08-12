package org.personal.washingmachine.facade;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.facade.dtos.PageRequestDTO;
import org.personal.washingmachine.facade.dtos.WashingMachineDTO;
import org.personal.washingmachine.facade.dtos.WashingMachineExpandedDTO;
import org.personal.washingmachine.facade.dtos.WashingMachineSimpleDTO;
import org.personal.washingmachine.service.WashingMachineService;
import org.personal.washingmachine.facade.utils.QueryDSLUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;
import static org.personal.washingmachine.facade.dtos.Mapper.*;

@Component
@RequiredArgsConstructor
public class WashingMachineFacade {
	private final WashingMachineService washingMachineService;

	public Page<WashingMachineSimpleDTO> loadPaginatedAndFiltered(PageRequestDTO pageRequestDTO) {
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

		Page<WashingMachine> responsePage = washingMachineService.loadPaginatedAndFiltered(booleanBuilder, pageRequest);

		return responsePage.map(wm -> WashingMachineMapper.toSimpleDTO(wm));
	}

	public WashingMachineExpandedDTO loadExpanded(String serialNumber) {
		WashingMachine washingMachine = washingMachineService.loadExpanded(serialNumber);
		return WashingMachineMapper.toExpandedDTO(washingMachine);
	}

	public void save(WashingMachineDTO washingMachineDTO, List<MultipartFile> imageFiles) {

		//TODO: 1. Should I check if the serial number is in use HERE? or in the service?

		WashingMachine washingMachine = WashingMachineMapper.toEntity(washingMachineDTO);
		imageFiles.forEach(image -> {
			WashingMachineImage washingMachineImage = getWashingMachineImage(image);
			washingMachine.addImage(washingMachineImage);
		});

		washingMachineService.save(washingMachine);
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
