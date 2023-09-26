package organizer.views.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.CodeKSM;
import organizer.model.services.CodeKSMService;
import organizer.utils.ExcelUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.CodeKSMEdit;

import java.io.File;

import static organizer.utils.Constants.*;
import static organizer.utils.ExcelUtils.getFileChooser;
import static organizer.utils.ExcelUtils.unloadCodeKSMToExcel;

@Controller
public class CodeKSMController {

    @Autowired
    private CodeKSMService codeKSMService;

    @FXML
    private TableView<CodeKSM> codeKSMContainer;
    @FXML
    private TextField search;


    @FXML
    private void initialize() {
        reload();
        search.textProperty().addListener((options, oldValue, newValue) -> {
            codeKSMContainer.getItems().clear();
            codeKSMContainer.getItems().addAll(codeKSMService.findByAllColumns(newValue));
        });
        codeKSMContainer.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));
    }

    private void reload() {
        codeKSMContainer.getItems().clear();
        codeKSMContainer.getItems().addAll(codeKSMService.findAll());
    }

    @FXML
    private void addNewCodeKSM() {
        CodeKSMEdit.addNewCodeKSM(this::saveKSM);
        reload();

    }

    private void saveKSM(CodeKSM codeKSM) {
        codeKSMService.save(codeKSM);
        reload();
    }

    @FXML
    private void deleteCodeKSM() {
        codeKSMService.delete(codeKSMContainer.getSelectionModel().getSelectedItem().getId());
        reload();

    }

    @FXML
    private void editCodeKSM() {
        CodeKSM codeKSM = codeKSMContainer.getSelectionModel().getSelectedItem();
        if (codeKSM != null) {
            CodeKSMEdit.edit(codeKSM, this::saveKSM);
        }
        reload();
    }

    @FXML
    private void unloadingToExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        try {
            File resultsFolder = directoryChooser.showDialog(codeKSMContainer.getScene().getWindow());
            unloadCodeKSMToExcel(codeKSMService, resultsFolder.toString());

        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
        }
    }

    @FXML
    private void uploadCodeKSM() {
        FileChooser fileChooser = getFileChooser(CODES_KSM_UPLOAD_TITLE_SUFFIX);
        try {
            File file = fileChooser.showOpenDialog(codeKSMContainer.getScene().getWindow());
            ExcelUtils.uploadCodeKSMFromExcel(file, codeKSMService);
            reload();
            Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
        }
    }

}
