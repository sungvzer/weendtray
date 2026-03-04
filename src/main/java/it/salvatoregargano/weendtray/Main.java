package it.salvatoregargano.weendtray;

import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.persistence.MigrationRunner;
import it.salvatoregargano.weendtray.ui.SceneFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Main extends Application {
    public static final String executionId = UUID.randomUUID().toString();
    private static Stage primaryStage;

    void tryLoadFont() {
        try (var fontStream = getClass().getResourceAsStream("fonts/Lato-Regular.ttf")) {
            Font.loadFont(fontStream, 12.0);
        } catch (IOException e) {
            System.err.println("Could not load font.");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        try {
            MigrationRunner.runMigrations();
        } catch (SQLException | IOException | URISyntaxException e) {
            CombinedLogger.getInstance().error("An error occurred while running the migrations.\n" + Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }

        tryLoadFont();

        URL homePage;

        if (!UserPersistence.atLeastOneAdminUser()) {
            homePage = getClass().getResource("AdminFirstPage.fxml");
        } else {
            homePage = getClass().getResource("HomePage.fxml");
        }
        FXMLLoader loader = new FXMLLoader(homePage);
        VBox root = loader.load();
        Scene scene = SceneFactory.createScene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("WeendTray");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/logo.png"))));
        primaryStage.show();
    }
}