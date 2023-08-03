package organizer.model.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Ordering")
public class Ordering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String number;
    private String position;
    @ManyToOne
    @JoinColumn(name = "ordering_id")
    private Nomenclature nomenclature;
    @ManyToOne
    @JoinColumn(name = "units_id")
    private Units unit;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal cost;
    private boolean requiresInstallation = false;
    private boolean isLeasing = false;
    private LocalDate date;
    private String remark;

    @Override
    public String toString() {
        return nomenclature.getName();
    }
}
