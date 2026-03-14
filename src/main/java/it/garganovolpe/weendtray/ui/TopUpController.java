package it.garganovolpe.weendtray.ui;

import it.garganovolpe.weendtray.acl.CredentialsService;
import it.garganovolpe.weendtray.telephone.billing.Wallet;
import it.garganovolpe.weendtray.telephone.billing.WalletService;
import it.garganovolpe.weendtray.utils.StringChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
/*
    * A controller for the top-up functionality in the user interface.
    * This controller manages the user input for topping up their wallet, 
    * including selecting a payment method and entering payment details.
    * It validates the input fields and interacts with the wallet service to add funds to the user's account, 
    * providing feedback on the success or failure of the operation.
*/
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
        amountTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), 0, change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                }));

        cardNumberField.setTextFormatter(
                new TextFormatter<String>(change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d{0,16}")) {
                        return change;
                    }
                    return null;
                }));

        expiryDateField.setTextFormatter(
                new TextFormatter<String>(change -> {
                    String newText = change.getControlNewText();
                    if (newText.length() > 2 && newText.charAt(2) != '/') {
                        return null;
                    }

                    if (newText.matches("^[012]?[0-9]?\\/?[0-9]{0,2}$")) {
                        return change;
                    }
                    return null;
                }));

        cvvField.setTextFormatter(
                new TextFormatter<String>(change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d{0,3}")) {
                        return change;
                    }
                    return null;
                }));

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
