package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.RawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RawMaterialRepository implements PanacheRepository<RawMaterial> {

    public Optional<RawMaterial> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<RawMaterial> findByNameContaining(String name) {
        return list("LOWER(name) LIKE LOWER(?1)", "%" + name + "%");
    }

    public boolean existsByName(String name) {
        return count("LOWER(name) = LOWER(?1)", name) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("LOWER(name) = LOWER(?1) AND id != ?2", name, id) > 0;
    }

    public List<RawMaterial> findAllOrderByName() {
        return list("ORDER BY name ASC");
    }
}
