package dk.cristi.app.webshop.management.repositories;

import dk.cristi.app.webshop.management.models.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
