package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.NotFoundException;
import com.autoflex.inventory.mapper.RawMaterialMapper;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    @Inject
    RawMaterialMapper rawMaterialMapper;

    public List<RawMaterialDTO> findAll() {
        return rawMaterialMapper.toDTOList(rawMaterialRepository.findAllOrderByName());
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Raw material not found with id: " + id));
        return rawMaterialMapper.toDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        validateRawMaterialName(dto.getName(), null);
        
        RawMaterial rawMaterial = rawMaterialMapper.toEntity(dto);
        rawMaterialRepository.persist(rawMaterial);
        
        return rawMaterialMapper.toDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Raw material not found with id: " + id));
        
        validateRawMaterialName(dto.getName(), id);
        rawMaterialMapper.updateEntity(rawMaterial, dto);
        rawMaterialRepository.persist(rawMaterial);
        
        return rawMaterialMapper.toDTO(rawMaterial);
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Raw material not found with id: " + id));
        
        // Check if raw material is used in any product
        if (productRawMaterialRepository.existsByRawMaterialId(id)) {
            throw new BusinessException("Cannot delete raw material that is associated with products");
        }
        
        rawMaterialRepository.delete(rawMaterial);
    }

    public List<RawMaterialDTO> search(String name) {
        return rawMaterialMapper.toDTOList(rawMaterialRepository.findByNameContaining(name));
    }

    private void validateRawMaterialName(String name, Long excludeId) {
        if (excludeId == null) {
            if (rawMaterialRepository.existsByName(name)) {
                throw new BusinessException("Raw material with name '" + name + "' already exists");
            }
        } else {
            if (rawMaterialRepository.existsByNameAndIdNot(name, excludeId)) {
                throw new BusinessException("Raw material with name '" + name + "' already exists");
            }
        }
    }
}
