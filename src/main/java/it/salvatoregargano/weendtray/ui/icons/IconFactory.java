package it.salvatoregargano.weendtray.ui.icons;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class IconFactory {
    public static Image getIconWithColor(String iconName, String hexColor) {
        if (!hexColor.startsWith("#") || (hexColor.length() != 7 && hexColor.length() != 9)) {
            throw new IllegalArgumentException("Invalid hex color format. Expected format: #RRGGBB or #RRGGBBAA");
        }

        Image baseIcon = new Image(
                IconFactory.class.getResourceAsStream("/icons/" + iconName + ".png"));

        int[] modifiedPixels = new int[(int) (baseIcon.getWidth() * baseIcon.getHeight())];

        // change the black pixels of the base icon to the specified color
        baseIcon.getPixelReader().getPixels(0, 0, (int) baseIcon.getWidth(), (int) baseIcon.getHeight(),
                PixelFormat.getIntArgbInstance(), modifiedPixels, 0, (int) baseIcon.getWidth());

        int newColor = Integer.parseInt(hexColor.substring(1), 16);
        for (int i = 0; i < modifiedPixels.length; i++) {
            int pixel = modifiedPixels[i];
            if ((pixel & 0xFF000000) == 0) {
                continue;
            }

            if ((modifiedPixels[i] & 0x00FFFFFF) == 0x000000) {
                modifiedPixels[i] = (modifiedPixels[i] & 0xFF000000) | newColor;
            }
        }

        WritableImage coloredIcon = new WritableImage((int) baseIcon.getWidth(), (int) baseIcon.getHeight());
        coloredIcon.getPixelWriter().setPixels(0, 0,
                (int) baseIcon.getWidth(), (int) baseIcon.getHeight(),
                PixelFormat.getIntArgbInstance(), modifiedPixels, 0, (int) baseIcon.getWidth());
        return coloredIcon;
    }
}
