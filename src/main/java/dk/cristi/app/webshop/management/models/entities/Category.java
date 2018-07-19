package dk.cristi.app.webshop.management.models.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Category name must contain at least 1 non whitespace character.")
    private String name;

    /**
     * Needed to create objects for testing
     */
    public Category(long id) {
        this.id = id;
    }

    /**
     * Needed for JPA
     */
    // @formatter:off
    protected Category() {}
    // @formatter:on

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
