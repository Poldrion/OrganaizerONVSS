package organizer.views.controller.popups;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organizer.model.OrganizerException;
import organizer.model.entities.BusinessPlan;
import organizer.views.controller.common.Dialog;

import java.util.function.Consumer;

import static organizer.utils.FormatUtils.formatNumber;
import static organizer.utils.FormatUtils.parseNumber;

public class BusinessPlanEdit {
    @FXML
    private Label message, title;
    @FXML
    private TextField firstYear;
    @FXML
    private TextField id;

    @FXML
    private TextField firstYearCost, firstYearCost1, firstYearCost2, firstYearCost3, firstYearCost4, firstYearCost5,
            firstYearCost6, firstYearCost7, firstYearCost8, firstYearCost9, firstYearCost10, firstYearCost11,
            firstYearCost12, firstYearCost13, firstYearCost14, firstYearCost15, firstYearCost16, firstYearCost17,
            firstYearCost18, firstYearCost19, firstYearCost20, firstYearCost21, firstYearCost22, firstYearCost23,
            firstYearCost24, firstYearCost25;

    @FXML
    private TextField firstYearCount, firstYearCount1, firstYearCount2, firstYearCount3, firstYearCount4, firstYearCount5,
            firstYearCount6, firstYearCount7, firstYearCount8, firstYearCount9, firstYearCount10, firstYearCount11,
            firstYearCount12, firstYearCount13, firstYearCount14, firstYearCount15, firstYearCount16, firstYearCount17,
            firstYearCount18, firstYearCount19, firstYearCount20, firstYearCount21, firstYearCount22, firstYearCount23,
            firstYearCount24, firstYearCount25;

    @FXML
    private TextField secondYearCost, secondYearCost1, secondYearCost2, secondYearCost3, secondYearCost4, secondYearCost5,
            secondYearCost6, secondYearCost7, secondYearCost8, secondYearCost9, secondYearCost10, secondYearCost11,
            secondYearCost12, secondYearCost13, secondYearCost14, secondYearCost15, secondYearCost16, secondYearCost17,
            secondYearCost18, secondYearCost19, secondYearCost20, secondYearCost21, secondYearCost22, secondYearCost23,
            secondYearCost24, secondYearCost25;

    @FXML
    private TextField secondYearCount, secondYearCount1, secondYearCount2, secondYearCount3, secondYearCount4, secondYearCount5,
            secondYearCount6, secondYearCount7, secondYearCount8, secondYearCount9, secondYearCount10, secondYearCount11,
            secondYearCount12, secondYearCount13, secondYearCount14, secondYearCount15, secondYearCount16, secondYearCount17,
            secondYearCount18, secondYearCount19, secondYearCount20, secondYearCount21, secondYearCount22, secondYearCount23,
            secondYearCount24, secondYearCount25;

    @FXML
    private TextField thirdYearCost, thirdYearCost1, thirdYearCost2, thirdYearCost3, thirdYearCost4, thirdYearCost5,
            thirdYearCost6, thirdYearCost7, thirdYearCost8, thirdYearCost9, thirdYearCost10, thirdYearCost11,
            thirdYearCost12, thirdYearCost13, thirdYearCost14, thirdYearCost15, thirdYearCost16, thirdYearCost17,
            thirdYearCost18, thirdYearCost19, thirdYearCost20, thirdYearCost21, thirdYearCost22, thirdYearCost23,
            thirdYearCost24, thirdYearCost25;

    @FXML
    private TextField thirdYearCount, thirdYearCount1, thirdYearCount2, thirdYearCount3, thirdYearCount4, thirdYearCount5,
            thirdYearCount6, thirdYearCount7, thirdYearCount8, thirdYearCount9, thirdYearCount10, thirdYearCount11,
            thirdYearCount12, thirdYearCount13, thirdYearCount14, thirdYearCount15, thirdYearCount16, thirdYearCount17,
            thirdYearCount18, thirdYearCount19, thirdYearCount20, thirdYearCount21, thirdYearCount22, thirdYearCount23,
            thirdYearCount24, thirdYearCount25;

    @FXML
    private TextField forthYearCost, forthYearCost1, forthYearCost2, forthYearCost3, forthYearCost4, forthYearCost5,
            forthYearCost6, forthYearCost7, forthYearCost8, forthYearCost9, forthYearCost10, forthYearCost11,
            forthYearCost12, forthYearCost13, forthYearCost14, forthYearCost15, forthYearCost16, forthYearCost17,
            forthYearCost18, forthYearCost19, forthYearCost20, forthYearCost21, forthYearCost22, forthYearCost23,
            forthYearCost24, forthYearCost25;

    @FXML
    private TextField forthYearCount, forthYearCount1, forthYearCount2, forthYearCount3, forthYearCount4, forthYearCount5,
            forthYearCount6, forthYearCount7, forthYearCount8, forthYearCount9, forthYearCount10, forthYearCount11,
            forthYearCount12, forthYearCount13, forthYearCount14, forthYearCount15, forthYearCount16, forthYearCount17,
            forthYearCount18, forthYearCount19, forthYearCount20, forthYearCount21, forthYearCount22, forthYearCount23,
            forthYearCount24, forthYearCount25;

    @FXML
    private TextField fifthYearCost, fifthYearCost1, fifthYearCost2, fifthYearCost3, fifthYearCost4, fifthYearCost5,
            fifthYearCost6, fifthYearCost7, fifthYearCost8, fifthYearCost9, fifthYearCost10, fifthYearCost11,
            fifthYearCost12, fifthYearCost13, fifthYearCost14, fifthYearCost15, fifthYearCost16, fifthYearCost17,
            fifthYearCost18, fifthYearCost19, fifthYearCost20, fifthYearCost21, fifthYearCost22, fifthYearCost23,
            fifthYearCost24, fifthYearCost25;

    @FXML
    private TextField fifthYearCount, fifthYearCount1, fifthYearCount2, fifthYearCount3, fifthYearCount4, fifthYearCount5,
            fifthYearCount6, fifthYearCount7, fifthYearCount8, fifthYearCount9, fifthYearCount10, fifthYearCount11,
            fifthYearCount12, fifthYearCount13, fifthYearCount14, fifthYearCount15, fifthYearCount16, fifthYearCount17,
            fifthYearCount18, fifthYearCount19, fifthYearCount20, fifthYearCount21, fifthYearCount22, fifthYearCount23,
            fifthYearCount24, fifthYearCount25;

    @FXML
    private Label category, category1, category2, category3, category4, category5, category6, category7, category8,
            category9, category10, category11, category12, category13, category14, category15, category16, category17,
            category18, category19, category20, category21, category22, category23, category24, category25;

    private Consumer<BusinessPlan> saveHandler;
    private BusinessPlan businessPlan;

    public static void addNewBPGeneral(Consumer<BusinessPlan> saveHandler) {
        editBPGeneral(null, saveHandler);
    }

    public static void editBPGeneral(BusinessPlan businessPlan, Consumer<BusinessPlan> saveHandler) {
        try {
            Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(CategoryEdit.class.getClassLoader().getResource("views/popups/BusinessPlanEdit.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            BusinessPlanEdit edit = loader.getController();
            edit.init(businessPlan, saveHandler);
            stage.setResizable(false);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(BusinessPlan businessPlan, Consumer<BusinessPlan> saveHandler) {
        this.saveHandler = saveHandler;

        if (businessPlan == null) {
            this.businessPlan = new BusinessPlan();
            this.title.setText("Добавить новую версию бизнес-плана");
        } else {
            this.businessPlan = businessPlan;
            this.title.setText("Редактировать версию бизнес-плана");
            this.id.setText(businessPlan.getId());
            this.firstYear.setText(String.valueOf(businessPlan.getFirstYear()));
            this.firstYearCount.setText(formatNumber(businessPlan.getFirstYearCount().get(category.getText())));
            this.firstYearCount1.setText(formatNumber(businessPlan.getFirstYearCount().get(category1.getText())));
            this.firstYearCount2.setText(formatNumber(businessPlan.getFirstYearCount().get(category2.getText())));
            this.firstYearCount3.setText(formatNumber(businessPlan.getFirstYearCount().get(category3.getText())));
            this.firstYearCount4.setText(formatNumber(businessPlan.getFirstYearCount().get(category4.getText())));
            this.firstYearCount5.setText(formatNumber(businessPlan.getFirstYearCount().get(category5.getText())));
            this.firstYearCount6.setText(formatNumber(businessPlan.getFirstYearCount().get(category6.getText())));
            this.firstYearCount7.setText(formatNumber(businessPlan.getFirstYearCount().get(category7.getText())));
            this.firstYearCount8.setText(formatNumber(businessPlan.getFirstYearCount().get(category8.getText())));
            this.firstYearCount9.setText(formatNumber(businessPlan.getFirstYearCount().get(category9.getText())));
            this.firstYearCount10.setText(formatNumber(businessPlan.getFirstYearCount().get(category10.getText())));
            this.firstYearCount11.setText(formatNumber(businessPlan.getFirstYearCount().get(category11.getText())));
            this.firstYearCount12.setText(formatNumber(businessPlan.getFirstYearCount().get(category12.getText())));
            this.firstYearCount13.setText(formatNumber(businessPlan.getFirstYearCount().get(category13.getText())));
            this.firstYearCount14.setText(formatNumber(businessPlan.getFirstYearCount().get(category14.getText())));
            this.firstYearCount15.setText(formatNumber(businessPlan.getFirstYearCount().get(category15.getText())));
            this.firstYearCount16.setText(formatNumber(businessPlan.getFirstYearCount().get(category16.getText())));
            this.firstYearCount17.setText(formatNumber(businessPlan.getFirstYearCount().get(category17.getText())));
            this.firstYearCount18.setText(formatNumber(businessPlan.getFirstYearCount().get(category18.getText())));
            this.firstYearCount19.setText(formatNumber(businessPlan.getFirstYearCount().get(category19.getText())));
            this.firstYearCount20.setText(formatNumber(businessPlan.getFirstYearCount().get(category20.getText())));
            this.firstYearCount21.setText(formatNumber(businessPlan.getFirstYearCount().get(category21.getText())));
            this.firstYearCount22.setText(formatNumber(businessPlan.getFirstYearCount().get(category22.getText())));
            this.firstYearCount23.setText(formatNumber(businessPlan.getFirstYearCount().get(category23.getText())));
            this.firstYearCount24.setText(formatNumber(businessPlan.getFirstYearCount().get(category24.getText())));
            this.firstYearCount25.setText(formatNumber(businessPlan.getFirstYearCount().get(category25.getText())));

            this.firstYearCost.setText(formatNumber(businessPlan.getFirstYearCost().get(category.getText())));
            this.firstYearCost1.setText(formatNumber(businessPlan.getFirstYearCost().get(category1.getText())));
            this.firstYearCost2.setText(formatNumber(businessPlan.getFirstYearCost().get(category2.getText())));
            this.firstYearCost3.setText(formatNumber(businessPlan.getFirstYearCost().get(category3.getText())));
            this.firstYearCost4.setText(formatNumber(businessPlan.getFirstYearCost().get(category4.getText())));
            this.firstYearCost5.setText(formatNumber(businessPlan.getFirstYearCost().get(category5.getText())));
            this.firstYearCost6.setText(formatNumber(businessPlan.getFirstYearCost().get(category6.getText())));
            this.firstYearCost7.setText(formatNumber(businessPlan.getFirstYearCost().get(category7.getText())));
            this.firstYearCost8.setText(formatNumber(businessPlan.getFirstYearCost().get(category8.getText())));
            this.firstYearCost9.setText(formatNumber(businessPlan.getFirstYearCost().get(category9.getText())));
            this.firstYearCost10.setText(formatNumber(businessPlan.getFirstYearCost().get(category10.getText())));
            this.firstYearCost11.setText(formatNumber(businessPlan.getFirstYearCost().get(category11.getText())));
            this.firstYearCost12.setText(formatNumber(businessPlan.getFirstYearCost().get(category12.getText())));
            this.firstYearCost13.setText(formatNumber(businessPlan.getFirstYearCost().get(category13.getText())));
            this.firstYearCost14.setText(formatNumber(businessPlan.getFirstYearCost().get(category14.getText())));
            this.firstYearCost15.setText(formatNumber(businessPlan.getFirstYearCost().get(category15.getText())));
            this.firstYearCost16.setText(formatNumber(businessPlan.getFirstYearCost().get(category16.getText())));
            this.firstYearCost17.setText(formatNumber(businessPlan.getFirstYearCost().get(category17.getText())));
            this.firstYearCost18.setText(formatNumber(businessPlan.getFirstYearCost().get(category18.getText())));
            this.firstYearCost19.setText(formatNumber(businessPlan.getFirstYearCost().get(category19.getText())));
            this.firstYearCost20.setText(formatNumber(businessPlan.getFirstYearCost().get(category20.getText())));
            this.firstYearCost21.setText(formatNumber(businessPlan.getFirstYearCost().get(category21.getText())));
            this.firstYearCost22.setText(formatNumber(businessPlan.getFirstYearCost().get(category22.getText())));
            this.firstYearCost23.setText(formatNumber(businessPlan.getFirstYearCost().get(category23.getText())));
            this.firstYearCost24.setText(formatNumber(businessPlan.getFirstYearCost().get(category24.getText())));
            this.firstYearCost25.setText(formatNumber(businessPlan.getFirstYearCost().get(category25.getText())));

            this.secondYearCount.setText(formatNumber(businessPlan.getSecondYearCount().get(category.getText())));
            this.secondYearCount1.setText(formatNumber(businessPlan.getSecondYearCount().get(category1.getText())));
            this.secondYearCount2.setText(formatNumber(businessPlan.getSecondYearCount().get(category2.getText())));
            this.secondYearCount3.setText(formatNumber(businessPlan.getSecondYearCount().get(category3.getText())));
            this.secondYearCount4.setText(formatNumber(businessPlan.getSecondYearCount().get(category4.getText())));
            this.secondYearCount5.setText(formatNumber(businessPlan.getSecondYearCount().get(category5.getText())));
            this.secondYearCount6.setText(formatNumber(businessPlan.getSecondYearCount().get(category6.getText())));
            this.secondYearCount7.setText(formatNumber(businessPlan.getSecondYearCount().get(category7.getText())));
            this.secondYearCount8.setText(formatNumber(businessPlan.getSecondYearCount().get(category8.getText())));
            this.secondYearCount9.setText(formatNumber(businessPlan.getSecondYearCount().get(category9.getText())));
            this.secondYearCount10.setText(formatNumber(businessPlan.getSecondYearCount().get(category10.getText())));
            this.secondYearCount11.setText(formatNumber(businessPlan.getSecondYearCount().get(category11.getText())));
            this.secondYearCount12.setText(formatNumber(businessPlan.getSecondYearCount().get(category12.getText())));
            this.secondYearCount13.setText(formatNumber(businessPlan.getSecondYearCount().get(category13.getText())));
            this.secondYearCount14.setText(formatNumber(businessPlan.getSecondYearCount().get(category14.getText())));
            this.secondYearCount15.setText(formatNumber(businessPlan.getSecondYearCount().get(category15.getText())));
            this.secondYearCount16.setText(formatNumber(businessPlan.getSecondYearCount().get(category16.getText())));
            this.secondYearCount17.setText(formatNumber(businessPlan.getSecondYearCount().get(category17.getText())));
            this.secondYearCount18.setText(formatNumber(businessPlan.getSecondYearCount().get(category18.getText())));
            this.secondYearCount19.setText(formatNumber(businessPlan.getSecondYearCount().get(category19.getText())));
            this.secondYearCount20.setText(formatNumber(businessPlan.getSecondYearCount().get(category20.getText())));
            this.secondYearCount21.setText(formatNumber(businessPlan.getSecondYearCount().get(category21.getText())));
            this.secondYearCount22.setText(formatNumber(businessPlan.getSecondYearCount().get(category22.getText())));
            this.secondYearCount23.setText(formatNumber(businessPlan.getSecondYearCount().get(category23.getText())));
            this.secondYearCount24.setText(formatNumber(businessPlan.getSecondYearCount().get(category24.getText())));
            this.secondYearCount25.setText(formatNumber(businessPlan.getSecondYearCount().get(category25.getText())));

            this.secondYearCost.setText(formatNumber(businessPlan.getSecondYearCost().get(category.getText())));
            this.secondYearCost1.setText(formatNumber(businessPlan.getSecondYearCost().get(category1.getText())));
            this.secondYearCost2.setText(formatNumber(businessPlan.getSecondYearCost().get(category2.getText())));
            this.secondYearCost3.setText(formatNumber(businessPlan.getSecondYearCost().get(category3.getText())));
            this.secondYearCost4.setText(formatNumber(businessPlan.getSecondYearCost().get(category4.getText())));
            this.secondYearCost5.setText(formatNumber(businessPlan.getSecondYearCost().get(category5.getText())));
            this.secondYearCost6.setText(formatNumber(businessPlan.getSecondYearCost().get(category6.getText())));
            this.secondYearCost7.setText(formatNumber(businessPlan.getSecondYearCost().get(category7.getText())));
            this.secondYearCost8.setText(formatNumber(businessPlan.getSecondYearCost().get(category8.getText())));
            this.secondYearCost9.setText(formatNumber(businessPlan.getSecondYearCost().get(category9.getText())));
            this.secondYearCost10.setText(formatNumber(businessPlan.getSecondYearCost().get(category10.getText())));
            this.secondYearCost11.setText(formatNumber(businessPlan.getSecondYearCost().get(category11.getText())));
            this.secondYearCost12.setText(formatNumber(businessPlan.getSecondYearCost().get(category12.getText())));
            this.secondYearCost13.setText(formatNumber(businessPlan.getSecondYearCost().get(category13.getText())));
            this.secondYearCost14.setText(formatNumber(businessPlan.getSecondYearCost().get(category14.getText())));
            this.secondYearCost15.setText(formatNumber(businessPlan.getSecondYearCost().get(category15.getText())));
            this.secondYearCost16.setText(formatNumber(businessPlan.getSecondYearCost().get(category16.getText())));
            this.secondYearCost17.setText(formatNumber(businessPlan.getSecondYearCost().get(category17.getText())));
            this.secondYearCost18.setText(formatNumber(businessPlan.getSecondYearCost().get(category18.getText())));
            this.secondYearCost19.setText(formatNumber(businessPlan.getSecondYearCost().get(category19.getText())));
            this.secondYearCost20.setText(formatNumber(businessPlan.getSecondYearCost().get(category20.getText())));
            this.secondYearCost21.setText(formatNumber(businessPlan.getSecondYearCost().get(category21.getText())));
            this.secondYearCost22.setText(formatNumber(businessPlan.getSecondYearCost().get(category22.getText())));
            this.secondYearCost23.setText(formatNumber(businessPlan.getSecondYearCost().get(category23.getText())));
            this.secondYearCost24.setText(formatNumber(businessPlan.getSecondYearCost().get(category24.getText())));
            this.secondYearCost25.setText(formatNumber(businessPlan.getSecondYearCost().get(category25.getText())));

            this.thirdYearCount.setText(formatNumber(businessPlan.getThirdYearCount().get(category.getText())));
            this.thirdYearCount1.setText(formatNumber(businessPlan.getThirdYearCount().get(category1.getText())));
            this.thirdYearCount2.setText(formatNumber(businessPlan.getThirdYearCount().get(category2.getText())));
            this.thirdYearCount3.setText(formatNumber(businessPlan.getThirdYearCount().get(category3.getText())));
            this.thirdYearCount4.setText(formatNumber(businessPlan.getThirdYearCount().get(category4.getText())));
            this.thirdYearCount5.setText(formatNumber(businessPlan.getThirdYearCount().get(category5.getText())));
            this.thirdYearCount6.setText(formatNumber(businessPlan.getThirdYearCount().get(category6.getText())));
            this.thirdYearCount7.setText(formatNumber(businessPlan.getThirdYearCount().get(category7.getText())));
            this.thirdYearCount8.setText(formatNumber(businessPlan.getThirdYearCount().get(category8.getText())));
            this.thirdYearCount9.setText(formatNumber(businessPlan.getThirdYearCount().get(category9.getText())));
            this.thirdYearCount10.setText(formatNumber(businessPlan.getThirdYearCount().get(category10.getText())));
            this.thirdYearCount11.setText(formatNumber(businessPlan.getThirdYearCount().get(category11.getText())));
            this.thirdYearCount12.setText(formatNumber(businessPlan.getThirdYearCount().get(category12.getText())));
            this.thirdYearCount13.setText(formatNumber(businessPlan.getThirdYearCount().get(category13.getText())));
            this.thirdYearCount14.setText(formatNumber(businessPlan.getThirdYearCount().get(category14.getText())));
            this.thirdYearCount15.setText(formatNumber(businessPlan.getThirdYearCount().get(category15.getText())));
            this.thirdYearCount16.setText(formatNumber(businessPlan.getThirdYearCount().get(category16.getText())));
            this.thirdYearCount17.setText(formatNumber(businessPlan.getThirdYearCount().get(category17.getText())));
            this.thirdYearCount18.setText(formatNumber(businessPlan.getThirdYearCount().get(category18.getText())));
            this.thirdYearCount19.setText(formatNumber(businessPlan.getThirdYearCount().get(category19.getText())));
            this.thirdYearCount20.setText(formatNumber(businessPlan.getThirdYearCount().get(category20.getText())));
            this.thirdYearCount21.setText(formatNumber(businessPlan.getThirdYearCount().get(category21.getText())));
            this.thirdYearCount22.setText(formatNumber(businessPlan.getThirdYearCount().get(category22.getText())));
            this.thirdYearCount23.setText(formatNumber(businessPlan.getThirdYearCount().get(category23.getText())));
            this.thirdYearCount24.setText(formatNumber(businessPlan.getThirdYearCount().get(category24.getText())));
            this.thirdYearCount25.setText(formatNumber(businessPlan.getThirdYearCount().get(category25.getText())));

            this.thirdYearCost.setText(formatNumber(businessPlan.getThirdYearCost().get(category.getText())));
            this.thirdYearCost1.setText(formatNumber(businessPlan.getThirdYearCost().get(category1.getText())));
            this.thirdYearCost2.setText(formatNumber(businessPlan.getThirdYearCost().get(category2.getText())));
            this.thirdYearCost3.setText(formatNumber(businessPlan.getThirdYearCost().get(category3.getText())));
            this.thirdYearCost4.setText(formatNumber(businessPlan.getThirdYearCost().get(category4.getText())));
            this.thirdYearCost5.setText(formatNumber(businessPlan.getThirdYearCost().get(category5.getText())));
            this.thirdYearCost6.setText(formatNumber(businessPlan.getThirdYearCost().get(category6.getText())));
            this.thirdYearCost7.setText(formatNumber(businessPlan.getThirdYearCost().get(category7.getText())));
            this.thirdYearCost8.setText(formatNumber(businessPlan.getThirdYearCost().get(category8.getText())));
            this.thirdYearCost9.setText(formatNumber(businessPlan.getThirdYearCost().get(category9.getText())));
            this.thirdYearCost10.setText(formatNumber(businessPlan.getThirdYearCost().get(category10.getText())));
            this.thirdYearCost11.setText(formatNumber(businessPlan.getThirdYearCost().get(category11.getText())));
            this.thirdYearCost12.setText(formatNumber(businessPlan.getThirdYearCost().get(category12.getText())));
            this.thirdYearCost13.setText(formatNumber(businessPlan.getThirdYearCost().get(category13.getText())));
            this.thirdYearCost14.setText(formatNumber(businessPlan.getThirdYearCost().get(category14.getText())));
            this.thirdYearCost15.setText(formatNumber(businessPlan.getThirdYearCost().get(category15.getText())));
            this.thirdYearCost16.setText(formatNumber(businessPlan.getThirdYearCost().get(category16.getText())));
            this.thirdYearCost17.setText(formatNumber(businessPlan.getThirdYearCost().get(category17.getText())));
            this.thirdYearCost18.setText(formatNumber(businessPlan.getThirdYearCost().get(category18.getText())));
            this.thirdYearCost19.setText(formatNumber(businessPlan.getThirdYearCost().get(category19.getText())));
            this.thirdYearCost20.setText(formatNumber(businessPlan.getThirdYearCost().get(category20.getText())));
            this.thirdYearCost21.setText(formatNumber(businessPlan.getThirdYearCost().get(category21.getText())));
            this.thirdYearCost22.setText(formatNumber(businessPlan.getThirdYearCost().get(category22.getText())));
            this.thirdYearCost23.setText(formatNumber(businessPlan.getThirdYearCost().get(category23.getText())));
            this.thirdYearCost24.setText(formatNumber(businessPlan.getThirdYearCost().get(category24.getText())));
            this.thirdYearCost25.setText(formatNumber(businessPlan.getThirdYearCost().get(category25.getText())));

            this.forthYearCount.setText(formatNumber(businessPlan.getForthYearCount().get(category.getText())));
            this.forthYearCount1.setText(formatNumber(businessPlan.getForthYearCount().get(category1.getText())));
            this.forthYearCount2.setText(formatNumber(businessPlan.getForthYearCount().get(category2.getText())));
            this.forthYearCount3.setText(formatNumber(businessPlan.getForthYearCount().get(category3.getText())));
            this.forthYearCount4.setText(formatNumber(businessPlan.getForthYearCount().get(category4.getText())));
            this.forthYearCount5.setText(formatNumber(businessPlan.getForthYearCount().get(category5.getText())));
            this.forthYearCount6.setText(formatNumber(businessPlan.getForthYearCount().get(category6.getText())));
            this.forthYearCount7.setText(formatNumber(businessPlan.getForthYearCount().get(category7.getText())));
            this.forthYearCount8.setText(formatNumber(businessPlan.getForthYearCount().get(category8.getText())));
            this.forthYearCount9.setText(formatNumber(businessPlan.getForthYearCount().get(category9.getText())));
            this.forthYearCount10.setText(formatNumber(businessPlan.getForthYearCount().get(category10.getText())));
            this.forthYearCount11.setText(formatNumber(businessPlan.getForthYearCount().get(category11.getText())));
            this.forthYearCount12.setText(formatNumber(businessPlan.getForthYearCount().get(category12.getText())));
            this.forthYearCount13.setText(formatNumber(businessPlan.getForthYearCount().get(category13.getText())));
            this.forthYearCount14.setText(formatNumber(businessPlan.getForthYearCount().get(category14.getText())));
            this.forthYearCount15.setText(formatNumber(businessPlan.getForthYearCount().get(category15.getText())));
            this.forthYearCount16.setText(formatNumber(businessPlan.getForthYearCount().get(category16.getText())));
            this.forthYearCount17.setText(formatNumber(businessPlan.getForthYearCount().get(category17.getText())));
            this.forthYearCount18.setText(formatNumber(businessPlan.getForthYearCount().get(category18.getText())));
            this.forthYearCount19.setText(formatNumber(businessPlan.getForthYearCount().get(category19.getText())));
            this.forthYearCount20.setText(formatNumber(businessPlan.getForthYearCount().get(category20.getText())));
            this.forthYearCount21.setText(formatNumber(businessPlan.getForthYearCount().get(category21.getText())));
            this.forthYearCount22.setText(formatNumber(businessPlan.getForthYearCount().get(category22.getText())));
            this.forthYearCount23.setText(formatNumber(businessPlan.getForthYearCount().get(category23.getText())));
            this.forthYearCount24.setText(formatNumber(businessPlan.getForthYearCount().get(category24.getText())));
            this.forthYearCount25.setText(formatNumber(businessPlan.getForthYearCount().get(category25.getText())));

            this.forthYearCost.setText(formatNumber(businessPlan.getForthYearCost().get(category.getText())));
            this.forthYearCost1.setText(formatNumber(businessPlan.getForthYearCost().get(category1.getText())));
            this.forthYearCost2.setText(formatNumber(businessPlan.getForthYearCost().get(category2.getText())));
            this.forthYearCost3.setText(formatNumber(businessPlan.getForthYearCost().get(category3.getText())));
            this.forthYearCost4.setText(formatNumber(businessPlan.getForthYearCost().get(category4.getText())));
            this.forthYearCost5.setText(formatNumber(businessPlan.getForthYearCost().get(category5.getText())));
            this.forthYearCost6.setText(formatNumber(businessPlan.getForthYearCost().get(category6.getText())));
            this.forthYearCost7.setText(formatNumber(businessPlan.getForthYearCost().get(category7.getText())));
            this.forthYearCost8.setText(formatNumber(businessPlan.getForthYearCost().get(category8.getText())));
            this.forthYearCost9.setText(formatNumber(businessPlan.getForthYearCost().get(category9.getText())));
            this.forthYearCost10.setText(formatNumber(businessPlan.getForthYearCost().get(category10.getText())));
            this.forthYearCost11.setText(formatNumber(businessPlan.getForthYearCost().get(category11.getText())));
            this.forthYearCost12.setText(formatNumber(businessPlan.getForthYearCost().get(category12.getText())));
            this.forthYearCost13.setText(formatNumber(businessPlan.getForthYearCost().get(category13.getText())));
            this.forthYearCost14.setText(formatNumber(businessPlan.getForthYearCost().get(category14.getText())));
            this.forthYearCost15.setText(formatNumber(businessPlan.getForthYearCost().get(category15.getText())));
            this.forthYearCost16.setText(formatNumber(businessPlan.getForthYearCost().get(category16.getText())));
            this.forthYearCost17.setText(formatNumber(businessPlan.getForthYearCost().get(category17.getText())));
            this.forthYearCost18.setText(formatNumber(businessPlan.getForthYearCost().get(category18.getText())));
            this.forthYearCost19.setText(formatNumber(businessPlan.getForthYearCost().get(category19.getText())));
            this.forthYearCost20.setText(formatNumber(businessPlan.getForthYearCost().get(category20.getText())));
            this.forthYearCost21.setText(formatNumber(businessPlan.getForthYearCost().get(category21.getText())));
            this.forthYearCost22.setText(formatNumber(businessPlan.getForthYearCost().get(category22.getText())));
            this.forthYearCost23.setText(formatNumber(businessPlan.getForthYearCost().get(category23.getText())));
            this.forthYearCost24.setText(formatNumber(businessPlan.getForthYearCost().get(category24.getText())));
            this.forthYearCost25.setText(formatNumber(businessPlan.getForthYearCost().get(category25.getText())));

            this.fifthYearCount.setText(formatNumber(businessPlan.getFifthYearCount().get(category.getText())));
            this.fifthYearCount1.setText(formatNumber(businessPlan.getFifthYearCount().get(category1.getText())));
            this.fifthYearCount2.setText(formatNumber(businessPlan.getFifthYearCount().get(category2.getText())));
            this.fifthYearCount3.setText(formatNumber(businessPlan.getFifthYearCount().get(category3.getText())));
            this.fifthYearCount4.setText(formatNumber(businessPlan.getFifthYearCount().get(category4.getText())));
            this.fifthYearCount5.setText(formatNumber(businessPlan.getFifthYearCount().get(category5.getText())));
            this.fifthYearCount6.setText(formatNumber(businessPlan.getFifthYearCount().get(category6.getText())));
            this.fifthYearCount7.setText(formatNumber(businessPlan.getFifthYearCount().get(category7.getText())));
            this.fifthYearCount8.setText(formatNumber(businessPlan.getFifthYearCount().get(category8.getText())));
            this.fifthYearCount9.setText(formatNumber(businessPlan.getFifthYearCount().get(category9.getText())));
            this.fifthYearCount10.setText(formatNumber(businessPlan.getFifthYearCount().get(category10.getText())));
            this.fifthYearCount11.setText(formatNumber(businessPlan.getFifthYearCount().get(category11.getText())));
            this.fifthYearCount12.setText(formatNumber(businessPlan.getFifthYearCount().get(category12.getText())));
            this.fifthYearCount13.setText(formatNumber(businessPlan.getFifthYearCount().get(category13.getText())));
            this.fifthYearCount14.setText(formatNumber(businessPlan.getFifthYearCount().get(category14.getText())));
            this.fifthYearCount15.setText(formatNumber(businessPlan.getFifthYearCount().get(category15.getText())));
            this.fifthYearCount16.setText(formatNumber(businessPlan.getFifthYearCount().get(category16.getText())));
            this.fifthYearCount17.setText(formatNumber(businessPlan.getFifthYearCount().get(category17.getText())));
            this.fifthYearCount18.setText(formatNumber(businessPlan.getFifthYearCount().get(category18.getText())));
            this.fifthYearCount19.setText(formatNumber(businessPlan.getFifthYearCount().get(category19.getText())));
            this.fifthYearCount20.setText(formatNumber(businessPlan.getFifthYearCount().get(category20.getText())));
            this.fifthYearCount21.setText(formatNumber(businessPlan.getFifthYearCount().get(category21.getText())));
            this.fifthYearCount22.setText(formatNumber(businessPlan.getFifthYearCount().get(category22.getText())));
            this.fifthYearCount23.setText(formatNumber(businessPlan.getFifthYearCount().get(category23.getText())));
            this.fifthYearCount24.setText(formatNumber(businessPlan.getFifthYearCount().get(category24.getText())));
            this.fifthYearCount25.setText(formatNumber(businessPlan.getFifthYearCount().get(category25.getText())));

            this.fifthYearCost.setText(formatNumber(businessPlan.getFifthYearCost().get(category.getText())));
            this.fifthYearCost1.setText(formatNumber(businessPlan.getFifthYearCost().get(category1.getText())));
            this.fifthYearCost2.setText(formatNumber(businessPlan.getFifthYearCost().get(category2.getText())));
            this.fifthYearCost3.setText(formatNumber(businessPlan.getFifthYearCost().get(category3.getText())));
            this.fifthYearCost4.setText(formatNumber(businessPlan.getFifthYearCost().get(category4.getText())));
            this.fifthYearCost5.setText(formatNumber(businessPlan.getFifthYearCost().get(category5.getText())));
            this.fifthYearCost6.setText(formatNumber(businessPlan.getFifthYearCost().get(category6.getText())));
            this.fifthYearCost7.setText(formatNumber(businessPlan.getFifthYearCost().get(category7.getText())));
            this.fifthYearCost8.setText(formatNumber(businessPlan.getFifthYearCost().get(category8.getText())));
            this.fifthYearCost9.setText(formatNumber(businessPlan.getFifthYearCost().get(category9.getText())));
            this.fifthYearCost10.setText(formatNumber(businessPlan.getFifthYearCost().get(category10.getText())));
            this.fifthYearCost11.setText(formatNumber(businessPlan.getFifthYearCost().get(category11.getText())));
            this.fifthYearCost12.setText(formatNumber(businessPlan.getFifthYearCost().get(category12.getText())));
            this.fifthYearCost13.setText(formatNumber(businessPlan.getFifthYearCost().get(category13.getText())));
            this.fifthYearCost14.setText(formatNumber(businessPlan.getFifthYearCost().get(category14.getText())));
            this.fifthYearCost15.setText(formatNumber(businessPlan.getFifthYearCost().get(category15.getText())));
            this.fifthYearCost16.setText(formatNumber(businessPlan.getFifthYearCost().get(category16.getText())));
            this.fifthYearCost17.setText(formatNumber(businessPlan.getFifthYearCost().get(category17.getText())));
            this.fifthYearCost18.setText(formatNumber(businessPlan.getFifthYearCost().get(category18.getText())));
            this.fifthYearCost19.setText(formatNumber(businessPlan.getFifthYearCost().get(category19.getText())));
            this.fifthYearCost20.setText(formatNumber(businessPlan.getFifthYearCost().get(category20.getText())));
            this.fifthYearCost21.setText(formatNumber(businessPlan.getFifthYearCost().get(category21.getText())));
            this.fifthYearCost22.setText(formatNumber(businessPlan.getFifthYearCost().get(category22.getText())));
            this.fifthYearCost23.setText(formatNumber(businessPlan.getFifthYearCost().get(category23.getText())));
            this.fifthYearCost24.setText(formatNumber(businessPlan.getFifthYearCost().get(category24.getText())));
            this.fifthYearCost25.setText(formatNumber(businessPlan.getFifthYearCost().get(category25.getText())));

        }
    }


    @FXML
    private void save() {
        try {
            businessPlan.setId(id.getText());
            businessPlan.setFirstYear(Integer.parseInt(firstYear.getText()));

            businessPlan.getFirstYearCount().put(category.getText(), parseNumber(firstYearCount.getText()));
            businessPlan.getFirstYearCount().put(category1.getText(), parseNumber(firstYearCount1.getText()));
            businessPlan.getFirstYearCount().put(category2.getText(), parseNumber(firstYearCount2.getText()));
            businessPlan.getFirstYearCount().put(category3.getText(), parseNumber(firstYearCount3.getText()));
            businessPlan.getFirstYearCount().put(category4.getText(), parseNumber(firstYearCount4.getText()));
            businessPlan.getFirstYearCount().put(category5.getText(), parseNumber(firstYearCount5.getText()));
            businessPlan.getFirstYearCount().put(category6.getText(), parseNumber(firstYearCount6.getText()));
            businessPlan.getFirstYearCount().put(category7.getText(), parseNumber(firstYearCount7.getText()));
            businessPlan.getFirstYearCount().put(category8.getText(), parseNumber(firstYearCount8.getText()));
            businessPlan.getFirstYearCount().put(category9.getText(), parseNumber(firstYearCount9.getText()));
            businessPlan.getFirstYearCount().put(category10.getText(), parseNumber(firstYearCount10.getText()));
            businessPlan.getFirstYearCount().put(category11.getText(), parseNumber(firstYearCount11.getText()));
            businessPlan.getFirstYearCount().put(category12.getText(), parseNumber(firstYearCount12.getText()));
            businessPlan.getFirstYearCount().put(category13.getText(), parseNumber(firstYearCount13.getText()));
            businessPlan.getFirstYearCount().put(category14.getText(), parseNumber(firstYearCount14.getText()));
            businessPlan.getFirstYearCount().put(category15.getText(), parseNumber(firstYearCount15.getText()));
            businessPlan.getFirstYearCount().put(category16.getText(), parseNumber(firstYearCount16.getText()));
            businessPlan.getFirstYearCount().put(category17.getText(), parseNumber(firstYearCount17.getText()));
            businessPlan.getFirstYearCount().put(category18.getText(), parseNumber(firstYearCount18.getText()));
            businessPlan.getFirstYearCount().put(category19.getText(), parseNumber(firstYearCount19.getText()));
            businessPlan.getFirstYearCount().put(category20.getText(), parseNumber(firstYearCount20.getText()));
            businessPlan.getFirstYearCount().put(category21.getText(), parseNumber(firstYearCount21.getText()));
            businessPlan.getFirstYearCount().put(category22.getText(), parseNumber(firstYearCount22.getText()));
            businessPlan.getFirstYearCount().put(category23.getText(), parseNumber(firstYearCount23.getText()));
            businessPlan.getFirstYearCount().put(category24.getText(), parseNumber(firstYearCount24.getText()));
            businessPlan.getFirstYearCount().put(category25.getText(), parseNumber(firstYearCount25.getText()));

            businessPlan.getFirstYearCost().put(category.getText(), parseNumber(firstYearCost.getText()));
            businessPlan.getFirstYearCost().put(category1.getText(), parseNumber(firstYearCost1.getText()));
            businessPlan.getFirstYearCost().put(category2.getText(), parseNumber(firstYearCost2.getText()));
            businessPlan.getFirstYearCost().put(category3.getText(), parseNumber(firstYearCost3.getText()));
            businessPlan.getFirstYearCost().put(category4.getText(), parseNumber(firstYearCost4.getText()));
            businessPlan.getFirstYearCost().put(category5.getText(), parseNumber(firstYearCost5.getText()));
            businessPlan.getFirstYearCost().put(category6.getText(), parseNumber(firstYearCost6.getText()));
            businessPlan.getFirstYearCost().put(category7.getText(), parseNumber(firstYearCost7.getText()));
            businessPlan.getFirstYearCost().put(category8.getText(), parseNumber(firstYearCost8.getText()));
            businessPlan.getFirstYearCost().put(category9.getText(), parseNumber(firstYearCost9.getText()));
            businessPlan.getFirstYearCost().put(category10.getText(), parseNumber(firstYearCost10.getText()));
            businessPlan.getFirstYearCost().put(category11.getText(), parseNumber(firstYearCost11.getText()));
            businessPlan.getFirstYearCost().put(category12.getText(), parseNumber(firstYearCost12.getText()));
            businessPlan.getFirstYearCost().put(category13.getText(), parseNumber(firstYearCost13.getText()));
            businessPlan.getFirstYearCost().put(category14.getText(), parseNumber(firstYearCost14.getText()));
            businessPlan.getFirstYearCost().put(category15.getText(), parseNumber(firstYearCost15.getText()));
            businessPlan.getFirstYearCost().put(category16.getText(), parseNumber(firstYearCost16.getText()));
            businessPlan.getFirstYearCost().put(category17.getText(), parseNumber(firstYearCost17.getText()));
            businessPlan.getFirstYearCost().put(category18.getText(), parseNumber(firstYearCost18.getText()));
            businessPlan.getFirstYearCost().put(category19.getText(), parseNumber(firstYearCost19.getText()));
            businessPlan.getFirstYearCost().put(category20.getText(), parseNumber(firstYearCost20.getText()));
            businessPlan.getFirstYearCost().put(category21.getText(), parseNumber(firstYearCost21.getText()));
            businessPlan.getFirstYearCost().put(category22.getText(), parseNumber(firstYearCost22.getText()));
            businessPlan.getFirstYearCost().put(category23.getText(), parseNumber(firstYearCost23.getText()));
            businessPlan.getFirstYearCost().put(category24.getText(), parseNumber(firstYearCost24.getText()));
            businessPlan.getFirstYearCost().put(category25.getText(), parseNumber(firstYearCost25.getText()));

            businessPlan.getSecondYearCount().put(category.getText(), parseNumber(secondYearCount.getText()));
            businessPlan.getSecondYearCount().put(category1.getText(), parseNumber(secondYearCount1.getText()));
            businessPlan.getSecondYearCount().put(category2.getText(), parseNumber(secondYearCount2.getText()));
            businessPlan.getSecondYearCount().put(category3.getText(), parseNumber(secondYearCount3.getText()));
            businessPlan.getSecondYearCount().put(category4.getText(), parseNumber(secondYearCount4.getText()));
            businessPlan.getSecondYearCount().put(category5.getText(), parseNumber(secondYearCount5.getText()));
            businessPlan.getSecondYearCount().put(category6.getText(), parseNumber(secondYearCount6.getText()));
            businessPlan.getSecondYearCount().put(category7.getText(), parseNumber(secondYearCount7.getText()));
            businessPlan.getSecondYearCount().put(category8.getText(), parseNumber(secondYearCount8.getText()));
            businessPlan.getSecondYearCount().put(category9.getText(), parseNumber(secondYearCount9.getText()));
            businessPlan.getSecondYearCount().put(category10.getText(), parseNumber(secondYearCount10.getText()));
            businessPlan.getSecondYearCount().put(category11.getText(), parseNumber(secondYearCount11.getText()));
            businessPlan.getSecondYearCount().put(category12.getText(), parseNumber(secondYearCount12.getText()));
            businessPlan.getSecondYearCount().put(category13.getText(), parseNumber(secondYearCount13.getText()));
            businessPlan.getSecondYearCount().put(category14.getText(), parseNumber(secondYearCount14.getText()));
            businessPlan.getSecondYearCount().put(category15.getText(), parseNumber(secondYearCount15.getText()));
            businessPlan.getSecondYearCount().put(category16.getText(), parseNumber(secondYearCount16.getText()));
            businessPlan.getSecondYearCount().put(category17.getText(), parseNumber(secondYearCount17.getText()));
            businessPlan.getSecondYearCount().put(category18.getText(), parseNumber(secondYearCount18.getText()));
            businessPlan.getSecondYearCount().put(category19.getText(), parseNumber(secondYearCount19.getText()));
            businessPlan.getSecondYearCount().put(category20.getText(), parseNumber(secondYearCount20.getText()));
            businessPlan.getSecondYearCount().put(category21.getText(), parseNumber(secondYearCount21.getText()));
            businessPlan.getSecondYearCount().put(category22.getText(), parseNumber(secondYearCount22.getText()));
            businessPlan.getSecondYearCount().put(category23.getText(), parseNumber(secondYearCount23.getText()));
            businessPlan.getSecondYearCount().put(category24.getText(), parseNumber(secondYearCount24.getText()));
            businessPlan.getSecondYearCount().put(category25.getText(), parseNumber(secondYearCount25.getText()));

            businessPlan.getSecondYearCost().put(category.getText(), parseNumber(secondYearCost.getText()));
            businessPlan.getSecondYearCost().put(category1.getText(), parseNumber(secondYearCost1.getText()));
            businessPlan.getSecondYearCost().put(category2.getText(), parseNumber(secondYearCost2.getText()));
            businessPlan.getSecondYearCost().put(category3.getText(), parseNumber(secondYearCost3.getText()));
            businessPlan.getSecondYearCost().put(category4.getText(), parseNumber(secondYearCost4.getText()));
            businessPlan.getSecondYearCost().put(category5.getText(), parseNumber(secondYearCost5.getText()));
            businessPlan.getSecondYearCost().put(category6.getText(), parseNumber(secondYearCost6.getText()));
            businessPlan.getSecondYearCost().put(category7.getText(), parseNumber(secondYearCost7.getText()));
            businessPlan.getSecondYearCost().put(category8.getText(), parseNumber(secondYearCost8.getText()));
            businessPlan.getSecondYearCost().put(category9.getText(), parseNumber(secondYearCost9.getText()));
            businessPlan.getSecondYearCost().put(category10.getText(), parseNumber(secondYearCost10.getText()));
            businessPlan.getSecondYearCost().put(category11.getText(), parseNumber(secondYearCost11.getText()));
            businessPlan.getSecondYearCost().put(category12.getText(), parseNumber(secondYearCost12.getText()));
            businessPlan.getSecondYearCost().put(category13.getText(), parseNumber(secondYearCost13.getText()));
            businessPlan.getSecondYearCost().put(category14.getText(), parseNumber(secondYearCost14.getText()));
            businessPlan.getSecondYearCost().put(category15.getText(), parseNumber(secondYearCost15.getText()));
            businessPlan.getSecondYearCost().put(category16.getText(), parseNumber(secondYearCost16.getText()));
            businessPlan.getSecondYearCost().put(category17.getText(), parseNumber(secondYearCost17.getText()));
            businessPlan.getSecondYearCost().put(category18.getText(), parseNumber(secondYearCost18.getText()));
            businessPlan.getSecondYearCost().put(category19.getText(), parseNumber(secondYearCost19.getText()));
            businessPlan.getSecondYearCost().put(category20.getText(), parseNumber(secondYearCost20.getText()));
            businessPlan.getSecondYearCost().put(category21.getText(), parseNumber(secondYearCost21.getText()));
            businessPlan.getSecondYearCost().put(category22.getText(), parseNumber(secondYearCost22.getText()));
            businessPlan.getSecondYearCost().put(category23.getText(), parseNumber(secondYearCost23.getText()));
            businessPlan.getSecondYearCost().put(category24.getText(), parseNumber(secondYearCost24.getText()));
            businessPlan.getSecondYearCost().put(category25.getText(), parseNumber(secondYearCost25.getText()));

            businessPlan.getThirdYearCount().put(category.getText(), parseNumber(thirdYearCount.getText()));
            businessPlan.getThirdYearCount().put(category1.getText(), parseNumber(thirdYearCount1.getText()));
            businessPlan.getThirdYearCount().put(category2.getText(), parseNumber(thirdYearCount2.getText()));
            businessPlan.getThirdYearCount().put(category3.getText(), parseNumber(thirdYearCount3.getText()));
            businessPlan.getThirdYearCount().put(category4.getText(), parseNumber(thirdYearCount4.getText()));
            businessPlan.getThirdYearCount().put(category5.getText(), parseNumber(thirdYearCount5.getText()));
            businessPlan.getThirdYearCount().put(category6.getText(), parseNumber(thirdYearCount6.getText()));
            businessPlan.getThirdYearCount().put(category7.getText(), parseNumber(thirdYearCount7.getText()));
            businessPlan.getThirdYearCount().put(category8.getText(), parseNumber(thirdYearCount8.getText()));
            businessPlan.getThirdYearCount().put(category9.getText(), parseNumber(thirdYearCount9.getText()));
            businessPlan.getThirdYearCount().put(category10.getText(), parseNumber(thirdYearCount10.getText()));
            businessPlan.getThirdYearCount().put(category11.getText(), parseNumber(thirdYearCount11.getText()));
            businessPlan.getThirdYearCount().put(category12.getText(), parseNumber(thirdYearCount12.getText()));
            businessPlan.getThirdYearCount().put(category13.getText(), parseNumber(thirdYearCount13.getText()));
            businessPlan.getThirdYearCount().put(category14.getText(), parseNumber(thirdYearCount14.getText()));
            businessPlan.getThirdYearCount().put(category15.getText(), parseNumber(thirdYearCount15.getText()));
            businessPlan.getThirdYearCount().put(category16.getText(), parseNumber(thirdYearCount16.getText()));
            businessPlan.getThirdYearCount().put(category17.getText(), parseNumber(thirdYearCount17.getText()));
            businessPlan.getThirdYearCount().put(category18.getText(), parseNumber(thirdYearCount18.getText()));
            businessPlan.getThirdYearCount().put(category19.getText(), parseNumber(thirdYearCount19.getText()));
            businessPlan.getThirdYearCount().put(category20.getText(), parseNumber(thirdYearCount20.getText()));
            businessPlan.getThirdYearCount().put(category21.getText(), parseNumber(thirdYearCount21.getText()));
            businessPlan.getThirdYearCount().put(category22.getText(), parseNumber(thirdYearCount22.getText()));
            businessPlan.getThirdYearCount().put(category23.getText(), parseNumber(thirdYearCount23.getText()));
            businessPlan.getThirdYearCount().put(category24.getText(), parseNumber(thirdYearCount24.getText()));
            businessPlan.getThirdYearCount().put(category25.getText(), parseNumber(thirdYearCount25.getText()));

            businessPlan.getThirdYearCost().put(category.getText(), parseNumber(thirdYearCost.getText()));
            businessPlan.getThirdYearCost().put(category1.getText(), parseNumber(thirdYearCost1.getText()));
            businessPlan.getThirdYearCost().put(category2.getText(), parseNumber(thirdYearCost2.getText()));
            businessPlan.getThirdYearCost().put(category3.getText(), parseNumber(thirdYearCost3.getText()));
            businessPlan.getThirdYearCost().put(category4.getText(), parseNumber(thirdYearCost4.getText()));
            businessPlan.getThirdYearCost().put(category5.getText(), parseNumber(thirdYearCost5.getText()));
            businessPlan.getThirdYearCost().put(category6.getText(), parseNumber(thirdYearCost6.getText()));
            businessPlan.getThirdYearCost().put(category7.getText(), parseNumber(thirdYearCost7.getText()));
            businessPlan.getThirdYearCost().put(category8.getText(), parseNumber(thirdYearCost8.getText()));
            businessPlan.getThirdYearCost().put(category9.getText(), parseNumber(thirdYearCost9.getText()));
            businessPlan.getThirdYearCost().put(category10.getText(), parseNumber(thirdYearCost10.getText()));
            businessPlan.getThirdYearCost().put(category11.getText(), parseNumber(thirdYearCost11.getText()));
            businessPlan.getThirdYearCost().put(category12.getText(), parseNumber(thirdYearCost12.getText()));
            businessPlan.getThirdYearCost().put(category13.getText(), parseNumber(thirdYearCost13.getText()));
            businessPlan.getThirdYearCost().put(category14.getText(), parseNumber(thirdYearCost14.getText()));
            businessPlan.getThirdYearCost().put(category15.getText(), parseNumber(thirdYearCost15.getText()));
            businessPlan.getThirdYearCost().put(category16.getText(), parseNumber(thirdYearCost16.getText()));
            businessPlan.getThirdYearCost().put(category17.getText(), parseNumber(thirdYearCost17.getText()));
            businessPlan.getThirdYearCost().put(category18.getText(), parseNumber(thirdYearCost18.getText()));
            businessPlan.getThirdYearCost().put(category19.getText(), parseNumber(thirdYearCost19.getText()));
            businessPlan.getThirdYearCost().put(category20.getText(), parseNumber(thirdYearCost20.getText()));
            businessPlan.getThirdYearCost().put(category21.getText(), parseNumber(thirdYearCost21.getText()));
            businessPlan.getThirdYearCost().put(category22.getText(), parseNumber(thirdYearCost22.getText()));
            businessPlan.getThirdYearCost().put(category23.getText(), parseNumber(thirdYearCost23.getText()));
            businessPlan.getThirdYearCost().put(category24.getText(), parseNumber(thirdYearCost24.getText()));
            businessPlan.getThirdYearCost().put(category25.getText(), parseNumber(thirdYearCost25.getText()));

            businessPlan.getForthYearCount().put(category.getText(), parseNumber(forthYearCount.getText()));
            businessPlan.getForthYearCount().put(category1.getText(), parseNumber(forthYearCount1.getText()));
            businessPlan.getForthYearCount().put(category2.getText(), parseNumber(forthYearCount2.getText()));
            businessPlan.getForthYearCount().put(category3.getText(), parseNumber(forthYearCount3.getText()));
            businessPlan.getForthYearCount().put(category4.getText(), parseNumber(forthYearCount4.getText()));
            businessPlan.getForthYearCount().put(category5.getText(), parseNumber(forthYearCount5.getText()));
            businessPlan.getForthYearCount().put(category6.getText(), parseNumber(forthYearCount6.getText()));
            businessPlan.getForthYearCount().put(category7.getText(), parseNumber(forthYearCount7.getText()));
            businessPlan.getForthYearCount().put(category8.getText(), parseNumber(forthYearCount8.getText()));
            businessPlan.getForthYearCount().put(category9.getText(), parseNumber(forthYearCount9.getText()));
            businessPlan.getForthYearCount().put(category10.getText(), parseNumber(forthYearCount10.getText()));
            businessPlan.getForthYearCount().put(category11.getText(), parseNumber(forthYearCount11.getText()));
            businessPlan.getForthYearCount().put(category12.getText(), parseNumber(forthYearCount12.getText()));
            businessPlan.getForthYearCount().put(category13.getText(), parseNumber(forthYearCount13.getText()));
            businessPlan.getForthYearCount().put(category14.getText(), parseNumber(forthYearCount14.getText()));
            businessPlan.getForthYearCount().put(category15.getText(), parseNumber(forthYearCount15.getText()));
            businessPlan.getForthYearCount().put(category16.getText(), parseNumber(forthYearCount16.getText()));
            businessPlan.getForthYearCount().put(category17.getText(), parseNumber(forthYearCount17.getText()));
            businessPlan.getForthYearCount().put(category18.getText(), parseNumber(forthYearCount18.getText()));
            businessPlan.getForthYearCount().put(category19.getText(), parseNumber(forthYearCount19.getText()));
            businessPlan.getForthYearCount().put(category20.getText(), parseNumber(forthYearCount20.getText()));
            businessPlan.getForthYearCount().put(category21.getText(), parseNumber(forthYearCount21.getText()));
            businessPlan.getForthYearCount().put(category22.getText(), parseNumber(forthYearCount22.getText()));
            businessPlan.getForthYearCount().put(category23.getText(), parseNumber(forthYearCount23.getText()));
            businessPlan.getForthYearCount().put(category24.getText(), parseNumber(forthYearCount24.getText()));
            businessPlan.getForthYearCount().put(category25.getText(), parseNumber(forthYearCount25.getText()));

            businessPlan.getForthYearCost().put(category.getText(), parseNumber(forthYearCost.getText()));
            businessPlan.getForthYearCost().put(category1.getText(), parseNumber(forthYearCost1.getText()));
            businessPlan.getForthYearCost().put(category2.getText(), parseNumber(forthYearCost2.getText()));
            businessPlan.getForthYearCost().put(category3.getText(), parseNumber(forthYearCost3.getText()));
            businessPlan.getForthYearCost().put(category4.getText(), parseNumber(forthYearCost4.getText()));
            businessPlan.getForthYearCost().put(category5.getText(), parseNumber(forthYearCost5.getText()));
            businessPlan.getForthYearCost().put(category6.getText(), parseNumber(forthYearCost6.getText()));
            businessPlan.getForthYearCost().put(category7.getText(), parseNumber(forthYearCost7.getText()));
            businessPlan.getForthYearCost().put(category8.getText(), parseNumber(forthYearCost8.getText()));
            businessPlan.getForthYearCost().put(category9.getText(), parseNumber(forthYearCost9.getText()));
            businessPlan.getForthYearCost().put(category10.getText(), parseNumber(forthYearCost10.getText()));
            businessPlan.getForthYearCost().put(category11.getText(), parseNumber(forthYearCost11.getText()));
            businessPlan.getForthYearCost().put(category12.getText(), parseNumber(forthYearCost12.getText()));
            businessPlan.getForthYearCost().put(category13.getText(), parseNumber(forthYearCost13.getText()));
            businessPlan.getForthYearCost().put(category14.getText(), parseNumber(forthYearCost14.getText()));
            businessPlan.getForthYearCost().put(category15.getText(), parseNumber(forthYearCost15.getText()));
            businessPlan.getForthYearCost().put(category16.getText(), parseNumber(forthYearCost16.getText()));
            businessPlan.getForthYearCost().put(category17.getText(), parseNumber(forthYearCost17.getText()));
            businessPlan.getForthYearCost().put(category18.getText(), parseNumber(forthYearCost18.getText()));
            businessPlan.getForthYearCost().put(category19.getText(), parseNumber(forthYearCost19.getText()));
            businessPlan.getForthYearCost().put(category20.getText(), parseNumber(forthYearCost20.getText()));
            businessPlan.getForthYearCost().put(category21.getText(), parseNumber(forthYearCost21.getText()));
            businessPlan.getForthYearCost().put(category22.getText(), parseNumber(forthYearCost22.getText()));
            businessPlan.getForthYearCost().put(category23.getText(), parseNumber(forthYearCost23.getText()));
            businessPlan.getForthYearCost().put(category24.getText(), parseNumber(forthYearCost24.getText()));
            businessPlan.getForthYearCost().put(category25.getText(), parseNumber(forthYearCost25.getText()));

            businessPlan.getFifthYearCount().put(category.getText(), parseNumber(fifthYearCount.getText()));
            businessPlan.getFifthYearCount().put(category1.getText(), parseNumber(fifthYearCount1.getText()));
            businessPlan.getFifthYearCount().put(category2.getText(), parseNumber(fifthYearCount2.getText()));
            businessPlan.getFifthYearCount().put(category3.getText(), parseNumber(fifthYearCount3.getText()));
            businessPlan.getFifthYearCount().put(category4.getText(), parseNumber(fifthYearCount4.getText()));
            businessPlan.getFifthYearCount().put(category5.getText(), parseNumber(fifthYearCount5.getText()));
            businessPlan.getFifthYearCount().put(category6.getText(), parseNumber(fifthYearCount6.getText()));
            businessPlan.getFifthYearCount().put(category7.getText(), parseNumber(fifthYearCount7.getText()));
            businessPlan.getFifthYearCount().put(category8.getText(), parseNumber(fifthYearCount8.getText()));
            businessPlan.getFifthYearCount().put(category9.getText(), parseNumber(fifthYearCount9.getText()));
            businessPlan.getFifthYearCount().put(category10.getText(), parseNumber(fifthYearCount10.getText()));
            businessPlan.getFifthYearCount().put(category11.getText(), parseNumber(fifthYearCount11.getText()));
            businessPlan.getFifthYearCount().put(category12.getText(), parseNumber(fifthYearCount12.getText()));
            businessPlan.getFifthYearCount().put(category13.getText(), parseNumber(fifthYearCount13.getText()));
            businessPlan.getFifthYearCount().put(category14.getText(), parseNumber(fifthYearCount14.getText()));
            businessPlan.getFifthYearCount().put(category15.getText(), parseNumber(fifthYearCount15.getText()));
            businessPlan.getFifthYearCount().put(category16.getText(), parseNumber(fifthYearCount16.getText()));
            businessPlan.getFifthYearCount().put(category17.getText(), parseNumber(fifthYearCount17.getText()));
            businessPlan.getFifthYearCount().put(category18.getText(), parseNumber(fifthYearCount18.getText()));
            businessPlan.getFifthYearCount().put(category19.getText(), parseNumber(fifthYearCount19.getText()));
            businessPlan.getFifthYearCount().put(category20.getText(), parseNumber(fifthYearCount20.getText()));
            businessPlan.getFifthYearCount().put(category21.getText(), parseNumber(fifthYearCount21.getText()));
            businessPlan.getFifthYearCount().put(category22.getText(), parseNumber(fifthYearCount22.getText()));
            businessPlan.getFifthYearCount().put(category23.getText(), parseNumber(fifthYearCount23.getText()));
            businessPlan.getFifthYearCount().put(category24.getText(), parseNumber(fifthYearCount24.getText()));
            businessPlan.getFifthYearCount().put(category25.getText(), parseNumber(fifthYearCount25.getText()));

            businessPlan.getFifthYearCost().put(category.getText(), parseNumber(fifthYearCost.getText()));
            businessPlan.getFifthYearCost().put(category1.getText(), parseNumber(fifthYearCost1.getText()));
            businessPlan.getFifthYearCost().put(category2.getText(), parseNumber(fifthYearCost2.getText()));
            businessPlan.getFifthYearCost().put(category3.getText(), parseNumber(fifthYearCost3.getText()));
            businessPlan.getFifthYearCost().put(category4.getText(), parseNumber(fifthYearCost4.getText()));
            businessPlan.getFifthYearCost().put(category5.getText(), parseNumber(fifthYearCost5.getText()));
            businessPlan.getFifthYearCost().put(category6.getText(), parseNumber(fifthYearCost6.getText()));
            businessPlan.getFifthYearCost().put(category7.getText(), parseNumber(fifthYearCost7.getText()));
            businessPlan.getFifthYearCost().put(category8.getText(), parseNumber(fifthYearCost8.getText()));
            businessPlan.getFifthYearCost().put(category9.getText(), parseNumber(fifthYearCost9.getText()));
            businessPlan.getFifthYearCost().put(category10.getText(), parseNumber(fifthYearCost10.getText()));
            businessPlan.getFifthYearCost().put(category11.getText(), parseNumber(fifthYearCost11.getText()));
            businessPlan.getFifthYearCost().put(category12.getText(), parseNumber(fifthYearCost12.getText()));
            businessPlan.getFifthYearCost().put(category13.getText(), parseNumber(fifthYearCost13.getText()));
            businessPlan.getFifthYearCost().put(category14.getText(), parseNumber(fifthYearCost14.getText()));
            businessPlan.getFifthYearCost().put(category15.getText(), parseNumber(fifthYearCost15.getText()));
            businessPlan.getFifthYearCost().put(category16.getText(), parseNumber(fifthYearCost16.getText()));
            businessPlan.getFifthYearCost().put(category17.getText(), parseNumber(fifthYearCost17.getText()));
            businessPlan.getFifthYearCost().put(category18.getText(), parseNumber(fifthYearCost18.getText()));
            businessPlan.getFifthYearCost().put(category19.getText(), parseNumber(fifthYearCost19.getText()));
            businessPlan.getFifthYearCost().put(category20.getText(), parseNumber(fifthYearCost20.getText()));
            businessPlan.getFifthYearCost().put(category21.getText(), parseNumber(fifthYearCost21.getText()));
            businessPlan.getFifthYearCost().put(category22.getText(), parseNumber(fifthYearCost22.getText()));
            businessPlan.getFifthYearCost().put(category23.getText(), parseNumber(fifthYearCost23.getText()));
            businessPlan.getFifthYearCost().put(category24.getText(), parseNumber(fifthYearCost24.getText()));
            businessPlan.getFifthYearCost().put(category25.getText(), parseNumber(fifthYearCost25.getText()));

            saveHandler.accept(businessPlan);
            close();
            Dialog.DialogBuilder.builder().title("Выполнено успешно.").message("Создание/изменение версии БП выполнено успешно.").build().show();
        } catch (OrganizerException e) {
            message.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        title.getScene().getWindow().hide();
    }

}
