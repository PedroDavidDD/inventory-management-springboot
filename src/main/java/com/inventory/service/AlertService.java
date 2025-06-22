package com.inventory.service;

import com.inventory.entity.Alert;
import com.inventory.entity.Product;
import com.inventory.entity.User;
import com.inventory.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public void createExpirationAlert(Product product) {
        LocalDate alertDate = product.getExpirationDate().minusDays(product.getNotifyDaysBefore());
        Alert alert = new Alert(product, alertDate);
        alertRepository.save(alert);
    }

    public List<Alert> getPendingAlerts(User user) {
        return alertRepository.findPendingAlerts(user, LocalDate.now());
    }

    public List<Alert> getAllPendingAlerts(User user) {
        return alertRepository.findAllPendingAlerts(user);
    }

    public void markAlertAsNotified(Alert alert) {
        alert.setNotified(true);
        alertRepository.save(alert);
    }
}