package com.autoflex.inventory.mapper;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.entity.RawMaterial;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialMapper {

    public RawMaterialDTO toDTO(RawMaterial rawMaterial) {
        if (rawMaterial == null) {
            return null;
        }
        return new RawMaterialDTO(
                rawMaterial.getId(),
                rawMaterial.getName(),
                rawMaterial.getStockQuantity()
        );
    }

    public RawMaterial toEntity(RawMaterialDTO dto) {
        if (dto == null) {
            return null;
        }
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setId(dto.getId());
        rawMaterial.setName(dto.getName());
        rawMaterial.setStockQuantity(dto.getStockQuantity());
        return rawMaterial;
    }

    public void updateEntity(RawMaterial rawMaterial, RawMaterialDTO dto) {
        rawMaterial.setName(dto.getName());
        rawMaterial.setStockQuantity(dto.getStockQuantity());
    }

    public List<RawMaterialDTO> toDTOList(List<RawMaterial> rawMaterials) {
        return rawMaterials.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
