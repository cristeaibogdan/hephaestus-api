package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.dto.PageRequestDTO;
import org.personal.washingmachine.dto.WashingMachineDetailDTO;
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

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

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

	@Nested
	class testGetRecommendation {

		WashingMachineDetailDTO defaultDetail = new WashingMachineDetailDTO(
				false,
				false,
				false,
				false,
				false,
				false,
				0,
				false,
				0,
				false,
				"",
				false,
				"",
				false,
				false,
				0,
				false,
				0,
				false,
				"",
				false,
				"",
				0,
				0
		);

		@Test
		void should_ThrowValidationException_When_VisibleScratchesLengthIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesScratchesLength(-10)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesScratchesLength")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleScratchesLengthIsOver10() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesScratchesLength(12)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesScratchesLength")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleDentsDepthIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesDentsDepth(-10)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesDentsDepth")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleDentsDepthIsOver10() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesDentsDepth(12)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesDentsDepth")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMinorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMinorDamage(null)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesMinorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMinorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMinorDamage("A".repeat(201))
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesMinorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMajorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMajorDamage(null)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesMajorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMajorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMajorDamage("A".repeat(201))
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("visibleSurfacesMajorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenScratchesLengthIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesScratchesLength(-10)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesScratchesLength")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenScratchesLengthIsOver10() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesScratchesLength(12)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesScratchesLength")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenDentsDepthIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesDentsDepth(-10)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesDentsDepth")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenDentsDepthIsOver10() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesDentsDepth(12)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesDentsDepth")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMinorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMinorDamage(null)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesMinorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMinorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMinorDamage("A".repeat(201))
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesMinorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMajorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMajorDamage(null)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesMajorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMajorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMajorDamage("A".repeat(201))
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("hiddenSurfacesMajorDamage")));
		}

		@Test
		void should_ThrowValidationException_When_PriceIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.price(-1)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("price")));
		}

		@Test
		void should_ThrowValidationException_When_RepairPriceIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.repairPrice(-1)
					.build();

			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString("repairPrice")));
		}


	}
}
