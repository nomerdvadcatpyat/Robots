package gui.saveWindows;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;

public abstract class SavableJInternalFrame extends JInternalFrame implements Savable {

    public SavableJInternalFrame(String title, boolean resizable, boolean closable,
                                 boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    public void saveWindowSettings(File file) {
        SavableWindowSettings.writeWindowSettingsInFile(file, new SavableWindowSettings(getTitle(), getLocation(), getSize(), isIcon(), isMaximum()));
    }

    @Override
    public void loadWindowSettings(File file) {
        SavableWindowSettings ws = SavableWindowSettings.readWindowSettingsFromFile(file, getTitle());
        if (ws == null) setDefaultSettings();
        else {
            setLocation(ws.getLocation());
            setSize(ws.getSize());
            try {
                if (ws.isMaximum()) setMaximum(true);
                if (ws.isIcon()) setIcon(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }
}
