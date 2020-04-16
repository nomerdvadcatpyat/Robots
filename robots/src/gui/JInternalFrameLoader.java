package gui;

import javax.swing.*;
import java.beans.PropertyVetoException;

public class JInternalFrameLoader {
    public static void load(JInternalFrame frame, SavableWindowSettings ws) {
        frame.setLocation(ws.getLocation());
        frame.setSize(ws.getSize());
        try {
            if (ws.isMaximum()) frame.setMaximum(true);
            if (ws.isIcon()) frame.setIcon(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
