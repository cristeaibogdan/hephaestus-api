package org.personal.product.controller;

import org.personal.product.entity.dtos.ProductModelTypeDTO;
import org.personal.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
class ProductController {

    private final ProductService productService;

    @GetMapping("/{category}/manufacturers")
    List<String> getManufacturers(@PathVariable String category) {
        return productService.getManufacturers(category);
    }

    @GetMapping("/{manufacturer}/models-and-types")
    List<ProductModelTypeDTO> getModelsAndTypes(@PathVariable String manufacturer) {
        return productService.getModelsAndTypes(manufacturer);
    }
}
