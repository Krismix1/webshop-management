package dk.cristi.app.webshop.management.unit.services;

import dk.cristi.app.webshop.management.helpers.DummyTestData;
import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.repositories.ProductTypeRepository;
import dk.cristi.app.webshop.management.repositories.ProductTypeSpecificationKeyRepository;
import dk.cristi.app.webshop.management.repositories.ProductTypeSpecificationRepository;
import dk.cristi.app.webshop.management.services.ProductTypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeServiceTest {
    @Mock
    private ProductTypeRepository productTypeRepository;
    @Mock
    private ProductTypeSpecificationRepository productTypeSpecificationRepository;
    @Mock
    private ProductTypeSpecificationKeyRepository productTypeSpecificationKeyRepository;

    @InjectMocks
    private ProductTypeService productTypeService;

    @Before
    public void setup() {
        when(productTypeRepository.findAll())
                .thenReturn(Arrays.asList(DummyTestData.PRODUCT_TYPE(), DummyTestData.PRODUCT_TYPE_FROM_VO()));

        when(productTypeRepository.findByName(DummyTestData.PRODUCT_TYPE().getName()))
                .thenReturn(null);

        when(productTypeRepository.findByName(DummyTestData.PRODUCT_TYPE_FROM_VO().getName()))
                .thenReturn(DummyTestData.PRODUCT_TYPE_FROM_VO());

        when(productTypeRepository.save(DummyTestData.PRODUCT_TYPE()))
                .thenReturn(DummyTestData.PRODUCT_TYPE());

        when(productTypeRepository.save(DummyTestData.PRODUCT_TYPE_FROM_VO()))
                .thenThrow(org.springframework.dao.DuplicateKeyException.class); // some kind of error for now

        when(productTypeRepository.findById(1L))
                .then(invocation -> {
                    ProductType productType = DummyTestData.PRODUCT_TYPE_FROM_VO();
                    productType.setId(1);
                    return Optional.of(productType);
                });

        when(productTypeRepository.findById(2L))
                .thenReturn(Optional.empty());
    }

    // see if ProductTypeResourceTest tests for resources not found

    @Test
    public void createsNewResource() {
        fail("Not implemented");
    }

    @Test
    public void createsNewResource_handlesErrors() { // ??? or should the controller handle the exceptions
        fail("Not implemented");
    }

    @Test
    public void findByName() {
        Optional<ProductType> optionalProductType = productTypeService.findByName(DummyTestData.PRODUCT_TYPE_FROM_VO().getName());
        assertNotNull("Should not be null", optionalProductType);
        ProductType productType = optionalProductType.get();
        assertNotNull("Should not be null", productType);
        assertEquals("Should be the same object", DummyTestData.PRODUCT_TYPE_FROM_VO(), productType);
    }

    @Test
    public void findByName_NotFound() {
        Optional<ProductType> optionalProductType = productTypeService.findByName(DummyTestData.PRODUCT_TYPE().getName());
        assertNotNull("Should not be null", optionalProductType);
        assertFalse("Should be empty", optionalProductType.isPresent());
        try {
            optionalProductType.get();
            fail("Should've thrown exception");
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void fetchAll() {
        Collection<ProductType> productTypes = productTypeService.fetchAll();
        ProductType[] expected = new ProductType[]{
                DummyTestData.PRODUCT_TYPE(), DummyTestData.PRODUCT_TYPE_FROM_VO()
        };
        assertArrayEquals(expected, productTypes.toArray());
    }

    @Test
    public void findById() {
        Optional<ProductType> optionalProductType = productTypeService.findById(1);
        assertNotNull("Should not be null", optionalProductType);
        ProductType productType = optionalProductType.get();
        assertNotNull("Should not be null", productType);
        assertEquals("Should be the same object", DummyTestData.PRODUCT_TYPE_FROM_VO(), productType);
        assertEquals("Should have the same id", 1, productType.getId());
    }

    @Test
    public void findById_notFound() {
        Optional<ProductType> optionalProductType = productTypeService.findById(2);
        assertNotNull("Should not be null", optionalProductType);
        assertFalse("Should be empty", optionalProductType.isPresent());
        try {
            optionalProductType.get();
            fail("Should've thrown exception");
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }
    }
}
