package com.autoflex.inventory.dto;

import java.math.BigDecimal;

public class ProductionItemDTO {

    private Long productId;
    private String productName;
    private BigDecimal productValue;
    private int quantity;
    private BigDecimal subtotal;

    public ProductionItemDTO() {
    }

    public ProductionItemDTO(Long productId, String productName, BigDecimal productValue, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productValue = productValue;
        this.quantity = quantity;
        this.subtotal = productValue.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductValue() {
        return productValue;
    }

    public void setProductValue(BigDecimal productValue) {
        this.productValue = productValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
