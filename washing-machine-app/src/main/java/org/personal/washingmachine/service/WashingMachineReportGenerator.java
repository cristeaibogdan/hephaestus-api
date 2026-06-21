package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDamage;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class WashingMachineReportGenerator {

	public GetWashingMachineReportResponse getReport(WashingMachine washingMachine) {
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
			return new GetWashingMachineReportResponse(
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
		parameters.put("hephaestusLogo", getClass().getClassLoader().getResourceAsStream("images/hephaestus-logo.png"));
		parameters.put("HARServicesLogo", getClass().getClassLoader().getResourceAsStream("images/har-services-logo.png"));
		// Product Information
		parameters.put("category", washingMachine.getCategory());
		parameters.put("manufacturer", washingMachine.getManufacturer());
		parameters.put("serialNumber", washingMachine.getSerialNumber());
		parameters.put("model", washingMachine.getModel());
		parameters.put("type", washingMachine.getType());
		// Damage Type and Identification Mode
		parameters.put("identificationMode", enumToReadableLabel(washingMachine.getIdentificationMode().name()));
		parameters.put("returnType", enumToReadableLabel(washingMachine.getReturnType().name()));
		parameters.put("damageType", enumToReadableLabel(washingMachine.getDamageType().name()));
		// Damaged Product Images
		parameters.put("image01", new ByteArrayInputStream(image01));
		parameters.put("image02", new ByteArrayInputStream(image02));
		parameters.put("image03", new ByteArrayInputStream(image03));
		// Recommendation
		parameters.put("recommendation", washingMachine.getRecommendation().toString());
		parameters.put("createdAt", washingMachine.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		// Second page
		parameters.put("secondPage", getSecondPageReport());
		parameters.put("secondPageParameters", getReportSecondPageParameters(washingMachine.getWashingMachineDamage()));
		return parameters;
	}

	private Map<String, Object> getReportSecondPageParameters(WashingMachineDamage washingMachineDamage) {
		Map<String, Object> parameters = new HashMap<>();

		// ********************************
		// *** Package
		// ********************************

		parameters.put("applicablePackageDamage", washingMachineDamage.getPackageDamage().isApplicable());
		parameters.put("packageDamaged", washingMachineDamage.getPackageDamage().isPackageDamaged());
		parameters.put("packageDirty", washingMachineDamage.getPackageDamage().isPackageDirty());
		parameters.put("packageMaterialAvailable", washingMachineDamage.getPackageDamage().isPackageMaterialAvailable());

		// ********************************
		// *** Visible Surfaces
		// ********************************

		parameters.put("applicableVisibleSurfacesDamage", washingMachineDamage.getVisibleSurfaceDamage().isApplicable());

		parameters.put("visibleSurfacesHasScratches", washingMachineDamage.getVisibleSurfaceDamage().hasScratches());
		parameters.put("visibleSurfacesScratchesLength", washingMachineDamage.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength());

		parameters.put("visibleSurfacesHasDents", washingMachineDamage.getVisibleSurfaceDamage().hasDents());
		parameters.put("visibleSurfacesDentsDepth", washingMachineDamage.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth());

		parameters.put("visibleSurfacesHasMinorDamage", washingMachineDamage.getVisibleSurfaceDamage().hasMinorDamage());
		parameters.put("visibleSurfacesMinorDamage", washingMachineDamage.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage());

		parameters.put("visibleSurfacesHasMajorDamage", washingMachineDamage.getVisibleSurfaceDamage().hasMajorDamage());
		parameters.put("visibleSurfacesMajorDamage", washingMachineDamage.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage());

		// ********************************
		// *** Hidden Surfaces
		// ********************************

		parameters.put("applicableHiddenSurfacesDamage", washingMachineDamage.getHiddenSurfaceDamage().isApplicable());

		parameters.put("hiddenSurfacesHasScratches", washingMachineDamage.getHiddenSurfaceDamage().hasScratches());
		parameters.put("hiddenSurfacesScratchesLength", washingMachineDamage.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength());

		parameters.put("hiddenSurfacesHasDents", washingMachineDamage.getHiddenSurfaceDamage().hasDents());
		parameters.put("hiddenSurfacesDentsDepth", washingMachineDamage.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth());

		parameters.put("hiddenSurfacesHasMinorDamage", washingMachineDamage.getHiddenSurfaceDamage().hasMinorDamage());
		parameters.put("hiddenSurfacesMinorDamage", washingMachineDamage.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage());

		parameters.put("hiddenSurfacesHasMajorDamage", washingMachineDamage.getHiddenSurfaceDamage().hasMajorDamage());
		parameters.put("hiddenSurfacesMajorDamage", washingMachineDamage.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage());

		// ********************************
		// *** Pricing
		// ********************************

		parameters.put("price", washingMachineDamage.getCostAssessment().getPrice());
		parameters.put("repairPrice", washingMachineDamage.getCostAssessment().getRepairPrice());

		return parameters;
	}

	private JasperReport getSecondPageReport() throws JRException {
		InputStream reportPath = getClass().getClassLoader().getResourceAsStream("reports/WashingMachine_SecondPage.jrxml");
		return JasperCompileManager.compileReport(reportPath);
	}

	private final Map<String, String> specialCases = new HashMap<>(Map.of(
			"QR_CODE", "QR Code"
	));

	private String enumToReadableLabel(String enumKey) {
		String specialCaseLabel = specialCases.get(enumKey);
		if (specialCaseLabel != null) {
			return specialCaseLabel;
		}

		return Arrays.stream(enumKey.split("_"))
				.map(w -> w.toLowerCase())
				.map(w -> StringUtils.capitalize(w))
				.collect(Collectors.joining(" "));
	}
}
