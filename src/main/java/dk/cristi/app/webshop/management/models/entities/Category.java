package dk.cristi.app.webshop.management.models.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Category name must contain at least 1 non whitespace character.")
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
