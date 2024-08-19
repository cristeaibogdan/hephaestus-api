package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang3.time.StopWatch;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.entity.WashingMachineDetails;
import org.personal.washingmachine.entity.WashingMachineImage;
import org.personal.washingmachine.vo.WashingMachineReportVO;
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

	public WashingMachineReportVO getReport(WashingMachine washingMachine) {
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
			return new WashingMachineReportVO(
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
		parameters.put("secondPage", getSecondPageReport());
		parameters.put("secondPageParameters", getReportSecondPageParameters(washingMachine.getWashingMachineDetails()));
		return parameters;
	}

	private Map<String, Object> getReportSecondPageParameters(WashingMachineDetails washingMachineDetails) {
		Map<String, Object> parameters = new HashMap<>();

		// ********************************
		// *** Package
		// ********************************

		parameters.put("applicablePackageDamage", washingMachineDetails.getPackageDamage().isApplicablePackageDamage());
		parameters.put("packageDamaged", washingMachineDetails.getPackageDamage().isPackageDamaged());
		parameters.put("packageDirty", washingMachineDetails.getPackageDamage().isPackageDirty());
		parameters.put("packageMaterialAvailable", washingMachineDetails.getPackageDamage().isPackageMaterialAvailable());

		// ********************************
		// *** Visible Surfaces
		// ********************************

		parameters.put("applicableVisibleSurfacesDamage", washingMachineDetails.getVisibleSurfaceDamage().isApplicableVisibleSurfacesDamage());

		parameters.put("visibleSurfacesHasScratches", washingMachineDetails.getVisibleSurfaceDamage().isVisibleSurfacesHasScratches());
		parameters.put("visibleSurfacesScratchesLength", washingMachineDetails.getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength());

		parameters.put("visibleSurfacesHasDents", washingMachineDetails.getVisibleSurfaceDamage().isVisibleSurfacesHasDents());
		parameters.put("visibleSurfacesDentsDepth", washingMachineDetails.getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth());

		parameters.put("visibleSurfacesHasMinorDamage", washingMachineDetails.getVisibleSurfaceDamage().isVisibleSurfacesHasMinorDamage());
		parameters.put("visibleSurfacesMinorDamage", washingMachineDetails.getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage());

		parameters.put("visibleSurfacesHasMajorDamage", washingMachineDetails.getVisibleSurfaceDamage().isVisibleSurfacesHasMajorDamage());
		parameters.put("visibleSurfacesMajorDamage", washingMachineDetails.getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage());

		// ********************************
		// *** Hidden Surfaces
		// ********************************

		parameters.put("applicableHiddenSurfacesDamage", washingMachineDetails.getHiddenSurfaceDamage().isApplicableHiddenSurfacesDamage());

		parameters.put("hiddenSurfacesHasScratches", washingMachineDetails.getHiddenSurfaceDamage().isHiddenSurfacesHasScratches());
		parameters.put("hiddenSurfacesScratchesLength", washingMachineDetails.getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength());

		parameters.put("hiddenSurfacesHasDents", washingMachineDetails.getHiddenSurfaceDamage().isHiddenSurfacesHasDents());
		parameters.put("hiddenSurfacesDentsDepth", washingMachineDetails.getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth());

		parameters.put("hiddenSurfacesHasMinorDamage", washingMachineDetails.getHiddenSurfaceDamage().isHiddenSurfacesHasMinorDamage());
		parameters.put("hiddenSurfacesMinorDamage", washingMachineDetails.getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage());

		parameters.put("hiddenSurfacesHasMajorDamage", washingMachineDetails.getHiddenSurfaceDamage().isHiddenSurfacesHasMajorDamage());
		parameters.put("hiddenSurfacesMajorDamage", washingMachineDetails.getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage());

		// ********************************
		// *** Pricing
		// ********************************

		parameters.put("price", washingMachineDetails.getPrice());
		parameters.put("repairPrice", washingMachineDetails.getRepairPrice());

		return parameters;
	}

	private JasperReport getSecondPageReport() throws JRException {
		InputStream reportPath = getClass().getClassLoader().getResourceAsStream("reports/WashingMachine_SecondPage.jrxml");
		return JasperCompileManager.compileReport(reportPath);
	}
}
