package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.telephone.billing.Wallet;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;
import it.salvatoregargano.weendtray.utils.StringChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class TopUpController {
    @FXML
    private ChoiceBox<String> paymentMethodChoiceBox;

    @FXML
    private VBox creditCardDetailsBox;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expiryDateField;

    @FXML
    private TextField cardholderNameField;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField amountTextField;

    @FXML
    private void handleCardNumberInput(KeyEvent event) {
        String c = event.getCharacter();
        if (c.matches("\\D") || cardNumberField.getText().length() > 16) {
            cardNumberField.deletePreviousChar();
        }
    }

    @FXML
    private void handleExpiryDateInput(KeyEvent event) {
        String c = event.getCharacter();

        if (c.matches("[^0-9/]") || expiryDateField.getText().length() > 5) {
            expiryDateField.deletePreviousChar();
        }
    }

    @FXML
    private void handleCVVInput(KeyEvent event) {
        String c = event.getCharacter();
        if (c.matches("\\D")) {
            cvvField.deletePreviousChar();
        }
    }

    @FXML
    private void handleAmountInput(KeyEvent event) {
        String c = event.getCharacter();
        if (c.matches("\\D")) {
            amountTextField.deletePreviousChar();
        }
    }

    @FXML
    private void handleTopUp() {
        String selectedPaymentMethod = paymentMethodChoiceBox.getSelectionModel().getSelectedItem();

        if (amountTextField.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "L'importo è obbligatorio").showAndWait();
            return;
        }

        if (!checkCardDetails()) {
            return; // Stop if card details are invalid
        }
        try {
            Wallet wallet = WalletService.getInstance()
                    .getWallet(CredentialsService.getInstance().getLoggedUser().getId());

            WalletService.getInstance().addAmountToWallet(wallet, Double.parseDouble(amountTextField.getText()));
            AlertFactory
                    .createAlert(AlertType.INFORMATION,
                            "Ricarica effettuata con successo con metodo: " + selectedPaymentMethod)
                    .showAndWait();
        } catch (Exception e) {
            AlertFactory
                    .createAlert(AlertType.ERROR, "Errore durante il ricaricamento del portafoglio: " + e.getMessage())
                    .showAndWait();
        }
    }

    private boolean checkCardDetails() {
        if (paymentMethodChoiceBox.getSelectionModel().getSelectedItem().equals("Contanti")) {
            return true; // No card details needed for cash
        }

        if (cardholderNameField.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "Il nome del titolare della carta è obbligatorio").showAndWait();
            return false;
        }

        if (cvvField.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "Il CVV è obbligatorio").showAndWait();
            return false;
        }

        if (cardNumberField.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "Il numero della carta è obbligatorio").showAndWait();
            return false;
        }

        if (expiryDateField.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "La data di scadenza è obbligatoria").showAndWait();
            return false;
        }

        StringChecker creditCardNumberChecker = new StringChecker()
                .withExactLength(16, "Il numero della carta deve essere di 16 cifre")
                .withDigits(16, "Il numero della carta deve contenere solo cifre");
        String creditCardNumber = cardNumberField.getText();
        String validationError = creditCardNumberChecker.check(creditCardNumber);
        if (validationError != null) {
            AlertFactory.createAlert(AlertType.ERROR, "Errore nel numero carta: " + validationError).showAndWait();
            return false;
        }

        String expiryDate = expiryDateField.getText();
        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            AlertFactory.createAlert(AlertType.ERROR, "La data di scadenza deve essere nel formato MM/YY")
                    .showAndWait();
            return false;
        }

        return true;
    }

    @FXML
    private void initialize() {
        paymentMethodChoiceBox.getItems().addAll("Contanti", "Carta di credito/debito", "Bancomat");
        paymentMethodChoiceBox.getSelectionModel().selectFirst();

        paymentMethodChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Contanti".equals(newVal)) {
                creditCardDetailsBox.setVisible(false);
                creditCardDetailsBox.setManaged(false);
            } else {
                creditCardDetailsBox.setVisible(true);
                creditCardDetailsBox.setManaged(true);
            }
        });
    }
}
