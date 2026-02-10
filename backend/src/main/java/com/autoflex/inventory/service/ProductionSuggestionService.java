package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductionItemDTO;
import com.autoflex.inventory.dto.ProductionSuggestionDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@ApplicationScoped
public class ProductionSuggestionService {

    @Inject
    ProductRepository productRepository;

    /**
     * Calculates which products can be produced with available raw materials.
     * Prioritizes products with higher value.
     * 
     * Algorithm:
     * 1. Get all products ordered by value (descending)
     * 2. For each product, calculate how many units can be produced
     * 3. Reserve raw materials and add to suggestion
     * 4. Continue until no more products can be made
     * 
     * @return ProductionSuggestionDTO with items and total value
     */
    public ProductionSuggestionDTO calculateProductionSuggestion() {
        // Get products ordered by value (highest first for priority)
        List<Product> products = productRepository.findAllOrderByValueDesc();
        
        // Track available stock (copy to avoid modifying actual entities)
        Map<Long, BigDecimal> availableStock = new HashMap<>();
        
        // Initialize available stock from raw materials
        for (Product product : products) {
            for (ProductRawMaterial prm : product.getRawMaterials()) {
                Long rawMaterialId = prm.getRawMaterial().getId();
                if (!availableStock.containsKey(rawMaterialId)) {
                    availableStock.put(rawMaterialId, prm.getRawMaterial().getStockQuantity());
                }
            }
        }
        
        List<ProductionItemDTO> productionItems = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;
        
        // Keep trying to produce until no more products can be made
        boolean producedSomething;
        do {
            producedSomething = false;
            
            for (Product product : products) {
                if (product.getRawMaterials().isEmpty()) {
                    continue; // Skip products without raw materials
                }
                
                // Calculate how many units can be produced
                int maxUnits = calculateMaxProducibleUnits(product, availableStock);
                
                if (maxUnits > 0) {
                    // Reserve raw materials
                    reserveRawMaterials(product, maxUnits, availableStock);
                    
                    // Add to production items or update existing
                    updateProductionItems(productionItems, product, maxUnits);
                    
                    // Update total value
                    totalValue = totalValue.add(product.getValue().multiply(BigDecimal.valueOf(maxUnits)));
                    
                    producedSomething = true;
                }
            }
        } while (producedSomething);
        
        return new ProductionSuggestionDTO(productionItems, totalValue);
    }

    /**
     * Calculate maximum units that can be produced for a product
     * based on available raw materials.
     */
    private int calculateMaxProducibleUnits(Product product, Map<Long, BigDecimal> availableStock) {
        int maxUnits = Integer.MAX_VALUE;
        
        for (ProductRawMaterial prm : product.getRawMaterials()) {
            Long rawMaterialId = prm.getRawMaterial().getId();
            BigDecimal available = availableStock.getOrDefault(rawMaterialId, BigDecimal.ZERO);
            BigDecimal required = prm.getRequiredQuantity();
            
            if (required.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            
            // Calculate how many units can be made with this raw material
            int possibleUnits = available.divide(required, 0, RoundingMode.DOWN).intValue();
            maxUnits = Math.min(maxUnits, possibleUnits);
        }
        
        return maxUnits == Integer.MAX_VALUE ? 0 : maxUnits;
    }

    /**
     * Reserve raw materials by deducting from available stock.
     */
    private void reserveRawMaterials(Product product, int units, Map<Long, BigDecimal> availableStock) {
        for (ProductRawMaterial prm : product.getRawMaterials()) {
            Long rawMaterialId = prm.getRawMaterial().getId();
            BigDecimal required = prm.getRequiredQuantity().multiply(BigDecimal.valueOf(units));
            BigDecimal current = availableStock.getOrDefault(rawMaterialId, BigDecimal.ZERO);
            availableStock.put(rawMaterialId, current.subtract(required));
        }
    }

    /**
     * Update production items list with new production.
     */
    private void updateProductionItems(List<ProductionItemDTO> items, Product product, int additionalUnits) {
        // Check if product already exists in list
        Optional<ProductionItemDTO> existing = items.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();
        
        if (existing.isPresent()) {
            ProductionItemDTO item = existing.get();
            int newQuantity = item.getQuantity() + additionalUnits;
            item.setQuantity(newQuantity);
            item.setSubtotal(product.getValue().multiply(BigDecimal.valueOf(newQuantity)));
        } else {
            items.add(new ProductionItemDTO(
                    product.getId(),
                    product.getName(),
                    product.getValue(),
                    additionalUnits
            ));
        }
    }
}
