package com.inventory.repository;

import com.inventory.entity.Product;
import com.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByUserAndIsEnabledTrue(User user);
    
    @Query("SELECT p FROM Product p WHERE p.user = :user AND p.isEnabled = true AND " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Product> findByUserAndNameContaining(@Param("user") User user, @Param("name") String name);
    
    @Query("SELECT p FROM Product p JOIN p.tags t WHERE p.user = :user AND p.isEnabled = true AND t.name = :tagName")
    List<Product> findByUserAndTagName(@Param("user") User user, @Param("tagName") String tagName);
    
    @Query("SELECT p FROM Product p WHERE p.user = :user AND p.isEnabled = true AND p.status.name = :statusName")
    List<Product> findByUserAndStatusName(@Param("user") User user, @Param("statusName") String statusName);
    
    @Query("SELECT p FROM Product p WHERE p.user = :user AND p.isEnabled = true AND p.expirationDate <= :date")
    List<Product> findExpiredProducts(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT p FROM Product p WHERE p.user = :user AND p.isEnabled = true AND " +
           "p.expirationDate BETWEEN :startDate AND :endDate")
    List<Product> findExpiringProducts(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Product p WHERE p.user = :user AND p.isEnabled = true AND " +
           "p.lowStockThreshold IS NOT NULL AND p.quantity <= p.lowStockThreshold")
    List<Product> findLowStockProducts(@Param("user") User user);

    @Modifying
    @Query(value = "DELETE FROM product_tags WHERE tag_id = ?1", nativeQuery = true)
    void removeAllTagsFromProduct(String tagId);
}