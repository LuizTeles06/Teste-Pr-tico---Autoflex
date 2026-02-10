package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.NotFoundException;
import com.autoflex.inventory.mapper.RawMaterialMapper;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class RawMaterialServiceTest {

    @Inject
    RawMaterialService rawMaterialService;

    @InjectMock
    RawMaterialRepository rawMaterialRepository;

    @InjectMock
    ProductRawMaterialRepository productRawMaterialRepository;

    @Inject
    RawMaterialMapper rawMaterialMapper;

    private RawMaterial testRawMaterial;

    @BeforeEach
    void setUp() {
        testRawMaterial = new RawMaterial("Steel", new BigDecimal("100.0000"));
        testRawMaterial.setId(1L);
    }

    @Test
    void testFindAll() {
        List<RawMaterial> rawMaterials = Arrays.asList(
                testRawMaterial,
                new RawMaterial("Aluminum", new BigDecimal("50.0000"))
        );
        when(rawMaterialRepository.findAllOrderByName()).thenReturn(rawMaterials);

        List<RawMaterialDTO> result = rawMaterialService.findAll();

        assertEquals(2, result.size());
        verify(rawMaterialRepository, times(1)).findAllOrderByName();
    }

    @Test
    void testFindById_Success() {
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));

        RawMaterialDTO result = rawMaterialService.findById(1L);

        assertNotNull(result);
        assertEquals("Steel", result.getName());
        assertEquals(new BigDecimal("100.0000"), result.getStockQuantity());
    }

    @Test
    void testFindById_NotFound() {
        when(rawMaterialRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rawMaterialService.findById(99L));
    }

    @Test
    void testCreate_Success() {
        RawMaterialDTO dto = new RawMaterialDTO(null, "Plastic", new BigDecimal("200.0000"));
        when(rawMaterialRepository.existsByName("Plastic")).thenReturn(false);
        doNothing().when(rawMaterialRepository).persist(any(RawMaterial.class));

        RawMaterialDTO result = rawMaterialService.create(dto);

        assertNotNull(result);
        assertEquals("Plastic", result.getName());
        verify(rawMaterialRepository, times(1)).persist(any(RawMaterial.class));
    }

    @Test
    void testCreate_DuplicateName() {
        RawMaterialDTO dto = new RawMaterialDTO(null, "Steel", new BigDecimal("200.0000"));
        when(rawMaterialRepository.existsByName("Steel")).thenReturn(true);

        assertThrows(BusinessException.class, () -> rawMaterialService.create(dto));
    }

    @Test
    void testUpdate_Success() {
        RawMaterialDTO dto = new RawMaterialDTO(null, "Updated Steel", new BigDecimal("150.0000"));
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));
        when(rawMaterialRepository.existsByNameAndIdNot("Updated Steel", 1L)).thenReturn(false);
        doNothing().when(rawMaterialRepository).persist(any(RawMaterial.class));

        RawMaterialDTO result = rawMaterialService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Steel", result.getName());
    }

    @Test
    void testUpdate_NotFound() {
        RawMaterialDTO dto = new RawMaterialDTO(null, "Updated", new BigDecimal("150.0000"));
        when(rawMaterialRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rawMaterialService.update(99L, dto));
    }

    @Test
    void testDelete_Success() {
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));
        when(productRawMaterialRepository.existsByRawMaterialId(1L)).thenReturn(false);
        doNothing().when(rawMaterialRepository).delete(testRawMaterial);

        assertDoesNotThrow(() -> rawMaterialService.delete(1L));
        verify(rawMaterialRepository, times(1)).delete(testRawMaterial);
    }

    @Test
    void testDelete_NotFound() {
        when(rawMaterialRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rawMaterialService.delete(99L));
    }

    @Test
    void testDelete_AssociatedWithProducts() {
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));
        when(productRawMaterialRepository.existsByRawMaterialId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> rawMaterialService.delete(1L));
    }
}
