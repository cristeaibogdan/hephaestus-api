package org.personal.product.service;

import org.personal.product.exception.CustomException;
import org.personal.product.exception.ErrorCode;
import org.personal.product.entity.dtos.ProductModelTypeDTO;
import org.personal.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<String> getManufacturers(String category) {
        List<String> response = productRepository.getManufacturers(category.trim());

        if (response.isEmpty()) {
            throw new CustomException(ErrorCode.E_2001, "No manufacturers found");
        }

        return response;
    }


    public List<ProductModelTypeDTO> getModelsAndTypes(String manufacturer) {
        List<ProductModelTypeDTO> response = productRepository.findByManufacturer(manufacturer.trim());

        if (response.isEmpty()) {
            throw new CustomException(ErrorCode.E_2002, "No models or types found for the selected manufacturer");
        }

        return response;
    }
}
