package organizer.views.controller.popups;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organizer.model.OrganizerException;
import organizer.model.entities.BusinessPlan;
import organizer.model.entities.Category;
import organizer.model.services.CategoryService;
import organizer.views.controller.common.Dialog;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static organizer.utils.Constants.*;
import static organizer.utils.FormatUtils.formatNumber;
import static organizer.utils.FormatUtils.parseNumber;

public class BusinessPlanEdit {
	@FXML
	private Label message, title;
	@FXML
	private TextField id, firstYear;
	@FXML
	private TableView<Category> bpTableView;
	@FXML
	private TableColumn<Category, String> categoryCol, firstYearCountCol, firstYearCostCol, secondYearCountCol,
			secondYearCostCol, thirdYearCountCol, thirdYearCostCol, forthYearCountCol, forthYearCostCol,
			fifthYearCountCol, fifthYearCostCol;

	private CategoryService categoryService;
	private Consumer<BusinessPlan> saveHandler;
	private BusinessPlan businessPlan;

	@FXML
	private void save() {
		try {
			businessPlan.setId(id.getText());
			businessPlan.setFirstYear(Integer.parseInt(firstYear.getText()));

			saveHandler.accept(businessPlan);
			close();
			Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(ADD_OR_EDIT_BP_COMPLETED_SUCCESSFULLY).build().show();
		} catch (OrganizerException e) {
			message.setText(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void close() {
		title.getScene().getWindow().hide();
	}

	public static void addNewBPGeneral(Consumer<BusinessPlan> saveHandler, CategoryService categoryService) {
		editBPGeneral(null, saveHandler, categoryService);
	}

	public static void editBPGeneral(BusinessPlan businessPlan, Consumer<BusinessPlan> saveHandler, CategoryService categoryService) {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);

			FXMLLoader loader = new FXMLLoader(BusinessPlanEdit.class.getClassLoader().getResource("views/popups/BusinessPlanEdit.fxml"));
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);

			BusinessPlanEdit edit = loader.getController();
			edit.init(businessPlan, saveHandler, categoryService);

			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(BusinessPlan businessPlan, Consumer<BusinessPlan> saveHandler, CategoryService categoryService) {
		this.saveHandler = saveHandler;
		this.categoryService = categoryService;

		if (businessPlan == null) {
			this.businessPlan = new BusinessPlan();
			this.title.setText(ADD_NEW_VERSION_BP);
		} else {
			this.businessPlan = businessPlan;
			this.title.setText(EDIT_VERSION_BP);
			this.id.setText(businessPlan.getId());
			this.firstYear.setText(String.valueOf(businessPlan.getFirstYear()));
		}
		initBPTableView();
	}

	private void initBPTableView() {
		bpTableView.getItems().addAll(categoryService.findAll());
		settingBPTableView();
	}

	private void settingBPTableView() {
		categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		firstYearCountCol.setCellValueFactory(cellData -> {
			if ((businessPlan.getFirstYearCount().get(cellData.getValue().getName()) != null)) {
				return new SimpleStringProperty(formatNumber(businessPlan.getFirstYearCount().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		firstYearCostCol.setCellValueFactory(cellData -> {
			if (businessPlan.getFirstYearCost().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getFirstYearCost().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});

		secondYearCountCol.setCellValueFactory(cellData -> {
			if (businessPlan.getSecondYearCount().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getSecondYearCount().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		secondYearCostCol.setCellValueFactory(cellData -> {
			if (businessPlan.getSecondYearCost().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getSecondYearCost().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		thirdYearCountCol.setCellValueFactory(cellData -> {
			if (businessPlan.getThirdYearCount().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getThirdYearCount().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		thirdYearCostCol.setCellValueFactory(cellData -> {
			if (businessPlan.getThirdYearCost().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getThirdYearCost().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		forthYearCountCol.setCellValueFactory(cellData -> {
			if (businessPlan.getForthYearCount().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getForthYearCount().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		forthYearCostCol.setCellValueFactory(cellData -> {
			if (businessPlan.getForthYearCost().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getForthYearCost().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		fifthYearCountCol.setCellValueFactory(cellData -> {
			if (businessPlan.getFifthYearCount().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getFifthYearCount().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});
		fifthYearCostCol.setCellValueFactory(cellData -> {
			if (businessPlan.getFifthYearCost().get(cellData.getValue().getName()) != null) {
				return new SimpleStringProperty(formatNumber(businessPlan.getFifthYearCost().get(cellData.getValue().getName())));
			} else {
				return new SimpleStringProperty(formatNumber(BigDecimal.ZERO));
			}
		});

		setEditableCellForTableColumn(firstYearCountCol, firstYearCostCol, secondYearCountCol, secondYearCostCol,
				thirdYearCountCol, thirdYearCostCol, forthYearCountCol, forthYearCostCol, fifthYearCountCol, fifthYearCostCol);

		firstYearCountCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getFirstYearCount()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));
		firstYearCostCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getFirstYearCost()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));

		secondYearCountCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getSecondYearCount()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));
		secondYearCostCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getSecondYearCost()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));

		thirdYearCountCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getThirdYearCount()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));
		thirdYearCostCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getThirdYearCost()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));

		forthYearCountCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getForthYearCount()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));
		forthYearCostCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getForthYearCost()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));

		fifthYearCountCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getFifthYearCount()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));
		fifthYearCostCol.setOnEditCommit((TableColumn.CellEditEvent<Category, String> t) ->
				businessPlan.getFifthYearCost()
						.put(t.getTableView().getItems().get(t.getTablePosition().getRow()).getName(), parseNumber(t.getNewValue())));
	}

	@SafeVarargs
	private void setEditableCellForTableColumn(TableColumn<?, String>... columns) {
		for (TableColumn<?, String> column : columns) {
			column.setCellFactory(TextFieldTableCell.forTableColumn());
		}
	}
}
