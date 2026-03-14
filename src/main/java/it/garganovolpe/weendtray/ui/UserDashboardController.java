package it.garganovolpe.weendtray.ui;

import java.sql.SQLException;

import it.garganovolpe.weendtray.acl.CredentialsService;
import it.garganovolpe.weendtray.acl.RegularUser;
import it.garganovolpe.weendtray.patterns.Observer;
import it.garganovolpe.weendtray.telephone.billing.UserAccountKind;
import it.garganovolpe.weendtray.telephone.billing.Wallet;
import it.garganovolpe.weendtray.telephone.billing.WalletService;
import it.garganovolpe.weendtray.utils.StringFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
/*
        * A controller for the home page of the application, which serves as the main dashboard for regularusers after logging in.
        * This controller manages the display of user information, wallet balance, and navigation between different tabs based on user roles.
        * It also listens for updates to the user's wallet and refreshes the displayed information accordingly.
*/
public class UserDashboardController implements Observer<Wallet> {

        @FXML
        private Label userPlanType;

        @FXML
        private Label userDataTraffic;

        @FXML
        private Label userCallMinutes;

        @FXML
        private Label userMessagesAmount;

        @FXML
        private Label renewalLabel;

        @FXML
        private Label creditLabel;

        @FXML
        private Label userPhonePlan;

        @FXML
        private VBox usageBox;

        @FXML
        private ProgressBar dataProgress;

        @FXML
        private ProgressBar callProgress;

        @FXML
        private ProgressBar smsProgress;

        @FXML
        private Button callBtn;

        @FXML
        private Button smsBtn;

        @FXML
        private Button internetBtn;

        private RegularUser user;

        private Wallet userWallet;

        @FXML
        private void onInternet() {
                TabPane tabPane = (TabPane) userPlanType.getScene().lookup("#tabPane");

                Tab internetTab = tabPane.getTabs().stream().filter(tab -> "internetTab".equals(tab.getId()))
                                .findFirst()
                                .orElse(null);

                tabPane.getSelectionModel().select(internetTab);
        }

        @FXML
        private void onMessages() {
                TabPane tabPane = (TabPane) userPlanType.getScene().lookup("#tabPane");

                Tab messagesTab = tabPane.getTabs().stream().filter(tab -> "messagesTab".equals(tab.getId()))
                                .findFirst()
                                .orElse(null);

                tabPane.getSelectionModel().select(messagesTab);
        }

        @FXML
        private void onCall() {
                TabPane tabPane = (TabPane) userPlanType.getScene().lookup("#tabPane");

                Tab callTab = tabPane.getTabs().stream().filter(tab -> "callTab".equals(tab.getId()))
                                .findFirst()
                                .orElse(null);

                tabPane.getSelectionModel().select(callTab);
        }

        @FXML
        private void initialize() throws SQLException {
                WalletService.getInstance().addObserver(this);
                user = (RegularUser) CredentialsService.getInstance().getLoggedUser();
                userWallet = WalletService.getInstance().getWallet(user.getId());

                refreshUI();

        }

        private void refreshUI() {
                if (userWallet.getBalance() < 0) {
                        callBtn.setDisable(true);
                        smsBtn.setDisable(true);
                        internetBtn.setDisable(true);
                } else {
                        callBtn.setDisable(false);
                        smsBtn.setDisable(false);
                        internetBtn.setDisable(false);
                }

                var userPlanKind = user.getKind();

                userPhonePlan.setText("Piano Tariffario Attivo: " + user.getPhonePlan().toString());
                userPlanType.setText("Conto: " + userPlanKind.getName());
                creditLabel.setText(String.format("Credito residuo: %.2f €", userWallet.getBalance()));

                if (userPlanKind.equals(UserAccountKind.PAY_AS_YOU_GO)) {
                        usageBox.setManaged(false);
                        usageBox.setVisible(false);
                        renewalLabel.setManaged(false);
                        renewalLabel.setVisible(false);
                } else {
                        long dataLimitBytes = (long) (user.getPhonePlan().getDataLimitMB() * 1024 * 1024);
                        long dataUsedBytes = (long) (userWallet.getDataCount() * 1024 * 1024);
                        String humanReadableDataUsed = StringFormatter.humanReadableByteCountBin(dataUsedBytes);
                        String humanReadableDataLimit = StringFormatter.humanReadableByteCountBin(dataLimitBytes);
                        userDataTraffic.setText(
                                        String.format("%s / %s", humanReadableDataUsed, humanReadableDataLimit));
                        userCallMinutes.setText(String.format("%d min / %d min", userWallet.getMinutesCount(),
                                        user.getPhonePlan().getMinutesLimit()));
                        userMessagesAmount.setText(String.format("%d sms / %d sms", userWallet.getMessagesCount(),
                                        user.getPhonePlan().getMessagesLimit()));
                        dataProgress.setProgress(userWallet.getDataCount() / user.getPhonePlan().getDataLimitMB());
                        callProgress.setProgress(
                                        userWallet.getMinutesCount() / (double) user.getPhonePlan().getMinutesLimit());
                        smsProgress.setProgress(userWallet.getMessagesCount()
                                        / (double) user.getPhonePlan().getMessagesLimit());
                        renewalLabel.setText(String.format("Costo rinnovo: %.2f €/mese",
                                        user.getPhonePlan().getRenewalCost()));
                }
        }

        @Override
        public void update(Wallet event) {
                if (event.getUserId() == user.getId()) {
                        userWallet = event;
                        refreshUI();
                }
        }
}
