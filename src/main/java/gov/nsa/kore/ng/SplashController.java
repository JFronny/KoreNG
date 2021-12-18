package gov.nsa.kore.ng;

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
import java.util.Random;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    public VBox rootPane;
    @FXML
    public ProgressBar progressBar;

    private final Random rnd = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    if (rnd.nextDouble() < 0.02)
                        Thread.sleep(500);
                    Thread.sleep(rnd.nextInt(Main.FAST_SPLASH ? 10 : 20, Main.FAST_SPLASH ? 30 : 100));
                    progressBar.setProgress(i * 0.01);
                }
                Platform.runLater(() -> {
                    try {
                        Parent root = FXMLLoader.load(SplashController.class.getResource("main-view.fxml"));
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
