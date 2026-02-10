package com.autoflex.inventory.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false, length = 255)
    private String name;

    @NotNull(message = "Product value is required")
    @Positive(message = "Product value must be positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductRawMaterial> rawMaterials = new ArrayList<>();

    public Product() {
    }

    public Product(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
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

    public List<ProductRawMaterial> getRawMaterials() {
        return rawMaterials;
    }

    public void setRawMaterials(List<ProductRawMaterial> rawMaterials) {
        this.rawMaterials = rawMaterials;
    }

    public void addRawMaterial(RawMaterial rawMaterial, BigDecimal quantity) {
        ProductRawMaterial prm = new ProductRawMaterial(this, rawMaterial, quantity);
        rawMaterials.add(prm);
    }

    public void removeRawMaterial(RawMaterial rawMaterial) {
        rawMaterials.removeIf(prm -> prm.getRawMaterial().equals(rawMaterial));
    }
}
