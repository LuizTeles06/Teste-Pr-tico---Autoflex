package com.autoflex.inventory.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raw_materials")
public class RawMaterial extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Raw material name is required")
    @Column(nullable = false, length = 255)
    private String name;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false, precision = 15, scale = 4)
    private BigDecimal stockQuantity;

    @OneToMany(mappedBy = "rawMaterial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductRawMaterial> products = new ArrayList<>();

    public RawMaterial() {
    }

    public RawMaterial(String name, BigDecimal stockQuantity) {
        this.name = name;
        this.stockQuantity = stockQuantity;
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

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<ProductRawMaterial> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRawMaterial> products) {
        this.products = products;
    }
}
