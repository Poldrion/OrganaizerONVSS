package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CodeKSM")
public class CodeKSM {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    private Nomenclature nomenclature;


    public CodeKSM(String name) {
        this.name = name;
    }

    public CodeKSM(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeKSM codeKSM = (CodeKSM) o;
        return Objects.equals(id, codeKSM.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
