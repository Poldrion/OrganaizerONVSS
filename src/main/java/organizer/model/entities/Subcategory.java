package organizer.model.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Subcategory")
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Category category;
    @OneToMany(mappedBy = "subcategory")
    private Collection<Nomenclature> nomenclatures;


    public Subcategory(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    @Override
    public String toString() {
        return name;
    }
}
