package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;

public class SceneManager {
    public static void changeNodeSceneRootOrExit(Node node, URL rootUrl) {
        FXMLLoader loader = new FXMLLoader(rootUrl);
        try {
            Parent root = loader.load();
            node.getScene().setRoot(root);
        } catch (IOException e) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Errore non recuperabile, l'applicazione terminerà una volta chiuso questo menù.").showAndWait();
            CombinedLogger.getInstance().error("Could not load url:" + e.getMessage());
            System.exit(1);
        }
    }

}
