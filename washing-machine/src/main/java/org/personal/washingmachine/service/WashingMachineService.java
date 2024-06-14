package org.personal.washingmachine.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.dtos.*;
import org.personal.washingmachine.exception.CustomException;
import org.personal.washingmachine.exception.ErrorCode;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.utils.DamageCalculator;
import org.personal.washingmachine.service.utils.QueryDSLUtils;
import org.personal.washingmachine.service.utils.ReportGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static org.personal.washingmachine.entity.QWashingMachine.washingMachine;
import static org.personal.washingmachine.entity.dtos.Mapper.WashingMachineDetailsMapper.WashingMachineMapper;

@Service
@RequiredArgsConstructor
public class WashingMachineService {
    private final WashingMachineRepository washingMachineRepository;
    private final ReportGenerator reportGenerator;

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
            throw new CustomException(ErrorCode.E_1006, "Requested page is empty");
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
                .orElseThrow(() -> new CustomException(ErrorCode.E_1010, "No product with serial number found"));
    }

    @Transactional
    public void saveWashingMachine(WashingMachineDTO washingMachineDTO, List<MultipartFile> imageFiles) {

        boolean existingSerialNumber = washingMachineRepository.existsBySerialNumber(washingMachineDTO.serialNumber());
        if (existingSerialNumber) {
            throw new CustomException(ErrorCode.E_1005, "Serial number is taken");
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


    // TODO: Instead of having a middle man method, move to DamageCalculator
    // TODO: If you decide to make it a @Service. It currently is a static class.
    public WashingMachineEvaluationDTO generateWashingMachineDamageEvaluation(
            WashingMachineDetailsDTO washingMachineDetailsDTO) {

        int damageLevelForPackage = DamageCalculator.calculateDamageLevelForPackage(washingMachineDetailsDTO);
        int damageLevelForVisibleSurfaces = DamageCalculator.calculateDamageLevelForVisibleSurfaces(washingMachineDetailsDTO);
        int damageLevelForHiddenSurfaces = DamageCalculator.calculateDamageLevelForHiddenSurfaces(washingMachineDetailsDTO);
        int damageLevelForPricing = DamageCalculator.calculateDamageLevelForPricing(washingMachineDetailsDTO);

        int damageLevel = NumberUtils.max(
                damageLevelForPackage,
                damageLevelForVisibleSurfaces,
                damageLevelForHiddenSurfaces,
                damageLevelForPricing
        );

        String recommendation = DamageCalculator.getRecommendation(damageLevel);

        return new WashingMachineEvaluationDTO(damageLevel, recommendation);
    }

//*********************************************************************************************
//******************** JASPERSOFT REPORT
//*********************************************************************************************

    public WashingMachineReportDTO getWashingMachineReport(String serialNumber) {

        WashingMachine washingMachine = washingMachineRepository
                .findBySerialNumber(serialNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.E_1010, "No product with serial number found"));

        return reportGenerator.getWashingMachineReport(washingMachine);
    }

//*********************************************************************************************
//******************** HELPER METHODS
//*********************************************************************************************

    private WashingMachineImage getWashingMachineImage(MultipartFile imageFile) {

        byte[] image;

        try {
            image = imageFile.getBytes();
        } catch (IOException e) {
            throw new CustomException(e, ErrorCode.E_9999, "Could not extract bytes from image: " + imageFile.getName());
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
            default -> throw new CustomException(ErrorCode.E_1008, "Invalid image extension");
        };
    }

}
