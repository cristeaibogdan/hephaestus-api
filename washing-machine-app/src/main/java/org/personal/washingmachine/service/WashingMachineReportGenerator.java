package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang3.time.StopWatch;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.WashingMachineReportDTO;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetail;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class WashingMachineReportGenerator {

	public WashingMachineReportDTO getReport(WashingMachine washingMachine) {
		StopWatch stopWatch = StopWatch.createStarted();

		try {
			Map<String, Object> parameters = getReportFirstPageParameters(washingMachine);

			InputStream reportPath = getClass().getClassLoader().getResourceAsStream("reports/WashingMachine_FirstPage.jrxml");
			JasperReport compiledReport = JasperCompileManager.compileReport(reportPath);
			JasperPrint filledReport = JasperFillManager.fillReport(compiledReport, parameters, new JREmptyDataSource());

			// Export report locally - TEST PURPOSE ONLY
			// String exportPath = System.getProperty("user.dir")+"\\washing-machine\\src\\main\\resources\\reports\\Test.pdf";
			// JasperExportManager.exportReportToPdfFile(filledReport,exportPath);
			// return null;

			log.info("Executed in {} (minutes): {} (seconds): {} (milliseconds)",
					stopWatch.getTime(TimeUnit.MINUTES),
					stopWatch.getTime(TimeUnit.SECONDS),
					stopWatch.getTime(TimeUnit.MILLISECONDS));

			// Export report
			return new WashingMachineReportDTO(
					JasperExportManager.exportReportToPdf(filledReport),
					washingMachine.getCreatedAt().toString());

		} catch (JRException e) {
			throw new CustomException(e, ErrorCode.REPORT_GENERATION_FAIL);
		}
	}

	private Map<String, Object> getReportFirstPageParameters(WashingMachine washingMachine) throws JRException {

		List<WashingMachineImage> images = washingMachine.getWashingMachineImages();

		byte[] image01 = images.size() > 0
				? images.get(0).getImage()
				: new byte[0];
		byte[] image02 = images.size() > 1
				? images.get(1).getImage()
				: new byte[0];
		byte[] image03 = images.size() > 2
				? images.get(2).getImage()
				: new byte[0];

		Map<String, Object> parameters = new HashMap<>();
		// Static Images
		parameters.put("hephaestusLogo", getClass().getClassLoader().getResourceAsStream("reports/images/hephaestus-logo.png"));
		parameters.put("HARServicesLogo", getClass().getClassLoader().getResourceAsStream("reports/images/har-services-logo.png"));
		// Product Information
		parameters.put("category", washingMachine.getCategory());
		parameters.put("manufacturer", washingMachine.getManufacturer());
		parameters.put("serialNumber", washingMachine.getSerialNumber());
		parameters.put("model", washingMachine.getModel());
		parameters.put("type", washingMachine.getType());
		// Damage Type and Identification Mode
		parameters.put("identificationMode", washingMachine.getIdentificationMode().getLabel());
		parameters.put("returnType", washingMachine.getReturnType().getLabel());
		parameters.put("damageType", washingMachine.getDamageType().getLabel());
		// Damaged Product Images
		parameters.put("image01", new ByteArrayInputStream(image01));
		parameters.put("image02", new ByteArrayInputStream(image02));
		parameters.put("image03", new ByteArrayInputStream(image03));
		// Recommendation
		parameters.put("recommendation", washingMachine.getRecommendation().toString());
		parameters.put("createdAt", washingMachine.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		// Second page
		parameters.put("secondPage", getSecondPageReport());
		parameters.put("secondPageParameters", getReportSecondPageParameters(washingMachine.getWashingMachineDetail()));
		return parameters;
	}

	private Map<String, Object> getReportSecondPageParameters(WashingMachineDetail washingMachineDetail) {
		Map<String, Object> parameters = new HashMap<>();

		// ********************************
		// *** Package
		// ********************************

		parameters.put("applicablePackageDamage", washingMachineDetail.getPackageDamage().isApplicable());
		parameters.put("packageDamaged", washingMachineDetail.getPackageDamage().isPackageDamaged());
		parameters.put("packageDirty", washingMachineDetail.getPackageDamage().isPackageDirty());
		parameters.put("packageMaterialAvailable", washingMachineDetail.getPackageDamage().isPackageMaterialAvailable());

		// ********************************
		// *** Visible Surfaces
		// ********************************

		parameters.put("applicableVisibleSurfacesDamage", washingMachineDetail.getVisibleSurfaceDamage().isApplicable());

		parameters.put("visibleSurfacesHasScratches", washingMachineDetail.getVisibleSurfaceDamage().isVisibleSurfacesHasScratches());
		parameters.put("visibleSurfacesScratchesLength", washingMachineDetail.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength());

		parameters.put("visibleSurfacesHasDents", washingMachineDetail.getVisibleSurfaceDamage().isVisibleSurfacesHasDents());
		parameters.put("visibleSurfacesDentsDepth", washingMachineDetail.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth());

		parameters.put("visibleSurfacesHasMinorDamage", washingMachineDetail.getVisibleSurfaceDamage().isVisibleSurfacesHasMinorDamage());
		parameters.put("visibleSurfacesMinorDamage", washingMachineDetail.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage());

		parameters.put("visibleSurfacesHasMajorDamage", washingMachineDetail.getVisibleSurfaceDamage().isVisibleSurfacesHasMajorDamage());
		parameters.put("visibleSurfacesMajorDamage", washingMachineDetail.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage());

		// ********************************
		// *** Hidden Surfaces
		// ********************************

		parameters.put("applicableHiddenSurfacesDamage", washingMachineDetail.getHiddenSurfaceDamage().isApplicable());

		parameters.put("hiddenSurfacesHasScratches", washingMachineDetail.getHiddenSurfaceDamage().hasScratches());
		parameters.put("hiddenSurfacesScratchesLength", washingMachineDetail.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength());

		parameters.put("hiddenSurfacesHasDents", washingMachineDetail.getHiddenSurfaceDamage().isHiddenSurfacesHasDents());
		parameters.put("hiddenSurfacesDentsDepth", washingMachineDetail.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth());

		parameters.put("hiddenSurfacesHasMinorDamage", washingMachineDetail.getHiddenSurfaceDamage().isHiddenSurfacesHasMinorDamage());
		parameters.put("hiddenSurfacesMinorDamage", washingMachineDetail.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage());

		parameters.put("hiddenSurfacesHasMajorDamage", washingMachineDetail.getHiddenSurfaceDamage().isHiddenSurfacesHasMajorDamage());
		parameters.put("hiddenSurfacesMajorDamage", washingMachineDetail.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage());

		// ********************************
		// *** Pricing
		// ********************************

		parameters.put("price", washingMachineDetail.getPrice());
		parameters.put("repairPrice", washingMachineDetail.getRepairPrice());

		return parameters;
	}

	private JasperReport getSecondPageReport() throws JRException {
		InputStream reportPath = getClass().getClassLoader().getResourceAsStream("reports/WashingMachine_SecondPage.jrxml");
		return JasperCompileManager.compileReport(reportPath);
	}
}
