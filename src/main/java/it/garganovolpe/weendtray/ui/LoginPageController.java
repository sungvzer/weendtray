package it.garganovolpe.weendtray.ui;

import java.io.IOException;
import java.net.URL;

import it.garganovolpe.weendtray.acl.BadCredentialsError;
import it.garganovolpe.weendtray.acl.CredentialsService;
import it.garganovolpe.weendtray.acl.CredentialsServiceError;
import it.garganovolpe.weendtray.acl.UserDeactivatedError;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginPageController {
    @FXML
    TextField username;

    @FXML
    CheckBox keepSession;

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
            CredentialsService.getInstance().login(username.getText(), passwordField.getText(),
                    keepSession.isSelected());
        } catch (CredentialsServiceError e) {
            if (e instanceof BadCredentialsError) {
                AlertFactory.createAlert(Alert.AlertType.ERROR, "Nome utente o password errati.").showAndWait();
            }
            if (e instanceof UserDeactivatedError) {
                AlertFactory.createAlert(Alert.AlertType.ERROR, "L'utenza non è attiva.").showAndWait();
            }
            return;
        }

        URL url = getClass().getResource("/it/garganovolpe/weendtray/HomePage.fxml");
        SceneManager.getInstance().changeNodeSceneRootOrExit(username, url);
    }

    public void onSignup() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/garganovolpe/weendtray/NewUser.fxml"));
            Stage newUserStage = new Stage();
            newUserStage.setTitle("Nuovo Utente");
            newUserStage.setScene(new Scene(root));
            newUserStage.setResizable(false);
            newUserStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
