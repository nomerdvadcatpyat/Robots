package gui;

import gui.game.Robot;
import gui.mvc.Observer;
import gui.saveWindows.*;

import javax.swing.JPanel;
import java.awt.TextArea;
import java.awt.BorderLayout;

public class RobotCoordinatesWindow extends SavableJInternalFrame implements Observer {

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
    public void setDefaultSettings() {
        setSize(120, 70);
    }

    @Override
    public void update(Object obj) {
        m_content.setText(m_robot.getX() + " " + m_robot.getY());
    }
}
