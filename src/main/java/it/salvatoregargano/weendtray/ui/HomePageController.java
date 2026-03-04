package it.salvatoregargano.weendtray.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class HomePageController {
    @FXML
    TextField emailField;

    @FXML
    TextField passwordField;

    public void onLogin(MouseEvent mouseEvent) {
        System.out.println("email: " + emailField.getText() + ", password: " + passwordField.getText());
    }
}
