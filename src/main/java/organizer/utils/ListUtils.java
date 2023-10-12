package organizer.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ListUtils {

	public static List<Integer> getYears() {
		Properties properties = new Properties();
		List<Integer> years = new ArrayList<>();
		try {
			FileReader reader = new FileReader("properties/config.properties");
			properties.load(reader);
			for (int i = 0; i < Integer.parseInt(properties.getProperty("countYears")); i++) {
				years.add(Integer.parseInt(properties.getProperty("firstYear")) + i);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return years;
	}

	public static List<String> getYearsByString() {
		Properties properties = new Properties();
		List<String> years = new ArrayList<>();
		try {
			FileReader reader = new FileReader("properties/config.properties");
			properties.load(reader);
			for (int i = 0; i < Integer.parseInt(properties.getProperty("countYears")); i++) {
				years.add(String.valueOf(Integer.parseInt(properties.getProperty("firstYear")) + i));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return years;
	}

	public static Integer getFirstYear() {
		Properties properties = new Properties();
		int firstYear = 0;
		try {
			FileReader reader = new FileReader("properties/config.properties");
			properties.load(reader);
			firstYear = Integer.parseInt(properties.getProperty("firstYear"));
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return firstYear;
	}

	public static void setFirstYear(String firstYear) {
		Properties properties = new Properties();
		try {
			FileReader reader = new FileReader("properties/config.properties");
			properties.load(reader);
			properties.setProperty("firstYear", firstYear);
			FileWriter writer = new FileWriter("properties/config.properties");
			properties.store(writer, null);
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCountYears() {
		Properties properties = new Properties();
		String countYears = null;
		try {
			FileReader reader = new FileReader("properties/config.properties");
			properties.load(reader);
			countYears = properties.getProperty("countYears");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return countYears;
	}

	public static void setCountYears(String countYears) {
		Properties properties = new Properties();
		try {
			FileReader reader = new FileReader("properties/config.properties");
			properties.load(reader);
			properties.setProperty("countYears", countYears);
			FileWriter writer = new FileWriter("properties/config.properties");
			properties.store(writer, null);
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getLeasingCategories() {
		List<String> leasingCategoriesList = new ArrayList<>();
		Properties properties = new Properties();
		InputStream fis;
		try {
			fis = new FileInputStream("properties/leasing_category.properties");
			InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
			properties.load(reader);
			int countLeasingCategories = Integer.parseInt(properties.getProperty("counts"));

			if (countLeasingCategories > 0) {
				for (int i = 0; i < countLeasingCategories; i++) {
					leasingCategoriesList.add(properties.getProperty("category" + i));
				}
			}
			fis.close();
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return leasingCategoriesList;

	}

}
