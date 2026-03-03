open module WeendTray {
    requires bcrypt;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires info.picocli;

    exports it.salvatoregargano.weendtray to javafx.graphics, info.picocli;
}