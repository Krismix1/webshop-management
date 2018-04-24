package dk.cristi.app.webshop.management.controllers;

import dk.cristi.app.webshop.management.controllers.http_exceptions.Http404Exception;
import dk.cristi.app.webshop.management.models.domain.ProductTypeVO;
import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.models.entities.ProductTypeSpecification;
import dk.cristi.app.webshop.management.models.entities.ProductTypeSpecificationKey;
import dk.cristi.app.webshop.management.services.ProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/products/types")
@Api(tags = {"product-types"})
public class ProductTypeResource {

    private final ProductTypeService productTypeService;

    @Autowired
    public ProductTypeResource(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Get all product types.")
    public Collection<ProductType> fetchAll() {
        return productTypeService.fetchAll();
    }

    @GetMapping(value = "/{name}", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Get a product type.")
    public ProductType fetchOne(@PathVariable("name") String name) {

        final Optional<ProductType> productTypeOptional = productTypeService.findByName(name);
        // return if present, otherwise return a 404
        return productTypeOptional.orElseThrow(Http404Exception::new);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Create a new product type", notes = "On success, returns the URI for the new created resource")
    public ResponseEntity<?> postProductType(@Valid @RequestBody ProductTypeVO productTypeVO) {

        final ProductType productType = new ProductType();
        productType.setName(productTypeVO.getName());
        productType.setDescription(productTypeVO.getDescription());

        final List<ProductTypeSpecification> productTypeSpecifications = Stream.of(productTypeVO.getSpecifications())
                .map(specification -> {
                    ProductTypeSpecificationKey key = new ProductTypeSpecificationKey();
                    key.setKeyName(specification.getKeyName());
                    key.setType(specification.getKeyType());

                    ProductTypeSpecification productTypeSpecification = new ProductTypeSpecification();
                    productTypeSpecification.setValue(specification.getValue());
                    productTypeSpecification.setProductTypeSpecificationKey(key);
                    productTypeSpecification.setProductType(productType);

                    return productTypeSpecification;
                })
                .collect(Collectors.toList());

        productType.setSpecifications(productTypeSpecifications);

        productTypeService.save(productType);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{name}").buildAndExpand(productType.getName()).toUri();

        return ResponseEntity.created(location).build();
    }
}
