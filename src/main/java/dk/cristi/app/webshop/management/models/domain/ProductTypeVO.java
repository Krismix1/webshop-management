package dk.cristi.app.webshop.management.models.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductTypeVO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private ProductTypeSpecificationVO[] specifications;

    // @formatter:off
    // Needed for Jackson Deserializer
    protected ProductTypeVO() {}
    // @formatter:on

    public ProductTypeVO(String name, String description, ProductTypeSpecificationVO[] specifications) {
        this.name = name;
        this.description = description;
        this.specifications = specifications;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductTypeSpecificationVO[] getSpecifications() {
        return specifications;
    }
}
