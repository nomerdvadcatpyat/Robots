package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.HashMap;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private File file = new File(System.getProperty("user.home") + File.separator + "windows.txt");
    private WindowSettings mainWs = new WindowSettings("Main");

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

        HashMap<String, Boolean> openInternalFrames = new HashMap<>();

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);


        JInternalFrame[] internalFrames = desktopPane.getAllFrames();
        for (JInternalFrame internalFrame : internalFrames)
            openInternalFrames.put(internalFrame.getTitle(), false);

        if (file.exists() && file.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                while (true) {
                    try {
                        WindowSettings ws = (WindowSettings) ois.readObject();
                        if (ws.getTitle().equals("Main")) loadMainFrameState(ws);
                        for (JInternalFrame internalFrame : internalFrames) {
                            if (internalFrame.getTitle().equals(ws.getTitle())) {
                                openInternalFrames.put(internalFrame.getTitle(), true);
                                loadInternalFrameState(internalFrame, ws);
                            }
                        }
                    } catch (EOFException e) {
                        break;
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            setExtendedState(MAXIMIZED_BOTH);
            gameWindow.setSize(400, 400);
            for (JInternalFrame internalFrame : internalFrames) {
                openInternalFrames.put(internalFrame.getTitle(), true);
            }
        }

        try {
            for (JInternalFrame internalFrame : internalFrames) {
                if (!openInternalFrames.get(internalFrame.getTitle())) internalFrame.setClosed(true);
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

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
                closeMainWindow();
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

    private void loadMainFrameState(WindowSettings ws) {
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

    private void loadInternalFrameState(JInternalFrame internalFrame, WindowSettings ws) throws PropertyVetoException {
        if(!ws.isIcon())
            desktopPane.setComponentZOrder(internalFrame,ws.getzOrder());
        internalFrame.setLocation(ws.getLocation());
        internalFrame.setSize(ws.getSize());
        if (ws.isMaximum()) internalFrame.setMaximum(true);
        if (ws.isIcon()) internalFrame.setIcon(true);
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

    private void closeMainWindow() {
        Object[] options = {"Да", "Нет"};
        int reply = JOptionPane
                .showOptionDialog(null, "Вы уверены что хотите выйти?",
                        "Выход", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (reply == JOptionPane.YES_OPTION) {
            try (
                    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            ) {
                oos.writeObject(mainWs);
                JInternalFrame[] frames = desktopPane.getAllFrames();
                for (JInternalFrame internalFrame :
                        frames) {
                    WindowSettings ws = new WindowSettings(internalFrame.getTitle());
                    ws.setMaximum(internalFrame.isMaximum());
                    ws.setIcon(internalFrame.isIcon());
                    ws.setzOrder(desktopPane.getComponentZOrder(internalFrame));
                    internalFrame.setIcon(false);
                    internalFrame.setMaximum(false);
                    ws.setSize(internalFrame.getSize());
                    ws.setLocation(internalFrame.getLocation());
                    oos.writeObject(ws);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
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
            exitButton.addActionListener((event) -> closeMainWindow());
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
}
