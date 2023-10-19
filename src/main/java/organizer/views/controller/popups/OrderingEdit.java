package organizer.views.controller.popups;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organizer.model.OrganizerException;
import organizer.model.entities.Nomenclature;
import organizer.model.entities.Ordering;
import organizer.model.entities.Units;
import organizer.model.services.NomenclatureService;
import organizer.utils.FormatUtils;
import organizer.views.controller.common.Dialog;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static organizer.utils.Constants.*;

public class OrderingEdit {
	@FXML
	private TextField number, position, nomenclature, count, price, search;
	@FXML
	private ComboBox<Units> unit;
	@FXML
	private DatePicker date;
	@FXML
	private RadioButton installationTrue, leasingTrue;
	@FXML
	private Label title, message;
	@FXML
	private TextArea remark;
	@FXML
	private TableView<Nomenclature> nomenclatureTableView;
	@FXML
	private TableColumn<Nomenclature, String> id, name, codeKSMId, codeKSMName, technicalRequirementId,
			technicalRequirementName;

	private Ordering ordering;
	private Consumer<Ordering> saveHandler;
//	private Supplier<List<Nomenclature>> supplierNomenclature;
	private Supplier<List<Units>> supplierUnits;
	private NomenclatureService nomenclatureService;

	public static void addNewOrdering(Consumer<Ordering> saveHandler,
									  Supplier<List<Units>> supplierUnits,
									  NomenclatureService nomenclatureService) {
		editOrdering(null, saveHandler, supplierUnits, nomenclatureService);
	}


	public static void editOrdering(Ordering ordering,
									Consumer<Ordering> saveHandler,
									Supplier<List<Units>> supplierUnits,
									NomenclatureService nomenclatureService) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(TechnicalRequirementEdit.class.getClassLoader().getResource("views/popups/OrderingEdit.fxml"));
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);

			OrderingEdit controller = loader.getController();
			controller.init(ordering, saveHandler, supplierUnits, nomenclatureService);

			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Ordering ordering,
					  Consumer<Ordering> saveHandler,
					  Supplier<List<Units>> supplierUnits,
					  NomenclatureService nomenclatureService) {
		this.saveHandler = saveHandler;
		this.nomenclatureService = nomenclatureService;
		this.supplierUnits = supplierUnits;
		this.unit.getItems().addAll(this.supplierUnits.get());
		this.nomenclatureTableView.getItems().addAll(this.nomenclatureService.findByAllColumns(search.getText()));

		if (ordering == null) {
			this.ordering = new Ordering();
			this.title.setText(ADD_NEW_ORDERING);
		} else {
			this.ordering = ordering;
			this.title.setText(EDIT_ORDERING);
			this.number.setText(String.valueOf(ordering.getNumber()));
			this.position.setText(String.valueOf(ordering.getPosition()));
			this.nomenclature.setText(ordering.getNomenclature().getId());
			this.count.setText(FormatUtils.formatNumber(ordering.getCount()));
			this.price.setText(FormatUtils.formatNumber(ordering.getPrice()));
			this.unit.setValue(ordering.getUnit());
			this.date.setValue(ordering.getDate());
			if (ordering.isRequiresInstallation()) {
				this.installationTrue.fire();
			}
			if (ordering.isLeasing()) {
				this.leasingTrue.fire();
			}
			this.remark.setText(ordering.getRemark());
		}
	}


	@FXML
	private void initialize() {
		settingsTableView();
		settingListenerForElements();
	}

	@FXML
	private void save() {
		try {
			ordering.setNumber(this.number.getText());
			ordering.setPosition(this.position.getText());
			ordering.setNomenclature(nomenclatureService.findById(this.nomenclature.getText()));
			ordering.setCount(FormatUtils.parseNumber(this.count.getText()));
			ordering.setPrice(FormatUtils.parseNumber(this.price.getText()));
			ordering.setUnit(this.unit.getValue());
			ordering.setRequiresInstallation(this.installationTrue.isSelected());
			ordering.setLeasing(this.leasingTrue.isSelected());
			ordering.setDate(this.date.getValue());
			ordering.setRemark(this.remark.getText());
			ordering.setCost(FormatUtils.parseNumber(this.count.getText()).multiply(FormatUtils.parseNumber(this.price.getText())));

			if (validateFields()) {
				saveHandler.accept(ordering);
				close();
				Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(ADD_OR_EDIT_ORDERING_COMPLETED_SUCCESSFULLY).build().show();
			} else {
				message.setText(EMPTY_FIELDS);
			}
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

	private void settingListenerForElements() {
		search.textProperty().addListener((options, oldValue, newValue) -> reload());
		nomenclatureTableView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				this.nomenclature.setText(this.nomenclatureTableView.getSelectionModel().getSelectedItem().getId());
			}
		});
	}

	private void settingsTableView() {
		this.id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
		this.name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		this.codeKSMId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeKSM().getId()));
		this.codeKSMName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeKSM().getName()));
		this.technicalRequirementId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTechnicalRequirement().getId()));
		this.technicalRequirementName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTechnicalRequirement().getName()));
	}

	private void reload() {
		nomenclatureTableView.getItems().clear();
		nomenclatureTableView.getItems().addAll(this.nomenclatureService.findByAllColumns(search.getText()));
	}

	private boolean validateFields() {
		return !this.nomenclature.getText().isEmpty() &&
			   !this.count.getText().isEmpty() &&
			   !this.price.getText().isEmpty() &&
			   this.date.getValue() != null &&
			   !this.unit.getSelectionModel().isEmpty();
	}

}
