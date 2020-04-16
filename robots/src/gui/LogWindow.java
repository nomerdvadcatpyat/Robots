package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import gui.saveWindows.JInternalFrameLoader;
import gui.saveWindows.Savable;
import gui.saveWindows.SavableWindowSettings;
import gui.saveWindows.SavableWindowsStorage;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import java.io.File;

public class LogWindow extends JInternalFrame implements LogChangeListener, Savable {
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        SavableWindowsStorage.add(this);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
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
        setLocation(10, 10);
        setSize(300, 800);
    }
}
