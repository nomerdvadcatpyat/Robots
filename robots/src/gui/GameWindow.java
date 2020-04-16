package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.File;

public class GameWindow extends JInternalFrame implements Savable {
    private final GameVisualizer m_visualizer;

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        SavableWindowsStorage.add(this);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void saveWindowSettings(File file) {
        SavableWindowSettings.writeWindowSettingsInFile(file, new SavableWindowSettings(getTitle(), getLocation(), getSize(), isIcon(), isMaximum()));
    }

    @Override
    public void loadWindowSettings(File file) {
        SavableWindowSettings ws = SavableWindowSettings.readWindowSettingsFromFile(file, getTitle());
        if (ws == null) setDefaultSettings();
        else JInternalFrameLoader.load(this,ws);
    }

    @Override
    public void setDefaultSettings() {
        setSize(400, 400);
    }
}

