package com.autoflex.inventory.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product_raw_materials")
public class ProductRawMaterial extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @NotNull(message = "Required quantity is mandatory")
    @Positive(message = "Required quantity must be positive")
    @Column(name = "required_quantity", nullable = false, precision = 15, scale = 4)
    private BigDecimal requiredQuantity;

    public ProductRawMaterial() {
    }

    public ProductRawMaterial(Product product, RawMaterial rawMaterial, BigDecimal requiredQuantity) {
        this.product = product;
        this.rawMaterial = rawMaterial;
        this.requiredQuantity = requiredQuantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public BigDecimal getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(BigDecimal requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRawMaterial that = (ProductRawMaterial) o;
        return Objects.equals(product, that.product) && 
               Objects.equals(rawMaterial, that.rawMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, rawMaterial);
    }
}
