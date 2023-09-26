package organizer.views.controller.popups;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organizer.model.OrganizerException;
import organizer.model.entities.Category;
import organizer.views.controller.common.Dialog;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import static organizer.utils.Constants.*;

public class CategoryEdit {

    @FXML
    private Label title;
    @FXML
    private Label message;
    @FXML
    private TextField name;

    private Consumer<Category> saveHandler;
    private Category category;

    public static void addNewCategory(Consumer<Category> saveHandler) {
        editCategory(null, saveHandler);
    }

    public static void editCategory(Category category, Consumer<Category> saveHandler) {
        try {
            Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(CategoryEdit.class.getClassLoader().getResource("views/popups/CategoryEdit.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            CategoryEdit edit = loader.getController();
            edit.init(category, saveHandler);
            stage.setResizable(false);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Category category, Consumer<Category> saveHandler) {
        this.saveHandler = saveHandler;

        if (category == null) {
            this.category = new Category();
            this.title.setText(ADD_NEW_CATEGORY);
        } else {
            this.category = category;
            this.title.setText(EDIT_CATEGORY);
            this.name.setText(category.getName());
        }


    }

    @FXML
    private void close() {
        name.getScene().getWindow().hide();
    }

    @FXML
    private void save() {

        try {
            category.setName(name.getText());
            saveHandler.accept(category);
            close();
            Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(ADD_OR_EDIT_CATEGORY_COMPLETED_SUCCESSFULLY).build().show();
        } catch (OrganizerException e) {
            message.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
