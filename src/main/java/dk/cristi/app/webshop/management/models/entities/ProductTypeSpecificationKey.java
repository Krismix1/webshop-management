package dk.cristi.app.webshop.management.models.entities;

import javax.persistence.*;

@Table(name = "prod_type_spec_keys")
@Entity
public class ProductTypeSpecificationKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "key_name", length = 32)
    private String keyName;
    @Column(nullable = false, length = 8)
    private String type;

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getType() {
        return type;
    }
}
