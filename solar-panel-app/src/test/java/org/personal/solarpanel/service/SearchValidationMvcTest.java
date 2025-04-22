package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.mapper.SolarPanelMapper;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolarPanelApplicationService.class)
class SearchValidationMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean SolarPanelRepository repository;
	@MockBean SolarPanelMapper mapper;

	@Test
	void should_ThrowValidationException_When_PageIndexIsNegative() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(
				TestData.createSearchSolarPanelRequest().toBuilder()
						.pageIndex(-100)
						.pageSize(5)
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
				TestData.createSearchSolarPanelRequest().toBuilder()
						.pageIndex(0)
						.pageSize(0)
						.build()
		);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))))
				.andExpect(content().string(containsString("pageSize")));
	}

	private ResultActions performRequest(SearchSolarPanelRequest request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}
