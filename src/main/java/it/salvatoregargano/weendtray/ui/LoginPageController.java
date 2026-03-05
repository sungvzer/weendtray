package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.CredentialsService;
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
        boolean validCredentials = CredentialsService.getInstance().checkUserCredentials(username.getText(), passwordField.getText());

        if (!validCredentials) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Nome utente o password errati.").showAndWait();
            return;
        }

        URL url = getClass().getResource("/it/salvatoregargano/weendtray/HomePage.fxml");
        SceneManager.changeNodeSceneRootOrExit(username, url);
    }
}
