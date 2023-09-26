package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.BusinessPlan;
import organizer.model.entities.Category;
import organizer.model.services.BusinessPlanService;
import organizer.model.services.CategoryService;
import organizer.utils.ExcelUtils;
import organizer.utils.FormatUtils;
import organizer.utils.TableviewElementUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.BusinessPlanEdit;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;

import static organizer.utils.Constants.*;
import static organizer.utils.ExcelUtils.getFileChooser;
import static organizer.utils.ExcelUtils.unloadBPToExcel;

@Controller
public class BusinessPlanController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private BusinessPlanService businessPlanService;

	@FXML
	private TableView<Category> businessPlanTableView;
	@FXML
	private TableColumn<Category, String> categoryCol,
			firstYearCostCol, firstYearCountCol, firstYearPriceCol,
			secondYearCostCol, secondYearCountCol, secondYearPriceCol,
			thirdYearCostCol, thirdYearCountCol, thirdYearPriceCol,
			forthYearCostCol, forthYearCountCol, forthYearPriceCol,
			fifthYearCostCol, fifthYearCountCol, fifthYearPriceCol;

	@FXML
	private Label titleFirstYear, titleSecondYear, titleThirdYear, titleForthYear, titleFifthYear;
	@FXML
	private Label firstYearBPTitle, secondYearBPTitle, thirdYearBPTitle, forthYearBPTitle, fifthYearBPTitle;
	@FXML
	private ComboBox<BusinessPlan> version;


	@FXML
	private void initialize() {
		settingComboBox();
		reload();

		version.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			settingsTitleTable();
			settingsTable();
			settingsBPTitles();
		});
	}

	@FXML
	private void add() {
		BusinessPlanEdit.addNewBPGeneral(this::saveBPGeneral);
		reload();
	}

	private void saveBPGeneral(BusinessPlan businessPlan) {
		businessPlanService.save(businessPlan);
		reload();
	}

	@FXML
	private void delete() {
		businessPlanService.delete(version.getValue().getId());
		reload();
	}

	private void reload() {
		settingsTitleTable();
		settingsTable();
		settingsBPTitles();
		businessPlanTableView.refresh();
	}

	@FXML
	private void edit() {
		BusinessPlan businessPlan = version.getSelectionModel().getSelectedItem();
		if (businessPlan != null) {
			BusinessPlanEdit.editBPGeneral(businessPlan, this::saveBPGeneral);
		}
		reload();
	}

	private void settingsTable() {
		businessPlanTableView.getItems().clear();
		businessPlanTableView.getItems().addAll(categoryService.findAll());
		categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

		if (version.getValue() != null) {
			//TODO - релизовать сортировку по аналогии с влкдакой детализации
			firstYearCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getFirstYearCount())));
			firstYearCountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			firstYearCountCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			firstYearCostCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getFirstYearCost())));
			firstYearCostCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			firstYearCostCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			firstYearPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(getPriceFromDataHashMap(cellData.getValue(),
					version.getValue().getFirstYearCost(), version.getValue().getFirstYearCount())));
			firstYearPriceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			firstYearPriceCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			secondYearCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getSecondYearCount())));
			secondYearCountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			secondYearCountCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			secondYearCostCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getSecondYearCost())));
			secondYearCostCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			secondYearCostCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			secondYearPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(getPriceFromDataHashMap(cellData.getValue(),
					version.getValue().getSecondYearCost(), version.getValue().getSecondYearCount())));
			secondYearPriceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			secondYearPriceCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			thirdYearCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getThirdYearCount())));
			thirdYearCountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			thirdYearCountCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			thirdYearCostCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getThirdYearCost())));
			thirdYearCostCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			thirdYearCostCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			thirdYearPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(getPriceFromDataHashMap(cellData.getValue(),
					version.getValue().getThirdYearCost(), version.getValue().getThirdYearCount())));
			thirdYearPriceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			thirdYearPriceCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			forthYearCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getForthYearCount())));
			forthYearCountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			forthYearCountCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			forthYearCostCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getForthYearCost())));
			forthYearCostCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			forthYearCostCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			forthYearPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(getPriceFromDataHashMap(cellData.getValue(),
					version.getValue().getForthYearCost(), version.getValue().getForthYearCount())));
			forthYearPriceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			forthYearPriceCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			fifthYearCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getFifthYearCount())));
			fifthYearCountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			fifthYearCountCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			fifthYearCostCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDataFromHashMap(cellData.getValue(), version.getValue().getFifthYearCost())));
			fifthYearCostCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			fifthYearCostCol.setComparator(Comparator.comparing(FormatUtils::parseNumber));

			fifthYearPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(getPriceFromDataHashMap(cellData.getValue(),
					version.getValue().getFifthYearCost(), version.getValue().getFifthYearCount())));
			fifthYearPriceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
			fifthYearPriceCol.setComparator(Comparator.comparing(FormatUtils::parseNumberOrdering));
		}
		businessPlanTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));

		TableviewElementUtils.setContextMenuForTable(businessPlanTableView);
	}

	private void settingComboBox() {
		version.getItems().clear();
		version.getItems().addAll(businessPlanService.findAll());
		version.getSelectionModel().selectFirst();
	}

	private void settingsTitleTable() {
		if (version.getValue() != null) {
			titleFirstYear.setText(String.valueOf(version.getValue().getFirstYear()));
			titleSecondYear.setText(String.valueOf(version.getValue().getFirstYear() + 1));
			titleThirdYear.setText(String.valueOf(version.getValue().getFirstYear() + 2));
			titleForthYear.setText(String.valueOf(version.getValue().getFirstYear() + 3));
			titleFifthYear.setText(String.valueOf(version.getValue().getFirstYear() + 4));
		} else {
			titleFirstYear.setText(FIRST_YEAR);
			titleSecondYear.setText(SECOND_YEAR);
			titleThirdYear.setText(THIRD_YEAR);
			titleForthYear.setText(FOURTH_YEAR);
			titleFifthYear.setText(FIFTH_YEAR);
		}
	}

	private String getDataFromHashMap(Category category, HashMap<String, BigDecimal> map) {
		BigDecimal result = map.get(category.getName());
		return FormatUtils.formatNumber(result);
	}

	private String getPriceFromDataHashMap(Category category, HashMap<String, BigDecimal> cost, HashMap<String, BigDecimal> count) {
		try {
			BigDecimal result = cost.get(category.getName()).divide(count.get(category.getName()), 2, RoundingMode.HALF_UP);
			return FormatUtils.formatNumber(result);
		} catch (ArithmeticException e) {
			return FormatUtils.formatNumber(BigDecimal.ZERO);
		}
	}

	private void setBPTitle(Label title, HashMap<String, BigDecimal> cost) {
		BigDecimal result = BigDecimal.ZERO;
		for (BigDecimal element : cost.values()) {
			result = result.add(element);
		}
		title.setText(FormatUtils.formatNumber(result) + CURRENCY_WITH_TAX_SUFFIX);
	}

	private void settingsBPTitles() {
		if (version.getValue() != null) {
			setBPTitle(firstYearBPTitle, version.getValue().getFirstYearCost());
			setBPTitle(secondYearBPTitle, version.getValue().getSecondYearCost());
			setBPTitle(thirdYearBPTitle, version.getValue().getThirdYearCost());
			setBPTitle(forthYearBPTitle, version.getValue().getForthYearCost());
			setBPTitle(fifthYearBPTitle, version.getValue().getFifthYearCost());
		} else {
			setBPTitle(firstYearBPTitle, new HashMap<>());
			setBPTitle(secondYearBPTitle, new HashMap<>());
			setBPTitle(thirdYearBPTitle, new HashMap<>());
			setBPTitle(forthYearBPTitle, new HashMap<>());
			setBPTitle(fifthYearBPTitle, new HashMap<>());
		}
	}

	@FXML
	private void unloadingToExcel() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

		try {
			File resultsFolder = directoryChooser.showDialog(businessPlanTableView.getScene().getWindow());
			unloadBPToExcel(businessPlanService, resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	@FXML
	private void uploadBP() {
		FileChooser fileChooser = getFileChooser(BP_UPLOAD_TITLE_SUFFIX);
		try {
			File file = fileChooser.showOpenDialog(version.getScene().getWindow());

			ExcelUtils.uploadBPFromExcel(file, businessPlanService);
			initialize();
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
		}
	}


}
