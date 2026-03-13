open module WeendTray {
    requires bcrypt;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires javafx.media;

    exports it.salvatoregargano.weendtray;
}
