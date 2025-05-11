package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.SaveWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SaveIntegrationTest extends BaseIntegrationTest {

	private static final MockMultipartFile MOCK_IMAGE_FILE = new MockMultipartFile( // API expects at least 1 image when saving.
			"imageFiles",
			"whatever.jpeg",
			MediaType.IMAGE_JPEG_VALUE,
			"image content".getBytes());

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeEach
	void checkNoDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {

		@Test
		void should_saveRequest_With_AllPropertiesInDB() {
			// GIVEN
			SaveWashingMachineRequest request = new SaveWashingMachineRequest(
					"Washing Machine",
					IdentificationMode.DATA_MATRIX,
					"WhirlPool",
					"model100",
					"type200",
					"I'm saved",
					ReturnType.SERVICE,
					DamageType.IN_USE,
					new SaveWashingMachineRequest.WashingMachineDetail(
							true,
							false,
							true,
							5,
							2,
							"some minor visible surface damage",
							"major visible damage",
							8,
							3,
							"hidden minor damage",
							"some more hidden major damage",
							1_000,
							100
					)
			);

			// WHEN
			underTest.save(request, List.of(MOCK_IMAGE_FILE));

			// THEN
			WashingMachine actual = repository.findBySerialNumber("I'm saved")
					.orElseThrow();

			assertThat(actual).satisfies(act -> {
				// main fields
				assertThat(act.getCategory()).isEqualTo(request.category());
				assertThat(act.getManufacturer()).isEqualTo(request.manufacturer());
				assertThat(act.getIdentificationMode()).isEqualTo(request.identificationMode());
				assertThat(act.getReturnType()).isEqualTo(request.returnType());
				assertThat(act.getDamageType()).isEqualTo(request.damageType());
				assertThat(act.getSerialNumber()).isEqualTo(request.serialNumber());
				assertThat(act.getModel()).isEqualTo(request.model());
				assertThat(act.getType()).isEqualTo(request.type());
				assertThat(act.getRecommendation()).isNotNull();
				assertThat(act.getCreatedAt().toLocalDate()).isEqualTo(LocalDate.now());

				// package
				assertThat(act.getWashingMachineDetail().getPackageDamage().isPackageDamaged()).isEqualTo(request.washingMachineDetail().packageDamaged());
				assertThat(act.getWashingMachineDetail().getPackageDamage().isPackageDirty()).isEqualTo(request.washingMachineDetail().packageDirty());
				assertThat(act.getWashingMachineDetail().getPackageDamage().isPackageMaterialAvailable()).isEqualTo(request.washingMachineDetail().packageMaterialAvailable());

				// visible surfaces
				assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength()).isEqualTo(request.washingMachineDetail().visibleSurfacesScratchesLength());
				assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth()).isEqualTo(request.washingMachineDetail().visibleSurfacesDentsDepth());
				assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage()).isEqualTo(request.washingMachineDetail().visibleSurfacesMinorDamage());
				assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage()).isEqualTo(request.washingMachineDetail().visibleSurfacesMajorDamage());

				// hidden surfaces
				assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength()).isEqualTo(request.washingMachineDetail().hiddenSurfacesScratchesLength());
				assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth()).isEqualTo(request.washingMachineDetail().hiddenSurfacesDentsDepth());
				assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage()).isEqualTo(request.washingMachineDetail().hiddenSurfacesMinorDamage());
				assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage()).isEqualTo(request.washingMachineDetail().hiddenSurfacesMajorDamage());

				// pricing
				assertThat(act.getWashingMachineDetail().getCostAssessment().getPrice()).isEqualTo(request.washingMachineDetail().price());
				assertThat(act.getWashingMachineDetail().getCostAssessment().getRepairPrice()).isEqualTo(request.washingMachineDetail().repairPrice());

				// images
				assertThat(act.getWashingMachineImages()).hasSize(1);
			});
		}
	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnStatusCreated_When_DTOSaved() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createSaveWashingMachineRequest().toBuilder()
							.serialNumber("I'm ready to be saved in DB")
							.washingMachineDetail(
									TestData.createValidSaveWashingMachineDetailRequest().toBuilder()
											.packageDirty(true)
											.build())
							.build()
			);

			// THEN
			resultActions
					.andExpect(status().isCreated())
					.andExpect(content().string(emptyString()));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumberAlreadyTaken() throws Exception {
			// GIVEN
			SaveWashingMachineRequest request = TestData.createSaveWashingMachineRequest().toBuilder()
					.serialNumber("I already exist in DB")
					.washingMachineDetail(
							TestData.createValidSaveWashingMachineDetailRequest().toBuilder()
									.packageDamaged(true)
									.build())
					.build();

			performRequest(request);

			// WHEN
			ResultActions resultActions = performRequest(request);

			// THEN
			resultActions
					.andExpect(status().isConflict())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		private ResultActions performRequest(SaveWashingMachineRequest request) throws Exception {
			String jsonRequest = jackson.writeValueAsString(request);
			return mockMvc.perform(
					multipart("/api/v1/washing-machines/save")
							.file(new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
									"saveWashingMachineRequest",
									"I_Don't_Matter",
									MediaType.APPLICATION_JSON_VALUE,
									jsonRequest.getBytes())
							)
							.file(MOCK_IMAGE_FILE)
							.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			);
		}
	}
}
