package it.salvatoregargano.weendtray.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

public class SceneFactory {
    private static final String GLOBAL_CSS = Objects.requireNonNull(SceneFactory.class.getResource("main.css")).toExternalForm();

    static void addStylesheets(Scene scene) {
        scene.getStylesheets().add(GLOBAL_CSS);
    }
    
    public static Scene createScene(Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        addStylesheets(scene);
        return scene;
    }

    public static Scene createScene(Parent root) {
        Scene scene = new Scene(root);
        addStylesheets(scene);
        return scene;
    }

}
