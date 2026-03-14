package it.garganovolpe.weendtray.ui;

import java.util.Objects;

import javafx.scene.Parent;
import javafx.scene.Scene;
/*
    * A factory class for creating styled scenes used throughout the application.
    * This factory ensures that all scenes have a consistent look and feel by applying a global stylesheet.
*/
public class SceneFactory {
    private static final String GLOBAL_CSS = Objects.requireNonNull(SceneFactory.class.getResource("main.css"))
            .toExternalForm();

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
