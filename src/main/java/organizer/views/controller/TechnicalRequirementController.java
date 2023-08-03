package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.TechnicalRequirement;
import organizer.model.services.TechnicalRequirementService;
import organizer.utils.ExcelUtils;
import organizer.utils.TableviewElementUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.TechnicalRequirementEdit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import static organizer.utils.ExcelUtils.getFileChooser;
import static organizer.utils.ExcelUtils.unloadTRToExcel;


@Controller
public class TechnicalRequirementController {

    @Autowired
    private TechnicalRequirementService technicalRequirementService;
    private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");


    @FXML
    private TableView<TechnicalRequirement> technicalRequirementContainer;
    @FXML
    private TextField search;

    @FXML
    private TableColumn<TechnicalRequirement, String> dateCreateCol;

    @FXML
    private void initialize() {
        dateCreateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCreate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        reload();
        search.textProperty().addListener((options, oldValue, newValue) -> {
            technicalRequirementContainer.getItems().clear();
            technicalRequirementContainer.getItems().addAll(technicalRequirementService.findAllBySomeColumn(newValue));
        });

        technicalRequirementContainer.setPlaceholder(new Label("Нет элементов"));

        TableviewElementUtils.setContextMenuForTable(technicalRequirementContainer);
    }


    private void saveTechnicalRequirement(TechnicalRequirement technicalRequirement) {
        technicalRequirementService.save(technicalRequirement);
        reload();
    }

    private void reload() {
        technicalRequirementContainer.getItems().clear();
        technicalRequirementContainer.getItems().addAll(technicalRequirementService.findAll());

    }

    @FXML
    private void addNewTechnicalRequirement() {
        TechnicalRequirementEdit.addNewTechnicalRequirement(this::saveTechnicalRequirement);
    }

    @FXML
    private void deleteTechnicalRequirement() {
        technicalRequirementService.delete(technicalRequirementContainer.getSelectionModel().getSelectedItem().getId());
        reload();
    }

    @FXML
    private void editTechnicalRequirement() {
        TechnicalRequirement technicalRequirement = technicalRequirementContainer.getSelectionModel().getSelectedItem();
        if (technicalRequirement != null) {
            TechnicalRequirementEdit.editTechnicalRequirement(technicalRequirement, this::saveTechnicalRequirement);
        }

    }

    @FXML
    private void unloadingToExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите место сохранения файла");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        try {
            File resultsFolder = directoryChooser.showDialog(technicalRequirementContainer.getScene().getWindow());
            unloadTRToExcel(technicalRequirementService, resultsFolder.toString());


        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title("Операция выгрузки прервана!").message("Не была выбрана папка для сохранения результатов выгрузки.").build().show();
        }
    }

    @FXML
    private void uploadTechnicalRequirement(){
        FileChooser fileChooser = getFileChooser("ОЛ/ТТ");
        try {
            File file = fileChooser.showOpenDialog(technicalRequirementContainer.getScene().getWindow());

            ExcelUtils.uploadTechnicalRequirementFromExcel(file, technicalRequirementService);
            reload();
            Dialog.DialogBuilder.builder().title("Операция загрузки выполнена").message("Загрузка данных выполнена успешно").build().show();
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title("Операция загрузки прервана!").message("Не был выбран файл для загрузки.").build().show();
        }
    }

}
