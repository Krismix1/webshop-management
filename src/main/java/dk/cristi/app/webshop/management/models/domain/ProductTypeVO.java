package dk.cristi.app.webshop.management.models.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductTypeVO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private ProductTypeSpecificationVO[] specifications;
    @Positive(message = "Category id must be a positive integer")
    private long categoryId;

    // @formatter:off
    // Needed for Jackson Deserializer
    protected ProductTypeVO() {}
    // @formatter:on

    public ProductTypeVO(String name, String description, long categoryId, ProductTypeSpecificationVO[] specifications) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.specifications = specifications;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public ProductTypeSpecificationVO[] getSpecifications() {
        return specifications;
    }
}
