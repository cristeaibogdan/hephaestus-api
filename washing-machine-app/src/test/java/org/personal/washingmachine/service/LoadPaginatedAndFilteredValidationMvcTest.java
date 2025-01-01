package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class LoadPaginatedAndFilteredValidationMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	SearchWashingMachineRequest searchWashingMachineRequest = new SearchWashingMachineRequest(
			0,
			2,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
	);

	@Test
	void should_ThrowValidationException_When_PageIndexIsNegative() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(
				searchWashingMachineRequest.toBuilder()
						.pageIndex(-1)
						.build()
		);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))))
				.andExpect(content().string(containsString("pageIndex")));
	}

	@Test
	void should_ThrowValidationException_When_PageSizeIsLessThanOne() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(
				searchWashingMachineRequest.toBuilder()
						.pageSize(0)
						.build()
		);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))))
				.andExpect(content().string(containsString("pageSize")));
	}

	private ResultActions performRequest(SearchWashingMachineRequest request) throws Exception {
		return mockMvc.perform(
				post("/api/v1/washing-machines")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}
