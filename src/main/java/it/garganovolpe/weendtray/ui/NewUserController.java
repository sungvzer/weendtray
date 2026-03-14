package it.garganovolpe.weendtray.ui;

import java.util.ArrayList;

import it.garganovolpe.weendtray.acl.RegularUser;
import it.garganovolpe.weendtray.acl.RegularUserBuilder;
import it.garganovolpe.weendtray.acl.UserAddress;
import it.garganovolpe.weendtray.acl.UserPersistence;
import it.garganovolpe.weendtray.acl.UserRole;
import it.garganovolpe.weendtray.telephone.billing.PhonePlan;
import it.garganovolpe.weendtray.telephone.billing.UserAccountKind;
import it.garganovolpe.weendtray.utils.StringChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/*
    * A controller for the new user registration page.
    * This controller manages the user input for creating a new account, including validating the input fields,
    * checking for existing usernames and phone numbers, and saving the new user to the persistence layer upon successful validation.
*/
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
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField countryField;

    @FXML
    private ChoiceBox<String> phonePlanChoiceBox;
    @FXML
    private ChoiceBox<String> accountKindChoiceBox;

    @FXML
    private void initialize() {
        PhonePlan[] plans = PhonePlan.values();
        ArrayList<String> planNames = new ArrayList<>();
        for (PhonePlan plan : plans) {
            planNames.add(plan.toString());
        }

        phonePlanChoiceBox.getItems().addAll(planNames);
        phonePlanChoiceBox.setValue(planNames.get(0));

        UserAccountKind[] accountKinds = UserAccountKind.values();
        ArrayList<String> accountKindNames = new ArrayList<>();
        for (UserAccountKind accountKind : accountKinds) {
            accountKindNames.add(accountKind.getName());
        }
        accountKindChoiceBox.getItems().addAll(accountKindNames);
        accountKindChoiceBox.setValue(accountKindNames.get(0));

        phoneNumberField.setTextFormatter(
                new javafx.scene.control.TextFormatter<Integer>(new javafx.util.converter.IntegerStringConverter(), 0,
                        change -> {
                            String newText = change.getControlNewText();
                            if (newText.matches("\\d*")) {
                                return change;
                            }
                            return null;
                        }));
    }

    @FXML
    private void handleSaveUser() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty()
                || surnameField.getText().isEmpty() || phoneNumberField.getText().isEmpty()
                || addressField.getText().isEmpty() || cityField.getText().isEmpty() || stateField.getText().isEmpty()
                || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty()) {
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

        if (UserPersistence.getInstance().getUserByUsername(usernameField.getText()) != null) {
            AlertFactory.createAlert(AlertType.ERROR, "Il nome utente è già in uso.").showAndWait();
            return;
        }

        if (UserPersistence.getInstance().isPhoneNumberInUse(phoneNumberField.getText())) {
            AlertFactory.createAlert(AlertType.ERROR, "Il numero di telefono è già in uso.").showAndWait();
            return;
        }

        UserAddress address = new UserAddress(addressField.getText(), cityField.getText(), postalCodeField.getText(),
                countryField.getText(), stateField.getText());

        UserAccountKind accountKind = UserAccountKind.valueOf(accountKindChoiceBox.getValue());

        RegularUser user = (RegularUser) new RegularUserBuilder().withPlainTextPassword(passwordField.getText())
                .withUsername(usernameField.getText())
                .withRole(UserRole.USER)
                .withName(nameField.getText())
                .withSurname(surnameField.getText())
                .withPhoneNumber("+39" + phoneNumberField.getText())
                .withPhonePlan(PhonePlan.valueOf(phonePlanChoiceBox.getValue()))
                .withKind(accountKind)
                .withAddress(address).build();

        UserPersistence.getInstance().saveUser(user);
        surnameField.getScene().getWindow().hide();
    }
}
