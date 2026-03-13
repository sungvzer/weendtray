package it.salvatoregargano.weendtray.ui;

import java.util.ArrayList;

import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserAddress;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.telephone.billing.PhonePlan;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;
import it.salvatoregargano.weendtray.ui.icons.IconFactory;
import it.salvatoregargano.weendtray.utils.StringChecker;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class UserInfoController {
    private User user;
    private SimpleBooleanProperty isEditing;

    @FXML
    private ImageView disableUserIcon;
    @FXML
    private Button disableUserButton;

    @FXML
    private HBox phoneHBox;
    @FXML
    private HBox planHBox;
    @FXML
    private HBox addressHBox;
    @FXML
    private HBox cityHBox;
    @FXML
    private HBox stateHBox;
    @FXML
    private HBox postalCodeHBox;
    @FXML
    private HBox countryHBox;

    @FXML
    private ImageView editUserIcon;
    @FXML
    private Button editUserButton;

    @FXML
    private ImageView saveUserIcon;
    @FXML
    private Button saveUserButton;

    @FXML
    private ImageView enableUserIcon;
    @FXML
    private Button enableUserButton;

    @FXML
    private ImageView cancelIcon;
    @FXML
    private Button cancelButton;

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox<String> phonePlanChoiceBox;
    @FXML
    private ChoiceBox<String> accountKindChoiceBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField stateTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField countryTextField;

    @FXML
    private void initialize() {
        PhonePlan[] plans = PhonePlan.values();
        ArrayList<String> planNames = new ArrayList<>();
        for (PhonePlan plan : plans) {
            planNames.add(plan.toString());
        }
        phonePlanChoiceBox.getItems().setAll(planNames);

        UserAccountKind[] accountKinds = UserAccountKind.values();
        ArrayList<String> accountKindNames = new ArrayList<>();
        for (UserAccountKind accountKind : accountKinds) {
            accountKindNames.add(accountKind.getName());
        }
        accountKindChoiceBox.getItems().addAll(accountKindNames);

        isEditing = new SimpleBooleanProperty(false);
        isEditing.addListener((obs, oldVal, newVal) -> {
            setNodeVisible(saveUserButton, newVal);
            setNodeVisible(editUserButton, oldVal);
            setNodeVisible(cancelButton, newVal);
            nameTextField.setEditable(newVal);
            surnameTextField.setEditable(newVal);
            usernameTextField.setEditable(newVal);
            phoneTextField.setEditable(newVal);
            passwordField.setEditable(newVal);
            phonePlanChoiceBox.setDisable(!newVal);
            addressTextField.setEditable(newVal);
            cityTextField.setEditable(newVal);
            stateTextField.setEditable(newVal);
            postalCodeTextField.setEditable(newVal);
            countryTextField.setEditable(newVal);
            accountKindChoiceBox.setDisable(!newVal);
        });

        disableUserIcon.setImage(IconFactory.getIconWithColor("phone_disabled", "#FF0000"));
        enableUserIcon.setImage(IconFactory.getIconWithColor("phone_enabled", "#00FF00"));
        editUserIcon.setImage(IconFactory.getIconWithColor("edit", "#000000"));
        saveUserIcon.setImage(IconFactory.getIconWithColor("save", "#000000"));
        cancelIcon.setImage(IconFactory.getIconWithColor("cancel", "#FF0000"));
        setNodeVisible(cancelButton, false);
        setNodeVisible(saveUserButton, false);
    }

    @FXML
    private void handleSaveUser() {
        String passwordResult = passwordField.getText().isEmpty() ? null
                : StringChecker.passwordChecker().check(passwordField.getText());
        if (passwordResult != null) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, passwordResult).showAndWait();
            return;
        }

        if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty()
                || usernameTextField.getText().isEmpty() || phoneTextField.getText().isEmpty()) {
            AlertFactory
                    .createAlert(Alert.AlertType.ERROR,
                            "I campi nome, cognome, nome utente e numero di telefono non possono essere vuoti.")
                    .showAndWait();
            return;
        }

        String phoneNumberValidationResult = StringChecker.phoneNumberChecker().check(phoneTextField.getText());
        if (phoneNumberValidationResult != null) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, phoneNumberValidationResult).showAndWait();
            return;
        }

        User userWithNewUsername = UserPersistence.getInstance().getUserByUsername(usernameTextField.getText());
        if (userWithNewUsername != null && userWithNewUsername.getId() != user.getId()) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Il nome utente è già in uso.").showAndWait();
            return;
        }

        if (!phoneTextField.getText().equals(((RegularUser) user).getPhoneNumber())
                && UserPersistence.getInstance().isPhoneNumberInUse(phoneTextField.getText())) {
            AlertFactory.createAlert(Alert.AlertType.ERROR, "Il numero di telefono è già in uso.").showAndWait();
            return;
        }

        user.setId(user.getId());
        user.setUsername(usernameTextField.getText());
        user.setName(nameTextField.getText());
        user.setSurname(surnameTextField.getText());
        if (!passwordField.getText().isEmpty()) {
            user.setPassword(User.hashPassword(passwordField.getText()));
        }
        if (!user.isAdminProperty().get()) {
            ((RegularUser) user).setPhoneNumber("+39" + phoneTextField.getText());
            ((RegularUser) user).setPhonePlan(PhonePlan.valueOf(phonePlanChoiceBox.getValue()));
            UserAddress address = new UserAddress(addressTextField.getText(), cityTextField.getText(),
                    postalCodeTextField.getText(), countryTextField.getText(), stateTextField.getText());
            ((RegularUser) user).setAddress(address);
            ((RegularUser) user).setKind(UserAccountKind.valueOf(accountKindChoiceBox.getValue()));
        }
        UserPersistence.getInstance().saveUser(user);
        loadUser(user);

        isEditing.set(false);
    }

    @FXML
    private void handleCancel() {
        isEditing.set(false);
        passwordField.setText(null);
        loadUser(user);
    }

    @FXML
    private void handleDisableUser() {
        if (user != null) {
            if (user.isAdminProperty().get()
                    && UserPersistence.getInstance().listUsers().stream().filter((u) -> u.isAdminProperty().get())
                            .count() == 1) {
                AlertFactory
                        .createAlert(Alert.AlertType.ERROR,
                                "Impossibile disattivare l'utente: è l'unico admin del sistema.")
                        .showAndWait();
                return;
            }

            var result = AlertFactory
                    .createAlert(Alert.AlertType.CONFIRMATION,
                            "Sicuro di voler disdire l'utenza " + user.getUsername() + "?")
                    .showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                user.setActive(!user.isActive());
                UserPersistence.getInstance().saveUser(user);
            }

            loadUser(user);
        }
    }

    @FXML
    private void handleEnableUser() {
        if (user != null) {
            user.setActive(true);
            UserPersistence.getInstance().saveUser(user);
            loadUser(user);
        }
    }

    private void setNodeVisible(Node button, boolean visible) {
        button.setVisible(visible);
        button.setManaged(visible);
    }

    public void loadUser(User user) {
        this.user = user;

        // Common properties
        usernameTextField.setText(user.getUsername());
        nameTextField.setText(user.getName());
        surnameTextField.setText(user.getSurname());
        // Admin properties
        if (user.isAdminProperty().get()) {
            setNodeVisible(disableUserButton, false);
            setNodeVisible(enableUserButton, false);
            setNodeVisible(phoneTextField, false);
            return;
        }

        RegularUser ru = (RegularUser) user;
        phoneTextField.setText(ru.getPhoneNumber().substring(3));
        setNodeVisible(phoneHBox, true);

        addressTextField.setText(ru.getAddress().getAddress());
        cityTextField.setText(ru.getAddress().getCity());
        stateTextField.setText(ru.getAddress().getState());
        postalCodeTextField.setText(ru.getAddress().getPostalCode());
        countryTextField.setText(ru.getAddress().getCountry());
        setNodeVisible(addressHBox, true);
        setNodeVisible(cityHBox, true);
        setNodeVisible(stateHBox, true);
        setNodeVisible(postalCodeHBox, true);
        setNodeVisible(countryHBox, true);

        phonePlanChoiceBox.setValue(ru.getPhonePlan().toString());
        setNodeVisible(planHBox, true);
        accountKindChoiceBox.setValue(ru.getKind().getName());
        if (user.isActive()) {
            setNodeVisible(disableUserButton, true);
            setNodeVisible(enableUserButton, false);
        } else {
            setNodeVisible(disableUserButton, false);
            setNodeVisible(enableUserButton, true);
        }
    }

    @FXML
    private void handleEditUser() {
        if (user == null) {
            return;
        }

        isEditing.set(true);
    }
}
