module it.salvatoregargano.weendtray {
    requires javafx.controls;
    requires javafx.fxml;
    requires info.picocli;
    requires java.sql;

    opens it.salvatoregargano.weendtray to javafx.fxml, info.picocli;
    exports it.salvatoregargano.weendtray;

}