package organizer.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ListUtils {
    public static final List<String> PERIOD = Arrays.asList("Весь год", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
    public static final List<String> CATEGORIES = Arrays.asList("Все категории", "УЭЦН полнокомплектные импортные", "УЭЦН полнокомплектные отечественные",
            "АКИВ", "ПЭД", "Газосепараторы и диспергаторы", "Полнокомплектные УЭВН", "Система погружной телеметрии (ТМС)",
            "Трансформатор ТМПН", "СУ с ЧРП", "Станция управления", "Кабель погружной для УЭЦН до 160 градусов",
            "Кабель погружной для УЭЦН до 230 градусов", "Термоизолированная НКТ", "Стеклопластиковая НКТ",
            "Фонтанная арматура (в т.ч. АФК с КГ)", "Насосно-компрессорные трубы", "Штанговые насосы",
            "Насосные штанги", "Защита ЭЦН от мех.примесей ШУМ, ЖНШ, МВФ", "УДХ для ингибирования скважин",
            "Протекторы для защиты кабеля", "Протектолайзеры", "Пакера и компановки ОРЗ", "Новая техника",
            "Оборудование для ОРЭ", "Прочее");
    public static final List<String> YEARS_FOR_COMPARISON = Arrays.asList("Первый год", "Второй год", "Третий год", "Четвертый год", "Пятый год");
    public static final List<String> LEASING_CATEGORIES = Arrays.asList("УЭЦН полнокомплектные импортные", "УЭЦН полнокомплектные отечественные",
            "АКИВ", "Трансформатор ТМПН", "СУ с ЧРП", "Станция управления");

    public static List<String> getYears() {
        Properties properties = new Properties();
        List<String> years = new ArrayList<>();
        try {
            properties.load(new FileReader("properties/config.properties"));
            for (int i = 0; i < Integer.parseInt(properties.getProperty("countYears")); i++) {
                years.add(String.valueOf(Integer.parseInt(properties.getProperty("firstYear")) + i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return years;
    }

    public static String getFirstYear() {
        Properties properties = new Properties();
        String firstYear = null;
        try {
            properties.load(new FileReader("properties/config.properties"));
            firstYear = properties.getProperty("firstYear");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstYear;
    }

    public static void setFirstYear(String firstYear) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("properties/config.properties"));
            properties.setProperty("firstYear", firstYear);
            properties.store(new FileWriter("properties/config.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCountYears() {
        Properties properties = new Properties();
        String countYears = null;
        try {
            properties.load(new FileReader("properties/config.properties"));
            countYears = properties.getProperty("countYears");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countYears;
    }

    public static void setCountYears(String countYears) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("properties/config.properties"));
            properties.setProperty("countYears", countYears);
            properties.store(new FileWriter("properties/config.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
