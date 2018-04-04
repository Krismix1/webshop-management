package dk.cristi.app.webshop.management.services;

import dk.cristi.app.webshop.management.models.entities.Product;
import dk.cristi.app.webshop.management.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> fetchAll() {
        return (List<Product>) productRepository.findAll();
    }
}
