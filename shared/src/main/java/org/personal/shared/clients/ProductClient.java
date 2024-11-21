package org.personal.shared.clients;

import org.personal.shared.clients.config.CustomErrorDecoder;
import org.personal.shared.clients.dtos.GetModelAndTypeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        value = "PRODUCT",
        configuration = CustomErrorDecoder.class,
        url = "${product.service.url}"
)
public interface ProductClient {

    @GetMapping("/{category}/manufacturers")
    List<String> getManufacturers(@PathVariable String category);

    @GetMapping("/{manufacturer}/models-and-types")
    List<GetModelAndTypeResponse> getModelsAndTypes(@PathVariable String manufacturer);
}
