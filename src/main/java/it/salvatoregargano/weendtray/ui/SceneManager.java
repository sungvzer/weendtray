package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneManager {
    public static final String I18N_BUNDLE_NAME = "messages";
    public static final Locale I18N_DEFAULT_LOCALE = Locale.ITALIAN;

    public static void changeNodeSceneRootOrExit(Node node, URL rootUrl) {
        ResourceBundle bundle = ResourceBundle.getBundle(I18N_BUNDLE_NAME, I18N_DEFAULT_LOCALE);

        FXMLLoader loader = new FXMLLoader(rootUrl);
        loader.setResources(bundle);
        try {
            Parent root = loader.load();
            node.getScene().setRoot(root);
        } catch (IOException e) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Errore non recuperabile, l'applicazione terminerà una volta chiuso questo menù.").showAndWait();
            CombinedLogger.getInstance().error("Could not load HomePage:" + e.getMessage());
            System.exit(1);
        }
    }

}
