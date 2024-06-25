package org.personal.product.service;

import org.personal.product.entity.dtos.ProductModelTypeDTO;
import org.personal.product.repository.ProductRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.shared.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService underTest;

    @Mock
    private ProductRepository productRepositoryMock;

    @Nested
    class TestGetManufacturers {

        @Test
        void should_ThrowCustomException_When_DatabaseIsEmpty() {
            // GIVEN
            given(productRepositoryMock.getManufacturers(anyString()))
                    .willReturn(emptyList());

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.getManufacturers(RandomStringUtils.random(5)))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_ReturnList_When_DatabaseHasData() {
            // GIVEN
            List<String> expected = new ArrayList<>();
            expected.add("Bosch");
            expected.add("Siemens");
            expected.add("Panasonic");

            given(productRepositoryMock.getManufacturers(anyString()))
                    .willReturn(expected);

            // WHEN
            List<String> actual = underTest.getManufacturers(RandomStringUtils.random(5));

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class TestGetModelsAndTypes {

        @Test
        void should_ThrowCustomException_When_DatabaseIsEmpty() {
            // GIVEN
            given(productRepositoryMock.findByManufacturer(anyString()))
                    .willReturn(emptyList());

            // WHEN & THEN
            assertThatThrownBy(() -> underTest.getModelsAndTypes(RandomStringUtils.random(5)))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        void should_ReturnList_When_DatabaseHasData() {
            // GIVEN
            List<ProductModelTypeDTO> expected = new ArrayList<>();
            expected.add(new ProductModelTypeDTO("ModelOne", "TypeOne"));
            expected.add(new ProductModelTypeDTO("ModelTwo", "TypeTwo"));
            expected.add(new ProductModelTypeDTO("ModelThree", "TypeThree"));

            given(productRepositoryMock.findByManufacturer(anyString()))
                    .willReturn(expected);

            // WHEN
            List<ProductModelTypeDTO> actual = underTest.getModelsAndTypes(RandomStringUtils.random(5));

            // THEN
            assertThat(actual).isEqualTo(expected);
        }
    }

}