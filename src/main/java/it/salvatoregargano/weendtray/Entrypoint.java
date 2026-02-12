package it.salvatoregargano.weendtray;

import it.salvatoregargano.weendtray.persistence.MigrationRunner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.UUID;


public class Entrypoint extends Application {
    public static final String executionId = UUID.randomUUID().toString();

    @Override
    public void start(Stage stage) {
        try {
            MigrationRunner.runMigrations();
        } catch (Exception e) {
            return;
        }

        Label label = new Label("GUI Mode Activated");
        Scene scene = new Scene(new StackPane(label), 400, 300);
        stage.setScene(scene);
        stage.setTitle("My App - GUI");
        stage.show();
    }

}