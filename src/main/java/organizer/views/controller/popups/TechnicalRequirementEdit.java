package organizer.views.controller.popups;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import organizer.model.OrganizerException;
import organizer.model.entities.TechnicalRequirement;
import organizer.views.controller.common.Dialog;

import java.util.function.Consumer;

public class TechnicalRequirementEdit {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField codeKSM;
    @FXML
    private TextField codeKSMName;
    @FXML
    private TextField fileTRName;
    @FXML
    private TextField systemNumberTransaction;
    @FXML
    private DatePicker dateCreate;
    @FXML
    private Label message;
    @FXML
    private Label title;

    private TechnicalRequirement technicalRequirement;
    private Consumer<TechnicalRequirement> saveHandler;

    public static void addNewTechnicalRequirement(Consumer<TechnicalRequirement> saveHandler) {
        editTechnicalRequirement(null, saveHandler);
    }

    public static void editTechnicalRequirement(TechnicalRequirement technicalRequirement, Consumer<TechnicalRequirement> saveHandler) {
        try {
            Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
            FXMLLoader loader = new FXMLLoader(TechnicalRequirementEdit.class.getClassLoader().getResource("views/popups/TechnicalRequirementEdit.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            TechnicalRequirementEdit controller = loader.getController();
            controller.init(technicalRequirement, saveHandler);

            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(TechnicalRequirement technicalRequirement, Consumer<TechnicalRequirement> saveHandler) {
        this.saveHandler = saveHandler;

        if (technicalRequirement == null) {
            this.technicalRequirement = new TechnicalRequirement();
            this.title.setText("Добавить новую карточку ОЛ/ТТ");
        } else {
            this.technicalRequirement = technicalRequirement;
            this.title.setText("Редактировать текущую карточку ОЛ/ТТ");
            this.id.setText(technicalRequirement.getId());
            this.name.setText(technicalRequirement.getName());
            this.codeKSM.setText(technicalRequirement.getCodeKSM());
            this.codeKSMName.setText(technicalRequirement.getCodeKSM());
            this.fileTRName.setText(technicalRequirement.getFileTRName());
            this.systemNumberTransaction.setText(technicalRequirement.getSystemNumberTransaction());
            this.dateCreate.setValue(technicalRequirement.getDateCreate());
        }
    }

    @FXML
    private void close() {
        name.getScene().getWindow().hide();
    }

    @FXML
    private void save() {
        try {
            technicalRequirement.setId(this.id.getText());
            technicalRequirement.setName(this.name.getText());
            technicalRequirement.setCodeKSM(this.codeKSM.getText());
            technicalRequirement.setCodeKSMName(this.codeKSMName.getText());
            technicalRequirement.setFileTRName(this.fileTRName.getText());
            technicalRequirement.setSystemNumberTransaction(this.systemNumberTransaction.getText());
            technicalRequirement.setDateCreate(this.dateCreate.getValue());

            saveHandler.accept(technicalRequirement);
            close();
            Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Создание/изменение карточки ОЛ/ТТ выполнено успешно.").build().show();
        } catch (OrganizerException e) {
            message.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
