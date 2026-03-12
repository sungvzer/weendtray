package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;
import it.salvatoregargano.weendtray.acl.UserPersistence;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;


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

    private int userId;


}
