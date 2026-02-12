package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public List<Product> findAllWithRawMaterials() {
        return getEntityManager()
                .createQuery("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.rawMaterials", Product.class)
                .getResultList();
    }

    public Optional<Product> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<Product> findAllOrderByValueDesc() {
        return list("ORDER BY value DESC");
    }

    public List<Product> findByNameContaining(String name) {
        return list("LOWER(name) LIKE LOWER(?1)", "%" + name + "%");
    }

    public boolean existsByName(String name) {
        return count("LOWER(name) = LOWER(?1)", name) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("LOWER(name) = LOWER(?1) AND id != ?2", name, id) > 0;
    }
}
