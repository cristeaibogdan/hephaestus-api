package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.dto.PageRequestDTO;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
public class WashingMachineApplicationServiceMvcValidationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	@Nested
	class testLoadPaginatedAndFiltered {

		@Test
		void should_ThrowValidationException_When_PageIndexIsNegative() throws Exception {
			// GIVEN
			PageRequestDTO dto = PageRequestDTO.builder()
					.pageIndex(-1)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("pageIndex")));
		}

		@Test
		void should_ThrowValidationException_When_PageSizeIsLessThanOne() throws Exception {
			// GIVEN
			PageRequestDTO dto = PageRequestDTO.builder()
					.pageSize(-1)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("pageSize")));
		}
	}
}
