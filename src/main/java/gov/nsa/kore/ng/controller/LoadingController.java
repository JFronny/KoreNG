package gov.nsa.kore.ng.controller;

import gov.nsa.kore.ng.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoadingController {
    @FXML
    public Text text;
    @FXML
    public ProgressBar loading;

    public static LoadingProvider show(String text, Window current) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loading-dialog.fxml"));
        Parent root = fxmlLoader.load();
        LoadingController controller = fxmlLoader.getController();
        controller.text.setText(text);
        stage.setScene(new Scene(root));
        stage.setTitle(text);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(current);
        stage.show();
        return new LoadingProvider() {
            @Override
            public void close() {
                Platform.runLater(() -> {
                    stage.hide();
                    stage.close();
                });
            }

            @Override
            public void setProgress(double d) {
                controller.loading.setProgress(d);
            }
        };
    }
}
