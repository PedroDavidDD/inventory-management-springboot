package com.inventory.service;

import com.inventory.dto.ProductDto;
import com.inventory.entity.*;
import com.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Autowired
    private AlertService alertService;

    public Product createProduct(ProductDto productDto, User user) {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setUser(user);
        product.setName(productDto.getName());
        product.setEntryDate(productDto.getEntryDate());
        product.setExpirationDate(productDto.getExpirationDate());
        product.setQuantity(productDto.getQuantity());
        product.setIsEnabled(productDto.getIsEnabled());
        product.setNotifyDaysBefore(productDto.getNotifyDaysBefore());
        product.setLowStockThreshold(productDto.getLowStockThreshold());
        product.setUseLowStockAlert(productDto.getUseLowStockAlert());
        product.setUseExpirationAlert(productDto.getUseExpirationAlert());
        product.setUseRecurrentAlert(productDto.getUseRecurrentAlert());
        product.setAlertTime(productDto.getAlertTime());

        // Determinar estado del producto
        updateProductStatus(product);

        // Procesar tags
        if (productDto.getTagNames() != null && !productDto.getTagNames().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : productDto.getTagNames()) {
                Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                tags.add(tag);
            }
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);

        // Crear alerta de vencimiento si está habilitada
        if (product.getUseExpirationAlert()) {
            alertService.createExpirationAlert(savedProduct);
        }

        return savedProduct;
    }

    public Product updateProduct(UUID productId, ProductDto productDto, User user) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!product.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permisos para editar este producto");
        }

        product.setName(productDto.getName());
        product.setEntryDate(productDto.getEntryDate());
        product.setExpirationDate(productDto.getExpirationDate());
        product.setQuantity(productDto.getQuantity());
        product.setIsEnabled(productDto.getIsEnabled());
        product.setNotifyDaysBefore(productDto.getNotifyDaysBefore());
        product.setLowStockThreshold(productDto.getLowStockThreshold());
        product.setUseLowStockAlert(productDto.getUseLowStockAlert());
        product.setUseExpirationAlert(productDto.getUseExpirationAlert());
        product.setUseRecurrentAlert(productDto.getUseRecurrentAlert());
        product.setAlertTime(productDto.getAlertTime());

        // Actualizar estado del producto
        updateProductStatus(product);

        // Actualizar tags
        product.getTags().clear();
        if (productDto.getTagNames() != null && !productDto.getTagNames().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : productDto.getTagNames()) {
                Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                tags.add(tag);
            }
            product.setTags(tags);
        }

        return productRepository.save(product);
    }

    private void updateProductStatus(Product product) {
        LocalDate today = LocalDate.now();
        String statusName;

        if (product.getExpirationDate().isBefore(today)) {
            statusName = "vencido";
        } else if (product.getExpirationDate().minusDays(product.getNotifyDaysBefore()).isBefore(today) ||
                  product.getExpirationDate().minusDays(product.getNotifyDaysBefore()).isEqual(today)) {
            statusName = "próximo a vencer";
        } else {
            statusName = "activo";
        }

        ProductStatus status = productStatusRepository.findByName(statusName)
            .orElseThrow(() -> new RuntimeException("Estado de producto no encontrado: " + statusName));
        product.setStatus(status);
    }

    public List<Product> getProductsByUser(User user) {
        return productRepository.findByUserAndIsEnabledTrue(user);
    }

    public List<Product> searchProducts(User user, String name, String tagName, String statusName) {
        if (name != null && !name.trim().isEmpty()) {
            return productRepository.findByUserAndNameContaining(user, name);
        } else if (tagName != null && !tagName.trim().isEmpty()) {
            return productRepository.findByUserAndTagName(user, tagName);
        } else if (statusName != null && !statusName.trim().isEmpty()) {
            return productRepository.findByUserAndStatusName(user, statusName);
        } else {
            return productRepository.findByUserAndIsEnabledTrue(user);
        }
    }

    public void deleteProduct(UUID productId, User user) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!product.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar este producto");
        }

        product.setIsEnabled(false);
        productRepository.save(product);
    }

    public ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setEntryDate(product.getEntryDate());
        dto.setExpirationDate(product.getExpirationDate());
        dto.setQuantity(product.getQuantity());
        dto.setIsEnabled(product.getIsEnabled());
        dto.setStatusName(product.getStatus() != null ? product.getStatus().getName() : null);
        dto.setNotifyDaysBefore(product.getNotifyDaysBefore());
        dto.setLowStockThreshold(product.getLowStockThreshold());
        dto.setUseLowStockAlert(product.getUseLowStockAlert());
        dto.setUseExpirationAlert(product.getUseExpirationAlert());
        dto.setUseRecurrentAlert(product.getUseRecurrentAlert());
        dto.setAlertTime(product.getAlertTime());
        
        if (product.getTags() != null) {
            dto.setTagNames(product.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}