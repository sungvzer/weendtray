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
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Main extends Application {
    public static final String executionId = UUID.randomUUID().toString();
    public static final String I18N_BUNDLE_NAME = "messages";
    public static final Locale I18N_DEFAULT_LOCALE = Locale.ITALIAN;

    void tryLoadFont() {
        try (var fontStream = getClass().getResourceAsStream("fonts/Lato-Regular.ttf")) {
            Font.loadFont(fontStream, 12.0);
        } catch (IOException e) {
            System.err.println("Could not load font.");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
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
            homePage = getClass().getResource("LoginPage.fxml");
        }

        FXMLLoader loader = new FXMLLoader(homePage);
        VBox root = loader.load();
        Scene scene = SceneFactory.createScene(root);

        stage.setScene(scene);
        stage.setTitle("WeendTray");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/logo.png"))));
        stage.show();
    }
}