package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class IsSerialNumberInUseMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void should_ReturnStatusOk(boolean expected) throws Exception {
		// GIVEN
		String serialNumber = "ABC-987";

		given(repository.existsBySerialNumber(serialNumber))
				.willReturn(expected);

		// WHEN
		ResultActions resultActions = performRequest(serialNumber);

		// THEN
		resultActions
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(String.valueOf(expected))));
	}

	private ResultActions performRequest(String serialNumber) throws Exception {
		return mockMvc.perform(
				get("/api/v1/washing-machines/{serialNumber}/validate", serialNumber)
		);
	}
}
