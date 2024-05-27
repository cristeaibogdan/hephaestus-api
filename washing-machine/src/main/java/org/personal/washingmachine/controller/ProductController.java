package org.personal.washingmachine.controller;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.entity.dtos.ProductModelTypeDTO;
import org.personal.washingmachine.service.microservice.common.ProductClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
class ProductController {

    private final ProductClient productClient;

    @GetMapping("/{category}/manufacturers")
    List<String> getManufacturers(@PathVariable String category) {
        return productClient.getManufacturers(category);
    }

    @GetMapping("/{manufacturer}/models-and-types")
    List<ProductModelTypeDTO> getModelsAndTypes(@PathVariable String manufacturer) {
        return productClient.getModelsAndTypes(manufacturer);
    }
}
