package org.personal.washingmachine.service.microservice.common;

import org.personal.washingmachine.entity.dtos.ProductModelTypeDTO;
import org.personal.washingmachine.service.microservice.CustomErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        value = "PRODUCT",
        configuration = CustomErrorDecoder.class,
//        url = "http://localhost:8081/api/v1/products"
        url = "${product.service.url}"
)
public interface ProductClient {

    @GetMapping("/{category}/manufacturers")
    List<String> getManufacturers(@PathVariable String category);

    @GetMapping("/{manufacturer}/models-and-types")
    List<ProductModelTypeDTO> getModelsAndTypes(@PathVariable String manufacturer);
}
