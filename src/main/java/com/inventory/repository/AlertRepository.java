package com.inventory.repository;

import com.inventory.entity.Alert;
import com.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    @Query("SELECT a FROM Alert a WHERE a.product.user = :user AND a.alertDate <= :date AND a.notified = false")
    List<Alert> findPendingAlerts(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Alert a WHERE a.product.user = :user AND a.notified = false")
    List<Alert> findAllPendingAlerts(@Param("user") User user);
}