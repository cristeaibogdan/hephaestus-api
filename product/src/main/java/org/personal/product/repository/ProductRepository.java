package org.personal.product.repository;

import org.personal.product.dto.ProductModelTypeDTO;
import org.personal.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT cp.manufacturer " +
            "FROM Product cp " +
            "WHERE LOWER(cp.category) = LOWER(?1)")
    List<String> getManufacturers(String category);

    @Query("SELECT new org.personal.product.dto.ProductModelTypeDTO(cp.model, cp.type) " +
            "FROM Product cp " +
            "WHERE LOWER(cp.manufacturer) = LOWER(?1)")
    List<ProductModelTypeDTO> findByManufacturer(String manufacturer);
}
