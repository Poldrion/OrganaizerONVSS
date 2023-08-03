package organizer.views.controller.popups;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import organizer.model.OrganizerException;
import organizer.model.entities.*;
import organizer.model.services.CodeKSMService;
import organizer.model.services.TechnicalRequirementService;
import organizer.views.controller.common.Dialog;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Controller
public class NomenclatureEdit {

    @FXML
    private TextField codeKSM;
    @FXML
    private TextField technicalRequirement;
    @FXML
    private Label title;
    @FXML
    private Label message;
    @FXML
    private ComboBox<Subcategory> subcategory;
    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField searchByKSMTF, searchByTRTF;
    @FXML
    private TableView<CodeKSM> codeKSMTableView;
    @FXML
    private TableColumn<CodeKSM, String> codeKSMIdCol, codeKSMNameCol;
    @FXML
    private TableView<TechnicalRequirement> TRTableView;
    @FXML
    private TableColumn<TechnicalRequirement, String> TRIdCol, TRNameCol;


    private Consumer<Nomenclature> saveHandler;
    private Nomenclature nomenclature;
    private Supplier<List<CodeKSM>> supplierCodeKSM;
    private Supplier<List<TechnicalRequirement>> supplierTechnicalRequirement;
    private CodeKSMService codeKSMService;
    private TechnicalRequirementService technicalRequirementService;


    public static void addNewObject(Consumer<Nomenclature> saveHandler, Supplier<List<Category>> supplierCategory, Supplier<List<CodeKSM>> supplierCodeKSM, Supplier<List<TechnicalRequirement>> supplierTechnicalRequirement, CodeKSMService codeKSMService, TechnicalRequirementService technicalRequirementService) {
        edit(null, saveHandler, supplierCategory, supplierCodeKSM, supplierTechnicalRequirement, codeKSMService, technicalRequirementService);
    }


    public static void edit(Nomenclature nomenclature, Consumer<Nomenclature> saveHandler, Supplier<List<Category>> supplierCategory, Supplier<List<CodeKSM>> supplierCodeKSM, Supplier<List<TechnicalRequirement>> supplierTechnicalRequirement, CodeKSMService codeKSMService, TechnicalRequirementService technicalRequirementService) {
        try {
            Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
            FXMLLoader loader = new FXMLLoader(NomenclatureEdit.class.getClassLoader().getResource("views/popups/NomenclatureEdit.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            NomenclatureEdit controller = loader.getController();
            controller.init(nomenclature, saveHandler, supplierCategory, supplierCodeKSM, supplierTechnicalRequirement, codeKSMService, technicalRequirementService);

            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init(Nomenclature nomenclature, Consumer<Nomenclature> saveHandler, Supplier<List<Category>> supplierCategory, Supplier<List<CodeKSM>> supplierCodeKSM, Supplier<List<TechnicalRequirement>> supplierTechnicalRequirement, CodeKSMService codeKSMService, TechnicalRequirementService technicalRequirementService) {
        this.saveHandler = saveHandler;
        this.supplierCodeKSM = supplierCodeKSM;
        this.supplierTechnicalRequirement = supplierTechnicalRequirement;
        this.codeKSMService = codeKSMService;
        this.technicalRequirementService = technicalRequirementService;

        codeKSMTableView.getItems().addAll(this.codeKSMService.findAll());
        TRTableView.getItems().addAll(technicalRequirementService.findAll());

        category.getItems().addAll(supplierCategory.get());
        Category currentCategory = category.getItems().get(0);
        category.setValue(currentCategory);

        subcategory.getItems().addAll(currentCategory.getSubcategories());
        subcategory.getSelectionModel().selectFirst();

        category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            subcategory.getItems().clear();
            List<Subcategory> subcategoryList = newValue.getSubcategories().stream().toList();
            subcategory.getItems().addAll(subcategoryList);
            subcategory.getSelectionModel().selectFirst();
        });


        if (nomenclature == null) {
            this.nomenclature = new Nomenclature();
            this.title.setText("Добавление новой номенклатуры");
        } else {
            this.nomenclature = nomenclature;
            this.title.setText("Редактирование текущей номенклатуры");
            this.category.setValue(nomenclature.getSubcategory().getCategory());
            this.subcategory.setValue(nomenclature.getSubcategory());
            this.codeKSM.setText(nomenclature.getCodeKSM().getId());
            this.technicalRequirement.setText(nomenclature.getTechnicalRequirement().getId());
        }
    }

    @FXML
    private void close() {
        codeKSM.getScene().getWindow().hide();
    }

    @FXML
    private void save() {

        try {
            CodeKSM tempCodeKSM = null;
            TechnicalRequirement tempTechnicalRequirement = null;
            try {
                tempCodeKSM = this.supplierCodeKSM.get()
                        .stream()
                        .filter(x -> x.getId().equals(this.codeKSM.getText()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException e) {
                message.setText(String.format("Указанный Код КСМ №%s не найден.", codeKSM.getText()));
            }

            try {
                tempTechnicalRequirement = this.supplierTechnicalRequirement.get()
                        .stream()
                        .filter(x -> x.getId().equals(this.technicalRequirement.getText()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException e) {
                message.setText(String.format("Указанная карточка ОЛ/ТТ №%s не найдена.", technicalRequirement.getText()));
            }
            try {
                if (technicalRequirement.getText().isEmpty() || technicalRequirement.getText() == null) {
                    nomenclature.setId(tempCodeKSM.getId());
                } else {
                    nomenclature.setId(tempCodeKSM.getId() + "_" + tempTechnicalRequirement.getId());
                }
                nomenclature.setTechnicalRequirement(tempTechnicalRequirement);
                nomenclature.setName(tempCodeKSM.getName());
                nomenclature.setSubcategory(this.subcategory.getValue());
                nomenclature.setCodeKSM(tempCodeKSM);
                saveHandler.accept(nomenclature);
                close();
            } catch (NullPointerException e) {
                message.setText(message.getText() + "\nПроверьте правильность ввода кода КСМ и номера карточки ОЛ/ТТ.");
            }
            Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Создание/изменение номенклатуры выполнено успешно.").build().show();
        } catch (OrganizerException e) {
            message.setText(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            message.setText(String.format("Указанный ОЛ/ТТ №%s уже используется с другим КСМ.", technicalRequirement.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        settingsCodeKSMTableView();
        settingsTRTableView();
        searchByKSMTF.textProperty().addListener((options, oldValue, newValue) -> reloadCodeKSM());
        searchByTRTF.textProperty().addListener((options, oldValue, newValue) -> reloadTR());

    }

    private void settingsCodeKSMTableView() {
        codeKSMIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        codeKSMNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        codeKSMTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                this.codeKSM.setText(this.codeKSMTableView.getSelectionModel().getSelectedItem().getId());
            }
        });
    }

    private void settingsTRTableView() {
        TRIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        TRNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TRTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                this.technicalRequirement.setText(this.TRTableView.getSelectionModel().getSelectedItem().getId());
            }
        });
    }

    private void reloadCodeKSM() {
        codeKSMTableView.getItems().clear();
        codeKSMTableView.getItems().addAll(this.codeKSMService.findByAllColumns(searchByKSMTF.getText()));
    }

    private void reloadTR() {
        TRTableView.getItems().clear();
        TRTableView.getItems().addAll(this.technicalRequirementService.findAllBySomeColumn(searchByTRTF.getText()));
    }

}
