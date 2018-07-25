package dk.cristi.app.webshop.management.unit.controllers;

import dk.cristi.app.webshop.management.controllers.ProductTypeResource;
import dk.cristi.app.webshop.management.controllers.http_exceptions.Http404Exception;
import dk.cristi.app.webshop.management.helpers.DummyTestData;
import dk.cristi.app.webshop.management.models.domain.ProductTypeVO;
import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.services.CategoryService;
import dk.cristi.app.webshop.management.services.ProductTypeService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@SpringBootTest - seems to be unnecessary for unit test
@RunWith(MockitoJUnitRunner.class)
// https://dzone.com/articles/spring-boot-unit-testing-and-mocking-with-mockito
public class ProductTypeResourceTest {

    private static final String UTF_8 = "UTF-8";
    @Mock
    private ProductTypeService productTypeService;
    @Mock
    private CategoryService categoryService;
    private UriComponentsBuilder uriComponentsBuilder;

    @InjectMocks
    private ProductTypeResource productTypeResource;

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

        when(categoryService.fetchOne(1))
                .thenReturn(Optional.of(DummyTestData.CATEGORY_1()));

        when(categoryService.fetchOne(2))
                .thenReturn(Optional.empty());

        when(productTypeService.save(DummyTestData.PRODUCT_TYPE_FROM_VO()))
                .thenReturn(DummyTestData.PRODUCT_TYPE_FROM_VO());

        uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");
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
    public void postProductType_throws404Exception() {
        // throws a Http404Exception when category is not found
        try {
            final String name = "new prod";
            final String description = "prod created from test";
            final ProductTypeVO productTypeVO = new ProductTypeVO(name, description, 2, DummyTestData.SPEC_ARRAY());
            productTypeResource.postProductType(productTypeVO, uriComponentsBuilder);
            fail("Should've failed because category does not exist");
        } catch (Http404Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void postProductType() {
        // creates the new resource and returns the location of it
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();
        String locationUri = UriUtils.encode(postData.getName(), UTF_8);
        final ResponseEntity<?> responseEntity = productTypeResource.postProductType(postData, uriComponentsBuilder);
        assertEquals("Should create with success", HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        assertTrue("Should have Location header", responseEntity.getHeaders().containsKey("Location"));
        assertThat("Location of the newly created resource must be specified", responseEntity.getHeaders().getLocation().toASCIIString(), CoreMatchers.containsString(locationUri));
    }
}