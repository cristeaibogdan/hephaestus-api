package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.CreateWashingMachineDetailRequest;
import org.personal.washingmachine.dto.CreateWashingMachineRequest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // Ensure Hibernate sessions are properly managed in your testing environment. Avoids "could not initialize proxy" Exception. Specific to tests only.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaveIntegrationTest extends BaseIntegrationTest {

	static final MockMultipartFile MOCK_IMAGE_FILE = new MockMultipartFile( // API expects at least 1 image when saving.
			"imageFiles",
			"whatever.jpeg",
			MediaType.IMAGE_JPEG_VALUE,
			"image content".getBytes());

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeEach
	void databaseIsEmpty() {
		assertThat(washingMachineRepository.count()).isZero();
	}

	@Test
	void should_saveRequest_With_AllPropertiesInDB() {
		// GIVEN
		CreateWashingMachineDetailRequest detail = new CreateWashingMachineDetailRequest(
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
		);

		CreateWashingMachineRequest request = new CreateWashingMachineRequest(
				"Washing Machine",
				IdentificationMode.DATA_MATRIX,
				"WhirlPool",
				"model100",
				"type200",
				"I'm saved",
				ReturnType.SERVICE,
				DamageType.IN_USE,
				detail
		);

		// WHEN
		underTest.save(request, List.of(MOCK_IMAGE_FILE));

		// THEN
		WashingMachine actual = washingMachineRepository.findBySerialNumber("I'm saved")
				.orElseThrow();

		assertThat(actual)
				.satisfies(act -> {
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
					assertThat(act.getWashingMachineDetail().getPackageDamage().isPackageDamaged()).isEqualTo(request.createWashingMachineDetailRequest().packageDamaged());
					assertThat(act.getWashingMachineDetail().getPackageDamage().isPackageDirty()).isEqualTo(request.createWashingMachineDetailRequest().packageDirty());
					assertThat(act.getWashingMachineDetail().getPackageDamage().isPackageMaterialAvailable()).isEqualTo(request.createWashingMachineDetailRequest().packageMaterialAvailable());

					// visible surfaces
					assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesScratchesLength()).isEqualTo(request.createWashingMachineDetailRequest().visibleSurfacesScratchesLength());
					assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesDentsDepth()).isEqualTo(request.createWashingMachineDetailRequest().visibleSurfacesDentsDepth());
					assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesMinorDamage()).isEqualTo(request.createWashingMachineDetailRequest().visibleSurfacesMinorDamage());
					assertThat(act.getWashingMachineDetail().getVisibleSurfaceDamage().getVisibleSurfacesMajorDamage()).isEqualTo(request.createWashingMachineDetailRequest().visibleSurfacesMajorDamage());

					// hidden surfaces
					assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesScratchesLength()).isEqualTo(request.createWashingMachineDetailRequest().hiddenSurfacesScratchesLength());
					assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesDentsDepth()).isEqualTo(request.createWashingMachineDetailRequest().hiddenSurfacesDentsDepth());
					assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesMinorDamage()).isEqualTo(request.createWashingMachineDetailRequest().hiddenSurfacesMinorDamage());
					assertThat(act.getWashingMachineDetail().getHiddenSurfaceDamage().getHiddenSurfacesMajorDamage()).isEqualTo(request.createWashingMachineDetailRequest().hiddenSurfacesMajorDamage());

					// pricing
					assertThat(act.getWashingMachineDetail().getPrice()).isEqualTo(request.createWashingMachineDetailRequest().price());
					assertThat(act.getWashingMachineDetail().getRepairPrice()).isEqualTo(request.createWashingMachineDetailRequest().repairPrice());

					// images
					assertThat(act.getWashingMachineImages()).hasSize(1);
				});
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_SerialNumberAlreadyTaken() throws Exception {
			// GIVEN
			CreateWashingMachineDetailRequest detail = TestData.createWashingMachineDetailRequest().toBuilder()
					.packageDamaged(true)
					.build();

			CreateWashingMachineRequest request = TestData.createWashingMachineRequest().toBuilder()
					.serialNumber("I already exist in DB")
					.createWashingMachineDetailRequest(detail)
					.build();

			performRequest(request);

			// WHEN
			ResultActions resultActions = performRequest(request);

			// THEN
			resultActions
					.andExpect(status().isConflict())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		private ResultActions performRequest(CreateWashingMachineRequest request) throws Exception {
			String jsonRequest = jackson.writeValueAsString(request);
			return mockMvc.perform(
					multipart("/api/v1/washing-machines/save")
							.file(new MockMultipartFile( // avoids error Content-Type 'application/octet-stream' is not supported
									"createWashingMachineRequest",
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
