package dk.cristi.app.webshop.management.models.domain;

import dk.cristi.app.webshop.management.validators.IsNullOrIsNotBlankConstraint;

import javax.validation.constraints.NotBlank;

public class ProductTypeSpecificationVO {
    // TODO: 20-Mar-18 Add validation annotations
    @NotBlank
    private String keyName;
    @NotBlank
    private String defaultValue;
    @NotBlank
    private String keyType;
    @IsNullOrIsNotBlankConstraint
    private String description;

    // @formatter:off
    // Needed for Jackson Deserializer
    protected ProductTypeSpecificationVO(){}
    // @formatter:on

    public ProductTypeSpecificationVO(String keyName, String defaultValue, String keyType, String description) {
        this.keyName = keyName;
        this.defaultValue = defaultValue;
        this.keyType = keyType;
        this.description = description;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getKeyType() {
        return keyType;
    }

    public String getDescription() {
        return description;
    }
}
