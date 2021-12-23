package gov.nsa.kore.ng.controller;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.util.FakeLoadingProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    public VBox rootPane;
    @FXML
    public ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                FakeLoadingProvider.provideFakeLoad(Main.FAST_SPLASH, progressBar::setProgress);
                Platform.runLater(() -> {
                    try {
                        Parent root = FXMLLoader.load(Main.class.getResource("main-view.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("Kore.NG v6");
                        stage.show();
                        rootPane.getScene().getWindow().hide();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
