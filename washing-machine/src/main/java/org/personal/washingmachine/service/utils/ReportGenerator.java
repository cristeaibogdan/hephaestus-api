package org.personal.washingmachine.service.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang3.time.StopWatch;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetails;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.entity.dtos.WashingMachineReportDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ReportGenerator {

    public Optional<WashingMachineReportDTO> getWashingMachineReport(WashingMachine washingMachine) {
        try {
            StopWatch stopWatch = StopWatch.createStarted();

            // 1. Extract images
            List<WashingMachineImage> images = washingMachine.getWashingMachineImages();

            byte[] image01 = new byte[0];
            byte[] image02 = new byte[0];
            byte[] image03 = new byte[0];

            switch (images.size()) {
                case 0:
                    break;
                case 1:
                    image01 = images.get(0).getImage();
                    break;
                case 2:
                    image01 = images.get(0).getImage();
                    image02 = images.get(1).getImage();
                    break;
                case 3:
                    image01 = images.get(0).getImage();
                    image02 = images.get(1).getImage();
                    image03 = images.get(2).getImage();
                    break;
            }

            // 2. Get the paths for static images
            InputStream hephaestusLogo = getClass().getClassLoader().getResourceAsStream("reports/images/hephaestus-logo.png");
            InputStream HARServicesLogo = getClass().getClassLoader().getResourceAsStream("reports/images/har-services-logo.png");

            // 3. Create a MAP to hold all key - value pairs
            Map<String, Object> parameters = new HashMap<>();
            // Static Images
            parameters.put("hephaestusLogo", hephaestusLogo);
            parameters.put("HARServicesLogo", HARServicesLogo);
            // Product Information
            parameters.put("category", washingMachine.getCategory());
            parameters.put("manufacturer", washingMachine.getManufacturer());
            parameters.put("serialNumber", washingMachine.getSerialNumber());
            parameters.put("model", washingMachine.getModel());
            parameters.put("type", washingMachine.getType());
            // Damage Type and Identification Mode
            parameters.put("damageType", washingMachine.getDamageType());
            parameters.put("returnType", washingMachine.getReturnType());
            parameters.put("identificationMode", washingMachine.getIdentificationMode());
            // Damaged Product Images
            parameters.put("image01", new ByteArrayInputStream(image01));
            parameters.put("image02", new ByteArrayInputStream(image02));
            parameters.put("image03", new ByteArrayInputStream(image03));
            // Recommendation
            parameters.put("damageLevel", washingMachine.getDamageLevel());
            parameters.put("recommendation", washingMachine.getRecommendation());
            parameters.put("createdAt", washingMachine.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            // Second page
            parameters.put("secondPage", getWashingMachineReportSecondPage());
            parameters.put("secondPageParameters", getWashingMachineReportSecondPageParameters(washingMachine.getWashingMachineDetails()));

            // 4. Generate report
            InputStream filePath = getClass().getClassLoader().getResourceAsStream("reports/WashingMachine_FirstPage.jrxml");

            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

            // 5. Export report locally - TEST PURPOSE ONLY
//            String exportPath = System.getProperty("user.dir")+"\\src\\main\\resources\\reports\\Test.pdf";
//            JasperExportManager.exportReportToPdfFile(print,exportPath);
//            return null;

            log.info("Executed in "
                    + stopWatch.getTime(TimeUnit.MINUTES) + "(minutes):"
                    + stopWatch.getTime(TimeUnit.SECONDS) + "(seconds):"
                    + stopWatch.getTime(TimeUnit.MILLISECONDS) + "(milliseconds)");

            // 5. Export report
            return Optional.of(
                    new WashingMachineReportDTO(
                            JasperExportManager.exportReportToPdf(print),
                            washingMachine.getCreatedAt().toString()
                    ));

        } catch (JRException e) {
            log.error("Error generating report " + e);
            return Optional.empty();
        }
    }

    private Map<String, Object> getWashingMachineReportSecondPageParameters(WashingMachineDetails washingMachineDetails) {
        Map<String, Object> parameters = new HashMap<>();

        // ********************************
        // *** Package
        // ********************************

        parameters.put("applicablePackageDamage", washingMachineDetails.isApplicablePackageDamage());
        parameters.put("packageDamaged", washingMachineDetails.isPackageDamaged());
        parameters.put("packageDirty", washingMachineDetails.isPackageDirty());
        parameters.put("packageMaterialAvailable", washingMachineDetails.isPackageMaterialAvailable());

        // ********************************
        // *** Visible Surfaces
        // ********************************

        parameters.put("applicableVisibleSurfacesDamage", washingMachineDetails.isApplicableVisibleSurfacesDamage());

        parameters.put("visibleSurfacesHasScratches", washingMachineDetails.isVisibleSurfacesHasScratches());
        parameters.put("visibleSurfacesScratchesLength", washingMachineDetails.getVisibleSurfacesScratchesLength());

        parameters.put("visibleSurfacesHasDents", washingMachineDetails.isVisibleSurfacesHasDents());
        parameters.put("visibleSurfacesDentsDepth", washingMachineDetails.getVisibleSurfacesDentsDepth());

        parameters.put("visibleSurfacesHasSmallDamage", washingMachineDetails.isVisibleSurfacesHasSmallDamage());
        parameters.put("visibleSurfacesSmallDamage", washingMachineDetails.getVisibleSurfacesSmallDamage());

        parameters.put("visibleSurfacesHasBigDamage", washingMachineDetails.isVisibleSurfacesHasBigDamage());
        parameters.put("visibleSurfacesBigDamage", washingMachineDetails.getVisibleSurfacesBigDamage());

        // ********************************
        // *** Hidden Surfaces
        // ********************************

        parameters.put("applicableHiddenSurfacesDamage", washingMachineDetails.isApplicableHiddenSurfacesDamage());

        parameters.put("hiddenSurfacesHasScratches", washingMachineDetails.isHiddenSurfacesHasScratches());
        parameters.put("hiddenSurfacesScratchesLength", washingMachineDetails.getHiddenSurfacesScratchesLength());

        parameters.put("hiddenSurfacesHasDents", washingMachineDetails.isHiddenSurfacesHasDents());
        parameters.put("hiddenSurfacesDentsDepth", washingMachineDetails.getHiddenSurfacesDentsDepth());

        parameters.put("hiddenSurfacesHasSmallDamage", washingMachineDetails.isHiddenSurfacesHasSmallDamage());
        parameters.put("hiddenSurfacesSmallDamage", washingMachineDetails.getHiddenSurfacesSmallDamage());

        parameters.put("hiddenSurfacesHasBigDamage", washingMachineDetails.isHiddenSurfacesHasBigDamage());
        parameters.put("hiddenSurfacesBigDamage", washingMachineDetails.getHiddenSurfacesBigDamage());

        // ********************************
        // *** Pricing
        // ********************************

        parameters.put("price", washingMachineDetails.getPrice());
        parameters.put("repairPrice", washingMachineDetails.getRepairPrice());

        return parameters;
    }

    private JasperReport getWashingMachineReportSecondPage() throws JRException {
        InputStream filePath = getClass().getClassLoader().getResourceAsStream("reports/WashingMachine_SecondPage.jrxml");
        return JasperCompileManager.compileReport(filePath);
    }
}
