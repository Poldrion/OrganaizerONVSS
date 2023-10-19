package organizer.utils;

import java.time.LocalDate;
import java.util.List;

public class CurrentData {

    public static final List<String> YEARS = ListUtils.getYearsByString();
    public static final LocalDate CURRENT_DATE = LocalDate.now();
    public static int CURRENT_YEAR = CURRENT_DATE.getYear();
    public static String CURRENT_PERIOD = "Весь год";

}
