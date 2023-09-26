package organizer.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import static organizer.utils.Constants.PATTERN_FOR_NUMBERS;
import static organizer.utils.Constants.STRING_FORMAT_FOR_NUMBER_FORMAT_EXCEPTION;

public class FormatUtils {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(PATTERN_FOR_NUMBERS);

    public static String formatNumber(BigDecimal data) {
        return DECIMAL_FORMAT.format(data);
    }

    public static BigDecimal parseNumber(String str) {
        try {
            return BigDecimal.valueOf(DECIMAL_FORMAT.parse(str.replaceAll(" ", "").replaceAll("\\.", ",").trim()).doubleValue());
        } catch (ParseException e) {
            throw new NumberFormatException(String.format(STRING_FORMAT_FOR_NUMBER_FORMAT_EXCEPTION, str));
        }
    }

    public static Long parseNumberOrdering(String str){
        if (!str.trim().equals("")){
            try{
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(String.format(STRING_FORMAT_FOR_NUMBER_FORMAT_EXCEPTION, str));
            }
        }else {
            return (long) -1;
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
