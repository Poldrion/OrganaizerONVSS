package organizer.views.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import organizer.model.entities.BusinessPlan;
import organizer.model.entities.Comparison;
import organizer.model.entities.Units;
import organizer.model.repositories.AccountRepository;
import organizer.model.services.BusinessPlanService;
import organizer.model.services.ComparisonService;
import organizer.model.services.UnitsService;
import organizer.utils.CurrentData;
import organizer.utils.ListUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.UnitsEdit;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.NoSuchElementException;

import static organizer.utils.Constants.*;

@Controller
public class SettingsController {

    @Autowired
    private UnitsService unitsService;
    @Autowired
    private ComparisonService comparisonService;
    @Autowired
    private BusinessPlanService businessPlanService;

    @Autowired
    private AccountRepository accountRepository;

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_FOR_SHORT_DATE);

    @FXML
    private TextField countYears;
    @FXML
    private TextField firstYear;
    @FXML
    private TextField idUnits;
    @FXML
    private Label unitMessage;
    @FXML
    private ComboBox<String> year;
    @FXML
    private ComboBox<BusinessPlan> bp;
    @FXML
    private ComboBox<String> count;
    @FXML
    private ComboBox<String> cost;

    @FXML
    private Label comparisonID, comparisonBP, comparisonMessage, propertiesMessage;

    private final HashMap<String, HashMap<String, BigDecimal>> countMaps = new HashMap<>();
    private final HashMap<String, HashMap<String, BigDecimal>> costMaps = new HashMap<>();

    @FXML
    private void initialize() {
        firstYear.setText(String.valueOf(ListUtils.getFirstYear()));
        countYears.setText(ListUtils.getCountYears());
        year.getItems().addAll(CurrentData.YEARS);
        bp.getItems().addAll(businessPlanService.findAll());

        count.getItems().addAll(YEARS_FOR_COMPARISON);
        cost.getItems().addAll(YEARS_FOR_COMPARISON);

        bp.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            countMaps.put(FIRST_YEAR, bp.getValue().getFirstYearCount());
            countMaps.put(SECOND_YEAR, bp.getValue().getSecondYearCount());
            countMaps.put(THIRD_YEAR, bp.getValue().getThirdYearCount());
            countMaps.put(FOURTH_YEAR, bp.getValue().getForthYearCount());
            countMaps.put(FIFTH_YEAR, bp.getValue().getFifthYearCount());

            costMaps.put(FIRST_YEAR, bp.getValue().getFirstYearCost());
            costMaps.put(SECOND_YEAR, bp.getValue().getSecondYearCost());
            costMaps.put(THIRD_YEAR, bp.getValue().getThirdYearCost());
            costMaps.put(FOURTH_YEAR, bp.getValue().getForthYearCost());
            costMaps.put(FIFTH_YEAR, bp.getValue().getFifthYearCost());
        });

        year.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> settingsComparisonLabel());

        /* Базовые цвета приложения
        #E38B29
        #F1A661
        #FFD8A9
        #FDEEDC
        #AD2B03
         */


    }

    private void settingsComparisonLabel() {
        try {
            comparisonID.setText(comparisonService.searchById(year.getValue()).getId());
            comparisonBP.setText(comparisonService.searchById(year.getValue()).getBusinessPlan().getId());
        } catch (NoSuchElementException e) {
            comparisonID.setText(INFORMATION_NOT_FOUND);
            comparisonBP.setText(INFORMATION_NOT_FOUND);
        }
    }


    @FXML
    private void saveProperties() {
        ListUtils.setFirstYear(firstYear.getText());
        ListUtils.setCountYears(countYears.getText());
        propertiesMessage.setTextFill(Color.GREEN);
        propertiesMessage.setText(SETTINGS_SAVE_SUCCESSFULLY);
    }

    @FXML
    private void openUnits() {
        UnitsEdit.openEditUnits(this::saveUnits, unitsService::findAll);
    }

    private void saveUnits(Units units) {
        unitsService.save(units);
    }

    @FXML
    private void deleteUnits() {
        try {
            unitsService.deleteById(Integer.parseInt(idUnits.getText()));
            unitMessage.setTextFill(Color.GREEN);
            unitMessage.setText(String.format(STRING_FORMAT_FOR_REMOVE_UNIT, idUnits.getText()));
        } catch (EmptyResultDataAccessException e) {
            unitMessage.setTextFill(Color.RED);
            unitMessage.setText(String.format(STRING_FORMAT_FOR_UNFOUNDED_UNIT, idUnits.getText()));
        }
        idUnits.clear();
    }

    @FXML
    private void saveBP() {
        try {
            Comparison comparison = new Comparison();
            comparison.setId(String.valueOf(year.getValue()));
            comparison.setBusinessPlan(bp.getValue());
            comparison.setCount(countMaps.get(count.getValue()));
            comparison.setCost(costMaps.get(cost.getValue()));

            comparisonService.save(comparison);

            comparisonMessage.setTextFill(Color.GREEN);
            comparisonMessage.setText(CHANGES_SAVE_SUCCESSFULLY);
            settingsComparisonLabel();
        } catch (Exception e) {
            comparisonMessage.setTextFill(Color.RED);
            comparisonMessage.setText(CHANGES_SAVE_ERROR);
            settingsComparisonLabel();
        }
    }

    @FXML
    private void backupDB() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(CHOOSE_PLACE_FOR_SAVE_FILE);
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        try {
            File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());
            String pathString = resultsFolder + "/" + LocalDate.now().format(dtf);

            Path path = Path.of(pathString);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            accountRepository.backupDB(pathString + "/backup.zip");
        } catch (NullPointerException | NoSuchFileException e) {
            Dialog.DialogBuilder.builder().title(BACKUP_ABORTED).message(CHOOSE_FOLDER_FOR_BACKUP_NOT_SELECTED).build().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
