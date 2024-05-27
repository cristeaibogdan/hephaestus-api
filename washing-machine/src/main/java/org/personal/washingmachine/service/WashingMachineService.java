package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.WashingMachine_;
import org.personal.washingmachine.entity.dtos.*;
import org.personal.washingmachine.exception.CustomException;
import org.personal.washingmachine.exception.ErrorCode;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.personal.washingmachine.service.utils.DamageCalculator;
import org.personal.washingmachine.service.utils.ReportGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WashingMachineService {

    private final WashingMachineRepository washingMachineRepository;
    private final ReportGenerator reportGenerator;
    private final SpecBuilder<WashingMachine> specBuilder = new SpecBuilder<>();

    public Page<WashingMachineDTO> getPaginatedAndFilteredWashingMachines(PageRequestDTO pageRequestDTO) {

        PageRequest pageRequest = PageRequest.of(
                pageRequestDTO.pageIndex(),
                pageRequestDTO.pageSize(),
                Sort.by(WashingMachine_.CREATED_AT).descending()
        );

        Specification<WashingMachine> specification = specBuilder
                .addStringLikeCondition(WashingMachine_.CATEGORY, pageRequestDTO.category())
                .and(specBuilder.addStringLikeCondition(WashingMachine_.MANUFACTURER, pageRequestDTO.manufacturer()))

                .and(specBuilder.addStringLikeCondition(WashingMachine_.DAMAGE_TYPE, pageRequestDTO.damageType()))
                .and(specBuilder.addStringLikeCondition(WashingMachine_.RETURN_TYPE, pageRequestDTO.returnType()))
                .and(specBuilder.addStringLikeCondition(WashingMachine_.IDENTIFICATION_MODE, pageRequestDTO.identificationMode()))

                .and(specBuilder.addStringLikeCondition(WashingMachine_.SERIAL_NUMBER, pageRequestDTO.serialNumber()))
                .and(specBuilder.addStringLikeCondition(WashingMachine_.MODEL, pageRequestDTO.model()))
                .and(specBuilder.addStringLikeCondition(WashingMachine_.TYPE, pageRequestDTO.type()))
                .and(specBuilder.addStringLikeCondition(WashingMachine_.RECOMMENDATION, pageRequestDTO.recommendation()))

                .and(specBuilder.addIntegerEqualCondition(WashingMachine_.DAMAGE_LEVEL, pageRequestDTO.damageLevel()))

                .and(specBuilder.addTimestampEqualCondition(WashingMachine_.CREATED_AT, pageRequestDTO.createdAt()));

        Page<WashingMachine> responsePage = washingMachineRepository.findAll(specification, pageRequest);

        if (responsePage.isEmpty()) {
            throw new CustomException(ErrorCode.E_1006, "Requested page is empty");
        }

        // Transform entities to DTOs before returning
        return new PageImpl<WashingMachineDTO>(
                responsePage.getContent().stream()
                        .map(washingMachine -> washingMachine.toWashingMachineDTO())
                        .toList(),
                responsePage.getPageable(),
                responsePage.getTotalElements()
        );
    }

    public WashingMachineExpandedDTO getWashingMachineExpanded(String serialNumber) {
        return washingMachineRepository
                .findBySerialNumber(serialNumber)
                .map(washingMachine -> washingMachine.toWashingMachineExpandedDTO())
                .orElseThrow(() -> new CustomException(ErrorCode.E_1010, "No product with serial number found"));
    }

    public void saveWashingMachine(WashingMachine washingMachine, List<MultipartFile> imageFiles) {

        boolean existingSerialNumber = washingMachineRepository.existsBySerialNumber(washingMachine.getSerialNumber());
        if (existingSerialNumber) {
            throw new CustomException(ErrorCode.E_1005, "Serial number is taken");
        }

        List<WashingMachineImage> washingMachineImages = getWashingMachineImages(imageFiles);

        // 5. SET THE IMAGE LIST TO THE DAMAGED PRODUCT AND SAVE IN DB
        washingMachine.setWashingMachineImages(washingMachineImages);
        washingMachineRepository.save(washingMachine);
    }

    public boolean isSerialNumberInUse(String serialNumber) {
        return washingMachineRepository.existsBySerialNumber(serialNumber);
    }


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

        return reportGenerator
                .getWashingMachineReport(washingMachine)
                .orElseThrow(() -> new CustomException(ErrorCode.E_1011, "Error while creating Jasper report"));
    }

//*********************************************************************************************
//******************** HELPER METHODS
//*********************************************************************************************

    private List<WashingMachineImage> getWashingMachineImages(List<MultipartFile> imageFiles) {
        List<WashingMachineImage> washingMachineImages = new ArrayList<>();

        imageFiles.forEach(imageFile -> {

            // 1. GET IMAGE BYTES
            byte[] image = new byte[0];

            try {
                image = imageFile.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 2. BUILD IMAGEPREFIX
            String imagePrefix = "data:image/" + getImageExtension(imageFile) + ";base64,";

            // 3. BUILD DAMAGED PRODUCT IMAGE
            WashingMachineImage washingMachineImage = new WashingMachineImage(
                    imagePrefix,
                    image);

            // 4. SAVE IT IN THE LIST
            washingMachineImages.add(washingMachineImage);
        });

        return washingMachineImages;
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
