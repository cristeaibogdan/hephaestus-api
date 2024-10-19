package org.personal.product.repository;

import org.personal.product.dto.GetModelAndTypeResponse;
import org.personal.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.manufacturer " +
            "FROM Product p " +
            "WHERE LOWER(p.category) = LOWER(?1)")
    List<String> getManufacturers(String category);

    @Query("SELECT new org.personal.product.dto.GetModelAndTypeResponse(p.model, p.type) " +
            "FROM Product p " +
            "WHERE LOWER(p.manufacturer) = LOWER(?1)")
    List<GetModelAndTypeResponse> findByManufacturer(String manufacturer);
}