package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.SearchWashingMachineRequest;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class LoadPaginatedAndFilteredMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	@Test
	void should_ThrowValidationException_When_PageIndexIsNegative() throws Exception {
		// GIVEN
		SearchWashingMachineRequest request = TestData.searchWashingMachineRequest().toBuilder()
				.pageIndex(-1)
				.build();

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				post("/api/v1/washing-machines")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));

		// THEN
		resultActions
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString("pageIndex")));
	}

	@Test
	void should_ThrowValidationException_When_PageSizeIsLessThanOne() throws Exception {
		// GIVEN
		SearchWashingMachineRequest request = TestData.searchWashingMachineRequest().toBuilder()
				.pageSize(0)
				.build();

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				post("/api/v1/washing-machines")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));

		// THEN
		resultActions
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString("pageSize")));
	}

	private static final String[] availableLocales = {"en", "es", "it", "ro", "sl"};

	private static Stream<String> localeProvider() {
		return Stream.of(availableLocales);
	}

	@ParameterizedTest
	@MethodSource("localeProvider")
	void should_ThrowCustomException_When_InvalidDate(String locale) throws Exception {
		// GIVEN
		SearchWashingMachineRequest request = TestData.searchWashingMachineRequest().toBuilder()
				.createdAt("invalid date")
				.build();

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				post("/api/v1/washing-machines")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.header("Accept-Language", locale));

		// THEN
		resultActions
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(not(containsString("Internal Translation Error"))));
	}
}
