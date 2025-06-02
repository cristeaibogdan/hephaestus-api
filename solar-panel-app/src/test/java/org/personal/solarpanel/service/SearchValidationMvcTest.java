package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.mapper.SolarPanelMapper;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
				TestData.createSearchSolarPanelRequest()
						.withPageIndex(-100)
						.withPageSize(5)
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
				TestData.createSearchSolarPanelRequest()
						.withPageIndex(0)
						.withPageSize(0)
		);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))))
				.andExpect(content().string(containsString("pageSize")));
	}

	@ParameterizedTest(name = "Invalid sort direction: {0}")
	@ValueSource(strings = {"some gibberish", " sa", "ASC", "DESC"})
	void should_ThrowValidationException_When_SortDirectionInvalid(String sortDirection) throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(
				TestData.createSearchSolarPanelRequest()
						.withPageIndex(0)
						.withPageSize(1)
						.withSortDirection(sortDirection)
		);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))))
				.andExpect(content().string(containsString("sortDirection")));
	}

	@ParameterizedTest(name = "Valid sort direction: {0}")
	@ValueSource(strings = {"asc", "desc", ""})
	void should_ReturnStatusOk_When_SortDirectionValid(String sortDirection) throws Exception {
		// GIVEN
		given(repository.findAll(any(BooleanBuilder.class), any(Pageable.class)))
				.willReturn(Page.empty());

		// WHEN
		ResultActions resultActions = performRequest(
				TestData.createSearchSolarPanelRequest()
						.withPageIndex(0)
						.withPageSize(1)
						.withSortDirection(sortDirection)
		);

		// THEN
		resultActions.andExpect(status().isOk());
	}

	private ResultActions performRequest(SearchSolarPanelRequest request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
		);
	}
}
