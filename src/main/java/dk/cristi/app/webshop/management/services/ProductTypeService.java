package dk.cristi.app.webshop.management.services;

import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.repositories.ProductTypeRepository;
import dk.cristi.app.webshop.management.repositories.ProductTypeSpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeSpecificationRepository productTypeSpecificationRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeSpecificationRepository productTypeSpecificationRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeSpecificationRepository = productTypeSpecificationRepository;
    }

    public Collection<ProductType> fetchAll() {
        return (Collection<ProductType>) productTypeRepository.findAll();
    }

    @Transactional
    public ProductType save(@NonNull ProductType productType) {
        // Save the product type
        productType = productTypeRepository.save(productType);
        // Save the specification for the product
        productTypeSpecificationRepository.saveAll(productType.getSpecifications());
        return productType;
    }

    public Optional<ProductType> findByName(@NonNull String name) {
        return Optional.ofNullable(productTypeRepository.findByName(name));
    }

    public Optional<ProductType> findById(long id) {
        return productTypeRepository.findById(id);
    }
}
