package organizer.views.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import organizer.Organizer;
import organizer.views.controller.common.Dialog;
import organizer.utils.Menu;

import java.io.IOException;

@Controller
public class MainFrameController {

    @FXML
    private VBox sideBar;
    @FXML
    private StackPane contentView;


    @FXML
    private void initialize() {
        loadView(Menu.Main);
    }

    @FXML
    private void clickMenu(MouseEvent event) {
        Node node = (Node) event.getSource();
        if (node.getId().equals("Exit")) {
            // подтверждение выхода
            Dialog.DialogBuilder.builder()
                    .title("Подтверждение")
                    .message("Вы действительно хотите выйти из программы?")
                    .okActionListener(() -> sideBar.getScene().getWindow().hide())
                    .build().show();
        } else {
            Menu menu = Menu.valueOf(node.getId());
            loadView(menu);
        }
    }

    private void loadView(Menu menu) {
        try {
            for (Node node : sideBar.getChildren()) {
                node.getStyleClass().remove("active");
                if (node.getId().equals(menu.name())) {
                    node.getStyleClass().add("active");
                } else {
                    node.getStyleClass().remove("active");
                }
            }

            contentView.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(menu.getFxml()));
            loader.setControllerFactory(Organizer.getApplicationContext()::getBean);
            Parent view = loader.load();
            contentView.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void show() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(MainFrameController.class.getClassLoader().getResource("views/MainFrame.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Органайзер ОНСС");
            stage.getIcons().add(new Image("images/appIcon.png"));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
