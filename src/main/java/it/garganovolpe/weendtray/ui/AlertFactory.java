package it.garganovolpe.weendtray.ui;

import java.util.Objects;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertFactory {
    private static final String GLOBAL_CSS = Objects.requireNonNull(AlertFactory.class.getResource("main.css"))
            .toExternalForm();

    public static Alert createAlert(Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        addStylesheets(alert);
        return alert;
    }

    public static Alert createAlert(Alert.AlertType alertType,
            String contentText,
            ButtonType... buttons) {
        Alert alert = new Alert(alertType, contentText, buttons);
        addStylesheets(alert);
        return alert;
    }

    static void addStylesheets(Alert alert) {
        alert.getDialogPane().getStylesheets().add(GLOBAL_CSS);
    }
}
