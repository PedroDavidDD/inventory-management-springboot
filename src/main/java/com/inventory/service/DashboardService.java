package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.entity.User;
import com.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> getDashboardData(User user) {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Total de productos registrados
        List<Product> allProducts = productRepository.findByUserAndIsEnabledTrue(user);
        dashboardData.put("totalProducts", allProducts.size());
        
        // Productos con stock crítico
        List<Product> lowStockProducts = productRepository.findLowStockProducts(user);
        dashboardData.put("lowStockProducts", lowStockProducts.size());
        
        // Productos próximos a vencer (próximos 7 días)
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        List<Product> expiringProducts = productRepository.findExpiringProducts(user, today, nextWeek);
        dashboardData.put("expiringProducts", expiringProducts.size());
        
        // Productos vencidos
        List<Product> expiredProducts = productRepository.findExpiredProducts(user, today);
        dashboardData.put("expiredProducts", expiredProducts.size());
        
        // Porcentaje de merma
        double wastePercentage = allProducts.size() > 0 ? 
            (double) expiredProducts.size() / allProducts.size() * 100 : 0;
        dashboardData.put("wastePercentage", Math.round(wastePercentage * 100.0) / 100.0);
        
        return dashboardData;
    }
}