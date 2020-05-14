package gui;

import gui.saveWindows.JInternalFrameLoader;
import gui.saveWindows.Savable;
import gui.saveWindows.SavableWindowSettings;
import gui.saveWindows.SavableWindowsStorage;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.TextArea;
import java.awt.BorderLayout;
import java.io.File;
import java.util.Observer;
import java.util.Observable;

public class RobotCoordinatesWindow extends JInternalFrame implements Savable, Observer {

    private TextArea m_content;
    private Robot m_robot;

    public RobotCoordinatesWindow(Robot robot) {
        super("Координаты робота", true, true, true, true);
        m_robot = robot;
        SavableWindowsStorage.add(this);
        robot.addObserver(this);

        m_content = new TextArea("");
        m_content.setSize(400, 300);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_content, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public void update(Observable o, Object key)
    {
        m_content.setText(m_robot.getX() + " " + m_robot.getY());
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
        setSize(120, 70);
    }
}
