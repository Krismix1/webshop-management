package dk.cristi.app.webshop.management.unit.controllers;

import dk.cristi.app.webshop.management.controllers.ProductTypeResource;
import dk.cristi.app.webshop.management.controllers.http_exceptions.Http404Exception;
import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.services.ProductTypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
// https://dzone.com/articles/spring-boot-unit-testing-and-mocking-with-mockito
public class ProductTypeResourceTest {

    @Mock
    ProductTypeService productTypeService;

    @InjectMocks
    ProductTypeResource productTypeResource;

    @Before
    public void loadData() {
        when(productTypeService.fetchAll())
                .thenReturn(Collections.emptyList());

        ProductType productType = new ProductType();
        productType.setName("prod_1");
        productType.setDescription("test description");
        productType.setSpecifications(Collections.emptyList());

        when(productTypeService.findByName("prod_1"))
                .thenReturn(Optional.of(productType));
    }

    @Test
    public void fetchAll() throws Exception {
        final Collection<ProductType> productTypeVOs = productTypeResource.fetchAll();
        assertEquals(0, productTypeVOs.size());
    }

    @Test
    public void fetchOne() {
        final ProductType productType = productTypeResource.fetchOne("prod_1");
        assertNotNull(productType);
    }

    @Test
    public void fetchOne_NotFound() {
        try {
            final ProductType productType = productTypeResource.fetchOne("prod_2");
            assertTrue(false);
        } catch (Http404Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void postProductType() {

    }
}