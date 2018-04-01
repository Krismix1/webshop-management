package dk.cristi.app.webshop.management.repositories;

import dk.cristi.app.webshop.management.models.entities.Category;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Long> {
}
