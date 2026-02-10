package com.autoflex.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductionSuggestionDTO {

    private List<ProductionItemDTO> items;
    private BigDecimal totalValue;

    public ProductionSuggestionDTO() {
    }

    public ProductionSuggestionDTO(List<ProductionItemDTO> items, BigDecimal totalValue) {
        this.items = items;
        this.totalValue = totalValue;
    }

    // Getters and Setters
    public List<ProductionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ProductionItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
