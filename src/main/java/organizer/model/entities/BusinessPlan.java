package organizer.model.entities;

import lombok.Data;
import organizer.model.services.CategoryService;

import javax.persistence.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Data
@Entity
@Table(name = "BusinessPlan")
public class BusinessPlan {
    @Transient
    private CategoryService categoryService;

    @Id
    private String id;
    private int firstYear;

    @Lob
    private HashMap<String, BigDecimal> firstYearCount = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> secondYearCount = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> thirdYearCount = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> forthYearCount = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> fifthYearCount = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> firstYearCost = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> secondYearCost = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> thirdYearCost = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> forthYearCost = new HashMap<>();
    @Lob
    private HashMap<String, BigDecimal> fifthYearCost = new HashMap<>();


    public BusinessPlan() {
        List<String> categories = new ArrayList<>();
        Properties categoryProperties = new Properties();
        try {
            categoryProperties.load(new FileInputStream("properties/categories.properties"));
            int counts = Integer.parseInt(categoryProperties.getProperty("counts"));
            for (int i = 0; i < counts; i++) {
                categories.add(categoryProperties.getProperty("category" + i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String str : categories) {
            firstYearCount.put(str, BigDecimal.ZERO);
            secondYearCount.put(str, BigDecimal.ZERO);
            thirdYearCount.put(str, BigDecimal.ZERO);
            forthYearCount.put(str, BigDecimal.ZERO);
            fifthYearCount.put(str, BigDecimal.ZERO);
            firstYearCost.put(str, BigDecimal.ZERO);
            secondYearCost.put(str, BigDecimal.ZERO);
            thirdYearCost.put(str, BigDecimal.ZERO);
            forthYearCost.put(str, BigDecimal.ZERO);
            fifthYearCost.put(str, BigDecimal.ZERO);
        }
    }

    @Override
    public String toString() {
        return id;
    }
}
