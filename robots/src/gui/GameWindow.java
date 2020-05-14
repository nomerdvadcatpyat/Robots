package gui;

import gui.saveWindows.JInternalFrameLoader;
import gui.saveWindows.Savable;
import gui.saveWindows.SavableWindowSettings;
import gui.saveWindows.SavableWindowsStorage;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameWindow extends JInternalFrame implements Savable {
    private final GameVisualizer m_visualizer;
    private Robot robot;

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        SavableWindowsStorage.add(this);
        robot = new Robot();
        m_visualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public Robot getRobot() {
        return robot;
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

