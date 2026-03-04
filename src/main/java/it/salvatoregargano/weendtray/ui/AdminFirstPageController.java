package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserBuilder;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminFirstPageController {
    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    PasswordField passwordRepeat;

    @FXML
    void onConfirm() {
        confirmed();
    }

    @FXML
    void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) {
            return;
        }
        confirmed();
    }

    void confirmed() {
        String first, second;
        first = password.getText();
        second = passwordRepeat.getText();

        if (!first.equals(second)) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Le due password non coincidono!").showAndWait();
            return;
        }

        User user = new UserBuilder()
                .withUsername(username.getText())
                .withPlainTextPassword(password.getText())
                .withRole(UserRole.ADMIN)
                .build();

        UserPersistence.saveUser(user);
        AlertFactory.createAlert(Alert.AlertType.CONFIRMATION, "Utente admin creato, verrai reindirizzato alla schermata di accesso.").showAndWait();


        var homePage = getClass().getResource("/it/salvatoregargano/weendtray/HomePage.fxml");

        FXMLLoader loader = new FXMLLoader(homePage);
        try {
            VBox root = loader.load();
            username.getScene().setRoot(root);
        } catch (IOException e) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Errore non recuperabile, l'applicazione terminerà una volta chiuso questo menù.").showAndWait();
            CombinedLogger.getInstance().error("Could not load HomePage:" + e.getMessage());
            System.exit(1);
        }
    }
}
