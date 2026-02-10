package com.autoflex.inventory.mapper;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        List<ProductRawMaterialDTO> rawMaterialDTOs = null;
        if (product.getRawMaterials() != null) {
            rawMaterialDTOs = product.getRawMaterials().stream()
                    .map(this::toProductRawMaterialDTO)
                    .collect(Collectors.toList());
        }
        
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getValue(),
                rawMaterialDTOs
        );
    }

    public ProductDTO toDTOWithoutRawMaterials(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDTO(product.getId(), product.getName(), product.getValue());
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setValue(dto.getValue());
        return product;
    }

    public void updateEntity(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setValue(dto.getValue());
    }

    public ProductRawMaterialDTO toProductRawMaterialDTO(ProductRawMaterial prm) {
        if (prm == null) {
            return null;
        }
        return new ProductRawMaterialDTO(
                prm.getId(),
                prm.getRawMaterial().getId(),
                prm.getRawMaterial().getName(),
                prm.getRequiredQuantity()
        );
    }

    public List<ProductDTO> toDTOList(List<Product> products) {
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> toDTOListWithoutRawMaterials(List<Product> products) {
        return products.stream()
                .map(this::toDTOWithoutRawMaterials)
                .collect(Collectors.toList());
    }
}
