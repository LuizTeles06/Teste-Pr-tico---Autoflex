package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.NotFoundException;
import com.autoflex.inventory.mapper.ProductMapper;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    @Inject
    ProductMapper productMapper;

    public List<ProductDTO> findAll() {
        return productMapper.toDTOList(productRepository.listAll());
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        validateProductName(dto.getName(), null);
        
        Product product = productMapper.toEntity(dto);
        productRepository.persist(product);
        
        if (dto.getRawMaterials() != null && !dto.getRawMaterials().isEmpty()) {
            for (ProductRawMaterialDTO rawMaterialDTO : dto.getRawMaterials()) {
                addRawMaterialToProduct(product, rawMaterialDTO);
            }
        }
        
        return productMapper.toDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        
        validateProductName(dto.getName(), id);
        productMapper.updateEntity(product, dto);
        
        // Update raw materials
        if (dto.getRawMaterials() != null) {
            // Remove existing associations
            product.getRawMaterials().clear();
            productRawMaterialRepository.deleteByProductId(id);
            
            // Add new associations
            for (ProductRawMaterialDTO rawMaterialDTO : dto.getRawMaterials()) {
                addRawMaterialToProduct(product, rawMaterialDTO);
            }
        }
        
        productRepository.persist(product);
        return productMapper.toDTO(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        
        productRawMaterialRepository.deleteByProductId(id);
        productRepository.delete(product);
    }

    @Transactional
    public ProductDTO addRawMaterial(Long productId, ProductRawMaterialDTO dto) {
        Product product = productRepository.findByIdOptional(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
        
        // Check if association already exists
        if (productRawMaterialRepository.findByProductIdAndRawMaterialId(productId, dto.getRawMaterialId()).isPresent()) {
            throw new BusinessException("Raw material is already associated with this product");
        }
        
        addRawMaterialToProduct(product, dto);
        productRepository.persist(product);
        
        return productMapper.toDTO(product);
    }

    @Transactional
    public ProductDTO updateRawMaterial(Long productId, Long rawMaterialId, ProductRawMaterialDTO dto) {
        Product product = productRepository.findByIdOptional(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
        
        ProductRawMaterial prm = productRawMaterialRepository
                .findByProductIdAndRawMaterialId(productId, rawMaterialId)
                .orElseThrow(() -> new NotFoundException("Raw material association not found"));
        
        prm.setRequiredQuantity(dto.getRequiredQuantity());
        productRawMaterialRepository.persist(prm);
        
        return productMapper.toDTO(product);
    }

    @Transactional
    public ProductDTO removeRawMaterial(Long productId, Long rawMaterialId) {
        Product product = productRepository.findByIdOptional(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
        
        ProductRawMaterial prm = productRawMaterialRepository
                .findByProductIdAndRawMaterialId(productId, rawMaterialId)
                .orElseThrow(() -> new NotFoundException("Raw material association not found"));
        
        product.getRawMaterials().remove(prm);
        productRawMaterialRepository.delete(prm);
        
        return productMapper.toDTO(product);
    }

    private void addRawMaterialToProduct(Product product, ProductRawMaterialDTO dto) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(dto.getRawMaterialId())
                .orElseThrow(() -> new NotFoundException("Raw material not found with id: " + dto.getRawMaterialId()));
        
        ProductRawMaterial prm = new ProductRawMaterial(product, rawMaterial, dto.getRequiredQuantity());
        product.getRawMaterials().add(prm);
        productRawMaterialRepository.persist(prm);
    }

    private void validateProductName(String name, Long excludeId) {
        if (excludeId == null) {
            if (productRepository.existsByName(name)) {
                throw new BusinessException("Product with name '" + name + "' already exists");
            }
        } else {
            if (productRepository.existsByNameAndIdNot(name, excludeId)) {
                throw new BusinessException("Product with name '" + name + "' already exists");
            }
        }
    }
}
