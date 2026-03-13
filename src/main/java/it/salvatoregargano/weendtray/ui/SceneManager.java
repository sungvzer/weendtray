package it.salvatoregargano.weendtray.ui;

import java.io.IOException;
import java.net.URL;

import it.salvatoregargano.weendtray.logging.GetLoggerProviderFromEnv;
import it.salvatoregargano.weendtray.logging.LoggerInjector;
import it.salvatoregargano.weendtray.logging.LoggerProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

public class SceneManager {
    private static SceneManager instance;

    @GetLoggerProviderFromEnv(defaultType = "COMBINED")
    private LoggerProvider loggerProvider;

    private SceneManager() {
        LoggerInjector.inject(this);
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public FXMLLoader changeNodeSceneRootOrExit(Node node, URL rootUrl) {
        FXMLLoader loader = new FXMLLoader(rootUrl);
        try {
            Parent root = loader.load();
            node.getScene().setRoot(root);
        } catch (IOException e) {
            AlertFactory
                    .createAlert(Alert.AlertType.ERROR,
                            "Errore non recuperabile, l'applicazione terminerà una volta chiuso questo menù.")
                    .showAndWait();
            loggerProvider.createLogger().error("Could not load url:" + e.getMessage());
            System.exit(1);
        }
        return loader;
    }

}
