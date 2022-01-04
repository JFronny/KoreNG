package gov.nsa.kore.ng.controller;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.node.AINode;
import gov.nsa.kore.ng.model.EvaluationResult;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
    private AINode continueNode = null;

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
                AINode current = getNode();
                continueNode = null;
                EvaluationResult result = current.evaluate(s, new EvaluationParameter(s));
                if (result.success()) {
                    dialogPane.getChildren().add(new TextEntry(inputBoxIcon.getIconLiteral(), result.result().isPresent() ? result.result().get() : "<No Answer>"));
                    if (result.continueNode().isPresent()) {
                        continueNode = result.continueNode().get();
                    }
                }
                else {
                    showError(result.result().isPresent() ? result.result().get() : "Failed to execute");
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
                showError(e);
            }
            new Thread(() -> {
                try {
                    Main.loadAI(file.toPath());
                    selectIcon(inputBox.getText());
                    FakeLoadingProvider.provideFakeLoad(true, c.get()::setProgress);
                } catch (InterruptedException | IOException | XmlException | EvaluationException e) {
                    Platform.runLater(() -> showError(e));
                } finally {
                    c.get().close();
                }
            }).start();
        }
    }

    private void showError(Throwable t) {
        showError(t.toString());
    }

    private void showError(String t) {
        Alert alert = new Alert(Alert.AlertType.ERROR, t, ButtonType.OK);
        alert.show();
    }

    private void selectIcon(String inputText) {
        if (RegexUtil.WHITESPACE.test(inputText))
            inputBoxIcon.setIconLiteral("far-comment");
        else
            inputBoxIcon.setIconLiteral(Main.SELECTED_AI == null ? "far-comment-dots" : getNode().getIcon(inputText));
    }

    private AINode getNode() {
        return continueNode == null ? Main.SELECTED_AI : continueNode;
    }
}
