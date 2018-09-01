package dk.cristi.app.webshop.management.helpers;

import dk.cristi.app.webshop.management.models.domain.ProductTypeSpecificationVO;
import dk.cristi.app.webshop.management.models.domain.ProductTypeVO;
import dk.cristi.app.webshop.management.models.entities.Category;
import dk.cristi.app.webshop.management.models.entities.ProductType;

import java.util.Collections;

public class DummyTestData {
    public static ProductTypeSpecificationVO SPEC_1() {
        return new ProductTypeSpecificationVO(
                "spec 1",
                "5",
                "int",
                ""
        );
    }

    public static ProductTypeSpecificationVO SPEC_2() {
        return new ProductTypeSpecificationVO(
                "spec 2",
                "true",
                "bool",
                ""
        );
    }

    public static ProductTypeSpecificationVO[] SPEC_ARRAY() {
        return new ProductTypeSpecificationVO[]{SPEC_1(), SPEC_2()};
    }

    public static ProductTypeVO PRODUCT_TYPE_VO() {
        return new ProductTypeVO(
                "prod type from VO",
                "prod type created from test VO",
                1,
                SPEC_ARRAY()
        );
    }

    public static ProductType PRODUCT_TYPE_FROM_VO() {
        ProductType productType = new ProductType();
        productType.setName(PRODUCT_TYPE_VO().getName());
        productType.setDescription(PRODUCT_TYPE_VO().getDescription());
        productType.setSpecifications(Collections.emptyList());
        productType.setCategory(CATEGORY_1());
        return productType;
    }

    public static ProductType PRODUCT_TYPE() {
        ProductType productType = new ProductType();
        productType.setName("new test prod");
        productType.setDescription("test description");
        productType.setSpecifications(Collections.emptyList());
        productType.setCategory(CATEGORY_1());
        return productType;
    }

    public static Category CATEGORY_1() {
        Category category = new Category(1);
        category.setName("test category");
        return category;
    }
}
