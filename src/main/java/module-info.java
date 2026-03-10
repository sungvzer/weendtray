open module WeendTray {
    requires bcrypt;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires java.desktop;

    exports it.salvatoregargano.weendtray;
}
