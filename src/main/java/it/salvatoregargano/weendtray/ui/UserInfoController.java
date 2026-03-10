package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.User;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class UserInfoController {
    @FXML
    private Text usernameText;

    @FXML
    private void initialize() {
    }

    public void loadUser(User user) {
        usernameText.setText(user.getUsername());
    }
}
