package dk.cristi.app.webshop.management.controllers;

import dk.cristi.app.webshop.management.controllers.http_exceptions.Http404Exception;
import dk.cristi.app.webshop.management.models.entities.Product;
import dk.cristi.app.webshop.management.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Api(tags = {"product"})
public class ProductResource {
    private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);
    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all products.")
    public List<Product> fetchProducts() {
        return productService.fetchAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get one product by id")
    public Product getProduct(@PathVariable("id") long id){
        return productService.fetchOne(id).orElseThrow(Http404Exception::new);
    }
}
