package com.inventory.repository;

import com.inventory.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, Integer> {
    Optional<ProductStatus> findByName(String name);
}