package dk.cristi.app.webshop.management.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table(name = "prod_type_specs")
@Entity
public class ProductTypeSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "value", length = 64)
    private String value;
    @OneToOne
    private ProductTypeSpecificationKey productTypeSpecificationKey;
    @ManyToOne
    @JsonIgnore
    private ProductType productType;
    private int position; // The position of the specification in the view

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ProductTypeSpecificationKey getProductTypeSpecificationKey() {
        return productTypeSpecificationKey;
    }

    public void setProductTypeSpecificationKey(ProductTypeSpecificationKey productTypeSpecificationKey) {
        this.productTypeSpecificationKey = productTypeSpecificationKey;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
