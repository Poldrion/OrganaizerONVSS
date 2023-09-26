package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.Category;
import organizer.model.entities.Ordering;
import organizer.model.services.*;
import organizer.utils.*;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.OrderingEdit;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static organizer.utils.Constants.*;
import static organizer.utils.ExcelUtils.*;
import static organizer.utils.FormatUtils.formatNumber;

@Controller
public class DetailsController {

	@Autowired
	private NomenclatureService nomenclatureService;
	@Autowired
	private OrderingService orderingService;
	@Autowired
	private UnitsService unitsService;
	@Autowired
	private SubcategoryService subcategoryService;
	@Autowired
	private CategoryService categoryService;

	private List<Category> categories;
	private final List<Ordering> orderingsForCurrentYear = new ArrayList<>();
	private LocalDate dateStart;
	private LocalDate dateEnd;

	@FXML
	private TextField search;
	@FXML
	private TextField presummCount;
	@FXML
	private TextField presummCostWithoutTax;
	@FXML
	private TextField presummCostWithTax;
	@FXML
	private CheckBox emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering;
	@FXML
	private RadioButton allEquipment, onlyLeasing, withoutLeasing;
	@FXML
	private ComboBox<String> period, category;
	@FXML
	private ComboBox<String> year;
	@FXML
	private TableView<Ordering> orderingTableView;
	@FXML
	private TableColumn<Ordering, String> numberCol;
	@FXML
	private TableColumn<Ordering, String> countCol;
	@FXML
	private TableColumn<Ordering, String> positionCol;
	@FXML
	private TableColumn<Ordering, String> priceCol, priceWithTaxCol, costCol, costWithTaxCol;
	@FXML
	private TableColumn<Ordering, String> codeKSMCol, nameCol, technicalRequirementCol, unitCol, dateCol, requiresInstallationCol, isLeasingCol, remarkCol;


	@FXML
	private void initialize() {
		categories = categoryService.findAll();

		year.getItems().addAll(CurrentData.YEARS);
		year.getSelectionModel().select(String.valueOf(CurrentData.CURRENT_YEAR));
		period.getItems().addAll(Constants.PERIOD);
		period.getSelectionModel().selectFirst();
		category.getItems().addAll(ALL_CATEGORIES);
		for (Category c : categories) {
			category.getItems().add(c.getName());
		}
		category.getSelectionModel().selectFirst();

		settingsDateFirstAndLastDayForCurrentYear();
		settingsOrderingTableView();
		settingsListenersForElement();
		initOrderingTable();
	}

	private void initOrderingTable() {
		orderingsForCurrentYear.clear();
		settingsDateFirstAndLastDayForCurrentYear();
		orderingsForCurrentYear.addAll(orderingService.findByYear(dateStart, dateEnd));
		reload();
		getPresumm();
	}

	/**
	 * Метод для установки первого и последнего дней в выбранном году для выгрузки из БД
	 */
	private void settingsDateFirstAndLastDayForCurrentYear() {
		dateStart = LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), 1, LocalDate.MIN.getDayOfMonth());
		dateEnd = LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), 12, LocalDate.MAX.getDayOfMonth());
	}

	private void reload() {
		orderingTableView.getItems().clear();

		Predicate<Ordering> codeKSM = ordering -> ordering.getNomenclature().getCodeKSM().getId().toLowerCase().contains(search.getText().toLowerCase());
		Predicate<Ordering> codeKSMName = ordering -> ordering.getNomenclature().getCodeKSM().getName().toLowerCase().contains(search.getText().toLowerCase());
		Predicate<Ordering> number = ordering -> ordering.getNumber().toLowerCase().contains(search.getText().toLowerCase());
		Predicate<Ordering> technicalRequirement = ordering -> ordering.getNomenclature().getTechnicalRequirement().getId().toLowerCase().contains(search.getText().toLowerCase());

		List<Ordering> temp = getOrderingsForCurrentPeriod(orderingsForCurrentYear);
		temp = getOrderingsForCurrentCategory(temp);
		temp = getOnlyLeasingOrderings(temp);
		temp = getWithoutLeasingOrderings(temp);
		temp = getOrderingsWithZeroCount(temp);
		temp = getOrderingsWithNumber(temp);
		temp = getOrderingsWithoutNumber(temp);
		temp = temp.stream().filter(number.or(codeKSM).or(codeKSMName).or(technicalRequirement)).collect(Collectors.toList());

		orderingTableView.getItems().addAll(temp);
	}

	/**
	 * Метод для фильтрации только заказов на поставку без номеров заявок
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsWithoutNumber(List<Ordering> orderingList) {
		if (onlyUncreatedOrdering.isSelected()) {
			orderingList = orderingList.stream().filter(x -> x.getNumber().equals("")).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку с номерами заявок
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsWithNumber(List<Ordering> orderingList) {
		if (onlyCreatedOrdering.isSelected()) {
			orderingList = orderingList.stream().filter(x -> !x.getNumber().equals("")).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации заказов на поставку с нулевым количеством
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsWithZeroCount(List<Ordering> orderingList) {
		if (!emptyCategory.isSelected()) {
			orderingList = orderingList.stream().filter(x -> x.getCount().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку без лизинговой схемы
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getWithoutLeasingOrderings(List<Ordering> orderingList) {
		if (withoutLeasing.isSelected()) {
			orderingList = orderingList.stream().filter(x -> !x.isLeasing()).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации только заказов на поставку по лизинговой схеме
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOnlyLeasingOrderings(List<Ordering> orderingList) {
		if (onlyLeasing.isSelected()) {
			orderingList = orderingList.stream().filter(Ordering::isLeasing).collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации заказов на поставку только для выбранной категории
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsForCurrentCategory(List<Ordering> orderingList) {
		if (!category.getSelectionModel().getSelectedItem().equals(ALL_CATEGORIES)) {
			orderingList = orderingList.stream()
					.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getSelectionModel().getSelectedItem()))
					.collect(Collectors.toList());
		}
		return orderingList;
	}

	/**
	 * Метод для фильтрации заказов на поставку со сроком поставки в выбранный период
	 *
	 * @param orderingList коллекция с исходными зазаказами
	 * @return отфильтрованная коллекция
	 */
	private List<Ordering> getOrderingsForCurrentPeriod(List<Ordering> orderingList) {
		orderingList = switch (period.getSelectionModel().getSelectedItem()) {
			case JANUARY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 1).collect(Collectors.toList());
			case FEBRUARY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 2).collect(Collectors.toList());
			case MARCH ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 3).collect(Collectors.toList());
			case APRIL ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 4).collect(Collectors.toList());
			case MAY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 5).collect(Collectors.toList());
			case JUNE ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 6).collect(Collectors.toList());
			case JULY ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 7).collect(Collectors.toList());
			case AUGUST ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 8).collect(Collectors.toList());
			case SEPTEMBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 9).collect(Collectors.toList());
			case OCTOBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 10).collect(Collectors.toList());
			case NOVEMBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 11).collect(Collectors.toList());
			case DECEMBER ->
					orderingList.stream().filter(x -> x.getDate().getMonth().getValue() == 12).collect(Collectors.toList());
			default -> orderingList;
		};
		return orderingList;
	}

	/**
	 * Метод для настройки Listener в элементах сцены
	 */
	private void settingsListenersForElement() {
		year.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			CurrentData.CURRENT_YEAR = Integer.parseInt(newValue);
			initOrderingTable();
			getPresumm();
		});

		period.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		search.textProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		allEquipment.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		onlyLeasing.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		withoutLeasing.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		emptyCategory.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPresumm();
		});
		onlyCreatedOrdering.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			onlyUncreatedOrdering.setDisable(onlyCreatedOrdering.isSelected());
			getPresumm();
		});
		onlyUncreatedOrdering.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			onlyCreatedOrdering.setDisable(onlyUncreatedOrdering.isSelected());
			getPresumm();
		});
	}


	@FXML
	private void add() {
		OrderingEdit.addNewOrdering(this::saveOrdering, unitsService::findAll, nomenclatureService);
	}

	@FXML
	private void delete() {
		orderingService.deleteById(orderingTableView.getSelectionModel().getSelectedItem().getId());
		initOrderingTable();
	}

	@FXML
	private void edit() {
		Ordering ordering = orderingTableView.getSelectionModel().getSelectedItem();
		if (ordering != null) {
			OrderingEdit.editOrdering(ordering, this::saveOrdering, unitsService::findAll, nomenclatureService);
		}
		initOrderingTable();
	}

	/**
	 * Метод для загрузки заявок из файла шаблона
	 */
	@FXML
	private void uploadOrdering() {
		FileChooser fileChooser = getFileChooser(ORDERINGS_UPLOAD_TITLE_SUFFIX);
		try {
			File file = fileChooser.showOpenDialog(year.getScene().getWindow());

			ExcelUtils.uploadOrderingsFromExcel(file, nomenclatureService, orderingService, unitsService);
			reload();
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
		}
	}

	/**
	 * Метод для выгрузки отфильтрованных заявок в файл
	 */
	@FXML
	private void unloadingToExcel() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

		try {
			File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());
			unloadOrderingToExcel(year, period, category, allEquipment, onlyLeasing, withoutLeasing, emptyCategory,
					onlyCreatedOrdering, onlyUncreatedOrdering, orderingTableView, resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	/**
	 * Метод для формирования шаблонов загрузки в систему
	 */
	@FXML
	private void createTemplates() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_TEMPLATES);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());

		unloadToOrderingTemplate(year, subcategoryService, orderingTableView, resultsFolder.toString());

	}


	private void saveOrdering(Ordering ordering) {
		orderingService.save(ordering);
//		reload();
		initOrderingTable();
	}

	private void settingsOrderingTableView() {
		numberCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumber())));
		numberCol.setStyle("-fx-alignment: CENTER-LEFT;");
		numberCol.setComparator(Comparator.comparing(FormatUtils::parseNumberOrdering));

		positionCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPosition())));
		positionCol.setStyle("-fx-alignment: CENTER-LEFT;");
		positionCol.setComparator(Comparator.comparing(FormatUtils::parseNumberOrdering));

		codeKSMCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getId()));
		codeKSMCol.setStyle("-fx-alignment: CENTER-LEFT;");
		nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getName()));
		nameCol.setStyle("-fx-alignment: CENTER-LEFT;");
		technicalRequirementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getTechnicalRequirement().getId()));
		technicalRequirementCol.setStyle("-fx-alignment: CENTER-LEFT;");
		unitCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().getName()));
		unitCol.setStyle("-fx-alignment: CENTER-LEFT;");

		countCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCount())));
		countCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		countCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

		priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getPrice())));
		priceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		priceCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

		priceWithTaxCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getPrice().multiply(BigDecimal.valueOf(1.20)))));
		priceWithTaxCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		priceWithTaxCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

		costCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCost())));
		costCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		costCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

		costWithTaxCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCost().multiply(BigDecimal.valueOf(1.20)))));
		costWithTaxCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		costWithTaxCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

		dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern(PATTERN_FOR_SHORT_DATE))));
		dateCol.setStyle("-fx-alignment: CENTER-LEFT;");
		dateCol.setComparator(Comparator.comparing(o -> LocalDate.parse(o, DateTimeFormatter.ofPattern(PATTERN_FOR_SHORT_DATE))));


		requiresInstallationCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isRequiresInstallation() ? YES : NO));
		requiresInstallationCol.setStyle("-fx-alignment: CENTER-LEFT;");
		isLeasingCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isLeasing() ? YES : NO));
		isLeasingCol.setStyle("-fx-alignment: CENTER-LEFT;");
		remarkCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRemark()));
		remarkCol.setStyle("-fx-alignment: CENTER-LEFT;");

		orderingTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));

		TableviewElementUtils.setContextMenuForTable(orderingTableView);
		duplicateRowContextMenu(orderingTableView);
		duplicateRowHotKeysHandler(orderingTableView);
		editOnDoubleClick(orderingTableView);


	}


	private void duplicateRowContextMenu(final TableView<?> table) {
		changeSelectionModelTableView(table);
		MenuItem duplicate = new MenuItem(DUPLICATE_ROW);
		duplicate.setOnAction(action -> duplicateRow(table));
		MenuItem remove = new MenuItem(REMOVE_ROW);
		remove.setOnAction(action -> delete());
		SeparatorMenuItem separator1 = new SeparatorMenuItem();
		SeparatorMenuItem separator2 = new SeparatorMenuItem();


		table.getContextMenu().getItems().addAll(separator1, duplicate, separator2, remove);
		changeSelectionModelTableView(table);
	}


	private void duplicateRowHotKeysHandler(final TableView<?> table) {
		changeSelectionModelTableView(table);
		table.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<>() {
			final KeyCombination duplicateRowKeyCombination = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);

			@Override
			public void handle(KeyEvent event) {
				if (duplicateRowKeyCombination.match(event)) {
					duplicateRow(table);
					event.consume();

				}
			}
		});
		changeSelectionModelTableView(table);
	}

	private void duplicateRow(TableView<?> table) {
		Ordering currentOrder = (Ordering) table.getSelectionModel().getSelectedItem();
		Ordering newOrder = new Ordering();
		if (currentOrder != null) {
			newOrder.setNumber(currentOrder.getNumber());
			newOrder.setPosition("");
			newOrder.setNomenclature(currentOrder.getNomenclature());
			newOrder.setUnit(currentOrder.getUnit());
			newOrder.setCount(currentOrder.getCount());
			newOrder.setPrice(currentOrder.getPrice());
			newOrder.setCost(currentOrder.getCost());
			newOrder.setRequiresInstallation(currentOrder.isRequiresInstallation());
			newOrder.setLeasing(currentOrder.isLeasing());
			newOrder.setDate(currentOrder.getDate());
			newOrder.setRemark(currentOrder.getRemark());
		}
		saveOrdering(newOrder);

	}

	private static void changeSelectionModelTableView(TableView<?> table) {
		if (table.getSelectionModel().isCellSelectionEnabled() && table.getSelectionModel().getSelectionMode() == SelectionMode.MULTIPLE) {
			table.getSelectionModel().setCellSelectionEnabled(false);
			table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		} else if (!table.getSelectionModel().isCellSelectionEnabled() && table.getSelectionModel().getSelectionMode() == SelectionMode.SINGLE) {
			table.getSelectionModel().setCellSelectionEnabled(true);
			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}

	}

	private void editOnDoubleClick(final TableView<?> table) {
		changeSelectionModelTableView(table);

		table.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getClickCount() == 2) {
				Ordering ordering = (Ordering) table.getSelectionModel().getSelectedItem();
				if (ordering != null) {
					OrderingEdit.editOrdering(ordering, this::saveOrdering, unitsService::findAll, nomenclatureService);
				}
				reload();
			}
		});
		changeSelectionModelTableView(table);
	}

	private void getPresumm() {
		ObservableList<Ordering> items = orderingTableView.getItems();
		BigDecimal summCount = BigDecimal.ZERO;
		BigDecimal summCostWithoutTax = BigDecimal.ZERO;
		for (Ordering ordering : items) {
			summCount = summCount.add(ordering.getCount());
			summCostWithoutTax = summCostWithoutTax.add(ordering.getCost());
		}
		BigDecimal summCostWithTax = summCostWithoutTax.multiply(BigDecimal.valueOf(1.20));
		presummCount.setText(formatNumber(summCount));
		presummCostWithoutTax.setText(formatNumber(summCostWithoutTax));
		presummCostWithTax.setText(formatNumber(summCostWithTax));
	}
}
