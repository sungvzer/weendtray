package it.salvatoregargano.weendtray.ui;

import java.util.Date;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.patterns.Observable;
import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;
import it.salvatoregargano.weendtray.telephone.billing.Biller;
import it.salvatoregargano.weendtray.ui.icons.IconFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CallTabController extends Observable<PhoneEvent> {
    @FXML
    private TextField phoneNumber;
    @FXML
    private Button clearButton;
    @FXML
    private Button callButton;
    @FXML
    private Button hangupButton;

    @FXML
    private HBox callDurationContainer;

    @FXML
    private Text callDurationText;

    private Timeline callTimer;

    private Date callStartTime;

    private boolean isCalling = false;

    @FXML
    private void onGridButtonAction(ActionEvent ev) {
        String digit = ((Button) ev.getSource()).getText();
        phoneNumber.setText(phoneNumber.getText() + digit);
    }

    @FXML
    private void toggleCalling() {
        if (phoneNumber.getText().isEmpty()) {
            AlertFactory.createAlert(AlertType.ERROR, "Il numero di telefono è obbligatorio")
                    .showAndWait();
            return;
        }

        isCalling = !isCalling;
        callButton.setVisible(!isCalling);
        callButton.setManaged(!isCalling);
        hangupButton.setVisible(isCalling);
        hangupButton.setManaged(isCalling);
        clearButton.setVisible(!isCalling);
        clearButton.setManaged(!isCalling);
        callDurationContainer.setVisible(isCalling);
        callDurationContainer.setManaged(isCalling);

        if (!isCalling) {
            java.time.Duration callDuration = java.time.Duration.ofMillis(
                    new Date().getTime() - callStartTime.getTime()).plusMinutes(1);
            callStartTime = null;
            if (callTimer != null) {
                callTimer.stop();
            }
            callDurationText.setText("");

            RegularUser user = (RegularUser) CredentialsService.getInstance().getLoggedUser();
            CallEvent callEvent = new CallEvent(user.getPhonePlan(), user.getPhoneNumber(), phoneNumber.getText(),
                    callDuration);
            notifyObservers(callEvent);

        } else {
            callStartTime = new Date();
            callDurationText.setText("00:00");
            if (callTimer != null) {
                callTimer.play();
            }
        }
    }

    @FXML
    private void initialize() {
        addObserver(Biller.getInstance());

        phoneNumber.setTextFormatter(
                new TextFormatter<String>(change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                }));

        ImageView callIcon = new ImageView(
                IconFactory.getIconWithColor("phone_enabled", "#00BB00"));
        callIcon.setFitWidth(16);
        callIcon.setFitHeight(16);
        callButton.setGraphic(callIcon);

        ImageView backspaceIcon = new ImageView(
                IconFactory.getIconWithColor("backspace", "#000000"));
        backspaceIcon.setFitWidth(16);
        backspaceIcon.setFitHeight(16);
        clearButton.setGraphic(backspaceIcon);

        ImageView hangupIcon = new ImageView(
                IconFactory.getIconWithColor("call_end", "#BB0000"));
        hangupIcon.setFitWidth(16);
        hangupIcon.setFitHeight(16);
        hangupButton.setGraphic(hangupIcon);

        callTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateDurationText();
        }));
        callTimer.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateDurationText() {
        if (callStartTime == null)
            return;

        long diffMillis = new Date().getTime() - callStartTime.getTime();
        long diffSeconds = diffMillis / 1000 % 60;
        long diffMinutes = diffMillis / (60 * 1000) % 60;
        long diffHours = diffMillis / (60 * 60 * 1000);

        String timeFormatted;
        if (diffHours > 0) {
            timeFormatted = String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
        } else {
            timeFormatted = String.format("%02d:%02d", diffMinutes, diffSeconds);
        }

        callDurationText.setText(timeFormatted);
    }

    @FXML
    private void onClear() {
        phoneNumber.setText("");
    }
}
