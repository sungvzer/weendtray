package it.salvatoregargano.weendtray.ui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.patterns.Observable;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;
import it.salvatoregargano.weendtray.telephone.billing.Biller;
import it.salvatoregargano.weendtray.utils.StringFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class InternetTabController extends Observable<PhoneEvent> {
    @FXML
    TextField urlTextField;

    @FXML
    Text statusText;

    @FXML
    CheckBox openBrowser;

    @FXML
    private void onGoButtonClicked() {
        statusText.setText("");
        String text = urlTextField.getText();
        User loggedUser = CredentialsService.getInstance().getLoggedUser();
        if (loggedUser == null) {
            AlertFactory.createAlert(AlertType.ERROR, "Utente non loggato.").showAndWait();
            return;
        }

        if (loggedUser.isAdminProperty().get()) {
            AlertFactory.createAlert(AlertType.ERROR, "Utente admin non ammesso.").showAndWait();
            return;
        }
        RegularUser regularUser = (RegularUser) loggedUser;

        try {
            URI uri = URI.create(text);
            if (uri.getScheme() == null) {
                text = "https://" + text;
            }

            uri = URI.create(text);

            if (openBrowser.isSelected()) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(uri);
                }
            }
            URL address = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) address.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                AlertFactory.createAlert(AlertType.ERROR, "URL non raggiungibile").showAndWait();
                return;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String responseBody = reader.lines().collect(Collectors.joining());
                int responseLengthBytes = responseBody.getBytes().length;
                notifyObservers(new DataUsageEvent(regularUser.getPhonePlan(), regularUser.getPhoneNumber(),
                        responseLengthBytes));
                statusText.setText(
                        "Caricamento completato. Dati scaricati: %s."
                                .formatted(StringFormatter.humanReadableByteCountBin(responseLengthBytes)));
            }
        } catch (Exception e) {
            AlertFactory.createAlert(AlertType.ERROR, "URL non valido").showAndWait();
        }
    }

    @FXML
    private void initialize() {
        addObserver(Biller.getInstance());
    }
}
