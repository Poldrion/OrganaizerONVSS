package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private Collection<Subcategory> subcategories;


    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
