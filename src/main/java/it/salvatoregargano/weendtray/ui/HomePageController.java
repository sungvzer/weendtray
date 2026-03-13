package it.salvatoregargano.weendtray.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.patterns.Observer;
import it.salvatoregargano.weendtray.telephone.billing.Wallet;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

public class HomePageController implements Observer<Wallet> {
    @FXML
    Text helloField;

    @FXML
    Tab usersTab;

    @FXML
    Tab topUpTab;

    @FXML
    Tab userDashboardTab;

    @FXML
    TabPane tabPane;

    private void initializeTab(Tab tab, String fxmlPath) {
        tab.selectedProperty().addListener((_, _, isNowSelected) -> {
            if (isNowSelected && tab.getContent() == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                try {
                    Node content = loader.load();
                    tab.setContent(content);
                } catch (IOException e) {
                    CombinedLogger.getInstance()
                            .error("Could not load %s tab.".formatted(tab.getText()) + e.getCause().getMessage());
                }
            }
        });

        if (tab.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            try {
                Node content = loader.load();
                tab.setContent(content);
            } catch (IOException e) {
                CombinedLogger.getInstance()
                        .error("Could not load %s tab.".formatted(tab.getText()) + e.getCause().getMessage());
            }
        }
    }

    public void initialize() {
        WalletService.getInstance().addObserver(this);

        final User user = CredentialsService.getInstance().getLoggedUser();

        if (user.getRole() == UserRole.ADMIN) {
            tabPane.getTabs().remove(userDashboardTab);
            helloField.setText("Ciao, %s! Sei un admin.".formatted(user.getUsername()));
        } else {
            tabPane.getTabs().remove(usersTab);
            RegularUser regularUser = (RegularUser) user;
            Optional<Wallet> walletOptional = Optional.empty();
            try {
                Wallet wallet = WalletService.getInstance().getWallet(regularUser.getId());
                walletOptional = Optional.of(wallet);
            } catch (Exception e) {
                CombinedLogger.getInstance().error("Could not retrieve wallet for user %s: %s"
                        .formatted(regularUser.getUsername(), e.getMessage()));
                AlertFactory.createAlert(AlertType.ERROR, "Impossibile recuperare il portafoglio")
                        .showAndWait();
            }

            updateHelloField(walletOptional.map(Wallet::getBalance).orElse(0.0));
        }

        initializeTab(usersTab, "/it/salvatoregargano/weendtray/UsersTab.fxml");
        initializeTab(userDashboardTab, "/it/salvatoregargano/weendtray/UserDashboard.fxml");
        initializeTab(topUpTab, "/it/salvatoregargano/weendtray/TopUp.fxml");
    }

    public void onLogout() {
        CredentialsService.getInstance().logout();
        URL url = getClass().getResource("/it/salvatoregargano/weendtray/LoginPage.fxml");
        SceneManager.changeNodeSceneRootOrExit(helloField, url);
    }

    private void updateHelloField(Double balance) {
        User user = CredentialsService.getInstance().getLoggedUser();
        if (user.getRole() == UserRole.ADMIN) {
            helloField.setText("Ciao, %s! Sei un admin.".formatted(user.getUsername()));
        } else {
            RegularUser regularUser = (RegularUser) user;
            helloField.setText("Ciao, %s! Numero: %s - Credito: €%.2f".formatted(
                    regularUser.getName(),
                    regularUser.getPhoneNumber(),
                    balance));
        }
    }

    @Override
    public void update(Wallet event) {
        if (CredentialsService.getInstance().getLoggedUser().getId() == event.getUserId()) {
            updateHelloField(event.getBalance());
        }
    }
}
