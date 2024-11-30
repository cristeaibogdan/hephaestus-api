package org.personal.product.service;

import org.personal.product.dto.GetModelAndTypeResponse;
import org.personal.product.repository.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.shared.exception.CustomException;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ProductServiceTest {

    private final ProductRepository productRepositoryMock = mock(ProductRepository.class);
    private final ProductService underTest = new ProductService(productRepositoryMock );

    @Nested
    class TestGetManufacturers {

        @Test
        void should_ThrowCustomException_When_DatabaseIsEmpty() {
            // GIVEN
            String category = "Washing Machine";
            given(productRepositoryMock.getManufacturers(category))
                    .willReturn(emptyList());

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.getManufacturers(category))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_ReturnList_When_DatabaseHasData() {
            // GIVEN
            List<String> expected = List.of("Bosch", "Siemens", "Panasonic");
            String category = "Washing Machine";

            given(productRepositoryMock.getManufacturers(category))
                    .willReturn(expected);

            // WHEN
            List<String> actual = underTest.getManufacturers(category);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class TestGetModelsAndTypes {

        @Test
        void should_ThrowCustomException_When_DatabaseIsEmpty() {
            // GIVEN
            String manufacturer = "Gorenje";

            given(productRepositoryMock.findByManufacturer(manufacturer))
                    .willReturn(emptyList());

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.getModelsAndTypes(manufacturer))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_ReturnList_When_DatabaseHasData() {
            // GIVEN
            List<GetModelAndTypeResponse> expected = List.of(
                    new GetModelAndTypeResponse("ModelOne", "TypeOne"),
                    new GetModelAndTypeResponse("ModelTwo", "TypeTwo"),
                    new GetModelAndTypeResponse("ModelThree", "TypeThree")
            );
            String manufacturer = "Gorenje";

            given(productRepositoryMock.findByManufacturer(manufacturer))
                    .willReturn(expected);

            // WHEN
            List<GetModelAndTypeResponse> actual = underTest.getModelsAndTypes(manufacturer);

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

}