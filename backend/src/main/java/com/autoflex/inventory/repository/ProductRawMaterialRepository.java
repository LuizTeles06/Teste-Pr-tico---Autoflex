package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.ProductRawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRawMaterialRepository implements PanacheRepository<ProductRawMaterial> {

    public List<ProductRawMaterial> findByProductId(Long productId) {
        return list("product.id", productId);
    }

    public List<ProductRawMaterial> findByRawMaterialId(Long rawMaterialId) {
        return list("rawMaterial.id", rawMaterialId);
    }

    public Optional<ProductRawMaterial> findByProductIdAndRawMaterialId(Long productId, Long rawMaterialId) {
        return find("product.id = ?1 AND rawMaterial.id = ?2", productId, rawMaterialId).firstResultOptional();
    }

    public void deleteByProductId(Long productId) {
        delete("product.id", productId);
    }

    public void deleteByRawMaterialId(Long rawMaterialId) {
        delete("rawMaterial.id", rawMaterialId);
    }

    public boolean existsByRawMaterialId(Long rawMaterialId) {
        return count("rawMaterial.id", rawMaterialId) > 0;
    }
}
