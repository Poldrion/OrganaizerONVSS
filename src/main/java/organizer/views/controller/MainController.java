package organizer.views.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import organizer.model.entities.Category;
import organizer.model.entities.Comparison;
import organizer.model.entities.Ordering;
import organizer.model.entities.Units;
import organizer.model.services.CategoryService;
import organizer.model.services.ComparisonService;
import organizer.model.services.OrderingService;
import organizer.utils.ListUtils;
import organizer.utils.TableviewElementUtils;
import organizer.views.controller.common.Dialog;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static organizer.utils.ExcelUtils.unloadMainTableToExcel;
import static organizer.utils.FormatUtils.*;

@Controller
public class MainController {

    @Autowired
    private OrderingService orderingService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ComparisonService comparisonService;

    private List<Ordering> orderingStorage;
    private Comparison comparison;

    @FXML
    private ComboBox<String> year;
    @FXML
    private ComboBox<String> period;
    @FXML
    private Label businessPlanGeneral, businessPlanLeasing, businessPlanWithoutLeasing;
    @FXML
    private Label businessPlanGeneralPercent, businessPlanLeasingPercent, businessPlanWithoutLeasingPercent;
    @FXML
    private Label factGeneral, factLeasing, factWithoutLeasing;
    @FXML
    private Label remainsGeneral, remainsLeasing, remainsWithoutLeasing;
    @FXML
    private Label offsetGeneral, offsetLeasing, offsetWithoutLeasing;
    @FXML
    private ProgressBar businessPlanGeneralPB, businessPlanLeasingPB, businessPlanWithoutLeasingPB;
    @FXML
    private TableView<Category> mainTableView;
    @FXML
    private TableColumn<Category, String> categoryCol;
    @FXML
    private TableColumn<Category, String> unitsCol, countCol, costCol, costWithTaxCol;
    @FXML
    private TableColumn<Category, String> countPlanCol, costPlanCol, costWithTaxPlanCol;
    @FXML
    private TableColumn<Category, String> countRemainsCol, costRemainsCol, costWithTaxRemainsCol;
    @FXML
    private TableColumn<Category, String> percentCol;

    @FXML
    private void initialize() {
        mainTableView.getItems().clear();

        if (categoryService.findAll().size() != 0) {
            mainTableView.getItems().addAll(categoryService.findAll());
        }
        year.getItems().addAll(ListUtils.getYears());
        LocalDate currentDate = LocalDate.now();
        year.getSelectionModel().select(String.valueOf(currentDate.getYear()));
        period.getItems().addAll(ListUtils.PERIOD);
        period.getSelectionModel().selectFirst();


        year.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            reload();
            mainTableView.refresh();
        });
        period.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            reload();
            mainTableView.refresh();
        });

        try {
            comparison = comparisonService.searchById(year.getValue());
        } catch (NoSuchElementException e) {
            Dialog.DialogBuilder.builder().title("Ошибка!!!").message("Не выбрана версия БП для текущего года").build().show();
        }
        if (comparison != null) {
            settingMainTableView();
            settingsLabelsAndProgressBars();
        }
    }

    private void settingsLabelsAndProgressBars() {
        businessPlanGeneral.setText(formatNumber(comparison.getGeneralCost()));
        businessPlanLeasing.setText(formatNumber(comparison.getLeasingCost()));
        businessPlanWithoutLeasing.setText(formatNumber(comparison.getCostWithoutLeasing()));

        businessPlanGeneralPercent.setText(formatNumber(getPercentGeneralBP().multiply(BigDecimal.valueOf(100))) + "%");
        businessPlanLeasingPercent.setText(formatNumber(getPercentLeasingBP().multiply(BigDecimal.valueOf(100))) + "%");
        businessPlanWithoutLeasingPercent.setText(formatNumber(getPercentBPWithoutLeasing().multiply(BigDecimal.valueOf(100))) + "%");

        businessPlanGeneralPB.setProgress(getPercentGeneralBP().doubleValue());
        businessPlanLeasingPB.setProgress(getPercentLeasingBP().doubleValue());
        businessPlanWithoutLeasingPB.setProgress(getPercentBPWithoutLeasing().doubleValue());

        factGeneral.setText(getCostWithTaxByYearAndPeriodFact());
        factLeasing.setText(getCostWithTaxByYearAndPeriodFactLeasing());
        factWithoutLeasing.setText(getCostWithTaxByYearAndPeriodFactWithoutLeasing());

        remainsGeneral.setText(getCostWithTaxByYearAndPeriodRemains());
        remainsLeasing.setText(getCostWithTaxByYearAndPeriodRemainsLeasing());
        remainsWithoutLeasing.setText(getCostWithTaxByYearAndPeriodRemainsWithoutLeasing());

        offsetGeneral.setText(formatNumber(parseNumber(businessPlanGeneral.getText())
                .subtract(parseNumber(factGeneral.getText()))
                .subtract(parseNumber(remainsGeneral.getText()))));
        offsetLeasing.setText(formatNumber(parseNumber(businessPlanLeasing.getText())
                .subtract(parseNumber(factLeasing.getText()))
                .subtract(parseNumber(remainsLeasing.getText()))));
        offsetWithoutLeasing.setText(formatNumber(parseNumber(businessPlanWithoutLeasing.getText())
                .subtract(parseNumber(factWithoutLeasing.getText()))
                .subtract(parseNumber(remainsWithoutLeasing.getText()))));

    }

    private void settingMainTableView() {
        reload();
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        unitsCol.setCellValueFactory(cellData -> new SimpleStringProperty(getUnit(cellData.getValue())));
        unitsCol.setStyle("-fx-alignment: CENTER;");
        // Бизнес-план
        countPlanCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCountPlan(cellData.getValue())));
        countPlanCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costPlanCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCostWithoutTaxPlan(cellData.getValue())));
        costPlanCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costWithTaxPlanCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCostPlan(cellData.getValue())));
        costWithTaxPlanCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        // Фактическая передача (указаны номера заявок и позиций
        countCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCountByYearAndPeriodFact(cellData.getValue())));
        countCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCostByYearAndPeriodFact(cellData.getValue())));
        costCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costWithTaxCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCostWithTaxByYearAndPeriodFact(cellData.getValue())));
        costWithTaxCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        // Остаток для передачи (отсутствуют номера заявок и позиций)
        countRemainsCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCountByYearAndPeriodRemains(cellData.getValue())));
        countRemainsCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costRemainsCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCostByYearAndPeriodRemains(cellData.getValue())));
        costRemainsCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        costWithTaxRemainsCol.setCellValueFactory(cellData -> new SimpleStringProperty(getCostWithTaxByYearAndPeriodRemains(cellData.getValue())));
        costWithTaxRemainsCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        // Процент передачи потребности по категориям
        percentCol.setCellValueFactory(cellData -> new SimpleStringProperty(getPercent(cellData.getValue())));
        percentCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        mainTableView.setPlaceholder(new Label("Нет элементов"));

        TableviewElementUtils.setContextMenuForTable(mainTableView);
    }

    @FXML
    void unloadingToExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите место сохранения файла");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        try {
            File resultsFolder = directoryChooser.showDialog(year.getScene().getWindow());

            unloadMainTableToExcel(year, period, businessPlanGeneral, businessPlanLeasing, businessPlanWithoutLeasing, businessPlanGeneralPercent,
                    businessPlanLeasingPercent, businessPlanWithoutLeasingPercent,
                    factGeneral, factLeasing, factWithoutLeasing,
                    remainsGeneral, remainsLeasing, remainsWithoutLeasing,
                    offsetGeneral, offsetLeasing, offsetWithoutLeasing,
                    mainTableView, categoryCol, unitsCol, countCol, costCol,
                    costWithTaxCol, countPlanCol, costPlanCol, costWithTaxPlanCol, countRemainsCol, costRemainsCol, costWithTaxRemainsCol, percentCol, resultsFolder.toString());
        } catch (NullPointerException e) {
            Dialog.DialogBuilder.builder().title("Операция выгрузки прервана!").message("Не была выбрана папка для сохранения результатов выгрузки.").build().show();
        }
    }

    private void reload() {
        LocalDate dateStart = getDateStartOrEndPeriod(1, LocalDate.MIN);
        LocalDate dateEnd = getDateStartOrEndPeriod(12, LocalDate.MAX);
        this.orderingStorage = orderingService.findByYear(dateStart, dateEnd);
        this.orderingStorage = getPeriodFilterList(this.orderingStorage);
        try {
            comparison = comparisonService.searchById(year.getValue());
        } catch (NoSuchElementException e) {
            Dialog.DialogBuilder.builder().title("Ошибка!!!").message("Не выбрана версия БП для текущего года").build().show();
        }
        settingsLabelsAndProgressBars();
    }


    private List<Ordering> getPeriodFilterList(List<Ordering> currentList) {
        return switch (period.getSelectionModel().getSelectedItem()) {
            case "Январь" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 1).collect(Collectors.toList());
            case "Февраль" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 2).collect(Collectors.toList());
            case "Март" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 3).collect(Collectors.toList());
            case "Апрель" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 4).collect(Collectors.toList());
            case "Май" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 5).collect(Collectors.toList());
            case "Июнь" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 6).collect(Collectors.toList());
            case "Июль" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 7).collect(Collectors.toList());
            case "Август" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 8).collect(Collectors.toList());
            case "Сентябрь" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 9).collect(Collectors.toList());
            case "Октябрь" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 10).collect(Collectors.toList());
            case "Ноябрь" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 11).collect(Collectors.toList());
            case "Декабрь" ->
                    currentList.stream().filter(x -> x.getDate().getMonth().getValue() == 12).collect(Collectors.toList());
            default -> currentList;
        };
    }

    // Для фактически переданной потребности

    /**
     * Метод возвращает строковое представление суммы по полю count объекта Ordering
     *
     * @param category категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     */
    private @NotNull String getCountByYearAndPeriodFact(Category category) {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCount;
        BigDecimal count = getBigDecimalFact(category, totalMapper);
        return formatNumber(count);
    }

    /**
     * Метод возвращает строковое представление суммы по полю cost объекта Ordering без учета НДС 20%
     *
     * @param category категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     */
    private @NotNull String getCostByYearAndPeriodFact(Category category) {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getBigDecimalFact(category, totalMapper);
        return formatNumber(cost);
    }

    /**
     * Метод возвращает строковое представление суммы по полю cost объекта Ordering с учетом НДС 20%
     *
     * @param category категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     */

    private @NotNull String getCostWithTaxByYearAndPeriodFact(Category category) {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getBigDecimalFact(category, totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    /**
     * Метод возвращает сумму значений BigDecimal для заявок в заданной категории в соотвествии с заданной фунцией (например по полю count объекта Ordering)
     *
     * @param category    категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     * @param totalMapper функция, указывающая поле объекта Ordering, по котороу необходимо получить сумму. <p>Пример:</p><p>Function<Ordering, BigDecimal> totalMapper = Ordering::getCount;<p/>
     */
    private BigDecimal getBigDecimalFact(Category category, Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderingsByCategory = orderingStorage.stream()
                .filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getName()))
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return orderingsByCategory.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getLeasingOrderingFact(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> leasingOrderings = orderingStorage.stream()
                .filter(Ordering::isLeasing)
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return leasingOrderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getOrderingWithoutLeasingFact(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> leasingOrderings = orderingStorage.stream()
                .filter(x -> !x.isLeasing())
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return leasingOrderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getFactOrdering(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> leasingOrderings = orderingStorage.stream()
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return leasingOrderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Для остатка передачи

    /**
     * Метод возвращает строковое представление суммы по полю count объекта Ordering
     *
     * @param category категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     */
    private @NotNull String getCountByYearAndPeriodRemains(Category category) {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCount;
        BigDecimal count = getBigDecimalRemains(category, totalMapper);
        return formatNumber(count);
    }

    /**
     * Метод возвращает строковое представление суммы по полю cost объекта Ordering без учета НДС 20%
     *
     * @param category категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     */
    private @NotNull String getCostByYearAndPeriodRemains(Category category) {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getBigDecimalRemains(category, totalMapper);
        return formatNumber(cost);
    }

    /**
     * Метод возвращает строковое представление суммы по полю cost объекта Ordering с учетом НДС 20%
     *
     * @param category категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     */

    private @NotNull String getCostWithTaxByYearAndPeriodRemains(Category category) {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getBigDecimalRemains(category, totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    /**
     * Метод возвращает сумму значений BigDecimal для заявок в заданной категории в соотвествии с заданной фунцией (например по полю count объекта Ordering)
     *
     * @param category    категория, для которой необходимо получить сумму по заданному полю объекта Ordering
     * @param totalMapper функция, указывающая поле объекта Ordering, по котороу необходимо получить сумму. <p>Пример:</p><p>Function<Ordering, BigDecimal> totalMapper = Ordering::getCount;<p/>
     */
    private BigDecimal getBigDecimalRemains(Category category, Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderingsByCategory = orderingStorage.stream()
                .filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getName()))
                .filter(x -> x.getNumber().equals(""))
                .toList();
        return orderingsByCategory.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String getUnit(Category category) {
        List<Ordering> orderingsByCategory = orderingStorage.stream()
                .filter(x -> x.getNomenclature().getSubcategory().getCategory().getName().equals(category.getName()))
                .toList();
        HashMap<Units, Integer> unitsInCategory = new HashMap<>();
        for (Ordering o : orderingsByCategory) {
            if (!unitsInCategory.containsKey(o.getUnit())) {
                unitsInCategory.put(o.getUnit(), 1);
            } else {
                unitsInCategory.put(o.getUnit(), unitsInCategory.get(o.getUnit()) + 1);
            }
        }
        Units unitsMaxCount = null;
        int maxCount = 0;
        for (Units u : unitsInCategory.keySet()) {
            if (unitsInCategory.get(u) > maxCount) {
                unitsMaxCount = u;
                maxCount = unitsInCategory.get(u);
            }
        }
        return unitsMaxCount != null ? unitsMaxCount.getName() : "";
    }

    private @NotNull String getCountPlan(@NotNull Category category) {
        HashMap<String, BigDecimal> temp = comparison.getCount();
        return formatNumber(temp.get(category.getName()));
    }

    private @NotNull String getCostPlan(@NotNull Category category) {
        HashMap<String, BigDecimal> temp = comparison.getCost();
        return formatNumber(temp.get(category.getName()));
    }

    private @NotNull String getCostWithoutTaxPlan(@NotNull Category category) {
        HashMap<String, BigDecimal> temp = comparison.getCost();
        return formatNumber(temp.get(category.getName()).divide(BigDecimal.valueOf(1.20), 2, RoundingMode.HALF_UP));
    }

    private @NotNull String getPercent(Category category) {
        try {
            BigDecimal result = parseNumber(getCostWithTaxByYearAndPeriodFact(category))
                    .divide(parseNumber(getCostPlan(category)), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            return formatNumber(result) + "%";
        } catch (ArithmeticException e) {
            return "0%";
        }
    }

    private @NotNull BigDecimal getPercentGeneralBP() {
        return getFactOrdering(Ordering::getCost)
                .multiply(BigDecimal.valueOf(1.2))
                .divide(comparison.getGeneralCost(), 4, RoundingMode.HALF_UP);
    }

    private @NotNull BigDecimal getPercentLeasingBP() {
        return getLeasingOrderingFact(Ordering::getCost)
                .multiply(BigDecimal.valueOf(1.2))
                .divide(comparison.getLeasingCost(), 4, RoundingMode.HALF_UP);
    }

    private @NotNull BigDecimal getPercentBPWithoutLeasing() {
        return getOrderingWithoutLeasingFact(Ordering::getCost)
                .multiply(BigDecimal.valueOf(1.2))
                .divide(comparison.getCostWithoutLeasing(), 4, RoundingMode.HALF_UP);
    }

    //********************************************************************************************

    // Факт
    private @NotNull String getCostWithTaxByYearAndPeriodFact() {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getFactGeneral(totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    private BigDecimal getFactGeneral(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderings = orderingStorage.stream()
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return orderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private @NotNull String getCostWithTaxByYearAndPeriodFactLeasing() {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getFactLeasing(totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    private BigDecimal getFactLeasing(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderings = orderingStorage.stream()
                .filter(Ordering::isLeasing)
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return orderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private @NotNull String getCostWithTaxByYearAndPeriodFactWithoutLeasing() {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getFactWithoutLeasing(totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    private BigDecimal getFactWithoutLeasing(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderings = orderingStorage.stream()
                .filter(ordering -> !ordering.isLeasing())
                .filter(x -> !x.getNumber().equals(""))
                .toList();
        return orderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Осталось
    private @NotNull String getCostWithTaxByYearAndPeriodRemains() {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getRemainsGeneral(totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    private BigDecimal getRemainsGeneral(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderings = orderingStorage.stream()
                .filter(x -> x.getNumber().equals(""))
                .toList();
        return orderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private @NotNull String getCostWithTaxByYearAndPeriodRemainsLeasing() {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getRemainsLeasing(totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    private BigDecimal getRemainsLeasing(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderings = orderingStorage.stream()
                .filter(Ordering::isLeasing)
                .filter(x -> x.getNumber().equals(""))
                .toList();
        return orderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private @NotNull String getCostWithTaxByYearAndPeriodRemainsWithoutLeasing() {
        Function<Ordering, BigDecimal> totalMapper = Ordering::getCost;
        BigDecimal cost = getRemainsWithoutLeasing(totalMapper);
        return formatNumber(cost.multiply(BigDecimal.valueOf(1.20)));
    }

    private BigDecimal getRemainsWithoutLeasing(Function<Ordering, BigDecimal> totalMapper) {
        List<Ordering> orderings = orderingStorage.stream()
                .filter(ordering -> !ordering.isLeasing())
                .filter(x -> x.getNumber().equals(""))
                .toList();
        return orderings.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalDate getDateStartOrEndPeriod(int month, @NotNull LocalDate firstOrLastDayMonth) {
        return LocalDate.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()), month, firstOrLastDayMonth.getDayOfMonth());
    }
}
