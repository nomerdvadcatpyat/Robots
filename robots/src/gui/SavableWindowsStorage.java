package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavableWindowsStorage {
    private static List<Savable> savableWindows = new ArrayList<>();

    public static void add(Savable window) {
        savableWindows.add(window);
    }

    public static void loadWindows(File file) {
        for (Savable window :
                savableWindows) {
            window.loadWindowSettings(file);
        }
        file.delete();
    }

    public static void saveWindows(File file) {
        for (Savable window :
                savableWindows) {
            window.saveWindowSettings(file);
        }
    }
}
