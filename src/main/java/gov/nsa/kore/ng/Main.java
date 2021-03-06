package gov.nsa.kore.ng;

import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.node.*;
import gov.nsa.kore.ng.model.node.base.AINode;
import gov.nsa.kore.ng.parse.AINodeTypeAdapter;
import gov.nsa.kore.ng.parse.StarScriptAINodeTypeAdapter;
import gov.nsa.kore.ng.parse.StoreNodeTypeAdapter;
import gov.nsa.kore.ng.parse.TypeAdapterBuilder;
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
import java.util.Arrays;
import java.util.Random;

public class Main extends Application {
    public static final XmlParser XML = new XmlParser()
            .register(TypeAdapterBuilder.buildRecursive(DownNode.Builder::new, "down"), DownNode.class, DownNode.Builder.class)
            .register(TypeAdapterBuilder.buildRecursive(RandomSelectNode.Builder::new, "random"), RandomSelectNode.class, RandomSelectNode.Builder.class)
            .register(TypeAdapterBuilder.buildRecursive(AllNode.Builder::new, "all"), AllNode.class, AllNode.Builder.class)
            .register(TypeAdapterBuilder.buildStarScrpt(OptionNode.Builder::new, "option"),OptionNode.class, OptionNode.Builder.class)
            .register(new AINodeTypeAdapter<>(new StarScriptAINodeTypeAdapter<>(new StoreNodeTypeAdapter("store"))), StoreNode.class, StoreNode.Builder.class);
    public static final ClearScript STAR_SCRIPT = new ClearScript();
    public static final Random RND = new Random(1024);
    public static AINode SELECTED_AI;
    public static boolean FAST_SPLASH = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 429, 265);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Kore.NG v6");
        stage.setScene(scene);
	    stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException, IOException, XmlException, EvaluationException {
        if (args.length > 0 && !args[0].equals("--fast-splash")) {
            loadAI(Path.of(args[0]));
        }
        FAST_SPLASH = args.length >= 2 && Arrays.asList(args).contains("--fast-splash");
        launch();
    }

    public static void loadAI(Path source) throws InterruptedException, IOException, XmlException, EvaluationException {
        try (InputStream is = Files.newInputStream(source)) {
            AINode.Builder<?> builder = XML.deserialize(is, AINode.Builder.class);
            SELECTED_AI = builder.build();
            builder.compile(SELECTED_AI);
        }
    }
}
