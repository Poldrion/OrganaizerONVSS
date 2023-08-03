package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Nomenclature")
public class Nomenclature {

    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "codeKSM_id")
    private CodeKSM codeKSM;
    //    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "technicalRequirement_id")
    private TechnicalRequirement technicalRequirement;
    @ManyToOne
    @JoinColumn(name = "nomenclature_id")
    private Subcategory subcategory;
    @OneToMany(mappedBy = "nomenclature")
    private Collection<Ordering> orderings;

    public Nomenclature(CodeKSM codeKSM, TechnicalRequirement technicalRequirement) {
        this.codeKSM = codeKSM;
        this.technicalRequirement = technicalRequirement;
        this.id = this.codeKSM.getId() + "_" + this.technicalRequirement.getId();
        this.name = codeKSM.getName();
    }

    public Nomenclature(CodeKSM codeKSM) {
        this.codeKSM = codeKSM;
        this.technicalRequirement = null;
        this.id = this.codeKSM.getId();
        this.name = codeKSM.getName();
    }


}
