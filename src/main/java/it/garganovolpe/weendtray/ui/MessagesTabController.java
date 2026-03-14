package it.garganovolpe.weendtray.ui;

import it.garganovolpe.weendtray.acl.CredentialsService;
import it.garganovolpe.weendtray.acl.RegularUser;
import it.garganovolpe.weendtray.patterns.Observable;
import it.garganovolpe.weendtray.telephone.MessageEvent;
import it.garganovolpe.weendtray.telephone.PhoneEvent;
import it.garganovolpe.weendtray.telephone.billing.Biller;
import it.garganovolpe.weendtray.utils.StringChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

        recipientField.setTextFormatter(
                new TextFormatter<String>(change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                }));
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

}
