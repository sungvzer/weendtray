open module WeendTray {
    requires bcrypt;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;

    exports it.salvatoregargano.weendtray;
}
