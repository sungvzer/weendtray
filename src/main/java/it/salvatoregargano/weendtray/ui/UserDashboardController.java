package it.salvatoregargano.weendtray.ui;

import java.sql.SQLException;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.patterns.Observer;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;
import it.salvatoregargano.weendtray.telephone.billing.Wallet;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

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
        private Button dataBtn;

        private RegularUser user;

        private Wallet userWallet;

        @FXML
        private void initialize() throws SQLException {
                WalletService.getInstance().addObserver(this);
                user = (RegularUser) CredentialsService.getInstance().getLoggedUser();
                userWallet = WalletService.getInstance().getWallet(user.getId());

                refreshUI();

        }

        private void refreshUI() {
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
                        userDataTraffic.setText(userWallet.getDataCount() + " MB" + " / "
                                        + user.getPhonePlan().getDataLimitMB() + " MB");
                        userCallMinutes.setText(
                                        userWallet.getMinutesCount() + " min" + " / "
                                                        + user.getPhonePlan().getMinutesLimit() + " min");
                        userMessagesAmount.setText(
                                        userWallet.getMessagesCount() + " sms" + " / "
                                                        + user.getPhonePlan().getMessagesLimit() + " sms");
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
