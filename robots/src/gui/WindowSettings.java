package gui;

import java.awt.*;
import java.io.Serializable;

public class WindowSettings implements Serializable {

    private int zOrder;
    private String title;
    private Point location;
    private Dimension size;
    private boolean isIcon;
    private boolean isMaximum;

    public WindowSettings(String title) {
        this.title = title;
    }

    public WindowSettings(String title, int zOrder, Point location, Dimension size, boolean isIcon, boolean isMaximum) {
        this.title = title;
        this.zOrder = zOrder;
        this.location = location;
        this.size = size;
        this.isIcon = isIcon;
        this.isMaximum = isMaximum;
    }

    public int getzOrder() {
        return zOrder;
    }

    public void setzOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public String getTitle() {
        return title;
    }

    public Point getLocation() {
        return location;
    }

    public Dimension getSize() {
        return size;
    }

    public boolean isIcon() {
        return isIcon;
    }

    public boolean isMaximum() {
        return isMaximum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public void setIcon(boolean icon) {
        isIcon = icon;
    }

    public void setMaximum(boolean maximum) {
        isMaximum = maximum;
    }
}
