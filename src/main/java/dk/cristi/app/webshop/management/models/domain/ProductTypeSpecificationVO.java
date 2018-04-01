package dk.cristi.app.webshop.management.models.domain;

import javax.validation.constraints.NotBlank;

public class ProductTypeSpecificationVO {
    // TODO: 20-Mar-18 Add validation annotations
    @NotBlank
    private String keyName;
    @NotBlank
    private String value;
    @NotBlank
    private String keyType;

    // @formatter:off
    // Needed for Jackson Deserializer
    protected ProductTypeSpecificationVO(){}
    // @formatter:on

    public ProductTypeSpecificationVO(String keyName, String value, String keyType) {
        this.keyName = keyName;
        this.value = value;
        this.keyType = keyType;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getValue() {
        return value;
    }

    public String getKeyType() {
        return keyType;
    }
}
