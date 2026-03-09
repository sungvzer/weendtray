package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.CredentialsService;
import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class HomePageController {
    @FXML
    Text helloField;

    @FXML
    Tab usersTab;


    public void initialize() {
        final User user = CredentialsService.getInstance().getLoggedUser();

        if (user.getRole() == UserRole.ADMIN) {
            helloField.setText("Ciao, %s! Sei un admin.".formatted(user.getUsername()));
        } else {
            RegularUser regularUser = (RegularUser) user;
            helloField.setText("Ciao, %s! Numero: %s".formatted(regularUser.getName(), regularUser.getPhoneNumber()));
        }

        usersTab.selectedProperty().addListener((_, _, isNowSelected) -> {
            if (isNowSelected && usersTab.getContent() == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/salvatoregargano/weendtray/UsersTab.fxml"));
                try {
                    Node content = loader.load();
                    usersTab.setContent(content);
                } catch (IOException e) {
                    CombinedLogger.getInstance().error("Could not load Users tab.");
                }
            }
        });

        if (usersTab.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/salvatoregargano/weendtray/UsersTab.fxml"));
            try {
                Node content = loader.load();
                usersTab.setContent(content);
            } catch (IOException e) {
                CombinedLogger.getInstance().error("Could not load Users tab.");
            }
        }
    }

    public void onLogout() {
        CredentialsService.getInstance().logout();
        URL url = getClass().getResource("/it/salvatoregargano/weendtray/LoginPage.fxml");
        SceneManager.changeNodeSceneRootOrExit(helloField, url);
    }
}
