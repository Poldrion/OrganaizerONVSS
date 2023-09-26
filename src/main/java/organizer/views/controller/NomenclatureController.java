package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.Category;
import organizer.model.entities.Nomenclature;
import organizer.model.entities.Subcategory;
import organizer.model.services.*;
import organizer.utils.ExcelUtils;
import organizer.utils.TableviewElementUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.NomenclatureEdit;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static organizer.utils.Constants.*;
import static organizer.utils.ExcelUtils.getFileChooser;
import static organizer.utils.ExcelUtils.unloadNomenclatureToExcel;

@Controller
public class NomenclatureController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private NomenclatureService nomenclatureService;
    @Autowired
    private CodeKSMService codeKSMService;
    @Autowired
    private TechnicalRequirementService technicalRequirementService;

    @FXML
    private ComboBox<Category> category;
    @FXML
    private TableView<Subcategory> subcategoryTableView;
    @FXML
    private TableView<Nomenclature> nomenclatureTableView;
    @FXML
    private TextField search;
    @FXML
    private TableColumn<Nomenclature, String> codeKSMId;
    @FXML
    private TableColumn<Nomenclature, String> codeKSMName;
    @FXML
    private TableColumn<Nomenclature, String> technicalRequirementId;
    @FXML
    private TableColumn<Nomenclature, String> technicalRequirementName;

    private Subcategory currentSubcategory;


    @FXML
    private void initialize() {
        category.getItems().clear();
        category.getItems().addAll(categoryService.findAll());
        category.getSelectionModel().selectFirst();
        searchSubcategory(category.getSelectionModel().getSelectedItem());
        codeKSMId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeKSM().getId()));
        codeKSMName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeKSM().getName()));
        technicalRequirementId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTechnicalRequirement().getId()));
        technicalRequirementName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTechnicalRequirement().getName()));

        category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> searchSubcategory(newValue));

        subcategoryTableView.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            currentSubcategory = newValue;
            reload();
        });

        search.textProperty().addListener((options, oldValue, newValue) -> {
            reload();
        });

        subcategoryTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));
        nomenclatureTableView.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));

        TableviewElementUtils.setContextMenuForTable(subcategoryTableView);
        TableviewElementUtils.setContextMenuForTable(nomenclatureTableView);
    }

    @FXML
    private void addNewNomenclature() {
        NomenclatureEdit.addNewObject(this::saveNomenclature, categoryService::findAll,
                codeKSMService::findAll, technicalRequirementService::findAll,
                codeKSMService, technicalRequirementService);
        reload();
    }

    private void saveNomenclature(Nomenclature nomenclature) {
        nomenclatureService.save(nomenclature);
        reload();
    }

    @FXML
    private void deleteNomenclature() {
        nomenclatureService.delete(nomenclatureTableView.getSelectionModel().getSelectedItem().getId());
        reload();
    }

    @FXML
    private void editNomenclature() {
        Nomenclature nomenclature = nomenclatureTableView.getSelectionModel().getSelectedItem();
        if (nomenclature != null) {
            NomenclatureEdit.edit(nomenclature, this::saveNomenclature, categoryService::findAll,
                    codeKSMService::findAll, technicalRequirementService::findAll,
                    codeKSMService, technicalRequirementService);
        }
        reload();
    }


    private void searchSubcategory(Category category) {
        subcategoryTableView.getItems().clear();
        List<Subcategory> subcategoryList = subcategoryService.search(category);
        subcategoryTableView.getItems().addAll(subcategoryList);
    }

    private void searchNomenclature(Subcategory subcategory) {
        nomenclatureTableView.getItems().clear();

        Predicate<Nomenclature> codeKSM = nomenclature ->
                nomenclature.getCodeKSM().getId().toLowerCase().contains(search.getText().toLowerCase());
        Predicate<Nomenclature> codeKSMName = ordering ->
                ordering.getCodeKSM().getName().toLowerCase().contains(search.getText().toLowerCase());
        Predicate<Nomenclature> technicalRequirementID = ordering ->
                ordering.getTechnicalRequirement().getId().toLowerCase().contains(search.getText().toLowerCase());
        Predicate<Nomenclature> technicalRequirementName = ordering ->
                ordering.getTechnicalRequirement().getName().toLowerCase().contains(search.getText().toLowerCase());

        List<Nomenclature> nomenclatureList = nomenclatureService.searchBySubcategory(subcategory);
        nomenclatureList = nomenclatureList.stream()
                .filter(codeKSM.or(codeKSMName).or(technicalRequirementID).or(technicalRequirementName))
                .collect(Collectors.toList());
        nomenclatureTableView.getItems().addAll(nomenclatureList);
    }

    private void reload() {
        nomenclatureTableView.getItems().clear();
        subcategoryTableView.getSelectionModel().select(currentSubcategory);
        searchNomenclature(currentSubcategory);
    }

    @FXML
    private void unloadingToExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        try {
            File resultsFolder = directoryChooser.showDialog(nomenclatureTableView.getScene().getWindow());
            unloadNomenclatureToExcel(nomenclatureService, resultsFolder.toString());
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
        }
    }

    @FXML
    private void uploadNomenclature() {
        FileChooser fileChooser = getFileChooser(NOMENCLATURES_UPLOAD_TITLE_SUFFIX);
        try {
            File file = fileChooser.showOpenDialog(nomenclatureTableView.getScene().getWindow());
            ExcelUtils.uploadNomenclatureFromExcel(file, nomenclatureService, codeKSMService, technicalRequirementService, subcategoryService);
            reload();
            Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
        }
    }


}
