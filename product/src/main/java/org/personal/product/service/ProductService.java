package org.personal.product.service;

import lombok.RequiredArgsConstructor;
import org.personal.product.dto.ProductModelTypeDTO;
import org.personal.product.repository.ProductRepository;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<String> getManufacturers(String category) {
        List<String> response = productRepository.getManufacturers(category.trim());

        if (response.isEmpty()) {
            throw new CustomException(ErrorCode.NO_MANUFACTURERS_FOUND);
        }

        return response;
    }

    public List<ProductModelTypeDTO> getModelsAndTypes(String manufacturer) {
        List<ProductModelTypeDTO> response = productRepository.findByManufacturer(manufacturer.trim());

        if (response.isEmpty()) {
            throw new CustomException(ErrorCode.NO_MODELS_TYPES_FOUND_FOR_MANUFACTURER);
        }

        return response;
    }
}
