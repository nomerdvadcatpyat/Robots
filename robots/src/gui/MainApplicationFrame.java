package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame implements Savable {
    private static File windowsSettingsFile = new File(System.getProperty("user.home") + File.separator + "windows.txt");
    private final JDesktopPane desktopPane = new JDesktopPane();
    private SavableWindowSettings mainWs = new SavableWindowSettings("Main");

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        pack();
        setVisible(true);

        SavableWindowsStorage.add(this);
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);

        SavableWindowsStorage.loadWindows(windowsSettingsFile);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                mainWs.setIcon(true);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                mainWs.setIcon(false);
                if (mainWs.isMaximum()) setExtendedState(MAXIMIZED_BOTH);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                showExitDialog();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainWs.setSize(e.getComponent().getSize());
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    mainWs.setMaximum(true);
                }
                if (getExtendedState() == JFrame.NORMAL) {
                    mainWs.setMaximum(false);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                mainWs.setLocation(e.getComponent().getLocation());
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void showExitDialog() {
        Object[] options = {"Да", "Нет"};
        int reply = JOptionPane
                .showOptionDialog(null, "Вы уверены что хотите выйти?",
                        "Выход", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (reply == JOptionPane.YES_OPTION) {
            closeMainWindow();
        }
    }

    private void closeMainWindow() {
        SavableWindowsStorage.saveWindows(windowsSettingsFile);
        System.exit(0);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu exitMenu = new JMenu("Меню выхода");

        {
            JMenuItem exitButton = new JMenuItem("Закрыть приложение");
            exitButton.addActionListener((event) -> showExitDialog());
            exitMenu.add(exitButton);
        }

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(exitMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public void saveWindowSettings(File file) {
        SavableWindowSettings.writeWindowSettingsInFile(file, mainWs);
    }

    @Override
    public void loadWindowSettings(File file) {
        SavableWindowSettings ws = SavableWindowSettings.readWindowSettingsFromFile(file, mainWs.getTitle());
        if (ws == null) setDefaultSettings();
        else {
            if (ws.isIcon()) {
                if (ws.isMaximum()) mainWs.setMaximum(true);
                setExtendedState(ICONIFIED);
            } else {
                if (ws.isMaximum()) setExtendedState(MAXIMIZED_BOTH);
                else setExtendedState(NORMAL);
            }
            setLocation(ws.getLocation());
            setSize(ws.getSize());
        }
    }

    @Override
    public void setDefaultSettings() {
        setExtendedState(MAXIMIZED_BOTH);
    }

}
