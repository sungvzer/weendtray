package it.salvatoregargano.weendtray.ui;

import java.util.ArrayList;

import it.salvatoregargano.weendtray.acl.PhonePlan;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.UserBuilder;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.utils.StringChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

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
    private void handlePhoneNumberInput(KeyEvent event) {
        String c = event.getCharacter();
        if (c.matches("\\D") || phoneNumberField.getText().length() > 10) {
            phoneNumberField.deletePreviousChar();
        }
    }

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

        String phoneValidationResult = StringChecker.phoneNumberChecker().check(phoneNumberField.getText());
        if (phoneValidationResult != null) {
            AlertFactory.createAlert(AlertType.ERROR, phoneValidationResult).showAndWait();
            return;
        }

        String passwordValidationResult = StringChecker.passwordChecker().check(passwordField.getText());

        if (passwordValidationResult != null) {
            AlertFactory.createAlert(AlertType.ERROR, passwordValidationResult).showAndWait();
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
                .withPhoneNumber("+39" + phoneNumberField.getText())
                .withPhonePlan(PhonePlan.valueOf(phonePlanChoiceBox.getValue())).build();

        UserPersistence.saveUser(user);
        surnameField.getScene().getWindow().hide();
    }
}
