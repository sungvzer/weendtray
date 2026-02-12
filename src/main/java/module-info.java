module WeendTray {
    requires bcrypt;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    exports it.salvatoregargano.weendtray to javafx.graphics;
    opens it.salvatoregargano.weendtray to javafx.fxml;

}