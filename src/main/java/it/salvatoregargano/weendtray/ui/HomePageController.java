package it.salvatoregargano.weendtray.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.telephone.billing.Wallet;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

public class HomePageController {
    @FXML
    Text helloField;

    @FXML
    Tab usersTab;

    @FXML
    Tab userDashboardTab;

    @FXML
    TabPane tabPane;

    public void initialize() {
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
            StringBuilder greeting = new StringBuilder(
                    "Ciao, %s! Numero: %s".formatted(regularUser.getName(), regularUser.getPhoneNumber()));
            if (walletOptional.isPresent()) {
                greeting.append(" - Credito: €%.2f".formatted(walletOptional.get().getBalance()));
            }

            helloField.setText(greeting.toString());
        }

        usersTab.selectedProperty().addListener((_, _, isNowSelected) -> {
            if (isNowSelected && usersTab.getContent() == null) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/it/salvatoregargano/weendtray/UsersTab.fxml"));
                try {
                    Node content = loader.load();
                    usersTab.setContent(content);
                } catch (IOException e) {
                    CombinedLogger.getInstance().error("Could not load Users tab." + e.getCause().getMessage());
                }
            }
        });

        if (usersTab.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/salvatoregargano/weendtray/UsersTab.fxml"));
            try {
                Node content = loader.load();
                usersTab.setContent(content);
            } catch (IOException e) {
                CombinedLogger.getInstance().error("Could not load Users tab." + e.getCause().getMessage());
            }
        }
        userDashboardTab.selectedProperty().addListener((_, _, isNowSelected) -> {
            if (isNowSelected && userDashboardTab.getContent() == null) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/it/salvatoregargano/weendtray/UserDashboard.fxml"));
                try {
                    Node content = loader.load();
                    userDashboardTab.setContent(content);
                } catch (IOException e) {
                    CombinedLogger.getInstance().error("Could not load Users tab." + e.getCause().getMessage());
                }
            }
        });

        if (userDashboardTab.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/salvatoregargano/weendtray/UserDashboard.fxml"));
            try {
                Node content = loader.load();
                userDashboardTab.setContent(content);
            } catch (IOException e) {
                CombinedLogger.getInstance().error("Could not load Users tab." + e.getCause().getMessage());
            }
        }


    }

    public void onLogout() {
        CredentialsService.getInstance().logout();
        URL url = getClass().getResource("/it/salvatoregargano/weendtray/LoginPage.fxml");
        SceneManager.changeNodeSceneRootOrExit(helloField, url);
    }
}
