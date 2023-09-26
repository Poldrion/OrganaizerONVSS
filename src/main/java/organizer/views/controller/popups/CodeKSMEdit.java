package organizer.views.controller.popups;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organizer.model.OrganizerException;
import organizer.model.entities.CodeKSM;
import organizer.views.controller.common.Dialog;

import java.util.function.Consumer;

import static organizer.utils.Constants.*;

public class CodeKSMEdit {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private Label message;
    @FXML
    private Label title;

    private CodeKSM codeKSM;
    private Consumer<CodeKSM> saveHandler;


    public static void addNewCodeKSM(Consumer<CodeKSM> saveHandler) {
        edit(null, saveHandler);
    }

    public static void edit(CodeKSM codeKSM, Consumer<CodeKSM> saveHandler) {
        try {
            Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
            FXMLLoader loader = new FXMLLoader(CodeKSMEdit.class.getClassLoader().getResource("views/popups/CodeKSMEdit.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            CodeKSMEdit controller = loader.getController();
            controller.init(codeKSM, saveHandler);

            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void save() {
        try {
            codeKSM.setId(id.getText());
            codeKSM.setName(name.getText());

            saveHandler.accept(codeKSM);
            close();
            Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(ADD_OR_EDIT_CODE_KSM_COMPLETED_SUCCESSFULLY).build().show();
        } catch (OrganizerException e) {
            message.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void close() {
        name.getScene().getWindow().hide();
    }

    private void init(CodeKSM codeKSM, Consumer<CodeKSM> saveHandler) {

        this.codeKSM = codeKSM;
        this.saveHandler = saveHandler;


        if (codeKSM == null) {
            title.setText(ADD_NEW_CODE_KSM);
            this.codeKSM = new CodeKSM();
        } else {
            title.setText(EDIT_CODE_KSM);
            this.codeKSM = codeKSM;
        }

        id.setText(this.codeKSM.getId());
        name.setText(this.codeKSM.getName());
    }


}
