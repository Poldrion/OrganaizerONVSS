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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

	@FXML
	private TextField search, preSumCount, preSumCostWithoutTax, preSumCostWithTax;
	@FXML
	private CheckBox emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering;
	@FXML
	private RadioButton allEquipment, onlyLeasing, withoutLeasing;
	@FXML
	private ComboBox<String> year, period, category;
	@FXML
	private TableView<Ordering> orderingTableView;
	@FXML
	private TableColumn<Ordering, String> numberCol, positionCol, codeKSMCol, nameCol, technicalRequirementCol, unitCol,
	countCol, priceCol, priceWithTaxCol, costCol, costWithTaxCol, dateCol, requiresInstallationCol,
	isLeasingCol, remarkCol;

	private List<Category> categories;
	private final List<Ordering> orderingsForCurrentYear = new ArrayList<>();
	private LocalDate dateStart;
	private LocalDate dateEnd;

	@FXML
	private void initialize() {
		initYearComboBox();
		initPeriodComboBox();
		initCategoriesComboBox();
		initOrderingTable();

		settingsDateFirstAndLastDayForCurrentYear();
		settingsOrderingTableView();
		settingsListenersForElement();
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

	private void initYearComboBox() {
		year.getItems().addAll(CurrentData.YEARS);
		year.getSelectionModel().select(String.valueOf(CurrentData.CURRENT_YEAR));
	}

	private void initPeriodComboBox() {
		period.getItems().addAll(Constants.PERIOD);
		period.getSelectionModel().select(CurrentData.CURRENT_PERIOD);
	}

	private void initCategoriesComboBox() {
		categories = categoryService.findAll();
		category.getItems().addAll(ALL_CATEGORIES);
		for (Category c : categories) {
			category.getItems().add(c.getName());
		}
		category.getSelectionModel().selectFirst();
	}

	private void initOrderingTable() {
		orderingsForCurrentYear.clear();
		settingsDateFirstAndLastDayForCurrentYear();
		orderingsForCurrentYear.addAll(orderingService.findByYear(dateStart, dateEnd));
		reload();
		getPreSum();
	}

	private void reload() {
		orderingTableView.getItems().clear();
		List<Ordering> temp = orderingService.getOrderingsAfterFilter(orderingsForCurrentYear, search, CurrentData.CURRENT_PERIOD, category,
				onlyLeasing, withoutLeasing, emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering);
		orderingTableView.getItems().addAll(temp);
	}

	/**
	 * Метод для установки первого и последнего дней в выбранном году для выгрузки из БД
	 */
	private void settingsDateFirstAndLastDayForCurrentYear() {
		dateStart = LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), 1, LocalDate.MIN.getDayOfMonth());
		dateEnd = LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), 12, LocalDate.MAX.getDayOfMonth());
	}

	/**
	 * Метод для настройки Listener в элементах сцены
	 */
	private void settingsListenersForElement() {
		year.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			CurrentData.CURRENT_YEAR = Integer.parseInt(newValue);
			initOrderingTable();
			getPreSum();
		});
		period.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			CurrentData.CURRENT_PERIOD = newValue;
			reload();
			getPreSum();
		});
		category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});
		search.textProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});
		allEquipment.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});
		onlyLeasing.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});
		withoutLeasing.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});
		emptyCategory.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});
		onlyCreatedOrdering.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			onlyUncreatedOrdering.setDisable(onlyCreatedOrdering.isSelected());
			getPreSum();
		});
		onlyUncreatedOrdering.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			onlyCreatedOrdering.setDisable(onlyUncreatedOrdering.isSelected());
			getPreSum();
		});
	}

	private void settingsOrderingTableView() {
		numberCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(String.valueOf(cellData.getValue().getNumber())));
		positionCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(String.valueOf(cellData.getValue().getPosition())));
		codeKSMCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getId()));
		nameCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getName()));
		technicalRequirementCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getNomenclature().getTechnicalRequirement().getId()));
		unitCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getUnit().getName()));
		countCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(formatNumber(cellData.getValue().getCount())));
		priceCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(formatNumber(cellData.getValue().getPrice())));
		priceWithTaxCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(formatNumber(cellData.getValue().getPrice().multiply(BigDecimal.valueOf(1.20)))));
		costCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(formatNumber(cellData.getValue().getCost())));
		costWithTaxCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(formatNumber(cellData.getValue().getCost().multiply(BigDecimal.valueOf(1.20)))));
		dateCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern(PATTERN_FOR_SHORT_DATE))));
		requiresInstallationCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().isRequiresInstallation() ? YES : NO));
		isLeasingCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().isLeasing() ? YES : NO));
		remarkCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getRemark()));

		settingStyleForTableColumn();
		settingComparatorForTableColumn();

		orderingTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));

		TableviewElementUtils.setContextMenuForTable(orderingTableView);
		duplicateRowContextMenu(orderingTableView);
		duplicateRowHotKeysHandler(orderingTableView);
		editOnDoubleClick(orderingTableView);
	}

	private void settingComparatorForTableColumn() {
		for (TableColumn<Ordering, String> orderingStringTableColumn : Arrays.asList(numberCol, positionCol)) {
			orderingStringTableColumn.setComparator(Comparator.comparing(FormatUtils::parseNumberOrdering));
		}
		for (TableColumn<Ordering, String> orderingStringTableColumn : Arrays.asList(countCol, priceCol, priceWithTaxCol,
				costCol, costWithTaxCol)) {
			orderingStringTableColumn.setComparator(Comparator.comparing(FormatUtils::parseNumber));
		}
		dateCol.setComparator(Comparator.comparing(o -> LocalDate.parse(o, DateTimeFormatter.ofPattern(PATTERN_FOR_SHORT_DATE))));
	}

	private void settingStyleForTableColumn() {
		for (TableColumn<Ordering, String> orderingStringTableColumn : Arrays.asList(numberCol, positionCol, codeKSMCol,
				nameCol, technicalRequirementCol, unitCol, dateCol, requiresInstallationCol, isLeasingCol, remarkCol)) {
			orderingStringTableColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		}
		for (TableColumn<Ordering, String> orderingStringTableColumn : Arrays.asList(countCol, priceCol, priceWithTaxCol,
				costCol, costWithTaxCol)) {
			orderingStringTableColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
		}
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

	private void saveOrdering(Ordering ordering) {
		orderingService.save(ordering);
		initOrderingTable();
	}

	private void getPreSum() {
		ObservableList<Ordering> items = orderingTableView.getItems();
		BigDecimal sumCount = BigDecimal.ZERO;
		BigDecimal sumCostWithoutTax = BigDecimal.ZERO;
		for (Ordering ordering : items) {
			sumCount = sumCount.add(ordering.getCount());
			sumCostWithoutTax = sumCostWithoutTax.add(ordering.getCost());
		}
		BigDecimal sumCostWithTax = sumCostWithoutTax.multiply(BigDecimal.valueOf(1.20));
		preSumCount.setText(formatNumber(sumCount));
		preSumCostWithoutTax.setText(formatNumber(sumCostWithoutTax));
		preSumCostWithTax.setText(formatNumber(sumCostWithTax));
	}
}
