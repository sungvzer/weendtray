package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.BadCredentialsError;
import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.CredentialsServiceError;
import it.salvatoregargano.weendtray.acl.UserDeactivatedError;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;

public class LoginPageController {
    @FXML
    TextField username;

    @FXML
    TextField passwordField;

    @FXML
    void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) {
            return;
        }
        onLogin();
    }


    public void onLogin() {
        try {
            CredentialsService.getInstance().login(username.getText(), passwordField.getText());
        } catch (CredentialsServiceError e) {
            if (e instanceof BadCredentialsError) {
                AlertFactory.createAlert(Alert.AlertType.ERROR, "Nome utente o password errati.").showAndWait();
            }
            if (e instanceof UserDeactivatedError) {
                AlertFactory.createAlert(Alert.AlertType.ERROR, "L'utenza non è attiva.").showAndWait();
            }
            return;
        }

        URL url = getClass().getResource("/it/salvatoregargano/weendtray/HomePage.fxml");
        SceneManager.changeNodeSceneRootOrExit(username, url);
    }

    public void onSignup() {
    }
}
