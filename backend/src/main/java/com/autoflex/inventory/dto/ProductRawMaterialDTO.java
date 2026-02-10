package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProductRawMaterialDTO {

    private Long id;

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    private String rawMaterialName;

    @NotNull(message = "Required quantity is mandatory")
    @Positive(message = "Required quantity must be positive")
    private BigDecimal requiredQuantity;

    public ProductRawMaterialDTO() {
    }

    public ProductRawMaterialDTO(Long id, Long rawMaterialId, String rawMaterialName, BigDecimal requiredQuantity) {
        this.id = id;
        this.rawMaterialId = rawMaterialId;
        this.rawMaterialName = rawMaterialName;
        this.requiredQuantity = requiredQuantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRawMaterialId() {
        return rawMaterialId;
    }

    public void setRawMaterialId(Long rawMaterialId) {
        this.rawMaterialId = rawMaterialId;
    }

    public String getRawMaterialName() {
        return rawMaterialName;
    }

    public void setRawMaterialName(String rawMaterialName) {
        this.rawMaterialName = rawMaterialName;
    }

    public BigDecimal getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(BigDecimal requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
}
