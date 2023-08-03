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

import static organizer.utils.ListUtils.YEARS_FOR_COMPARISON;

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


    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
        year.getItems().addAll(ListUtils.getYears());
        bp.getItems().addAll(businessPlanService.findAll());
        count.getItems().addAll(YEARS_FOR_COMPARISON);
        cost.getItems().addAll(YEARS_FOR_COMPARISON);

        bp.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            countMaps.put("Первый год", bp.getValue().getFirstYearCount());
            countMaps.put("Второй год", bp.getValue().getSecondYearCount());
            countMaps.put("Третий год", bp.getValue().getThirdYearCount());
            countMaps.put("Четвертый год", bp.getValue().getForthYearCount());
            countMaps.put("Пятый год", bp.getValue().getFifthYearCount());

            costMaps.put("Первый год", bp.getValue().getFirstYearCost());
            costMaps.put("Второй год", bp.getValue().getSecondYearCost());
            costMaps.put("Третий год", bp.getValue().getThirdYearCost());
            costMaps.put("Четвертый год", bp.getValue().getForthYearCost());
            costMaps.put("Пятый год", bp.getValue().getFifthYearCost());
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
            comparisonID.setText("Нет информации");
            comparisonBP.setText("Нет информации");
        }
    }


    @FXML
    private void saveProperties() {
        ListUtils.setFirstYear(firstYear.getText());
        ListUtils.setCountYears(countYears.getText());
        propertiesMessage.setTextFill(Color.GREEN);
        propertiesMessage.setText("Настройки успешно сохранены.");
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
            unitMessage.setText(String.format("Единица измерения с ID %s удалена.", idUnits.getText()));
        } catch (EmptyResultDataAccessException e) {
            unitMessage.setTextFill(Color.RED);
            unitMessage.setText(String.format("Единица измерения с ID %s не найдена.", idUnits.getText()));
        }
        idUnits.clear();
    }

    @FXML
    private void saveBP() {
        try {
            Comparison comparison = new Comparison();
            comparison.setId(year.getValue());
            comparison.setBusinessPlan(bp.getValue());
            comparison.setCount(countMaps.get(count.getValue()));
            comparison.setCost(costMaps.get(cost.getValue()));

            comparisonService.save(comparison);

            comparisonMessage.setTextFill(Color.GREEN);
            comparisonMessage.setText("Изменения успешно сохранены.");
            settingsComparisonLabel();
        } catch (Exception e) {
            comparisonMessage.setTextFill(Color.RED);
            comparisonMessage.setText("Произошла ошибка! Изменения не сохранены.");
            settingsComparisonLabel();
        }
    }

    @FXML
    private void backupDB() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите место сохранения файла");
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
            Dialog.DialogBuilder.builder().title("Операция резервирования прервана!").message("Не была выбрана папка для сохранения резервной копии.").build().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
