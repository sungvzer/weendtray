package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.*;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;
import it.salvatoregargano.weendtray.telephone.billing.Wallet;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.sql.SQLException;


public class UserDashboardController {

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
        user = (RegularUser) CredentialsService.getInstance().getLoggedUser();
        userWallet = WalletService.getInstance().getWallet(user.getId());
        var userPlanKind = user.getKind();

        if (userPlanKind.equals(UserAccountKind.PAY_AS_YOU_GO)){
            usageBox.setManaged(false);
            usageBox.setVisible(false);
            renewalLabel.setManaged(false);
            renewalLabel.setVisible(false);

        }

        userPlanType.setText(userPlanType.getText() + userPlanKind.getName());
        userDataTraffic.setText();

    }
}
