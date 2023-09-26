package organizer.views.controller.common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static organizer.utils.Constants.CLOSE_TITLE;

public class Dialog {

    @FXML
    private Label title;
    @FXML
    private Label message;
    @FXML
    private Button okBtn;
    @FXML
    private Button closeBtn;


    private Stage stage;

    public void show() {
        stage.showAndWait();
    }

    @FXML
    private void cancel() {
        okBtn.getScene().getWindow().hide();
    }

    public static class DialogBuilder {
        private String title;
        private String message;

        private ActionListener okActionListener;

        private DialogBuilder() {
        }

        public DialogBuilder okActionListener(ActionListener okActionListener) {
            this.okActionListener = okActionListener;
            return this;
        }

        public DialogBuilder message(String message) {
            this.message = message;
            return this;
        }

        public DialogBuilder title(String title) {
            this.title = title;
            return this;
        }

        public Dialog build() {

            try {
                Stage stage = new Stage(/*StageStyle.UNDECORATED*/);
                FXMLLoader loader = new FXMLLoader(Dialog.class.getClassLoader().getResource("views/common/Dialog.fxml"));
                Parent view = loader.load();
                Scene scene = new Scene(view);
                stage.setScene(scene);

                Dialog controller = loader.getController();
                controller.stage = stage;

                controller.title.setText(this.title);
                controller.message.setText(this.message);

                if (okActionListener != null) {
                    controller.okBtn.setOnAction(event -> {
                        controller.cancel();
                        okActionListener.doAction();
                    });
                } else {
                    controller.okBtn.setVisible(false);
                    controller.closeBtn.setText(CLOSE_TITLE);
                }

                return controller;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public static DialogBuilder builder() {
            return new DialogBuilder();
        }

    }

    public static interface ActionListener {
        void doAction();
    }
}
