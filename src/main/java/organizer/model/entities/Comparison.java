package organizer.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import organizer.utils.Constants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Comparison")
public class Comparison {

    @Id
    private String id; // год

    @OneToOne
    private BusinessPlan businessPlan; // версия БП
    @Lob
    private HashMap<String, BigDecimal> count; // мапа из версии БП
    @Lob
    private HashMap<String, BigDecimal> cost; // мапа из версии БП


    public BigDecimal getGeneralCost() {
        BigDecimal result = BigDecimal.ZERO;
        for (String str : cost.keySet()) {
            result = result.add(cost.get(str));
        }
        return result;
    }

    public BigDecimal getLeasingCost(){
        BigDecimal result = BigDecimal.ZERO;
        for (String str : cost.keySet()) {
            if (Constants.LEASING_CATEGORIES.contains(str)){
                result = result.add(cost.get(str));
            }
        }
        return result;
    }
    public BigDecimal getCostWithoutLeasing(){
        BigDecimal result = BigDecimal.ZERO;
        for (String str : cost.keySet()) {
            if (!Constants.LEASING_CATEGORIES.contains(str)){
                result = result.add(cost.get(str));
            }
        }
        return result;
    }
}
