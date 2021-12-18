package gov.nsa.kore.ng.util;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class TextEntry extends HBox {
    public TextEntry(String iconCode, String content) {
        List<Node> children = this.getChildren();
        FontIcon icon = new FontIcon(iconCode);
        children.add(icon);
        setMargin(icon, new Insets(2, 0, 0, 0));
        Label text = new Label(content);
        children.add(text);
        setMargin(text, new Insets(0, 0, 0, 4));
        setPadding(new Insets(3));
    }
}
