package org.personal.solarpanel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang3.time.StopWatch;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@RestController
public class SolarPanelReportGenerator {

	@GetMapping("/report")
	public void getReport() {
		StopWatch stopWatch = StopWatch.createStarted();

		try {
			Map<String, Object> parameters = getReportFirstPageParameters();

			InputStream reportPath = getClass().getClassLoader().getResourceAsStream("reports/SolarPanel.jrxml");
			JasperReport compiledReport = JasperCompileManager.compileReport(reportPath);
			JasperPrint filledReport = JasperFillManager.fillReport(compiledReport, parameters, new JREmptyDataSource());

			// Export report locally - TEST PURPOSE ONLY
			 String exportPath = System.getProperty("user.dir")+"\\solar-panel-app\\src\\main\\resources\\reports\\Test.pdf";
			 JasperExportManager.exportReportToPdfFile(filledReport,exportPath);
			// return null;

			log.info("Executed in {} (minutes): {} (seconds): {} (milliseconds)",
					stopWatch.getTime(TimeUnit.MINUTES),
					stopWatch.getTime(TimeUnit.SECONDS),
					stopWatch.getTime(TimeUnit.MILLISECONDS));

			// TODO: Export report response

		} catch (JRException e) {
			throw new CustomException(e, ErrorCode.REPORT_GENERATION_FAIL);
		}
	}

	private Map<String, Object> getReportFirstPageParameters() throws JRException {

		Map<String, Object> parameters = new HashMap<>();
		// Static Images
		parameters.put("hephaestusLogo", getClass().getClassLoader().getResourceAsStream("reports/images/hephaestus-logo.png"));
		parameters.put("HARServicesLogo", getClass().getClassLoader().getResourceAsStream("reports/images/har-services-logo.png"));
		// Product Information
		parameters.put("category", "Test001");
		parameters.put("manufacturer", "Test002");
		parameters.put("serialNumber", "Test003");
		parameters.put("model", "Test004");
		parameters.put("type", "Test005");
		// Recommendation
		parameters.put("recommendation", "RECYCLE");
		parameters.put("createdAt", "TestDate");
		// Damages
		parameters.put("hotSpots", false);
		parameters.put("microCracks", true);
		parameters.put("snailTrails", true);
		parameters.put("brokenGlass", false);
		parameters.put("additionalDetails", "Some random strings should popup here!");

		return parameters;
	}
}
