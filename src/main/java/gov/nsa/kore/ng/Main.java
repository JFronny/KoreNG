package gov.nsa.kore.ng;

import gov.nsa.kore.ng.model.AINode;
import gov.nsa.kore.ng.model.DownNode;
import gov.nsa.kore.ng.model.OptionNode;
import gov.nsa.kore.ng.model.RandomSelectNode;
import gov.nsa.kore.ng.parse.AINodeTypeAdapter;
import gov.nsa.kore.ng.parse.DownNodeTypeAdapter;
import gov.nsa.kore.ng.parse.OptionNodeTypeAdapter;
import gov.nsa.kore.ng.parse.RandomSelectNodeTypeAdapter;
import gov.nsa.kore.ng.util.ClearScript;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Main extends Application {
    public static final XmlParser XML = new XmlParser()
            .register(DownNode.class, new AINodeTypeAdapter<>(new DownNodeTypeAdapter()))
            .register(OptionNode.class, new AINodeTypeAdapter<>(new OptionNodeTypeAdapter()))
            .register(RandomSelectNode.class, new AINodeTypeAdapter<>(new RandomSelectNodeTypeAdapter()));
    public static final ClearScript STAR_SCRIPT = new ClearScript();
    public static final Random RND = new Random(1024);
    public static AINode SELECTED_AI;
    public static boolean FAST_SPLASH = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 429, 265);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Kore.NG v6");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException, IOException, XmlException {
        if (args.length == 0) {
            System.err.println("No kng file specified"); //TODO allow selecting .kng file in UI
            Thread.sleep(1000);
            return;
        }
        Path p = Path.of(args[0]);
        if (!Files.exists(p)) {
            System.err.println("Specified file doesn't exist");
            Thread.sleep(1000);
            return;
        }
        try (InputStream is = Files.newInputStream(p)) {
            SELECTED_AI = XML.deserialize(is, AINode.class);
        }
        FAST_SPLASH = args.length >= 2 && args[1].equals("--fast-splash");
        launch();
    }
}