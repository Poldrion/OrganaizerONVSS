package organizer.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public class FormatUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    public static String formatNumber(BigDecimal data) {
        return DECIMAL_FORMAT.format(data);
    }

    public static BigDecimal parseNumber(String str) {
        try {
            return BigDecimal.valueOf(DECIMAL_FORMAT.parse(str.replaceAll(" ", "").replaceAll("\\.", ",").trim()).doubleValue());
        } catch (ParseException e) {
            throw new NumberFormatException(String.format("Неверный формат числа %s", str));
        }
    }

    public static boolean isNumeric(String string) {
        // Checks if the provided string
        // is a numeric by applying a regular
        // expression on it.
        String regex = "[0-9]+[\\.||\\,]?[0-9]*";
        return Pattern.matches(regex, string);
    }

}
