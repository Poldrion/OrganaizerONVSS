package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TechnicalRequirement")
public class TechnicalRequirement {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "codeKSM")
    private String codeKSM;

    @Column(name = "codeKSMName")
    private String codeKSMName;

    @Column(name = "file_TR_name")
    private String fileTRName;

    @Column(name = "system_number_transaction")
    private String systemNumberTransaction;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @OneToOne
    private Nomenclature nomenclature;

    public TechnicalRequirement(String id, String name, String codeKSM, String codeKSMName, String fileTRName, String systemNumberTransaction, LocalDate dateCreate) {
        this.id = id;
        this.name = name;
        this.codeKSM = codeKSM;
        this.codeKSMName = codeKSMName;
        this.fileTRName = fileTRName;
        this.systemNumberTransaction = systemNumberTransaction;
        this.dateCreate = dateCreate;
    }
}
