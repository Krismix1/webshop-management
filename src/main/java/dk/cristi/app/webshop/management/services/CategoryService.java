package dk.cristi.app.webshop.management.services;

import dk.cristi.app.webshop.management.models.entities.Category;
import dk.cristi.app.webshop.management.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Collection<Category> fetchAll() {
        return (Collection<Category>) categoryRepository.findAll();
    }

    public Optional<Category> fetchOne(long id) {
        return categoryRepository.findById(id);
    }
}
