package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.NotFoundException;
import com.autoflex.inventory.mapper.ProductMapper;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProductServiceTest {

    @Inject
    ProductService productService;

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    RawMaterialRepository rawMaterialRepository;

    @InjectMock
    ProductRawMaterialRepository productRawMaterialRepository;

    @Inject
    ProductMapper productMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Test Product", new BigDecimal("100.00"));
        testProduct.setId(1L);
    }

    @Test
    void testFindAll() {
        List<Product> products = Arrays.asList(
                testProduct,
                new Product("Product 2", new BigDecimal("200.00"))
        );
        when(productRepository.listAll()).thenReturn(products);

        List<ProductDTO> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).listAll();
    }

    @Test
    void testFindById_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));

        ProductDTO result = productService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("100.00"), result.getValue());
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.findById(99L));
    }

    @Test
    void testCreate_Success() {
        ProductDTO dto = new ProductDTO(null, "New Product", new BigDecimal("150.00"));
        when(productRepository.existsByName("New Product")).thenReturn(false);
        doNothing().when(productRepository).persist(any(Product.class));

        ProductDTO result = productService.create(dto);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).persist(any(Product.class));
    }

    @Test
    void testCreate_DuplicateName() {
        ProductDTO dto = new ProductDTO(null, "Existing Product", new BigDecimal("150.00"));
        when(productRepository.existsByName("Existing Product")).thenReturn(true);

        assertThrows(BusinessException.class, () -> productService.create(dto));
    }

    @Test
    void testUpdate_Success() {
        ProductDTO dto = new ProductDTO(null, "Updated Product", new BigDecimal("200.00"));
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.existsByNameAndIdNot("Updated Product", 1L)).thenReturn(false);
        doNothing().when(productRawMaterialRepository).deleteByProductId(1L);
        doNothing().when(productRepository).persist(any(Product.class));

        ProductDTO result = productService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
    }

    @Test
    void testUpdate_NotFound() {
        ProductDTO dto = new ProductDTO(null, "Updated Product", new BigDecimal("200.00"));
        when(productRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(99L, dto));
    }

    @Test
    void testDelete_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRawMaterialRepository).deleteByProductId(1L);
        doNothing().when(productRepository).delete(testProduct);

        assertDoesNotThrow(() -> productService.delete(1L));
        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    void testDelete_NotFound() {
        when(productRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.delete(99L));
    }
}
