open module WeendTray {
    requires bcrypt;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports it.salvatoregargano.weendtray;
}
