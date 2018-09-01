package dk.cristi.app.webshop.management.services;

import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.models.entities.ProductTypeSpecification;
import dk.cristi.app.webshop.management.models.entities.ProductTypeSpecificationKey;
import dk.cristi.app.webshop.management.repositories.ProductTypeRepository;
import dk.cristi.app.webshop.management.repositories.ProductTypeSpecificationKeyRepository;
import dk.cristi.app.webshop.management.repositories.ProductTypeSpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeSpecificationRepository productTypeSpecificationRepository;
    private final ProductTypeSpecificationKeyRepository productTypeSpecificationKeyRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeSpecificationRepository productTypeSpecificationRepository, ProductTypeSpecificationKeyRepository productTypeSpecificationKeyRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeSpecificationRepository = productTypeSpecificationRepository;
        this.productTypeSpecificationKeyRepository = productTypeSpecificationKeyRepository;
    }

    public Collection<ProductType> fetchAll() {
        return (Collection<ProductType>) productTypeRepository.findAll();
    }

    @Transactional
    public ProductType save(@NonNull ProductType productType) {
        // Save the keys to the database
        final List<ProductTypeSpecificationKey> keys = productType.getSpecifications()
                .stream()
                .map(ProductTypeSpecification::getProductTypeSpecificationKey)
                .collect(Collectors.toList());
        productTypeSpecificationKeyRepository.saveAll(keys);
        // Save the product type
        productType = productTypeRepository.save(productType);
        // Save the specification with the saved keys
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
