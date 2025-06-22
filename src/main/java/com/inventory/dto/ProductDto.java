package com.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class ProductDto {
    private UUID id;

    @NotBlank(message = "Nombre del producto es requerido")
    private String name;

    @NotNull(message = "Fecha de ingreso es requerida")
    private LocalDate entryDate;

    @NotNull(message = "Fecha de vencimiento es requerida")
    private LocalDate expirationDate;

    private Integer quantity = 1;
    private Boolean isEnabled = true;
    private String statusName;

    @NotNull(message = "Días de notificación antes del vencimiento es requerido")
    private Integer notifyDaysBefore;

    private Integer lowStockThreshold;
    private Boolean useLowStockAlert = true;
    private Boolean useExpirationAlert = true;
    private Boolean useRecurrentAlert = false;
    private String alertTime = "09:00";
    private Set<String> tagNames;

    // Constructors
    public ProductDto() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }

    public Integer getNotifyDaysBefore() { return notifyDaysBefore; }
    public void setNotifyDaysBefore(Integer notifyDaysBefore) { this.notifyDaysBefore = notifyDaysBefore; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public Boolean getUseLowStockAlert() { return useLowStockAlert; }
    public void setUseLowStockAlert(Boolean useLowStockAlert) { this.useLowStockAlert = useLowStockAlert; }

    public Boolean getUseExpirationAlert() { return useExpirationAlert; }
    public void setUseExpirationAlert(Boolean useExpirationAlert) { this.useExpirationAlert = useExpirationAlert; }

    public Boolean getUseRecurrentAlert() { return useRecurrentAlert; }
    public void setUseRecurrentAlert(Boolean useRecurrentAlert) { this.useRecurrentAlert = useRecurrentAlert; }

    public String getAlertTime() { return alertTime; }
    public void setAlertTime(String alertTime) { this.alertTime = alertTime; }

    public Set<String> getTagNames() { return tagNames; }
    public void setTagNames(Set<String> tagNames) { this.tagNames = tagNames; }
}