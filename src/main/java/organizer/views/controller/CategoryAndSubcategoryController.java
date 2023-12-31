package organizer.views.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.Category;
import organizer.model.entities.Subcategory;
import organizer.model.services.CategoryService;
import organizer.model.services.SubcategoryService;
import organizer.utils.ExcelUtils;
import organizer.utils.TableviewElementUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.CategoryEdit;
import organizer.views.controller.popups.SubcategoryEdit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static organizer.utils.Constants.*;
import static organizer.utils.ExcelUtils.*;

@Controller
public class CategoryAndSubcategoryController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SubcategoryService subcategoryService;

	@FXML
	private TableView<Category> categoryContainer;
	@FXML
	private TableView<Subcategory> subcategoryContainer;

	@FXML
	private void initialize() {
		reload();

		settingListenerForElements();

		categoryContainer.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));
		subcategoryContainer.setPlaceholder(new Label(ELEMENTS_NOT_FOUND));

		TableviewElementUtils.setContextMenuForTable(categoryContainer);
		TableviewElementUtils.setContextMenuForTable(subcategoryContainer);
	}

	@FXML
	private void addNewCategory() {
		CategoryEdit.addNewCategory(this::saveCategory);
		try {
			saveCategoryList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@FXML
	private void addNewSubcategory() {
		SubcategoryEdit.addNewSubcategory(this::saveSubcategory, categoryService::findAll);
	}

	@FXML
	private void deleteCategory() {
		categoryService.delete(categoryContainer.getSelectionModel().getSelectedItem().getId());
		try {
			saveCategoryList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		reload();
	}

	@FXML
	private void deleteSubcategory() {
		subcategoryService.delete(subcategoryContainer.getSelectionModel().getSelectedItem().getId());
		reload();
	}

	@FXML
	private void editSubcategory() {
		Subcategory subcategory = subcategoryContainer.getSelectionModel().getSelectedItem();
		if (subcategory != null) {
			SubcategoryEdit.edit(subcategory, this::saveSubcategory, categoryService::findAll);
		}
	}

	@FXML
	private void unloadingCategoryToExcel() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

		try {
			File resultsFolder = directoryChooser.showDialog(categoryContainer.getScene().getWindow());
			unloadCategoryToExcel(categoryService, resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	@FXML
	private void unloadingSubcategoryToExcel() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

		try {
			File resultsFolder = directoryChooser.showDialog(categoryContainer.getScene().getWindow());
			unloadSubcategoryToExcel(subcategoryService, resultsFolder.toString());
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(UNLOADING_OPERATION_ABORTED).message(CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED).build().show();
		}
	}

	@FXML
	private void editCategory() {
		Category category = categoryContainer.getSelectionModel().getSelectedItem();
		if (category != null) {
			CategoryEdit.editCategory(category, this::saveCategory);
		}
	}

	@FXML
	private void uploadCategory() {
		FileChooser fileChooser = getFileChooser(CATEGORIES_UPLOAD_TITLE_SUFFIX);
		try {
			File file = fileChooser.showOpenDialog(categoryContainer.getScene().getWindow());

			ExcelUtils.uploadCategoryFromExcel(file, categoryService);
			reload();
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
		}
	}

	@FXML
	private void uploadSubcategory() {
		FileChooser fileChooser = getFileChooser(SUBCATEGORIES_UPLOAD_TITLE_SUFFIX);

		try {
			File file = fileChooser.showOpenDialog(categoryContainer.getScene().getWindow());
			ExcelUtils.uploadSubcategoryFromExcel(file, subcategoryService, categoryService);
			reload();
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_COMPLETED).message(DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY).build().show();
		} catch (NullPointerException e) {
			Dialog.DialogBuilder.builder().title(DOWNLOAD_OPERATION_ABORTED).message(FILE_FOR_DOWNLOAD_NOT_SELECTED).build().show();
		}
	}

	private void reload() {
		categoryContainer.getItems().clear();
		categoryContainer.getItems().addAll(categoryService.findAll());
		searchSubcategory();
	}

	private void searchSubcategory() {
		subcategoryContainer.getItems().clear();
		Category category = categoryContainer.getSelectionModel().getSelectedItem();
		List<Subcategory> listSubcategories = subcategoryService.search(category);
		subcategoryContainer.getItems().addAll(listSubcategories);
		categoryContainer.getSelectionModel().select(category);
	}

	private void saveCategoryList() throws IOException {
		List<Category> categoryList = categoryService.findAll();
		Properties properties = new Properties();
		InputStream fis = new FileInputStream("properties/categories.properties");
		InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
		properties.load(reader);
		int oldCount = Integer.parseInt(properties.getProperty("counts"));

		properties.setProperty("counts", String.valueOf(categoryList.size()));
		int newCount = Integer.parseInt(properties.getProperty("counts"));

		if (newCount < oldCount) {
			for (int i = 0; i < oldCount; i++) {
				properties.remove("category" + i);
			}
		}

		for (Category c : categoryList) {
			properties.setProperty("category" + categoryList.indexOf(c), c.getName());
		}
		OutputStream fos = new FileOutputStream("properties/categories.properties");
		OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
		properties.store(writer, null);

		fis.close();
		reader.close();
		fos.close();
		writer.close();
	}

	private void saveSubcategory(Subcategory subcategory) {
		subcategoryService.save(subcategory);
		reload();
	}

	private void saveCategory(Category category) {
		categoryService.save(category);
		reload();
		try {
			saveCategoryList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void settingListenerForElements() {
		categoryContainer.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
				Category category = categoryContainer.getSelectionModel().getSelectedItem();
				if (category != null) {
					searchSubcategory();
				}
			}

			if (event.getClickCount() == 2) {
				Category category = categoryContainer.getSelectionModel().getSelectedItem();
				if (category != null) {
					CategoryEdit.editCategory(category, this::saveCategory);
				}
			}
		});
	}

}
