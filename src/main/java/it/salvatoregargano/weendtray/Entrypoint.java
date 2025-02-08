package it.salvatoregargano.weendtray;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class Entrypoint extends Application {
    public static final String executionId = UUID.randomUUID().toString();
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 0;
    private static final int PATCH_VERSION = 0;
    public static final String version = String.format("%d.%d.%d", MAJOR_VERSION, MINOR_VERSION, PATCH_VERSION);

    @Override
    public void start(Stage stage) throws IOException {
        CombinedLogger.getInstance().info("Starting application version " + version);
        FXMLLoader fxmlLoader = new FXMLLoader(Entrypoint.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}