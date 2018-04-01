package dk.cristi.app.webshop.management.repositories;

import dk.cristi.app.webshop.management.models.entities.ProductType;
import org.springframework.data.repository.CrudRepository;

public interface ProductTypeRepository extends CrudRepository<ProductType, Long> {
    ProductType findByName(String name);
}
