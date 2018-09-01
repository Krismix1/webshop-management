package dk.cristi.app.webshop.management.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table(name = "prod_type_spec_keys")
@Entity
public class ProductTypeSpecificationKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", length = 32)
    private String name;
    @Column(nullable = false, length = 8)
    private String type;
    @Column(name = "descr")
    private String description;
    @Column(name = "value", length = 64)
    private String defaultValue;
    @ManyToOne
    @JsonIgnore
    private ProductType productType;

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
