module it.salvatoregargano.weendtray {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens it.salvatoregargano.weendtray to javafx.fxml;
    exports it.salvatoregargano.weendtray;
}