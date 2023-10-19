package organizer.views.controller.popups;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organizer.model.OrganizerException;
import organizer.model.entities.Category;
import organizer.model.entities.Subcategory;
import organizer.views.controller.common.Dialog;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static organizer.utils.Constants.*;

public class SubcategoryEdit {
	@FXML
	private Label title, message;
	@FXML
	private TextField name;
	@FXML
	private ComboBox<Category> category;

	private Subcategory subcategory;
	private Consumer<Subcategory> saveHandler;

	public static void addNewSubcategory(Consumer<Subcategory> saveHandler, Supplier<List<Category>> supplier) {
		edit(null, saveHandler, supplier);
	}

	public static void edit(Subcategory subcategory, Consumer<Subcategory> saveHandler, Supplier<List<Category>> supplier) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(CodeKSMEdit.class.getClassLoader().getResource("views/popups/SubcategoryEdit.fxml"));
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);

			SubcategoryEdit controller = loader.getController();
			controller.init(subcategory, saveHandler, supplier);

			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Subcategory subcategory, Consumer<Subcategory> saveHandler, Supplier<List<Category>> supplier) {
		this.saveHandler = saveHandler;
		category.getItems().addAll(supplier.get());

		if (subcategory == null) {
			title.setText(ADD_NEW_SUBCATEGORY);
			this.subcategory = new Subcategory();
		} else {
			title.setText(EDIT_SUBCATEGORY);
			this.subcategory = subcategory;
		}
		category.setValue(this.subcategory.getCategory());
		name.setText(this.subcategory.getName());
	}

	@FXML
	private void close() {
		name.getScene().getWindow().hide();
	}

	@FXML
	private void save() {
		try {
			subcategory.setCategory(category.getValue());
			subcategory.setName(name.getText());
			saveHandler.accept(subcategory);
			close();
			Dialog.DialogBuilder.builder().title(COMPLETED_SUCCESSFULLY).message(ADD_OR_EDIT_SUBCATEGORY_COMPLETED_SUCCESSFULLY).build().show();
		} catch (OrganizerException e) {
			message.setText(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
