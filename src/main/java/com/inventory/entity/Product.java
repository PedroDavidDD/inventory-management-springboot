package com.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(columnDefinition = "integer default 1")
    private Integer quantity = 1;

    @Column(name = "is_enabled", columnDefinition = "boolean default true")
    private Boolean isEnabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private ProductStatus status;

    @NotNull
    @Column(name = "notify_days_before", nullable = false)
    private Integer notifyDaysBefore;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold;

    @Column(name = "use_low_stock_alert", columnDefinition = "boolean default true")
    private Boolean useLowStockAlert = true;

    @Column(name = "use_expiration_alert", columnDefinition = "boolean default true")
    private Boolean useExpirationAlert = true;

    @Column(name = "use_recurrent_alert", columnDefinition = "boolean default false")
    private Boolean useRecurrentAlert = false;

    @Column(name = "alert_time", columnDefinition = "varchar default '09:00'")
    private String alertTime = "09:00";

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "product_tags",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "product_alert_days",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "day_id")
    )
    private Set<DayOfWeek> alertDays = new HashSet<>();

    // Constructors
    public Product() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }

    public Set<DayOfWeek> getAlertDays() { return alertDays; }
    public void setAlertDays(Set<DayOfWeek> alertDays) { this.alertDays = alertDays; }
}