package gov.nsa.kore.ng;

import gov.nsa.kore.ng.util.RegexUtil;
import gov.nsa.kore.ng.util.TextEntry;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

public class MainController implements Initializable {
    @FXML
    public TextField inputBox;
    @FXML
    public FontIcon inputBoxIcon;
    @FXML
    public VBox dialogPane;

    @FXML
    protected void evaluateInput() {
        String s = inputBox.getText();
        if (!RegexUtil.WHITESPACE.test(s)) {
            inputBox.setText("");
            dialogPane.getChildren().add(new TextEntry("fas-long-arrow-alt-right", s));
            try {
                dialogPane.getChildren().add(new TextEntry(inputBoxIcon.getIconLiteral(), Main.SELECTED_AI.evaluate(s, new LinkedList<>(Set.of(s)))));
            }
            catch (Throwable t) {
                dialogPane.getChildren().add(new TextEntry("far-times-circle", t.toString()));
            }
            Main.STAR_SCRIPT.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputBox.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (RegexUtil.WHITESPACE.test(newValue))
                inputBoxIcon.setIconLiteral("far-comment");
            else
                inputBoxIcon.setIconLiteral(Main.SELECTED_AI.getIcon(newValue));
        }));
    }
}
