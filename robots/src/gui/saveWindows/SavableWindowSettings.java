package gui.saveWindows;

import java.awt.*;
import java.io.*;
import java.util.HashSet;

public class SavableWindowSettings implements Serializable {

    private String title;
    private Point location;
    private Dimension size;
    private boolean isIcon;
    private boolean isMaximum;

    static HashSet<SavableWindowSettings> savedWindowsSettings = new HashSet<>();

    public SavableWindowSettings(String title) {
        this.title = title;
    }

    public SavableWindowSettings(String title, Point location, Dimension size, boolean isIcon, boolean isMaximum) {
        this.title = title;
        this.location = location;
        this.size = size;
        this.isIcon = isIcon;
        this.isMaximum = isMaximum;
    }
    
    public static SavableWindowSettings readWindowSettingsFromFile(File file, String windowTitle) {
        SavableWindowSettings res = null;
        if (file.exists() && file.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                if(savedWindowsSettings.size() == 0) savedWindowsSettings = (HashSet<SavableWindowSettings>) ois.readObject();
                for (SavableWindowSettings ws: savedWindowsSettings) {
                    if (windowTitle.equals(ws.getTitle())) {
                        res = ws;
                        savedWindowsSettings.remove(ws);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static void writeWindowSettingsInFile(File file, SavableWindowSettings ws) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        ) {
            savedWindowsSettings.add(ws);
            oos.writeObject(savedWindowsSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public boolean isIcon() {
        return isIcon;
    }

    public void setIcon(boolean icon) {
        isIcon = icon;
    }

    public boolean isMaximum() {
        return isMaximum;
    }

    public void setMaximum(boolean maximum) {
        isMaximum = maximum;
    }
}
