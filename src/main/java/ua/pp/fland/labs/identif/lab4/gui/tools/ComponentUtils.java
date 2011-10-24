package ua.pp.fland.labs.identif.lab4.gui.tools;

import javax.swing.*;
import java.awt.*;

/**
 * User: anna
 * Date: Oct 18, 2010
 */
public class ComponentUtils {

    private ComponentUtils() {
    }

    public static void setSize(JComponent component, int width, int height) {
        Dimension textPanelSize = new Dimension(width, height);
        component.setSize(textPanelSize);
        component.setPreferredSize(textPanelSize);
        component.setMaximumSize(textPanelSize);
        component.setMinimumSize(textPanelSize);
    }
}
