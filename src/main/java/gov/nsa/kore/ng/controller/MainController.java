package gov.nsa.kore.ng.controller;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.util.FakeLoadingProvider;
import gov.nsa.kore.ng.util.RegexUtil;
import gov.nsa.kore.ng.util.TextEntry;
import gov.nsa.kore.ng.util.xml.XmlException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class MainController implements Initializable {
    @FXML
    public TextField inputBox;
    @FXML
    public FontIcon inputBoxIcon;
    @FXML
    public ScrollPane dialogPaneContainer;
    @FXML
    public VBox dialogPane;

    @FXML
    protected void evaluateInput() {
        String s = inputBox.getText();
        if (!RegexUtil.WHITESPACE.test(s)) {
            if (Main.SELECTED_AI == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No AI is selected, please select one!", ButtonType.OK);
                alert.show();
            }
            else {
                dialogPane.getChildren().add(new TextEntry("fas-long-arrow-alt-right", s));
                try {
                    dialogPane.getChildren().add(new TextEntry(inputBoxIcon.getIconLiteral(), Main.SELECTED_AI.evaluate(s, new LinkedList<>(Set.of(s)))));
                }
                catch (Throwable t) {
                    showError(t);
                }
                inputBox.setText("");
                Main.STAR_SCRIPT.clear();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputBox.textProperty().addListener(((observable, oldValue, newValue) -> selectIcon(newValue)));
        dialogPaneContainer.vvalueProperty().bind(dialogPane.heightProperty());
    }

    @FXML
    public void loadKore(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Kore.NG AI", "*.kng"));
        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        if (file != null) {
            AtomicReference<LoadingProvider> c = new AtomicReference<>();
            try {
                c.set(LoadingController.show("Loading AI", ((Node)event.getSource()).getScene().getWindow()));
            } catch (IOException e) {
                Platform.runLater(() -> showError(e));
            }
            new Thread(() -> {
                try {
                    Main.loadAI(file.toPath());
                    selectIcon(inputBox.getText());
                    FakeLoadingProvider.provideFakeLoad(true, c.get()::setProgress);
                } catch (InterruptedException | IOException | XmlException e) {
                    showError(e);
                } finally {
                    c.get().close();
                }
            }).start();
        }
    }

    private void showError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR, t.toString(), ButtonType.OK);
        alert.show();
    }

    private void selectIcon(String inputText) {
        if (RegexUtil.WHITESPACE.test(inputText))
            inputBoxIcon.setIconLiteral("far-comment");
        else
            inputBoxIcon.setIconLiteral(Main.SELECTED_AI == null ? "far-comment-dots" : Main.SELECTED_AI.getIcon(inputText));
    }
}
