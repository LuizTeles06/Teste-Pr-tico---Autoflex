package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Product value is required")
    @Positive(message = "Product value must be positive")
    private BigDecimal value;

    private List<ProductRawMaterialDTO> rawMaterials;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, BigDecimal value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public ProductDTO(Long id, String name, BigDecimal value, List<ProductRawMaterialDTO> rawMaterials) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.rawMaterials = rawMaterials;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public List<ProductRawMaterialDTO> getRawMaterials() {
        return rawMaterials;
    }

    public void setRawMaterials(List<ProductRawMaterialDTO> rawMaterials) {
        this.rawMaterials = rawMaterials;
    }
}
