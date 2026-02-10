package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductionItemDTO;
import com.autoflex.inventory.dto.ProductionSuggestionDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProductionSuggestionServiceTest {

    @Inject
    ProductionSuggestionService productionSuggestionService;

    @InjectMock
    ProductRepository productRepository;

    private RawMaterial steel;
    private RawMaterial aluminum;
    private Product productA;
    private Product productB;

    @BeforeEach
    void setUp() {
        // Setup raw materials
        steel = new RawMaterial("Steel", new BigDecimal("100"));
        steel.setId(1L);
        
        aluminum = new RawMaterial("Aluminum", new BigDecimal("50"));
        aluminum.setId(2L);

        // Setup products
        productA = new Product("Product A", new BigDecimal("100.00"));
        productA.setId(1L);
        productA.setRawMaterials(new ArrayList<>());
        
        productB = new Product("Product B", new BigDecimal("150.00"));
        productB.setId(2L);
        productB.setRawMaterials(new ArrayList<>());
    }

    @Test
    void testCalculateProductionSuggestion_NoProducts() {
        when(productRepository.findAllOrderByValueDesc()).thenReturn(Collections.emptyList());

        ProductionSuggestionDTO result = productionSuggestionService.calculateProductionSuggestion();

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getTotalValue());
    }

    @Test
    void testCalculateProductionSuggestion_ProductWithoutRawMaterials() {
        when(productRepository.findAllOrderByValueDesc()).thenReturn(Arrays.asList(productA));

        ProductionSuggestionDTO result = productionSuggestionService.calculateProductionSuggestion();

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getTotalValue());
    }

    @Test
    void testCalculateProductionSuggestion_SingleProduct() {
        // Product A requires 10 steel to make 1 unit
        // Stock has 100 steel, so we can make 10 units of Product A
        ProductRawMaterial prm = new ProductRawMaterial(productA, steel, new BigDecimal("10"));
        productA.getRawMaterials().add(prm);

        when(productRepository.findAllOrderByValueDesc()).thenReturn(Arrays.asList(productA));

        ProductionSuggestionDTO result = productionSuggestionService.calculateProductionSuggestion();

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        
        ProductionItemDTO item = result.getItems().get(0);
        assertEquals(1L, item.getProductId());
        assertEquals("Product A", item.getProductName());
        assertEquals(10, item.getQuantity());
        assertEquals(new BigDecimal("1000.00"), result.getTotalValue());
    }

    @Test
    void testCalculateProductionSuggestion_PrioritizeHigherValue() {
        // Product A (value=100) requires 10 steel
        // Product B (value=150) requires 20 steel
        // Stock has 100 steel
        // Should prioritize Product B first (higher value)
        
        ProductRawMaterial prmA = new ProductRawMaterial(productA, steel, new BigDecimal("10"));
        productA.getRawMaterials().add(prmA);
        
        ProductRawMaterial prmB = new ProductRawMaterial(productB, steel, new BigDecimal("20"));
        productB.getRawMaterials().add(prmB);

        // Products sorted by value descending
        when(productRepository.findAllOrderByValueDesc()).thenReturn(Arrays.asList(productB, productA));

        ProductionSuggestionDTO result = productionSuggestionService.calculateProductionSuggestion();

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        
        // First item should be Product B (5 units, using 100 steel)
        ProductionItemDTO itemB = result.getItems().get(0);
        assertEquals("Product B", itemB.getProductName());
        assertEquals(5, itemB.getQuantity());
        
        // No steel left for Product A after making 5 Product B
        // But let's verify the items
    }

    @Test
    void testCalculateProductionSuggestion_MultipleRawMaterials() {
        // Product A requires:
        // - 10 steel (have 100)
        // - 25 aluminum (have 50)
        // Limiting factor is aluminum: 50/25 = 2 units
        
        ProductRawMaterial prmSteel = new ProductRawMaterial(productA, steel, new BigDecimal("10"));
        ProductRawMaterial prmAluminum = new ProductRawMaterial(productA, aluminum, new BigDecimal("25"));
        productA.getRawMaterials().add(prmSteel);
        productA.getRawMaterials().add(prmAluminum);

        when(productRepository.findAllOrderByValueDesc()).thenReturn(Arrays.asList(productA));

        ProductionSuggestionDTO result = productionSuggestionService.calculateProductionSuggestion();

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        
        ProductionItemDTO item = result.getItems().get(0);
        assertEquals(2, item.getQuantity()); // Limited by aluminum
        assertEquals(new BigDecimal("200.00"), result.getTotalValue());
    }

    @Test
    void testCalculateProductionSuggestion_InsufficientStock() {
        // Product A requires 200 steel but we only have 100
        ProductRawMaterial prm = new ProductRawMaterial(productA, steel, new BigDecimal("200"));
        productA.getRawMaterials().add(prm);

        when(productRepository.findAllOrderByValueDesc()).thenReturn(Arrays.asList(productA));

        ProductionSuggestionDTO result = productionSuggestionService.calculateProductionSuggestion();

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getTotalValue());
    }
}
