package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.Category;
import organizer.model.entities.Ordering;
import organizer.model.entities.OrderingResult;
import organizer.model.entities.Units;
import organizer.model.services.CategoryService;
import organizer.model.services.OrderingResultService;
import organizer.model.services.OrderingService;
import organizer.utils.*;
import organizer.views.controller.common.Dialog;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static organizer.utils.Constants.*;
import static organizer.utils.ExcelUtils.getFileChooser;
import static organizer.utils.FormatUtils.formatNumber;
import static organizer.utils.FormatUtils.parseNumber;

@Controller
public class AnalyticsController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderingService orderingService;
	@Autowired
	private OrderingResultService orderingResultService;

	// ----------------------------------- детализированная аналитика --------------------------------------------------------------
	@FXML
	private ComboBox<String> year;
	@FXML
	private ComboBox<String> period;
	@FXML
	private ComboBox<String> category;
	@FXML
	private CheckBox emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering, withTax;
	@FXML
	private RadioButton allEquipment, onlyLeasing, withoutLeasing;
	@FXML
	private TextField search,
			preSumCountPlan, preSumCostPlan,
			preSumCountFact, preSumCostFact,
			preSumCountDeviation, preSumCostDeviation;
	@FXML
	private TableView<Ordering> detailAnalyticsTableView;
	@FXML
	private TableColumn<Ordering, String> numberCol, positionCol, codeKSMCol, nameCol, technicalRequirementCol, unitCol,
			countPlanCol, pricePlanCol, costPlanCol,
			countFactCol, priceFactCol, costFactCol,
			countDeviationCol, priceDeviationCol, costDeviationCol,
			remarkCol;

	// ----------------------------------- сводная аналитика -----------------------------------------------------------------------
	@FXML
	private ComboBox<String> yearSum;
	@FXML
	private ComboBox<String> periodSum;
	@FXML
	private CheckBox withTaxSum;
	@FXML
	private TableView<Category> sumAnalyticsTableView;
	@FXML
	private TableColumn<Category, String> categorySumCol, unitsSumCol,
			countPlanSumCol, costPlanSumCol,
			countFactSumCol, costFactSumCol,
			countDeviationSumCol, costDeviationSumCol;

	@FXML
	private ToggleGroup groupEquipment;
	@FXML
	private TabPane analyticsTabPane;
	@FXML
	private Tab detailAnalyticsTab, sumAnalyticsTab;

	private List<Category> categories;
	private final List<Ordering> orderingsForCurrentYear = new ArrayList<>();
	private final List<OrderingResult> orderingResultsForCurrentOrdering = new ArrayList<>();
	private final List<Ordering> tempOrdering = new ArrayList<>();
	private final List<OrderingResult> tempOrderingResult = new ArrayList<>();
	private final Map<String, List<OrderingResult>> orderingResultsStorage = new HashMap<>();
	private LocalDate dateStart;
	private LocalDate dateEnd;

	@FXML
	private void initialize() {
		initYearComboBoxOnDetailTab();
		initYearComboBoxOnSumTab();
		initPeriodComboBoxOnDetailTab();
		initPeriodSumComboBoxOnDetailTab();
		initCategoriesComboBox();
		initAnalyticsTables();

		settingDateFirstAndLastDayForCurrentYear();
		settingDetailAnalyticsTableView();
		settingSumAnalyticsTableView();
		settingsListenersForElement();

		reload();

		getPreSum();
	}


	@FXML
	void unloadingToExcel() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		try {
			File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());

			ExcelUtils.unloadDetailAnalyticsTableToExcel(year, period, category,
					emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering, withTax,
					allEquipment, onlyLeasing, withoutLeasing, detailAnalyticsTableView,
					resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	@FXML
	void unloadingToExcelSum() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		try {
			File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());

			ExcelUtils.unloadSumAnalyticsTableToExcel(year, period, withTaxSum, sumAnalyticsTableView, resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	@FXML
	void unloadingOrderingResultsToExcel() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		try {
			File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());

			ExcelUtils.unloadOrderingResultsToExcel(tempOrderingResult, resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	@FXML
	void uploadOrdering() {
		FileChooser fileChooser = getFileChooser(RESULTS_ORDERINGS_UPLOAD_TITLE_SUFFIX);
		try {
			File file = fileChooser.showOpenDialog(year.getScene().getWindow());

			ExcelUtils.uploadOrderingResultsFromExcel(file, orderingResultService, orderingService);
			organizer.views.controller.common.Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
		}
		orderingResultsForCurrentOrdering.clear();
		orderingResultsForCurrentOrdering.addAll(orderingResultService.findAllByOrdering(orderingsForCurrentYear));
		reload();
	}

	@FXML
	void clearOrderingResultForCurrentYear() {
		int count = 0;
		for (OrderingResult orderingResult : orderingResultsForCurrentOrdering) {
			orderingResultService.deleteById(orderingResult.getId());
			count++;
		}
		Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(REMOVE_ORDERING + "\nУдалено результатов: " + count).build().show();
		orderingResultsForCurrentOrdering.clear();

		reload();
	}

	private void initYearComboBoxOnDetailTab() {
		year.getItems().addAll(CurrentData.YEARS);
		year.getSelectionModel().select(String.valueOf(CurrentData.CURRENT_YEAR));
	}

	private void initYearComboBoxOnSumTab() {
		yearSum.getItems().addAll(CurrentData.YEARS);
		yearSum.getSelectionModel().select(String.valueOf(CurrentData.CURRENT_YEAR));
	}

	private void initPeriodComboBoxOnDetailTab() {
		period.getItems().addAll(Constants.PERIOD);
		period.getSelectionModel().select(CurrentData.CURRENT_PERIOD);
	}

	private void initPeriodSumComboBoxOnDetailTab() {
		periodSum.getItems().addAll(Constants.PERIOD);
		periodSum.getSelectionModel().select(CurrentData.CURRENT_PERIOD);
	}


	private void initCategoriesComboBox() {
		categories = categoryService.findAll();
		category.getItems().addAll(ALL_CATEGORIES);
		for (Category c : categories) {
			category.getItems().add(c.getName());
		}
		category.getSelectionModel().selectFirst();
	}

	private void initAnalyticsTables() {
		orderingsForCurrentYear.clear();
		orderingResultsForCurrentOrdering.clear();
		settingDateFirstAndLastDayForCurrentYear();
		orderingsForCurrentYear.addAll(orderingService.findByYear(dateStart, dateEnd));
		orderingResultsForCurrentOrdering.addAll(orderingResultService.findAllByOrdering(orderingsForCurrentYear));
//		reload();
	}

	private void settingDateFirstAndLastDayForCurrentYear() {
		dateStart = LocalDate.of(CurrentData.CURRENT_YEAR, 1, LocalDate.MIN.getDayOfMonth());
		dateEnd = LocalDate.of(CurrentData.CURRENT_YEAR, 12, LocalDate.MAX.getDayOfMonth());
	}

	private void settingDetailAnalyticsTableView() {
		numberCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumber())));
		numberCol.setComparator(Comparator.comparing(FormatUtils::parseNumberOrdering));

		positionCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPosition())));
		positionCol.setComparator(Comparator.comparing(FormatUtils::parseNumberOrdering));

		codeKSMCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getId()));
		nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getName()));
		technicalRequirementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getTechnicalRequirement().getId()));
		unitCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().getName()));

		// данные из созданных заявок на поставку
		countPlanCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCount())));
		pricePlanCol.setCellValueFactory(cellData -> {
			if (withTax.isSelected()) {
				return new SimpleStringProperty(formatNumber(cellData.getValue().getPrice().multiply(BigDecimal.valueOf(1.2))));
			} else {
				return new SimpleStringProperty(formatNumber(cellData.getValue().getPrice()));
			}
		});
		costPlanCol.setCellValueFactory(cellData -> {
			if (withTax.isSelected()) {
				return new SimpleStringProperty(formatNumber(cellData.getValue().getCost().multiply(BigDecimal.valueOf(1.2))));
			} else {
				return new SimpleStringProperty(formatNumber(cellData.getValue().getCost()));
			}
		});
		remarkCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRemark()));

		// данные из фактической проработки
		countFactCol.setCellValueFactory(cellData -> {
			if (getOrderingResultForCurrentOrdering(cellData.getValue()) == null) {
				return new SimpleStringProperty(formatNumber(cellData.getValue().getCount()));
			} else {
				return new SimpleStringProperty(formatNumber(getOrderingResultForCurrentOrdering(cellData.getValue()).getCount()));
			}
		});
		priceFactCol.setCellValueFactory(cellData -> {
			if (getOrderingResultForCurrentOrdering(cellData.getValue()) == null) {
				if (withTax.isSelected())
					return new SimpleStringProperty(formatNumber(cellData.getValue().getPrice().multiply(BigDecimal.valueOf(1.2))));
				else
					return new SimpleStringProperty(formatNumber(cellData.getValue().getPrice()));
			} else {
				if (withTax.isSelected()) {
					return new SimpleStringProperty(formatNumber(getOrderingResultForCurrentOrdering(cellData.getValue()).getPrice()));
				} else {
					return new SimpleStringProperty(formatNumber(getOrderingResultForCurrentOrdering(
							cellData.getValue()).getPrice().divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP)));
				}
			}
		});
		costFactCol.setCellValueFactory(cellData -> {
			if (getOrderingResultForCurrentOrdering(cellData.getValue()) == null) {
				if (withTax.isSelected())
					return new SimpleStringProperty(formatNumber(cellData.getValue().getCost().multiply(BigDecimal.valueOf(1.2))));
				else
					return new SimpleStringProperty(formatNumber(cellData.getValue().getCost()));
			} else {
				if (withTax.isSelected()) {
					return new SimpleStringProperty(formatNumber(getOrderingResultForCurrentOrdering(cellData.getValue()).getCost()));
				} else {
					return new SimpleStringProperty(formatNumber(getOrderingResultForCurrentOrdering(
							cellData.getValue()).getCost().divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP)));
				}
			}
		});

		// отклонения
		countDeviationCol.setCellValueFactory(cellData -> {
			String countPlan = String.valueOf(detailAnalyticsTableView.getColumns().get(6).getCellObservableValue(cellData.getValue()).getValue());
			String countFact = String.valueOf(detailAnalyticsTableView.getColumns().get(9).getCellObservableValue(cellData.getValue()).getValue());
			return new SimpleStringProperty(formatNumber(parseNumber(countFact).subtract(parseNumber(countPlan))));
		});

		priceDeviationCol.setCellValueFactory(cellData -> {
			String pricePlan = String.valueOf(detailAnalyticsTableView.getColumns().get(7).getCellObservableValue(cellData.getValue()).getValue());
			String priceFact = String.valueOf(detailAnalyticsTableView.getColumns().get(10).getCellObservableValue(cellData.getValue()).getValue());
			return new SimpleStringProperty(formatNumber(parseNumber(priceFact).subtract(parseNumber(pricePlan))));
		});
		costDeviationCol.setCellValueFactory(cellData -> {
			String costPlan = String.valueOf(detailAnalyticsTableView.getColumns().get(8).getCellObservableValue(cellData.getValue()).getValue());
			String costFact = String.valueOf(detailAnalyticsTableView.getColumns().get(11).getCellObservableValue(cellData.getValue()).getValue());
			return new SimpleStringProperty(formatNumber(parseNumber(costFact).subtract(parseNumber(costPlan))));
		});

		// общие настройки
		settingStyleForTableColumn();
		settingComparatorForDetailTableColumn(countPlanCol, pricePlanCol, costPlanCol);

		detailAnalyticsTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));
		TableviewElementUtils.setContextMenuForTable(detailAnalyticsTableView);
	}

	private void settingSumAnalyticsTableView() {
		categorySumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		unitsSumCol.setCellValueFactory(cellData -> new SimpleStringProperty(getUnit(cellData.getValue())));
		countPlanSumCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCountFromCurrentCategory(cellData.getValue())));
		costPlanSumCol.setCellValueFactory(cellData -> {
			if (withTaxSum.isSelected()) {
				return new SimpleStringProperty(formatNumber(getCostFromCurrentCategory(cellData.getValue()).multiply(BigDecimal.valueOf(1.2))));
			} else {
				return new SimpleStringProperty(formatNumber(getCostFromCurrentCategory(cellData.getValue())));
			}
		});
		//TODO реализовать настроки для остальных столбцов
		countFactSumCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCountOrderingResultFromCurrentCategory(cellData.getValue())));
		costFactSumCol.setCellValueFactory(cellData -> {
			if (withTaxSum.isSelected()) {
				return new SimpleStringProperty(formatNumber(getCostOrderingResultFromCurrentCategory(cellData.getValue())));
			} else {
				return new SimpleStringProperty(formatNumber(getCostOrderingResultFromCurrentCategory(cellData.getValue()).multiply(BigDecimal.valueOf(1.2))));
			}
		});
		countDeviationSumCol.setCellValueFactory(cellData -> {
			String countPlan = String.valueOf(sumAnalyticsTableView.getColumns().get(2).getCellObservableValue(cellData.getValue()).getValue());
			String countFact = String.valueOf(sumAnalyticsTableView.getColumns().get(4).getCellObservableValue(cellData.getValue()).getValue());
			return new SimpleStringProperty(formatNumber(parseNumber(countFact).subtract(parseNumber(countPlan))));
		});
		costDeviationSumCol.setCellValueFactory(cellData -> {
			String countPlan = String.valueOf(sumAnalyticsTableView.getColumns().get(3).getCellObservableValue(cellData.getValue()).getValue());
			String countFact = String.valueOf(sumAnalyticsTableView.getColumns().get(5).getCellObservableValue(cellData.getValue()).getValue());
			return new SimpleStringProperty(formatNumber(parseNumber(countFact).subtract(parseNumber(countPlan))));
		});

		settingStyleForTableColumn();
		settingComparatorForSumTableColumn();

		sumAnalyticsTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));
		TableviewElementUtils.setContextMenuForTable(sumAnalyticsTableView);
	}

	private void initOrderingResultsStorage() {
		List<Category> categoryList = categoryService.findAll();
		for (Category c : categoryList) {
			List<OrderingResult> temp = new ArrayList<>();
			for (OrderingResult element : tempOrderingResult) {
				if (element.getOrdering().getNomenclature().getSubcategory().getCategory().getName().equals(c.getName())) {
					temp.add(element);
				}
			}
			orderingResultsStorage.put(c.getName(), temp);
		}
	}

	private BigDecimal getCostOrderingResultFromCurrentCategory(Category currentCategory) {
		Function<OrderingResult, BigDecimal> orderingResultMapper = OrderingResult::getCost;
		Function<Ordering, BigDecimal> orderingMapper = Ordering::getCost;

		List<Ordering> tempOrdering = this.tempOrdering;
		tempOrdering = tempOrdering.stream()
				.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(currentCategory.getName()))
				.filter(x -> !x.getNumber().equals(""))
				.toList();
		List<OrderingResult> tempOrderingResult = orderingResultsStorage.get(currentCategory.getName());

		BigDecimal costFactWithOrderingResult = tempOrderingResult.stream().map(orderingResultMapper).reduce(BigDecimal.ZERO, BigDecimal::add);

		List<Ordering> tempOrderingWithoutResults = new ArrayList<>();
		for (Ordering ordering : tempOrdering) {
			Ordering temp = null;
			for (OrderingResult orderingResult : tempOrderingResult) {
				if (ordering.getNumber().equals(orderingResult.getNumber()) &&
					ordering.getPosition().equals(orderingResult.getPosition())) {
					temp = ordering;
					break;
				}
			}
			if (temp == null) {
				tempOrderingWithoutResults.add(ordering);
			}
		}
		BigDecimal costFactWithoutOrderingResult = tempOrderingWithoutResults.stream().map(orderingMapper).reduce(BigDecimal.ZERO, BigDecimal::add);

		return costFactWithOrderingResult.add(costFactWithoutOrderingResult.multiply(BigDecimal.valueOf(1.2)));
	}

	private String getCountOrderingResultFromCurrentCategory(Category currentCategory) {
		Function<OrderingResult, BigDecimal> orderingResultMapper = OrderingResult::getCount;
		Function<Ordering, BigDecimal> orderingMapper = Ordering::getCount;

		List<Ordering> tempOrdering = this.tempOrdering;
		tempOrdering = tempOrdering.stream()
				.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(currentCategory.getName()))
				.filter(x -> !x.getNumber().equals(""))
				.toList();

		List<OrderingResult> tempOrderingResult = orderingResultsStorage.get(currentCategory.getName());

		BigDecimal countFactWithOrderingResult = tempOrderingResult.stream().map(orderingResultMapper).reduce(BigDecimal.ZERO, BigDecimal::add);

		List<Ordering> tempOrderingWithoutResults = new ArrayList<>();
		for (Ordering ordering : tempOrdering) {
			Ordering temp = null;
			for (OrderingResult orderingResult : tempOrderingResult) {
				if (ordering.getNumber().equals(orderingResult.getNumber()) &&
					ordering.getPosition().equals(orderingResult.getPosition())) {
					temp = ordering;
					break;
				}
			}
			if (temp == null) {
				tempOrderingWithoutResults.add(ordering);
			}
		}
		BigDecimal countFactWithoutOrderingResult = tempOrderingWithoutResults.stream().map(orderingMapper).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal countFact = countFactWithOrderingResult.add(countFactWithoutOrderingResult);
		return formatNumber(countFact);
	}


	private String getCountFromCurrentCategory(Category currentCategory) {
		Function<Ordering, BigDecimal> orderingMapper = Ordering::getCount;
		List<Ordering> temp = tempOrdering;
		temp = temp.stream()
				.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(currentCategory.getName()))
				.filter(x -> !x.getNumber().equals(""))
				.toList();
		BigDecimal countPlan = temp.stream().map(orderingMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
		return formatNumber(countPlan);
	}

	private BigDecimal getCostFromCurrentCategory(Category currentCategory) {
		Function<Ordering, BigDecimal> orderingMapper = Ordering::getCost;
		List<Ordering> temp = tempOrdering;
		temp = temp.stream()
				.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(currentCategory.getName()))
				.filter(x -> !x.getNumber().equals(""))
				.toList();
		return temp.stream().map(orderingMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private String getUnit(Category category) {
		List<Ordering> orderingsByCategory = orderingsForCurrentYear.stream()
				.filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getName()))
				.toList();
		HashMap<Units, Integer> unitsInCategory = new HashMap<>();
		for (Ordering o : orderingsByCategory) {
			if (!unitsInCategory.containsKey(o.getUnit())) {
				unitsInCategory.put(o.getUnit(), 1);
			} else {
				unitsInCategory.put(o.getUnit(), unitsInCategory.get(o.getUnit()) + 1);
			}
		}
		Units unitsMaxCount = null;
		int maxCount = 0;
		for (Units u : unitsInCategory.keySet()) {
			if (unitsInCategory.get(u) > maxCount) {
				unitsMaxCount = u;
				maxCount = unitsInCategory.get(u);
			}
		}
		return unitsMaxCount != null ? unitsMaxCount.getName() : "";
	}

	private OrderingResult getOrderingResultForCurrentOrdering(Ordering ordering) {
		for (OrderingResult element : orderingResultsForCurrentOrdering) {
			if (element.getNumber().equals(ordering.getNumber()) &&
				element.getPosition().equals(ordering.getPosition())) {
				return element;
			}
		}
		return null;
	}

	@SafeVarargs
	private void settingComparatorForDetailTableColumn(TableColumn<Ordering, String>... columns) {
		for (TableColumn<Ordering, String> column : columns) {
			column.setComparator(Comparator.comparing(FormatUtils::parseNumber));
		}
	}

	@SafeVarargs
	private void settingComparatorForSumTableColumn(TableColumn<Category, String>... columns) {
		for (TableColumn<Category, String> column : columns) {
			column.setComparator(Comparator.comparing(FormatUtils::parseNumber));
		}
	}

	private void settingStyleForTableColumn() {
		for (TableColumn<Ordering, String> column : Arrays.asList(numberCol, positionCol, codeKSMCol, nameCol,
				technicalRequirementCol, unitCol, remarkCol)) {
			column.setStyle("-fx-alignment: CENTER-LEFT;");
		}

		for (TableColumn<Ordering, String> column : Arrays.asList(countPlanCol, pricePlanCol, costPlanCol,
				countFactCol, priceFactCol, costFactCol, countDeviationCol, priceDeviationCol, costDeviationCol)) {
			column.setStyle("-fx-alignment: CENTER-RIGHT;");
		}

		for (TableColumn<Category, String> column : Arrays.asList(countPlanSumCol, costPlanSumCol, countFactSumCol,
				costFactSumCol, countDeviationSumCol, costDeviationSumCol)) {
			column.setStyle("-fx-alignment: CENTER-RIGHT;");
		}
	}

	/**
	 * Метод для настройки Listener в элементах сцены
	 */
	private void settingsListenersForElement() {
		year.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			CurrentData.CURRENT_YEAR = Integer.parseInt(newValue);
			initAnalyticsTables();
			reload();
			getPreSum();
		});

		yearSum.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			CurrentData.CURRENT_YEAR = Integer.parseInt(newValue);
			initAnalyticsTables();
			reload();
			getPreSum();
		});

		period.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			CurrentData.CURRENT_PERIOD = newValue;
			reload();
			getPreSum();
		});

		periodSum.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
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

		analyticsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			year.getSelectionModel().select(String.valueOf(CurrentData.CURRENT_YEAR));
			yearSum.getSelectionModel().select(String.valueOf(CurrentData.CURRENT_YEAR));
			period.getSelectionModel().select(CurrentData.CURRENT_PERIOD);
			periodSum.getSelectionModel().select(CurrentData.CURRENT_PERIOD);
		});

		withTax.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
			getPreSum();
		});

		withTaxSum.selectedProperty().addListener((options, oldValue, newValue) -> {
			reload();
		});
	}

	private void reload() {
		detailAnalyticsTableView.getItems().clear();
		sumAnalyticsTableView.getItems().clear();
		tempOrdering.clear();
		tempOrderingResult.clear();
		tempOrdering.addAll(orderingService.getOrderingsAfterFilter(orderingsForCurrentYear, search, CurrentData.CURRENT_PERIOD, category,
				onlyLeasing, withoutLeasing, emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering));
		tempOrderingResult.addAll(orderingResultService.findAllByOrdering(tempOrdering));
		detailAnalyticsTableView.getItems().addAll(tempOrdering);
		sumAnalyticsTableView.getItems().addAll(categoryService.findAll());
		initOrderingResultsStorage();
	}

	private void getPreSum() {
		ObservableList<Ordering> items = detailAnalyticsTableView.getItems();
		BigDecimal sumPlanCount = BigDecimal.ZERO;
		BigDecimal sumPlanCost = BigDecimal.ZERO;
		BigDecimal sumFactCount = BigDecimal.ZERO;
		BigDecimal sumFactCost = BigDecimal.ZERO;
		BigDecimal sumDeviationCount = BigDecimal.ZERO;
		BigDecimal sumDeviationCost = BigDecimal.ZERO;

		for (Ordering ordering : items) {
			String tempPlanCount = String.valueOf(detailAnalyticsTableView.getColumns().get(6).getCellObservableValue(ordering).getValue());
			String tempPlanCost = String.valueOf(detailAnalyticsTableView.getColumns().get(8).getCellObservableValue(ordering).getValue());
			String tempFactCount = String.valueOf(detailAnalyticsTableView.getColumns().get(9).getCellObservableValue(ordering).getValue());
			String tempFactCost = String.valueOf(detailAnalyticsTableView.getColumns().get(11).getCellObservableValue(ordering).getValue());
			String tempDeviationCount = String.valueOf(detailAnalyticsTableView.getColumns().get(12).getCellObservableValue(ordering).getValue());
			String tempDeviationCost = String.valueOf(detailAnalyticsTableView.getColumns().get(14).getCellObservableValue(ordering).getValue());

			sumPlanCount = sumPlanCount.add(parseNumber(tempPlanCount));
			sumPlanCost = sumPlanCost.add(parseNumber(tempPlanCost));

			sumFactCount = sumFactCount.add(parseNumber(tempFactCount));
			sumFactCost = sumFactCost.add(parseNumber(tempFactCost));

			sumDeviationCount = sumDeviationCount.add(parseNumber(tempDeviationCount));
			sumDeviationCost = sumDeviationCost.add(parseNumber(tempDeviationCost));
		}

		preSumCountPlan.setText(formatNumber(sumPlanCount));
		preSumCostPlan.setText(formatNumber(sumPlanCost));

		preSumCountFact.setText(formatNumber(sumFactCount));
		preSumCostFact.setText(formatNumber(sumFactCost));

		preSumCountDeviation.setText(formatNumber(sumDeviationCount));
		preSumCostDeviation.setText(formatNumber(sumDeviationCost));
	}
}
