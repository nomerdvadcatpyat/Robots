package gui;

import gui.saveWindows.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameWindow extends SavableJInternalFrame {
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
    public void setDefaultSettings() {
        setSize(400, 400);
    }
}

