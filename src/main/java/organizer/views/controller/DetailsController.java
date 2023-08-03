package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.Category;
import organizer.model.entities.Ordering;
import organizer.model.services.*;
import organizer.utils.ExcelUtils;
import organizer.utils.ListUtils;
import organizer.utils.TableviewElementUtils;
import organizer.views.controller.common.Dialog;
import organizer.views.controller.popups.OrderingEdit;

import javafx.scene.input.MouseEvent;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static organizer.utils.ExcelUtils.*;
import static organizer.utils.FormatUtils.formatNumber;

@Controller
public class DetailsController {

    @Autowired
    private NomenclatureService nomenclatureService;
    @Autowired
    private OrderingService orderingService;
    @Autowired
    private UnitsService unitsService;
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private CategoryService categoryService;

    private List<Category> categories;

    @FXML
    private TextField search;
    @FXML
    private CheckBox emptyCategory, onlyCreatedOrdering, onlyUncreatedOrdering;
    @FXML
    private RadioButton allEquipment, onlyLeasing, withoutLeasing;

    @FXML
    private ComboBox<String> period, year, category;


    @FXML
    private TableView<Ordering> orderingTableView;
    @FXML
    private TableColumn<Ordering, String> numberCol, countCol;
    @FXML
    private TableColumn<Ordering, String> positionCol;
    @FXML
    private TableColumn<Ordering, String> priceCol, priceWithTaxCol, costCol, costWithTaxCol;
    @FXML
    private TableColumn<Ordering, String> codeKSMCol, nameCol, technicalRequirementCol, unitCol, dateCol, requiresInstallationCol, isLeasingCol, remarkCol;


    @FXML
    private void initialize() {
        categories = categoryService.findAll();

        year.getItems().addAll(ListUtils.getYears());
        LocalDate currentDate = LocalDate.now();
        year.getSelectionModel().select(String.valueOf(currentDate.getYear()));
        period.getItems().addAll(ListUtils.PERIOD);
        period.getSelectionModel().selectFirst();
        category.getItems().addAll("Все категории");
        for (Category c : categories) {
            category.getItems().add(c.getName());
        }
        category.getSelectionModel().selectFirst();

        settingsOrderingTableView();

        year.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> reload());
        period.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> reload());
        category.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> reload());
        search.textProperty().addListener((options, oldValue, newValue) -> reload());
        allEquipment.selectedProperty().addListener((options, oldValue, newValue) -> reload());
        onlyLeasing.selectedProperty().addListener((options, oldValue, newValue) -> reload());
        withoutLeasing.selectedProperty().addListener((options, oldValue, newValue) -> reload());
        emptyCategory.selectedProperty().addListener((options, oldValue, newValue) -> reload());
        onlyCreatedOrdering.selectedProperty().addListener((options, oldValue, newValue) -> {
            reload();
            onlyUncreatedOrdering.setDisable(onlyCreatedOrdering.isSelected());
        });
        onlyUncreatedOrdering.selectedProperty().addListener((options, oldValue, newValue) -> {
            reload();
            onlyCreatedOrdering.setDisable(onlyUncreatedOrdering.isSelected());
        });
        reload();
    }


    @FXML
    private void add() {
        OrderingEdit.addNewOrdering(this::saveOrdering, unitsService::findAll, nomenclatureService);
    }

    @FXML
    private void delete() {
        orderingService.deleteById(orderingTableView.getSelectionModel().getSelectedItem().getId());
        reload();
    }

    @FXML
    private void edit() {
        Ordering ordering = orderingTableView.getSelectionModel().getSelectedItem();
        if (ordering != null) {
            OrderingEdit.editOrdering(ordering, this::saveOrdering, unitsService::findAll, nomenclatureService);
        }
        reload();
    }

    @FXML
    private void uploadOrdering() {
        FileChooser fileChooser = getFileChooser("заявок");
        try {
            File file = fileChooser.showOpenDialog(year.getScene().getWindow());

            ExcelUtils.uploadOrderingsFromExcel(file, nomenclatureService, orderingService, unitsService);
            reload();
            Dialog.DialogBuilder.builder().title("Операция загрузки выполнена").message("Загрузка данных выполнена успешно").build().show();
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title("Операция загрузки прервана!").message("Не был выбран файл для загрузки.").build().show();
        }
    }

    @FXML
    private void unloadingToExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите место сохранения файла");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        try {
            File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());
            unloadOrderingToExcel(year, period, category, allEquipment, onlyLeasing, withoutLeasing, emptyCategory,
                    onlyCreatedOrdering, onlyUncreatedOrdering, orderingTableView, resultsFolder.toString());
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title("Операция выгрузки прервана!").message("Не была выбрана папка для сохранения результатов выгрузки.").build().show();
        }
    }

    @FXML
    private void createTemplates() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите место сохранения шаблонов загрузки");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());

        unloadToOrderingTemplate(year, subcategoryService, orderingTableView, resultsFolder.toString());

    }

    private void reload() {
        orderingTableView.getItems().clear();
        LocalDate dateStart = LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), 1, LocalDate.MIN.getDayOfMonth());
        LocalDate dateEnd = LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), 12, LocalDate.MAX.getDayOfMonth());
        List<Ordering> temp = orderingService.findByYear(dateStart, dateEnd);
        temp = temp.stream().filter(x -> x.getDate().getYear() == Integer.parseInt(year.getValue())).collect(Collectors.toList());
        temp = switch (period.getSelectionModel().getSelectedItem()) {
            case "Январь" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 1).collect(Collectors.toList());
            case "Февраль" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 2).collect(Collectors.toList());
            case "Март" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 3).collect(Collectors.toList());
            case "Апрель" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 4).collect(Collectors.toList());
            case "Май" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 5).collect(Collectors.toList());
            case "Июнь" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 6).collect(Collectors.toList());
            case "Июль" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 7).collect(Collectors.toList());
            case "Август" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 8).collect(Collectors.toList());
            case "Сентябрь" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 9).collect(Collectors.toList());
            case "Октябрь" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 10).collect(Collectors.toList());
            case "Ноябрь" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 11).collect(Collectors.toList());
            case "Декабрь" ->
                    temp.stream().filter(x -> x.getDate().getMonth().getValue() == 12).collect(Collectors.toList());
            default -> temp;
        };

        if (!category.getSelectionModel().getSelectedItem().equals("Все категории")) {
            temp = temp.stream()
                    .filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getSelectionModel().getSelectedItem()))
                    .collect(Collectors.toList());
        }

        if (onlyLeasing.isSelected()) {
            temp = temp.stream().filter(Ordering::isLeasing).collect(Collectors.toList());
        }

        if (withoutLeasing.isSelected()) {
            temp = temp.stream().filter(x -> !x.isLeasing()).collect(Collectors.toList());
        }

        if (!emptyCategory.isSelected()) {
            temp = temp.stream().filter(x -> x.getCount().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
        }
        if (onlyCreatedOrdering.isSelected()) {
            temp = temp.stream().filter(x -> !x.getNumber().equals("")).collect(Collectors.toList());
        }
        if (onlyUncreatedOrdering.isSelected()) {
            temp = temp.stream().filter(x -> x.getNumber().equals("")).collect(Collectors.toList());
        }

        Predicate<Ordering> codeKSM = ordering -> ordering.getNomenclature().getCodeKSM().getId().toLowerCase().contains(search.getText().toLowerCase());
        Predicate<Ordering> codeKSMName = ordering -> ordering.getNomenclature().getCodeKSM().getName().toLowerCase().contains(search.getText().toLowerCase());
        Predicate<Ordering> number = ordering -> ordering.getNumber().toLowerCase().contains(search.getText().toLowerCase());
        Predicate<Ordering> technicalRequirement = ordering -> ordering.getNomenclature().getTechnicalRequirement().getId().toLowerCase().contains(search.getText().toLowerCase());

        temp = temp.stream().filter(number.or(codeKSM).or(codeKSMName).or(technicalRequirement)).collect(Collectors.toList());

        orderingTableView.getItems().addAll(temp);
    }

    private void saveOrdering(Ordering ordering) {
        orderingService.save(ordering);
        reload();
    }

    private void settingsOrderingTableView() {
        numberCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumber())));
        numberCol.setStyle("-fx-alignment: CENTER-LEFT;");
        positionCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPosition())));
        positionCol.setStyle("-fx-alignment: CENTER-LEFT;");
        codeKSMCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getId()));
        codeKSMCol.setStyle("-fx-alignment: CENTER-LEFT;");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getCodeKSM().getName()));
        nameCol.setStyle("-fx-alignment: CENTER-LEFT;");
        technicalRequirementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomenclature().getTechnicalRequirement().getId()));
        technicalRequirementCol.setStyle("-fx-alignment: CENTER-LEFT;");
        unitCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnit().getName()));
        unitCol.setStyle("-fx-alignment: CENTER-LEFT;");
        countCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCount())));
        countCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getPrice())));
        priceCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        priceWithTaxCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getPrice().multiply(BigDecimal.valueOf(1.20)))));
        priceWithTaxCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCost())));
        costCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costWithTaxCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatNumber(cellData.getValue().getCost().multiply(BigDecimal.valueOf(1.20)))));
        costWithTaxCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        dateCol.setStyle("-fx-alignment: CENTER-LEFT;");
        requiresInstallationCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isRequiresInstallation() ? "Да" : "Нет"));
        requiresInstallationCol.setStyle("-fx-alignment: CENTER-LEFT;");
        isLeasingCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isLeasing() ? "Да" : "Нет"));
        isLeasingCol.setStyle("-fx-alignment: CENTER-LEFT;");
        remarkCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRemark()));
        remarkCol.setStyle("-fx-alignment: CENTER-LEFT;");

        orderingTableView.setPlaceholder(new Label("Нет элементов"));

        TableviewElementUtils.setContextMenuForTable(orderingTableView);
        duplicateRow(orderingTableView);
        editOnDoubleClick(orderingTableView);
    }

    private void duplicateRow(final TableView<?> table) {
        changeSelectionModelTableView(table);

        MenuItem duplicate = new MenuItem("Дублировать строку");
        duplicate.setOnAction(action -> {
            Ordering currentOrder = (Ordering) table.getSelectionModel().getSelectedItem();
            Ordering newOrder = new Ordering();
            if (currentOrder != null) {
                newOrder.setNumber(currentOrder.getNumber());
                newOrder.setPosition("");
                newOrder.setNomenclature(currentOrder.getNomenclature());
                newOrder.setUnit(currentOrder.getUnit());
                newOrder.setCount(currentOrder.getCount());
                newOrder.setPrice(currentOrder.getPrice());
                newOrder.setCost(currentOrder.getCost());
                newOrder.setRequiresInstallation(currentOrder.isRequiresInstallation());
                newOrder.setLeasing(currentOrder.isLeasing());
                newOrder.setDate(currentOrder.getDate());
                newOrder.setRemark(currentOrder.getRemark());
            }
            saveOrdering(newOrder);
        });

        table.getContextMenu().getItems().addAll(duplicate);

        changeSelectionModelTableView(table);

    }

    private static void changeSelectionModelTableView(TableView<?> table) {
        if (table.getSelectionModel().isCellSelectionEnabled() && table.getSelectionModel().getSelectionMode() == SelectionMode.MULTIPLE) {
            table.getSelectionModel().setCellSelectionEnabled(false);
            table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } else if (!table.getSelectionModel().isCellSelectionEnabled() && table.getSelectionModel().getSelectionMode() == SelectionMode.SINGLE) {
            table.getSelectionModel().setCellSelectionEnabled(true);
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

    }

    private void editOnDoubleClick(final TableView<?> table) {
        changeSelectionModelTableView(table);

        table.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                Ordering ordering = (Ordering) table.getSelectionModel().getSelectedItem();
                if (ordering != null) {
                    OrderingEdit.editOrdering(ordering, this::saveOrdering, unitsService::findAll, nomenclatureService);
                }
                reload();
            }
        });

        changeSelectionModelTableView(table);


    }

}
