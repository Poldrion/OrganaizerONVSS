package organizer.views.controller.popups;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import organizer.model.OrganizerException;
import organizer.model.entities.Units;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UnitsEdit {

    @FXML
    private Label message;
    @FXML
    private Label title;
    @FXML
    private TextField name;
    @FXML
    private TableView<Units> unitsTableview;

    private Consumer<Units> saveHandler;
    private Supplier<List<Units>> supplierUnits;

    @FXML
    private void initialize() {


    }


    public static void openEditUnits(Consumer<Units> saveHandler, Supplier<List<Units>> supplierUnits) {
        try {
            Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
            FXMLLoader loader = new FXMLLoader(TechnicalRequirementEdit.class.getClassLoader().getResource("views/popups/UnitsEdit.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            UnitsEdit controller = loader.getController();
            controller.init(saveHandler, supplierUnits);

            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(Consumer<Units> saveHandler, Supplier<List<Units>> supplierUnits) {
        this.saveHandler = saveHandler;
        this.supplierUnits = supplierUnits;
        unitsTableview.getItems().addAll(supplierUnits.get());
    }


    @FXML
    private void add() {
        try {
            Units newUnits = new Units();
            newUnits.setName(name.getText());
            List<Units> temp = supplierUnits.get();
            if (!temp.contains(newUnits)) {
                saveHandler.accept(newUnits);
                unitsTableview.getItems().clear();
                unitsTableview.getItems().addAll(supplierUnits.get());
            }
        } catch (OrganizerException e) {
            message.setText(e.getMessage());
        }
    }


    @FXML
    private void close() {
        title.getScene().getWindow().hide();
    }



}
