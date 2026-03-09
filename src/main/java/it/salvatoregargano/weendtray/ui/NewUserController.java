package it.salvatoregargano.weendtray.ui;

import java.util.ArrayList;

import it.salvatoregargano.weendtray.acl.PhonePlan;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.UserBuilder;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewUserController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ChoiceBox<String> phonePlanChoiceBox;

    @FXML
    private void initialize() {
        PhonePlan[] plans = PhonePlan.values();
        ArrayList<String> planNames = new ArrayList<>();
        for (PhonePlan plan : plans) {
            planNames.add(plan.toString());
        }

        phonePlanChoiceBox.getItems().addAll(planNames);
        phonePlanChoiceBox.setValue(planNames.get(0));
    }

    @FXML
    private void handleSaveUser() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty()
                || surnameField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "Tutti i campi devono essere compilati.").showAndWait();
            return;
        }

        if (phoneNumberField.getText().length() != 10 || !phoneNumberField.getText().matches("\\d+")) {
            AlertFactory.createAlert(AlertType.ERROR, "Il numero di telefono deve essere composto da 10 cifre.")
                    .showAndWait();
            return;
        }

        if (passwordField.getText().length() < 8) {
            AlertFactory.createAlert(AlertType.ERROR, "La password deve essere lunga almeno 8 caratteri.")
                    .showAndWait();
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            AlertFactory.createAlert(AlertType.ERROR, "Le password non coincidono.").showAndWait();
            return;
        }

        if (UserPersistence.getUserByUsername(usernameField.getText()) != null) {
            AlertFactory.createAlert(AlertType.ERROR, "Il nome utente è già in uso.").showAndWait();
            return;
        }

        if (UserPersistence.isPhoneNumberInUse(phoneNumberField.getText())) {
            AlertFactory.createAlert(AlertType.ERROR, "Il numero di telefono è già in uso.").showAndWait();
            return;
        }

        RegularUser user = (RegularUser) new UserBuilder().withPlainTextPassword(passwordField.getText())
                .withUsername(usernameField.getText())
                .withRole(UserRole.USER)
                .withName(nameField.getText())
                .withSurname(surnameField.getText())
                .withPhoneNumber(phoneNumberField.getText())
                .withPhonePlan(PhonePlan.valueOf(phonePlanChoiceBox.getValue())).build();

        UserPersistence.saveUser(user);
        surnameField.getScene().getWindow().hide();
    }
}
