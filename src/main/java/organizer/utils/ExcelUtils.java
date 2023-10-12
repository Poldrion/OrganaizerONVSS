package organizer.utils;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import organizer.model.entities.*;
import organizer.model.services.*;
import organizer.views.controller.common.Dialog;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static organizer.utils.Constants.*;

public class ExcelUtils {

	public static final String MAIN_TEMPLATE = "templates/main_template.xlsx";
	public static final String TEMP_MAIN_TEMPLATE = "templates/temp_main_template.xlsx";
	public static final String DETAILS_TEMPLATE = "templates/details_template.xlsx";
	public static final String TEMP_DETAILS_TEMPLATE = "templates/temp_details_template.xlsx";
	public static final String ORDERING_TEMPLATE = "templates/ordering_template.xlsx";
	public static final String TEMP_ORDERING_TEMPLATE = "templates/temp_ordering_template.xlsx";
	public static final String CATEGORY_TEMPLATE = "templates/categories_template.xlsx";
	public static final String TEMP_CATEGORY_TEMPLATE = "templates/temp_categories_template.xlsx";
	public static final String SUBCATEGORY_TEMPLATE = "templates/subcategories_template.xlsx";
	public static final String TEMP_SUBCATEGORY_TEMPLATE = "templates/temp_subcategories_template.xlsx";
	public static final String TR_TEMPLATE = "templates/technical_requirement_template.xlsx";
	public static final String TEMP_TR_TEMPLATE = "templates/temp_technical_requirement_template.xlsx";
	public static final String NOMENCLATURE_TEMPLATE = "templates/nomenclature_template.xlsx";
	public static final String TEMP_NOMENCLATURE_TEMPLATE = "templates/temp_nomenclature_template.xlsx";
	public static final String BP_TEMPLATE = "templates/bp_template.xlsx";
	public static final String TEMP_BP_TEMPLATE = "templates/temp_bp_template.xlsx";


	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_FOR_SHORT_DATE);
	private static final HashSet<String> ERROR_SET = new HashSet<>();
	private static int ERROR_COUNT = 0;

	/**
	 * Метод для выгрузки отображаемой в текущий момент информации в таблицы на главной вкладке в файл xlsx
	 *
	 * @param year                              год поставки
	 * @param period                            период поставки (весь год или отдельный месяц)
	 * @param businessPlanGeneral               элемент Label в который передается значение общего Бизнес-плана
	 * @param businessPlanLeasing               элемент Label в который передается значение Бизнес-плана в части лизинга
	 * @param businessPlanWithoutLeasing        элемент Label в который передается значение Бизнес-плана без учета лизинга (ДО)
	 * @param businessPlanGeneralPercent        элемент Label в который передается процент передачи потребности относительно общего Бизнес-плана
	 * @param businessPlanLeasingPercent        элемент Label в который передается процент передачи потребности относительно Бизнес-плана в части лизинга
	 * @param businessPlanWithoutLeasingPercent элемент Label в который передается процент передачи потребности относительно Бизнес-плана без учета лизинга (ДО)
	 * @param table                             таблица, из которой берется информация для выгрузки
	 * @param categoryCol                       столбец таблицы, в котором указываются категории оборудования
	 * @param unitsCol                          столбец таблицы, в котором указываются единицы измерения
	 * @param countCol                          столбец таблицы, в котором указывается количество оборудования (фактически переданная потребность)
	 * @param costCol                           столбец таблицы, в котором указывается стоимость оборудования без НДС (фактически переданная потребность)
	 * @param costWithTaxCol                    столбец таблицы, в котором указывается стоимость оборудования с НДС (фактически переданная потребность)
	 * @param countPlanCol                      столбец таблицы, в котором указывается количество оборудования (по бизнес-плану)
	 * @param costPlanCol                       столбец таблицы, в котором указывается стоимость оборудования без НДС (по бизнес-плану)
	 * @param costWithTaxPlanCol                столбец таблицы, в котором указывается стоимость оборудования с НДС (по бизнес-плану)
	 * @param percentCol                        столбец, в котром указывается процент передачи потребности по категории
	 * @param resultsFolder                     папка, в которую будет сохранен результат
	 */
	public static void unloadMainTableToExcel(@NotNull ComboBox<String> year, @NotNull ComboBox<String> period, @NotNull Label businessPlanGeneral,
											  @NotNull Label businessPlanLeasing, @NotNull Label businessPlanWithoutLeasing,
											  @NotNull Label businessPlanGeneralPercent, @NotNull Label businessPlanLeasingPercent,
											  @NotNull Label businessPlanWithoutLeasingPercent,
											  @NotNull Label factGeneral, @NotNull Label factLeasing, @NotNull Label factWithoutLeasing,
											  @NotNull Label remainsGeneral, @NotNull Label remainsLeasing, @NotNull Label remainsWithoutLeasing,
											  @NotNull Label offsetGeneral, @NotNull Label offsetLeasing, @NotNull Label offsetWithoutLeasing,
											  @NotNull TableView<Category> table,
											  TableColumn<Category, String> categoryCol, TableColumn<Category, String> unitsCol,
											  TableColumn<Category, String> countCol, TableColumn<Category, String> costCol,
											  TableColumn<Category, String> costWithTaxCol, TableColumn<Category, String> countPlanCol,
											  TableColumn<Category, String> costPlanCol, TableColumn<Category, String> costWithTaxPlanCol,
											  TableColumn<Category, String> countRemainsCol, TableColumn<Category, String> costRemainsCol,
											  TableColumn<Category, String> costWithTaxRemainsCol, TableColumn<Category, String> percentCol,
											  String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_MAIN_TEMPLATE, MAIN_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle stylePercent = getStyleOnlyNumber(wb, PATTERN_FOR_PERCENT);
			CellStyle styleNumber = getStyleOnlyNumber(wb, PATTERN_FOR_NUMBERS);
			CellStyle centerAlignment = getStyleCenterAlignmentWithBorder(wb);
			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);
			CellStyle styleBorderAllAroundPercent = getStyleBorderAllAroundAndNumberFormat(wb, PATTERN_FOR_PERCENT);
			CellStyle styleBorderAllAroundNumber = getStyleBorderAllAroundAndNumberFormat(wb, PATTERN_FOR_NUMBERS);


			// Год
			Row firstRow = sheet.getRow(0);
			firstRow.createCell(1).setCellValue(year.getSelectionModel().getSelectedItem());

			// Период, процент общий, БП общий, Факт общий, Остаток общий, Откл. общее
			Row secondRow = sheet.getRow(1);
			secondRow.createCell(1).setCellValue(period.getSelectionModel().getSelectedItem());
			Cell cell = secondRow.createCell(6);
			cell.setCellStyle(stylePercent);
			cell.setCellValue(FormatUtils.parseNumber(businessPlanGeneralPercent.getText().replace("%", "")).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP).doubleValue());
			cell = secondRow.createCell(7);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(businessPlanGeneral.getText()).doubleValue());
			cell = secondRow.createCell(8);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(factGeneral.getText()).doubleValue());
			cell = secondRow.createCell(9);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(remainsGeneral.getText()).doubleValue());
			cell = secondRow.createCell(10);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(offsetGeneral.getText()).doubleValue());

			// Процент лизинг, БП лизинг, Факт лизинг, Остаток лизинг, Откл. лизинг
			Row thirdRow = sheet.getRow(2);
			cell = thirdRow.createCell(6);
			cell.setCellStyle(stylePercent);
			cell.setCellValue(FormatUtils.parseNumber(businessPlanLeasingPercent.getText().replace("%", "")).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP).doubleValue());
			cell = thirdRow.createCell(7);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(businessPlanLeasing.getText()).doubleValue());
			cell = thirdRow.createCell(8);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(factLeasing.getText()).doubleValue());
			cell = thirdRow.createCell(9);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(remainsLeasing.getText()).doubleValue());
			cell = thirdRow.createCell(10);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(offsetLeasing.getText()).doubleValue());


			// Процент ДО, БП ДО, Факт ДО, Остаток ДО, Откл. ДО
			Row forthRow = sheet.getRow(3);
			cell = forthRow.createCell(6);
			cell.setCellStyle(stylePercent);
			cell.setCellValue(FormatUtils.parseNumber(businessPlanWithoutLeasingPercent.getText().replace("%", "")).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP).doubleValue());
			cell = forthRow.createCell(7);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(businessPlanWithoutLeasing.getText()).doubleValue());
			cell = forthRow.createCell(8);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(factWithoutLeasing.getText()).doubleValue());
			cell = forthRow.createCell(9);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(remainsWithoutLeasing.getText()).doubleValue());
			cell = forthRow.createCell(10);
			cell.setCellStyle(styleNumber);
			cell.setCellValue(FormatUtils.parseNumber(offsetWithoutLeasing.getText()).doubleValue());


			// Основаная таблица
			for (int i = 0; i < table.getItems().size(); i++) {
				Row row = sheet.createRow(7 + i);
				cell = row.createCell(1);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(categoryCol.getCellData(i));

				cell = row.createCell(2);
				cell.setCellStyle(centerAlignment);
				cell.setCellValue(unitsCol.getCellData(i));

				cell = row.createCell(3);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(countPlanCol.getCellData(i)).doubleValue());

				cell = row.createCell(4);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(costPlanCol.getCellData(i)).doubleValue());

				cell = row.createCell(5);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(costWithTaxPlanCol.getCellData(i)).doubleValue());

				cell = row.createCell(6);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(countCol.getCellData(i)).doubleValue());

				cell = row.createCell(7);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(costCol.getCellData(i)).doubleValue());

				cell = row.createCell(8);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(costWithTaxCol.getCellData(i)).doubleValue());

				cell = row.createCell(9);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(countRemainsCol.getCellData(i)).doubleValue());

				cell = row.createCell(10);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(costRemainsCol.getCellData(i)).doubleValue());

				cell = row.createCell(11);
				cell.setCellStyle(styleBorderAllAroundNumber);
				cell.setCellValue(FormatUtils.parseNumber(costWithTaxRemainsCol.getCellData(i)).doubleValue());

				cell = row.createCell(12);
				cell.setCellStyle(styleBorderAllAroundPercent);
				cell.setCellValue(FormatUtils.parseNumber(percentCol.getCellData(i).replace("%", "")).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP).doubleValue());
			}

			saveFile(resultsFolder, PREFIX_FOR_SAVE_FILE_TEMPLATE, Integer.parseInt(year.getSelectionModel().getSelectedItem()), wb, tempFile);

		} catch (IOException | InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Метод для выгрузки всех категорий в файл шаблона xlsx для переноса в другую БД
	 *
	 * @param service       сервис для работы с репозиторием Категорий
	 * @param resultsFolder папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadCategoryToExcel(@NotNull CategoryService service, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_CATEGORY_TEMPLATE, CATEGORY_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);

			List<Category> allCategories = service.findAll();

			Row row;
			Cell cell;

			for (int i = 0; i < allCategories.size(); i++) {
				row = sheet.createRow(8 + i);
				cell = row.createCell(0);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allCategories.get(i).getId());
				cell = row.createCell(1);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allCategories.get(i).getName());
			}
			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_CATEGORIES, wb, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			Dialog.DialogBuilder.builder().title(WRONG_FORMAT).message(e.getMessage()).build().show();
		}
	}

	/**
	 * Метод для выгрузки всех кодов КСМ  в файл шаблона xlsx для переноса в другую БД
	 *
	 * @param service       сервис для работы с репозиторием Кодов КСМ
	 * @param resultsFolder папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadCodeKSMToExcel(@NotNull CodeKSMService service, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_CATEGORY_TEMPLATE, CATEGORY_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);

			List<CodeKSM> allCategories = service.findAll();

			Row row;
			Cell cell;

			for (int i = 0; i < allCategories.size(); i++) {
				row = sheet.createRow(8 + i);
				cell = row.createCell(0);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allCategories.get(i).getId());
				cell = row.createCell(1);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allCategories.get(i).getName());
			}
			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_CODES_KSM, wb, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			Dialog.DialogBuilder.builder().title(WRONG_FORMAT).message(e.getMessage()).build().show();
		}
	}

	/**
	 * Метод для выгрузки всех подкатегорий  в файл шаблона xlsx для переноса в другую БД
	 *
	 * @param service       сервис для работы с репозиторием Подкатегорий
	 * @param resultsFolder папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadSubcategoryToExcel(@NotNull SubcategoryService service, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_SUBCATEGORY_TEMPLATE, SUBCATEGORY_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);

			List<Subcategory> allSubcategories = service.findAll();

			Row row;
			Cell cell;

			for (int i = 0; i < allSubcategories.size(); i++) {
				row = sheet.createRow(8 + i);
				cell = row.createCell(0);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getId());
				cell = row.createCell(1);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getName());
				cell = row.createCell(2);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getCategory().getId());
				cell = row.createCell(3);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getCategory().getName());
			}
			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_SUBCATEGORIES, wb, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			Dialog.DialogBuilder.builder().title(WRONG_FORMAT).message(e.getMessage()).build().show();
		}
	}

	/**
	 * Метод для выгрузки всех ОЛ/ТТ в файл шаблона xlsx для переноса в другую БД
	 *
	 * @param service       сервис для работы с репозиторием ОЛ/ТТ
	 * @param resultsFolder папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadTRToExcel(@NotNull TechnicalRequirementService service, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_TR_TEMPLATE, TR_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);
			CellStyle styleBorderAllAroundDate = getStyleBorderAllAroundAndDateFormat(wb, PATTERN_FOR_SHORT_DATE);

			List<TechnicalRequirement> allSubcategories = service.findAll();

			Row row;
			Cell cell;

			for (int i = 0; i < allSubcategories.size(); i++) {
				row = sheet.createRow(8 + i);
				cell = row.createCell(0);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getId());
				cell = row.createCell(1);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getName());
				cell = row.createCell(2);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getCodeKSM());
				cell = row.createCell(3);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getCodeKSMName());
				cell = row.createCell(4);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getFileTRName());
				cell = row.createCell(5);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allSubcategories.get(i).getSystemNumberTransaction());
				cell = row.createCell(6);
				cell.setCellStyle(styleBorderAllAroundDate);
				cell.setCellValue(allSubcategories.get(i).getDateCreate());
			}
			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_TECHNICAL_REQUIREMENTS, wb, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			Dialog.DialogBuilder.builder().title(WRONG_FORMAT).message(e.getMessage()).build().show();
		}
	}

	/**
	 * Метод для выгрузки всей номенклатуры в файл шаблона xlsx для переноса в другую БД
	 *
	 * @param service       сервис для работы с репозиторием номенклатуры
	 * @param resultsFolder папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadNomenclatureToExcel(@NotNull NomenclatureService service, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_NOMENCLATURE_TEMPLATE, NOMENCLATURE_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);
			CellStyle styleBorderAllAroundDate = getStyleBorderAllAroundAndDateFormat(wb, PATTERN_FOR_SHORT_DATE);

			List<Nomenclature> allNomenclatures = service.findAll();

			Row row;
			Cell cell;

			for (int i = 0; i < allNomenclatures.size(); i++) {
				row = sheet.createRow(8 + i);
				cell = row.createCell(0);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getId());
				cell = row.createCell(1);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getName());
				cell = row.createCell(2);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getCodeKSM().getId());
				cell = row.createCell(3);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getCodeKSM().getName());
				cell = row.createCell(4);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getTechnicalRequirement().getId());
				cell = row.createCell(5);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getTechnicalRequirement().getName());
				cell = row.createCell(6);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getSubcategory().getId());
				cell = row.createCell(7);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(allNomenclatures.get(i).getSubcategory().getName());
			}
			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_NOMENCLATURES, wb, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			Dialog.DialogBuilder.builder().title(WRONG_FORMAT).message(e.getMessage()).build().show();
		}
	}

	/**
	 * Метод для выгрузки всех версий БП в файл шаблона xlsx для переноса в другую БД
	 *
	 * @param service       сервис для работы с репозиторием бизнес-плана
	 * @param resultsFolder папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadBPToExcel(@NotNull BusinessPlanService service, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_BP_TEMPLATE, BP_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);
			CellStyle styleBorderAllAroundNumber = getStyleBorderAllAroundAndNumberFormat(wb, PATTERN_FOR_NUMBERS);


			List<BusinessPlan> allBPlans = service.findAll();

			Row row;
			Cell cell;
			int currentRowIndex = 8; // строка с которой будет начато заполнение итогового файла

			for (BusinessPlan element : allBPlans) {
				Set<String> keys = element.getFirstYearCost().keySet();
				String[] keysArr = keys.toArray(new String[0]);
				for (int i = 0; i < keys.size(); i++) {
					row = sheet.createRow(currentRowIndex);
					String keyName = keysArr[i];

					cell = row.createCell(0);
					cell.setCellStyle(styleBorderAllAround);
					cell.setCellValue(element.getId());

					cell = row.createCell(1);
					cell.setCellStyle(styleBorderAllAround);
					cell.setCellValue(keyName);

					cell = row.createCell(2);
					cell.setCellStyle(styleBorderAllAround);
					cell.setCellValue(element.getFirstYear());

					cell = row.createCell(3);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getFirstYearCount().get(keyName).doubleValue());

					cell = row.createCell(4);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getFirstYearCost().get(keyName).doubleValue());

					cell = row.createCell(5);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getSecondYearCount().get(keyName).doubleValue());

					cell = row.createCell(6);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getSecondYearCost().get(keyName).doubleValue());

					cell = row.createCell(7);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getThirdYearCount().get(keyName).doubleValue());

					cell = row.createCell(8);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getThirdYearCost().get(keyName).doubleValue());

					cell = row.createCell(9);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getForthYearCount().get(keyName).doubleValue());

					cell = row.createCell(10);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getForthYearCost().get(keyName).doubleValue());

					cell = row.createCell(11);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getFifthYearCount().get(keyName).doubleValue());

					cell = row.createCell(12);
					cell.setCellStyle(styleBorderAllAroundNumber);
					cell.setCellValue(element.getFifthYearCost().get(keyName).doubleValue());

					currentRowIndex++;
				}
			}

			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_BUSINESS_PLANS, wb, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			Dialog.DialogBuilder.builder().title(WRONG_FORMAT).message(e.getMessage()).build().show();
		}
	}

	/**
	 * Метод для выгрузки отображенных на экране заявок в файл xlsx
	 *
	 * @param year                  комбо-бокс для выбора года на форме заявок
	 * @param period                комбо-бокс для выбора периода на форме заявок
	 * @param category              комбо-бокс для выбора категории на форме заявок
	 * @param allEquipment          радио-кнопка для проверки выбора всего оборудования
	 * @param onlyLeasing           радио-кнопка для проверки выбора только лизинга
	 * @param withoutLeasing        радио-кнопка для проверки выбора только без лизинга
	 * @param emptyCategory         чекбокс для проверки отображения пустых категорий
	 * @param onlyCreatedOrdering   чекбокс для проверки отображения только фактически созданных заявок
	 * @param onlyUncreatedOrdering чекбокс для проверки отображения только фактически не созданных заявок
	 * @param table                 таблица из которой происходит выгрузка
	 * @param resultsFolder         папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadOrderingToExcel(@NotNull ComboBox<String> year, @NotNull ComboBox<String> period, @NotNull ComboBox<String> category, @NotNull RadioButton allEquipment, RadioButton onlyLeasing, RadioButton withoutLeasing, CheckBox emptyCategory, CheckBox onlyCreatedOrdering, CheckBox onlyUncreatedOrdering, TableView<Ordering> table, String resultsFolder) {
		try {
			File tempFile = getTempFile(TEMP_DETAILS_TEMPLATE, DETAILS_TEMPLATE);

			OPCPackage pkg = OPCPackage.open(tempFile);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE);

			CellStyle centerAlignment = getStyleCenterAlignmentWithBorder(wb);
			CellStyle styleBorderAllAround = getStyleBorderAllAround(wb);
			CellStyle styleBorderAllAroundNumber = getStyleBorderAllAroundAndNumberFormat(wb, PATTERN_FOR_NUMBERS);
			CellStyle styleBorderAllAroundDate = getStyleBorderAllAroundAndDateFormat(wb, PATTERN_FOR_SHORT_DATE);


			// Год
			Row row = sheet.getRow(0);
			row.createCell(2).setCellValue(year.getSelectionModel().getSelectedItem());
			// Период
			row = sheet.getRow(1);
			row.createCell(2).setCellValue(period.getSelectionModel().getSelectedItem());
			// Категория
			row = sheet.getRow(2);
			row.createCell(2).setCellValue(category.getSelectionModel().getSelectedItem());

			// Группа оборудования
			row = sheet.getRow(3);
			if (allEquipment.isSelected()) {
				row.createCell(2).setCellValue(allEquipment.getText());
			} else if (onlyLeasing.isSelected()) {
				row.createCell(2).setCellValue(onlyLeasing.getText());
			} else if (withoutLeasing.isSelected()) {
				row.createCell(2).setCellValue(withoutLeasing.getText());
			} else {
				row.createCell(2).setCellValue(UNDEFINED_EQUIPMENT_GROUP);
			}

			// Отображение пустых заявок
			row = sheet.getRow(4);
			if (emptyCategory.isSelected()) {
				row.createCell(2).setCellValue(YES);
			} else {
				row.createCell(2).setCellValue(NO);
			}

			// Отображение только фактически переданных заявок (с номерами заявок)
			row = sheet.getRow(5);
			if (onlyCreatedOrdering.isSelected()) {
				row.createCell(2).setCellValue(ONLY_CREATED_ORDERINGS);
			} else if (onlyUncreatedOrdering.isSelected()) {
				row.createCell(2).setCellValue(ONLY_UNCREATED_ORDERINGS);
			} else {
				row.createCell(2).setCellValue(ALL_ORDERINGS);
			}

			// Основаная таблица
			for (int i = 0; i < table.getItems().size(); i++) {
				row = sheet.createRow(8 + i);
				Cell cell = row.createCell(0);
				cell.setCellStyle(styleBorderAllAround);
				cell.setCellValue(Double.parseDouble(String.valueOf(table.getItems().get(i).getId())));
				for (int j = 0; j < table.getColumns().size(); j++) {
					if (table.getColumns().get(j).getCellData(i) != null) {
						switch (j) {
							case 0, 1, 2, 3, 4, 14:
								cell = row.createCell(j + 1);
								cell.setCellStyle(styleBorderAllAround);
								cell.setCellValue(table.getColumns().get(j).getCellData(i).toString());
								break;
							case 5:
								cell = row.createCell(j + 1);
								cell.setCellStyle(centerAlignment);
								cell.setCellValue(table.getColumns().get(j).getCellData(i).toString());
								break;
							case 6, 7, 8, 9, 10:
								cell = row.createCell(j + 1);
								cell.setCellStyle(styleBorderAllAroundNumber);
								cell.setCellValue(FormatUtils.parseNumber(table.getColumns().get(j).getCellData(i).toString()).doubleValue());
								break;
							case 11:
								cell = row.createCell(j + 1);
								cell.setCellStyle(styleBorderAllAroundDate);
								cell.setCellValue(table.getColumns().get(j).getCellData(i).toString());
								break;
							case 12, 13:
								cell = row.createCell(j + 1);
								cell.setCellStyle(centerAlignment);
								cell.setCellValue(table.getColumns().get(j).getCellData(i).toString());
								break;
						}
					} else {
						cell = row.createCell(j + 1);
						cell.setCellStyle(styleBorderAllAround);
						cell.setCellValue("");
					}
				}
			}

			saveFile(resultsFolder, FILENAME_FOR_UNLOAD_ORDERINGS, Integer.parseInt(year.getSelectionModel().getSelectedItem()), wb, tempFile);
		} catch (IOException | InvalidFormatException e) {
			throw new RuntimeException(e);
		}


	}

	/**
	 * Метод для загрузки заявок из файла xlsx
	 *
	 * @param file                файл из которого будет осуществлена загрузка
	 * @param nomenclatureService сервис для работы с репозиторием номенклатуры
	 * @param orderingService     сервис для работы с репозиторием заявок
	 * @param unitsService        сервис для работы с репозиторием единиц измерения
	 */
	public static void uploadOrderingsFromExcel(File file, NomenclatureService nomenclatureService, OrderingService orderingService, UnitsService unitsService) {

		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				Ordering ordering;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					ordering = new Ordering();
				} else {
					try {
						ordering = orderingService.findById(Long.parseLong(getCellText(row.getCell(0)).split("\\.")[0]));
					} catch (NoSuchElementException e) {
						ordering = new Ordering();
					}
				}

				try {
					Nomenclature nomenclature;
					if (getCellText(row.getCell(5)) == null || getCellText(row.getCell(5)).equals("")) {
						nomenclature = nomenclatureService.findById(getCellText(row.getCell(3)));
					} else {
						nomenclature = nomenclatureService.findById(getCellText(row.getCell(3)) + "_" + getCellText(row.getCell(5)));
					}
					ordering.setNomenclature(nomenclature);
					ordering.setNumber(getCellText(row.getCell(1)).split("\\.")[0]);
					ordering.setPosition(getCellText(row.getCell(2)).split("\\.")[0]);
					ordering.setUnit(unitsService.findByName(getCellText(row.getCell(6))));
					ordering.setCount(BigDecimal.valueOf(Double.parseDouble(getCellText(row.getCell(7)))));
					ordering.setPrice(BigDecimal.valueOf(Double.parseDouble(getCellText(row.getCell(8)))));
					ordering.setCost(ordering.getCount().multiply(ordering.getPrice()));
					try {
						ordering.setDate(LocalDate.parse(getCellText(row.getCell(12)), dtf));
					} catch (DateTimeParseException e) {
						ordering.setDate(row.getCell(12).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}
					ordering.setRequiresInstallation(getCellText(row.getCell(13)).equals(YES));
					ordering.setLeasing(getCellText(row.getCell(14)).equals(YES));
					ordering.setRemark(getCellText(row.getCell(15)));

				} catch (NoSuchElementException e) {
					notFoundNomenclature(rowNum, row);
					continue;
				}
				orderingService.save(ordering);
			}
			wb.close();

			// Логирование ошибок загрузки
			if (ERROR_SET.size() > 0) {
				Path fileErrorPath = Path.of(FILENAME_FOR_ERROR_LOG + LocalDate.now().format(dtf) + ".log");
				File fileError;
				if (!Files.exists(fileErrorPath)) {
					fileError = Files.createFile(fileErrorPath).toFile();
				} else {
					fileError = new File(String.valueOf(fileErrorPath));
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(fileError));
				for (String s : ERROR_SET) {
					bw.write(s);
				}
				bw.close();

				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + fileErrorPath);


				Dialog.DialogBuilder.builder().title(ERROR_UPLOADING_FILE).message(ERROR_UPLOADING_FILE_MESSAGE + ERROR_COUNT + ".").build().show();

				ERROR_COUNT = 0;
				ERROR_SET.clear();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Метод для загрузки версий БП из файла xlsx
	 *
	 * @param file                файл из которого будет осуществлена загрузка
	 * @param businessPlanService сервис для работы с репозиторием бизнес-плана
	 */
	public static void uploadBPFromExcel(File file, BusinessPlanService businessPlanService) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				BusinessPlan businessPlan;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					businessPlan = new BusinessPlan();
					businessPlan.setId(getCellText(row.getCell(0)));
					businessPlan.setFirstYear(Integer.parseInt(getCellText(row.getCell(2)).split("\\.")[0]));
				} else {
					try {
						businessPlan = businessPlanService.findById(getCellText(row.getCell(0)));
					} catch (NoSuchElementException e) {
						businessPlan = new BusinessPlan();
						businessPlan.setId(getCellText(row.getCell(0)));
						businessPlan.setFirstYear(Integer.parseInt(getCellText(row.getCell(2)).split("\\.")[0]));
					}
				}

				businessPlan.getFirstYearCount().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(3))));
				businessPlan.getFirstYearCost().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(4))));
				businessPlan.getSecondYearCount().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(5))));
				businessPlan.getSecondYearCost().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(6))));
				businessPlan.getThirdYearCount().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(7))));
				businessPlan.getThirdYearCost().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(8))));
				businessPlan.getForthYearCount().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(9))));
				businessPlan.getForthYearCost().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(10))));
				businessPlan.getFifthYearCount().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(11))));
				businessPlan.getFifthYearCost().put(getCellText(row.getCell(1)), FormatUtils.parseNumber(getCellText(row.getCell(12))));

				businessPlanService.save(businessPlan);
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	public static void uploadCategoryFromExcel(File file, CategoryService categoryService) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				Category category;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					category = new Category();
				} else {
					try {
						category = categoryService.findById(Integer.parseInt(getCellText(row.getCell(0)).split("\\.")[0]));
					} catch (NoSuchElementException e) {
						category = new Category();
						category.setId(Integer.parseInt(getCellText(row.getCell(0)).split("\\.")[0]));
					}
					category.setName(getCellText(row.getCell(1)));
				}
				categoryService.save(category);
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	public static void uploadSubcategoryFromExcel(File file, SubcategoryService subcategoryService, CategoryService categoryService) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				Subcategory subcategory;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					subcategory = new Subcategory();
				} else {
					try {
						subcategory = subcategoryService.findById(Integer.parseInt(getCellText(row.getCell(0)).split("\\.")[0]));
					} catch (NoSuchElementException e) {
						subcategory = new Subcategory();
						subcategory.setId(Integer.parseInt(getCellText(row.getCell(0)).split("\\.")[0]));
					}
					subcategory.setName(getCellText(row.getCell(1)));
					subcategory.setCategory(categoryService.findById(Integer.parseInt(getCellText(row.getCell(2)).split("\\.")[0])));
				}
				subcategoryService.save(subcategory);
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	public static void uploadCodeKSMFromExcel(File file, CodeKSMService codeKSMService) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				CodeKSM codeKSM;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					codeKSM = new CodeKSM();
				} else {
					try {
						codeKSM = codeKSMService.findById(getCellText(row.getCell(0)));
					} catch (NoSuchElementException e) {
						codeKSM = new CodeKSM();
						codeKSM.setId(getCellText(row.getCell(0)));
					}
					codeKSM.setName(getCellText(row.getCell(1)));
				}
				codeKSMService.save(codeKSM);
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	public static void uploadTechnicalRequirementFromExcel(File file, TechnicalRequirementService technicalRequirementService) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				TechnicalRequirement technicalRequirement;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					technicalRequirement = new TechnicalRequirement();
				} else {
					try {
						technicalRequirement = technicalRequirementService.findById(getCellText(row.getCell(0)));
					} catch (NoSuchElementException e) {
						technicalRequirement = new TechnicalRequirement();
						technicalRequirement.setId(getCellText(row.getCell(0)));
					}
					technicalRequirement.setName(getCellText(row.getCell(1)));
					technicalRequirement.setCodeKSM(getCellText(row.getCell(2)));
					technicalRequirement.setCodeKSMName(getCellText(row.getCell(3)));
					technicalRequirement.setFileTRName(getCellText(row.getCell(4)));
					technicalRequirement.setSystemNumberTransaction(getCellText(row.getCell(5)));
					try {
						technicalRequirement.setDateCreate(LocalDate.parse(getCellText(row.getCell(6)), dtf));
					} catch (DateTimeParseException e) {
						technicalRequirement.setDateCreate(row.getCell(6).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}
				}
				technicalRequirementService.save(technicalRequirement);
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	public static void uploadNomenclatureFromExcel(File file, NomenclatureService nomenclatureService,
												   CodeKSMService codeKSMService,
												   TechnicalRequirementService technicalRequirementService,
												   SubcategoryService subcategoryService) {
		try {
			OPCPackage pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = 8;
			int rowEnd = sheet.getLastRowNum();

			for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) continue;

				Nomenclature nomenclature;
				if (getCellText(row.getCell(0)).equals("") || getCellText(row.getCell(0)) == null) {
					if (getCellText(row.getCell(4)).equals("") || getCellText(row.getCell(4)) == null) {
						nomenclature = new Nomenclature(codeKSMService.findById(getCellText(row.getCell(2))));
					} else {
						nomenclature = new Nomenclature(codeKSMService.findById(getCellText(row.getCell(2))),
								technicalRequirementService.findById(getCellText(row.getCell(4))));
					}
				} else {
					try {
						nomenclature = nomenclatureService.findById(getCellText(row.getCell(0)));
					} catch (NoSuchElementException e) {
						if (getCellText(row.getCell(4)).equals("") || getCellText(row.getCell(4)) == null) {
							nomenclature = new Nomenclature(codeKSMService.findById(getCellText(row.getCell(2))));
						} else {
							nomenclature = new Nomenclature(codeKSMService.findById(getCellText(row.getCell(2))),
									technicalRequirementService.findById(getCellText(row.getCell(4))));
						}
					}
					nomenclature.setCodeKSM(codeKSMService.findById(getCellText(row.getCell(2))));
					if (getCellText(row.getCell(4)).equals("") || getCellText(row.getCell(4)) == null) {
						nomenclature.setTechnicalRequirement(technicalRequirementService.findById(""));
					} else {
						nomenclature.setTechnicalRequirement(technicalRequirementService.findById(getCellText(row.getCell(4))));
					}
					nomenclature.setName(nomenclature.getCodeKSM().getName());
					nomenclature.setSubcategory(subcategoryService.findById(Integer.parseInt(getCellText(row.getCell(6)).split("\\.")[0])));

				}
				nomenclatureService.save(nomenclature);
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Метод для выгрузки отображенных на экране позиций заявок
	 *
	 * @param year               комбо-бокс для выбора года поставки
	 * @param subcategoryService сервис для работы с репозиторием подкатегорий
	 * @param table              таблица из которой будет осуществляться выгрузку
	 * @param resultsFolder      папка, в которую будет сохранен файл выгрузки
	 */
	public static void unloadToOrderingTemplate(ComboBox<String> year, @NotNull SubcategoryService subcategoryService, @NotNull TableView<Ordering> table, String resultsFolder) {
		try {
			// Получение списка всех подкатегорий (Subcategory)
			List<Subcategory> subcategoryList = subcategoryService.findAll();

			// Получение общего списка Ordering, для которого необходимо создать шаблоны заявок
			List<Ordering> orderingList = table.getItems();

			// Временный список
			List<Ordering> tempList = new ArrayList<>();

			// Формируем рабочий список
			for (Subcategory subcategory : subcategoryList) {
				for (Ordering ordering : orderingList) {
					if (ordering.getNomenclature().getSubcategory().getName().equals(subcategory.getName())) {
						tempList.add(ordering);
					}
				}
				// При наличии номенклатуры текущей подкатегории (Subcategory) формируем шаблон загрузки
				if (tempList.size() > 0) {
					File tempFile = getTempFile(TEMP_ORDERING_TEMPLATE, ORDERING_TEMPLATE);

					OPCPackage pkg = OPCPackage.open(tempFile);
					XSSFWorkbook wb = new XSSFWorkbook(pkg);

					Row row;
					Cell cell;
					// Основной лист шаблона заявки
					Sheet sheet = wb.getSheet(NAME_SHEET_FOR_FILE_TEMPLATE_FOR_SAP);
					for (int i = 0; i < tempList.size(); i++) {
						int position = 10;
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						// ID заявки
						cell = row.createCell(0);
						cell.setCellValue(1);
						// вид заявки
						cell = row.createCell(1);
						cell.setCellValue(TYPE_OF_ORDERING_ID);
						// ID позиции
						cell = row.createCell(2);
						cell.setCellValue(position + i * 10);
						// Тип контировки
						cell = row.createCell(3);
						cell.setCellValue(TYPE_OF_CONTENTION_ID);
						// Тип позиции
						cell = row.createCell(4);
						cell.setCellValue(TYPE_OF_POSITION_ID);
						// Завод
						cell = row.createCell(5);
						cell.setCellValue(FACTORY_ID);
						// Склад
						cell = row.createCell(6);
						cell.setCellValue(STORAGE_ID);
						// Номер материала
						cell = row.createCell(7);
						cell.setCellValue(tempList.get(i).getNomenclature().getCodeKSM().getId());
						// Краткий текст материала
						cell = row.createCell(8);
						cell.setCellValue(tempList.get(i).getNomenclature().getCodeKSM().getName());
						// Количество
						cell = row.createCell(9);
						cell.setCellValue(tempList.get(i).getCount().doubleValue());
						// ЕИ
						cell = row.createCell(10);
						cell.setCellValue(tempList.get(i).getUnit().getName());
						// Группа закупок
						cell = row.createCell(12);
						cell.setCellValue(PURCHASING_GROUP_ID);
						// Закупочная организация
						cell = row.createCell(13);
						cell.setCellValue(PURCHASING_ORGANIZATION_ID);
						// Завод-постащик
						cell = row.createCell(14);
						cell.setCellValue(SUPPLIER_FACTORY_ID);
						// Отпускающий склад
						cell = row.createCell(15);
						cell.setCellValue(RELEASING_STORAGE_ID);
						// Дата поставки
						cell = row.createCell(16);
						cell.setCellValue(tempList.get(i).getDate().format(dtf));
						// ПФМ
						cell = row.createCell(29);
						cell.setCellValue(MANAGER_ID);
						// ФП
						cell = row.createCell(30);
						cell.setCellValue(FINANCIAL_POSITION_ID);
						// Вид деятельности
						cell = row.createCell(43);
						cell.setCellValue(TYPE_OF_ACTIVITY_ID);
						// Подразделение-заказчик
						cell = row.createCell(48);
						cell.setCellValue(CUSTOMER_DIVISION_ID);
						// Дата вовлечения
						cell = row.createCell(52);
						cell.setCellValue(tempList.get(i).getDate().plusDays(30).format(dtf));
						// Признак Сопутств.Услуг
						if (tempList.get(i).isRequiresInstallation()) {
							cell = row.createCell(56);
							cell.setCellValue(SIGN_OF_RELATED_SERVICES);
						}
						// Плановая цена
						cell = row.createCell(61);
						cell.setCellValue(tempList.get(i).getPrice().doubleValue());
						// Сегмент запаса
						cell = row.createCell(65);
						cell.setCellValue(tempList.get(i).getNomenclature().getTechnicalRequirement().getId());
						// Адрес поставки
						cell = row.createCell(66);
						cell.setCellValue(DELIVERY_ADDRESS_ID);

						if (tempList.get(i).isRequiresInstallation()) {
							// ШМР
							setRequiresInstallationRow(sheet, position, i, 1, CODE_KSM_INSTALLATION_SUPERVISION, SHORT_NAME_CODE_KSM_INSTALLATION_SUPERVISION, tempList);

							// ПНР
							setRequiresInstallationRow(sheet, position, i, 2, CODE_KSM_COMMISSIONING_WORKS, SHORT_NAME_CODE_KSM_COMMISSIONING_WORKS, tempList);
						}
					}
					// Лист "Обоснование плановой стоимости"
					sheet = wb.getSheet(NAME_SHEET_PLANNED_COST_FOR_FILE_TEMPLATE_FOR_SAP);
					for (int i = 0; i < tempList.size(); i++) {
						int position = 10;
						setSecondSheetAttribute(sheet, position + i * 10);

						if (tempList.get(i).isRequiresInstallation()) {
							// ШМР
							setSecondSheetAttribute(sheet, position + i * 10 + 1);

							// ПНР
							setSecondSheetAttribute(sheet, position + i * 10 + 2);
						}
					}

					String resultFolderForTemplates = resultsFolder + "\\" + subcategory.getName();
					Path path = Path.of(resultFolderForTemplates);
					if (!Files.exists(path)) {
						Files.createDirectory(path);
					}

					saveFileTemplates(resultFolderForTemplates, FILENAME_FOR_TEMPLATE_SAP, year, subcategory, wb, tempFile);
					tempList.clear();
					wb.close();
					pkg.close();
				}
			}
		} catch (IOException | InvalidFormatException e) {
			throw new RuntimeException(e);
		}

		Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(FORMATION_TEMPLATES_COMPLETED_SUCCESSFULLY).build().show();
	}

	/**
	 * Метод для создания и заполнения строк в шаблоне загрузки для ШМР и ПНР
	 *
	 * @param sheet              лист на котором необходимо создать и заполнить строки
	 * @param position           стартовая позиция для порядкового номера (кратно 10)
	 * @param i                  итеррация цикла
	 * @param offset             смещение порядкового номера для ШМР и ПНР (1 и 2 соответственно)
	 * @param codeKSMId          номер КСМ
	 * @param shortNameEquipment краткое наименование МТР в соответствии с КСМ
	 * @param tempList           рабочий лист с Ordering (заявками) текущей Subcategory (подкатегории)
	 */
	private static void setRequiresInstallationRow(@NotNull Sheet sheet, int position, int i, int offset, String codeKSMId, String shortNameEquipment, @NotNull List<Ordering> tempList) {
		Row row;
		Cell cell;
		row = sheet.createRow(sheet.getLastRowNum() + 1);
		// ID заявки
		cell = row.createCell(0);
		cell.setCellValue(1);
		// вид заявки
		cell = row.createCell(1);
		cell.setCellValue(TYPE_OF_ORDERING_ID);
		// ID позиции
		cell = row.createCell(2);
		cell.setCellValue(position + i * 10 + offset);
		// Тип контировки
		cell = row.createCell(3);
		cell.setCellValue(TYPE_OF_CONTENTION_FOR_RELATED_SERVICES_ID);
		// Завод
		cell = row.createCell(5);
		cell.setCellValue(FACTORY_ID);
		// Склад
		cell = row.createCell(6);
		cell.setCellValue(STORAGE_ID);
		// Номер материала
		cell = row.createCell(7);
		cell.setCellValue(codeKSMId);
		// Краткий текст материала
		cell = row.createCell(8);
		cell.setCellValue(shortNameEquipment);
		// Количество
		cell = row.createCell(9);
		cell.setCellValue(tempList.get(i).getCount().doubleValue());
		// Группа закупок
		cell = row.createCell(12);
		cell.setCellValue(PURCHASING_GROUP_ID);
		// Закупочная организация
		cell = row.createCell(13);
		cell.setCellValue(PURCHASING_ORGANIZATION_ID);
		// Дата поставки
		cell = row.createCell(16);
		cell.setCellValue(tempList.get(i).getDate().format(dtf));
		// МВЗ
		cell = row.createCell(25);
		cell.setCellValue(PLACE_OF_OCCURRENCE_OF_COSTS_ID);
		// ПФМ
		cell = row.createCell(29);
		cell.setCellValue(MANAGER_ID);
		// ФП
		cell = row.createCell(30);
		cell.setCellValue(FINANCIAL_POSITION_ID);
		// Вид деятельности
		cell = row.createCell(43);
		cell.setCellValue(TYPE_OF_ACTIVITY_ID);
		// Подразделение-заказчик
		cell = row.createCell(48);
		cell.setCellValue(CUSTOMER_DIVISION_ID);
		// Дата вовлечения
		cell = row.createCell(52);
		cell.setCellValue(tempList.get(i).getDate().plusDays(30).format(dtf));
		// Номер заявки на закупку сопутствующей услуги
		cell = row.createCell(54);
		cell.setCellValue(1);
		// Позиция заявки на закупку сопутствующей услуги
		cell = row.createCell(55);
		cell.setCellValue(position + i * 10);
		// Адрес поставки
		cell = row.createCell(66);
		cell.setCellValue(DELIVERY_ADDRESS_ID);
	}

	/**
	 * Метод для заполнения аналитик листа "Обоснование плановой стоимости"
	 *
	 * @param sheet    лист на котором необходимо создать и заполнить строки
	 * @param position позиция заявке (кратно 10, для ШМР и ПНР +1 и +2 соответственно). Пример: position + i * 10, где position - стартовая позиция (обычно 10), i - итеррация цикла
	 */
	private static void setSecondSheetAttribute(@NotNull Sheet sheet, int position) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		// ID заявки
		Cell cell = row.createCell(0);
		cell.setCellValue(1);
		// ID позиции
		cell = row.createCell(1);
		cell.setCellValue(position);
		// Идентификатор текста
		cell = row.createCell(2);
		cell.setCellValue(TEXT_ID);
		// Строка текста
		cell = row.createCell(3);
		cell.setCellValue(1);
	}

	/**
	 * Метод для извлечения строкового предстваления содержимого ячейки в зависимости от типа данных в ячейке
	 *
	 * @param cell ячейка, из которой извлекаются данные
	 * @return строковое предстваление данных из указанной ячейки
	 */
	private static String getCellText(@NotNull Cell cell) {
		String result = "";
		switch (cell.getCellType()) {
			case STRING -> result = cell.getRichStringCellValue().getString();
			case NUMERIC -> {
				if (DateUtil.isCellDateFormatted(cell)) {
					result = String.valueOf(cell.getDateCellValue());
				} else {
					result = String.valueOf(cell.getNumericCellValue());
				}
			}
			case BOOLEAN -> result = String.valueOf(cell.getBooleanCellValue());
			case FORMULA -> result = cell.getCellFormula();
			case BLANK -> result = "";
		}
		return result;
	}

	/**
	 * Сохранение результата выгрузки в файл xlsx
	 *
	 * @param resultsFolder папка в которую будет сохранен результат выгрузки (передается из DirectoryChooser)
	 * @param x             префикс наименования файла (например: "\Заявки ОНВСС на " или "\Передача потребности на ")
	 * @param year          год, к которому относится выгрузка
	 * @param wb            рабочая книга Excel, с которой происходит действие
	 * @param tempFile      временная копия файла шаблона (нужна для предотвращения порчи исходного шаблона)
	 */
	private static void saveFile(String resultsFolder, String x, int year, @NotNull XSSFWorkbook wb, File tempFile) throws IOException {
		String resultFile = resultsFolder + x + year + FILENAME_SUFFIX_YEAR_FOR_TEMPLATE_SAP + LocalDate.now().format(dtf) + ".xlsx";

		try (OutputStream fileOut = new FileOutputStream(resultFile)) {
			wb.write(fileOut);
		}
		wb.close();


		Files.delete(tempFile.toPath());
		Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + Path.of(resultFile));
	}

	/**
	 * Сохранение результата вырузки в файл xlsx без использования года поставки и текуще даты (преимущественно используется для выгрузки данных при миграции в другую БД)
	 *
	 * @param resultsFolder папка в которую будет сохранен результат выгрузки (передается из DirectoryChooser)
	 * @param x             префикс наименования файла (например: "\Заявки ОНВСС на " или "\Передача потребности на ")
	 * @param wb            рабочая книга Excel, с которой происходит действие
	 * @param tempFile      временная копия файла шаблона (нужна для предотвращения порчи исходного шаблона)
	 */
	private static void saveFile(String resultsFolder, String x, @NotNull XSSFWorkbook wb, File tempFile) throws IOException {
		String resultFile = resultsFolder + x + ".xlsx";

		try (OutputStream fileOut = new FileOutputStream(resultFile)) {
			wb.write(fileOut);
		}
		wb.close();

		Files.delete(tempFile.toPath());
		Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + Path.of(resultFile));
	}

	/**
	 * Сохранение результата вырузки в файл xlsx без использования года поставки и текуще даты и без открытия файла (преимущественно используется для выгрузки данных при миграции в другую БД)
	 *
	 * @param resultsFolder папка в которую будет сохранен результат выгрузки (передается из DirectoryChooser)
	 * @param x             префикс наименования файла (например: "\Заявки ОНВСС на " или "\Передача потребности на ")
	 * @param wb            рабочая книга Excel, с которой происходит действие
	 * @param tempFile      временная копия файла шаблона (нужна для предотвращения порчи исходного шаблона)
	 */
	private static void saveFileWithoutOpen(String resultsFolder, String x, @NotNull XSSFWorkbook wb, File tempFile) throws IOException {
		String resultFile = resultsFolder + x + ".xlsx";

		try (OutputStream fileOut = new FileOutputStream(resultFile)) {
			wb.write(fileOut);
		}
		wb.close();

		Files.delete(tempFile.toPath());
	}


	/**
	 * Сохранение шаблона выгрузки в файл xlsx
	 *
	 * @param resultsFolder папка в которую будет сохранен результат выгрузки (передается из DirectoryChooser)
	 * @param x             префикс наименования файла (например: "\\Шаблон загрузки на ")
	 * @param year          год шаблона загрузки
	 * @param subcategory   текущая подкатегория, для которой формируется шаблон загрузки
	 * @param wb            рабочая книга Excel, с которой происходит действие
	 * @param tempFile      временная копия файла шаблона (нужна для предотвращения порчи исходного шаблона)
	 */
	private static void saveFileTemplates(String resultsFolder, String x, @NotNull ComboBox<String> year,
										  @NotNull Subcategory subcategory, @NotNull XSSFWorkbook wb,
										  File tempFile) throws IOException {
		String resultFile = resultsFolder + x + year.getSelectionModel().getSelectedItem()
				+ FILENAME_SUFFIX_ACCESSORY_FOR_TEMPLATE_SAP + subcategory.getName()
				+ " " + LocalDate.now().format(dtf) + ".xlsx";

		try (OutputStream fileOut = new FileOutputStream(resultFile)) {
			wb.write(fileOut);
		}
		wb.close();

		Files.delete(tempFile.toPath());
	}

	/**
	 * Метод для создания временного файла шаблона. Необходим для предотвращения порчи исходного шаблона
	 *
	 * @param tempFileTemplate адрес создания временного файла шаблона
	 * @param fileTemplate     адрес нахождения исходного шаблона
	 * @return временный файл шаблона
	 */
	private static File getTempFile(String tempFileTemplate, String fileTemplate) throws IOException {
		Path tempFilePath = Path.of(tempFileTemplate);
		if (Files.exists(tempFilePath)) {
			Files.delete(tempFilePath);
		}
		return Files.copy(Path.of(fileTemplate), tempFilePath).toFile();
	}

	/**
	 * Метод для формирования попозиционного лога ошибок загрузки потребности
	 *
	 * @param rowNum итеррация цикла
	 * @param row    строка с которой происходит действие
	 */
	private static void notFoundNomenclature(int rowNum, @NotNull Row row) {
		String temp = String.format(STRING_FORMAT_FOR_LOGGING_ERRORS,
				(rowNum + 1), getCellText(row.getCell(1)).split("\\.")[0],
				getCellText(row.getCell(2)).split("\\.")[0],
				getCellText(row.getCell(3)),
				getCellText(row.getCell(4)),
				getCellText(row.getCell(5)),
				getCellText(row.getCell(6)),
				getCellText(row.getCell(7)),
				getCellText(row.getCell(8)),
				row.getCell(12).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(dtf),
				getCellText(row.getCell(13)),
				getCellText(row.getCell(14)),
				getCellText(row.getCell(15)));
		ERROR_COUNT++;
		ERROR_SET.add(temp);

	}

	private static CellStyle getStyleBorderAllAroundAndNumberFormat(@NotNull XSSFWorkbook wb, String format) {
		CellStyle styleBorderAllAroundPercent = wb.createCellStyle();
		styleBorderAllAroundPercent.setDataFormat(wb.createDataFormat().getFormat(format));
		setAllAroundBorderStyle(styleBorderAllAroundPercent);
		return styleBorderAllAroundPercent;
	}



	private static CellStyle getStyleBorderAllAround(@NotNull XSSFWorkbook wb) {
		CellStyle styleBorderAllAround = wb.createCellStyle();
		setAllAroundBorderStyle(styleBorderAllAround);
		return styleBorderAllAround;
	}

	private static CellStyle getStyleCenterAlignmentWithBorder(@NotNull XSSFWorkbook wb) {
		CellStyle centerAlignment = wb.createCellStyle();
		centerAlignment.setAlignment(HorizontalAlignment.CENTER);
		setAllAroundBorderStyle(centerAlignment);
		return centerAlignment;
	}

	private static  CellStyle getStyleOnlyNumber(@NotNull XSSFWorkbook wb, String format) {
		CellStyle stylePercent = wb.createCellStyle();
		stylePercent.setDataFormat(wb.createDataFormat().getFormat(format));
		return stylePercent;
	}

	private static  CellStyle getStyleBorderAllAroundAndDateFormat(@NotNull XSSFWorkbook wb, String format) {
		CellStyle styleBorderAllAroundDate = wb.createCellStyle();
		setAllAroundBorderStyle(styleBorderAllAroundDate);
		CreationHelper createHelper = wb.getCreationHelper();
		styleBorderAllAroundDate.setDataFormat(createHelper.createDataFormat().getFormat(format));
		return styleBorderAllAroundDate;
	}


	public static FileChooser getFileChooser(String fileName) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(FILE_CHOOSER_WINDOW_TITLE_PREFIX + fileName + FILE_CHOOSER_WINDOW_TITLE_SUFFIX);
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл Excel 2007+", "*.xlsx"));
		return fileChooser;
	}

	private static void setAllAroundBorderStyle(CellStyle cellStyle) {
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	}
}
