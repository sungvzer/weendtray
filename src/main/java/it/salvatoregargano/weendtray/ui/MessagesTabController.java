package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.patterns.Observable;
import it.salvatoregargano.weendtray.telephone.MessageEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;
import it.salvatoregargano.weendtray.telephone.billing.Biller;
import it.salvatoregargano.weendtray.utils.StringChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class MessagesTabController extends Observable<PhoneEvent> {
    @FXML
    private TextField recipientField;

    @FXML
    private TextArea messageArea;

    @FXML
    private Text maxCharCount;

    @FXML
    private Text usedCharCount;

    @FXML
    private Text messagesCount;

    @FXML
    private void initialize() {
        addObserver(Biller.getInstance());

        maxCharCount.setText("160");
        usedCharCount.setText("0");
        messagesCount.setText("1");
    }

    @FXML
    private void onMessageAreaKeyTyped() {
        int currentLength = messageArea.getText().length();
        usedCharCount.setText(String.valueOf(currentLength));
        int messages = (currentLength / 160) + 1;
        maxCharCount.setText(String.valueOf(160 * messages));
        messagesCount.setText(String.valueOf(messages));
    }

    @FXML
    private void onSendMessage() {
        if (recipientField.getText().isEmpty() || messageArea.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "Numero di telefono o messaggio vuoto").showAndWait();
            return;
        }

        String phoneCheck = StringChecker.phoneNumberChecker().check(recipientField.getText());
        if (phoneCheck != null) {
            AlertFactory.createAlert(AlertType.ERROR, phoneCheck).showAndWait();
            return;
        }

        RegularUser currentUser = (RegularUser) CredentialsService.getInstance().getLoggedUser();
        MessageEvent event = new MessageEvent(currentUser.getPhonePlan(), currentUser.getPhoneNumber(),
                recipientField.getText(), messageArea.getText());
        notifyObservers(event);
        recipientField.clear();
        messageArea.clear();
        onMessageAreaKeyTyped();
    }

    @FXML
    private void onRecipientFieldKeyTyped(KeyEvent ev) {
        String c = ev.getCharacter();
        if (c.matches("\\D")) {
            recipientField.deletePreviousChar();
        }
    }

}
