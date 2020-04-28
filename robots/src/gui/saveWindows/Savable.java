package gui.saveWindows;

import java.io.File;

public interface Savable {
    void saveWindowSettings(File file);

    void loadWindowSettings(File file);

    void setDefaultSettings();
}
