package it.salvatoregargano.weendtray.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CallTabController {
    @FXML
    private TextField numberTextField;

    @FXML
    private Label callStateLabel;

    @FXML
    private Button callButton;

    @FXML
    private Button hangUpButton;

    @FXML
    private Label callTimeLabel;

    @FXML
    private Label costLabel;
}
