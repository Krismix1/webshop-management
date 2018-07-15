package dk.cristi.app.webshop.management.unit.controllers;

import dk.cristi.app.webshop.management.controllers.ProductTypeResource;
import dk.cristi.app.webshop.management.controllers.http_exceptions.Http404Exception;
import dk.cristi.app.webshop.management.helpers.DummyTestData;
import dk.cristi.app.webshop.management.models.domain.ProductTypeVO;
import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.services.ProductTypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

//@SpringBootTest - seems to be unnecessary for unit test
@RunWith(MockitoJUnitRunner.class)
// https://dzone.com/articles/spring-boot-unit-testing-and-mocking-with-mockito
public class ProductTypeResourceTest {

    @Mock
    ProductTypeService productTypeService;

    @InjectMocks
    ProductTypeResource productTypeResource;

    @Before
    public void setup() {
        when(productTypeService.fetchAll())
                .thenReturn(Collections.singleton(DummyTestData.PRODUCT_TYPE()));

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
        assertEquals(1, productTypeVOs.size());
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
            fail("Should've failed because product does not exist");
        } catch (Http404Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void postProductType() {
        // this test is left intentionally to fail, so that it will be fixed later
        // it fails because ServletUriComponentsBuilder.fromCurrentRequest() cannot get the current request
        // which means that this test case needs to be performed under integration test
        final String name = "new prod";
        final String description = "prod created from test";
        final ProductTypeVO productTypeVO = new ProductTypeVO(name, description, 1, DummyTestData.SPEC_ARRAY());
        final ResponseEntity<?> responseEntity = productTypeResource.postProductType(productTypeVO);
        assertEquals("Should create with success", HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        assertTrue("Should have Location header", responseEntity.getHeaders().containsKey("Location"));
        assertTrue("Location of the newly created resource must be specified", responseEntity.getHeaders().getLocation().toASCIIString().contains(name));
    }
}