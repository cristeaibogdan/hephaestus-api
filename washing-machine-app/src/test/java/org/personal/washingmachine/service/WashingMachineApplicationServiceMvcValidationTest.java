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
	class testGetRecommendation {

		private final WashingMachineDetailDTO defaultDetail = new WashingMachineDetailDTO(
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

		private void validationFails(WashingMachineDetailDTO dto, String propertyName) throws Exception {
			// WHEN
			ResultActions resultActions = mockMvc.perform(
					post("/api/v1/washing-machines/recommendation")
							.content(jackson.writeValueAsString(dto))
							.contentType(MediaType.APPLICATION_JSON));

			// THEN
			resultActions
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(containsString(propertyName)));
		}

		@ParameterizedTest(name = "Validation fails for value = {0}")
		@ValueSource(ints = {-1, 11})
		void should_ThrowValidationException_When_VisibleScratchesLengthIsInvalid(int value) throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesScratchesLength(value)
					.build();

			validationFails(dto, "visibleSurfacesScratchesLength");
		}

		@ParameterizedTest(name = "Validation fails for value = {0}")
		@ValueSource(ints = {-1, 11})
		void should_ThrowValidationException_When_VisibleDentsDepthIsInvalid(int value) throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesDentsDepth(value)
					.build();

			validationFails(dto, "visibleSurfacesDentsDepth");
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMinorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMinorDamage(null)
					.build();

			validationFails(dto, "visibleSurfacesMinorDamage");
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMinorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMinorDamage("A".repeat(201))
					.build();

			validationFails(dto, "visibleSurfacesMinorDamage");
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMajorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMajorDamage(null)
					.build();

			validationFails(dto, "visibleSurfacesMajorDamage");
		}

		@Test
		void should_ThrowValidationException_When_VisibleSurfacesMajorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.visibleSurfacesMajorDamage("A".repeat(201))
					.build();

			validationFails(dto, "visibleSurfacesMajorDamage");
		}

		@ParameterizedTest(name = "Validation fails for value = {0}")
		@ValueSource(ints = {-1, 11})
		void should_ThrowValidationException_When_HiddenScratchesLengthIsInvalid(int value) throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesScratchesLength(value)
					.build();

			validationFails(dto, "hiddenSurfacesScratchesLength");
		}

		@ParameterizedTest(name = "Validation fails for value = {0}")
		@ValueSource(ints = {-1, 11})
		void should_ThrowValidationException_When_HiddenDentsDepthIsInvalid(int value) throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesDentsDepth(value)
					.build();

			validationFails(dto, "hiddenSurfacesDentsDepth");
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMinorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMinorDamage(null)
					.build();

			validationFails(dto, "hiddenSurfacesMinorDamage");
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMinorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMinorDamage("A".repeat(201))
					.build();

			validationFails(dto, "hiddenSurfacesMinorDamage");
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMajorDamageIsNull() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMajorDamage(null)
					.build();

			validationFails(dto, "hiddenSurfacesMajorDamage");
		}

		@Test
		void should_ThrowValidationException_When_HiddenSurfacesMajorDamageIsOver200Chars() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.hiddenSurfacesMajorDamage("A".repeat(201))
					.build();

			validationFails(dto, "hiddenSurfacesMajorDamage");
		}

		@Test
		void should_ThrowValidationException_When_PriceIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.price(-1)
					.build();

			validationFails(dto, "price");
		}

		@Test
		void should_ThrowValidationException_When_RepairPriceIsNegative() throws Exception {
			// GIVEN
			WashingMachineDetailDTO dto = defaultDetail.toBuilder()
					.repairPrice(-1)
					.build();

			validationFails(dto, "repairPrice");
		}

	}
}
