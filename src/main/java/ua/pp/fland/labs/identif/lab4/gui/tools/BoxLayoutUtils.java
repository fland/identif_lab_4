package ua.pp.fland.labs.identif.lab4.gui.tools;

import javax.swing.*;

public class BoxLayoutUtils {

    private BoxLayoutUtils() {
    }

    /**
     * Returns panel with vertical elements placing
     *
     * @return vertical panel
     */
    public static JPanel createVerticalPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
//        p.setBackground(Color.GREEN);
        return p;
    }

    /**
     * Returns panel with horizontal elements placing
     *
     * @return horizontal panel
     */
    public static JPanel createHorizontalPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
//        p.setBackground(Color.RED);
        return p;
    }

    public static void setGroupAlignmentX(JComponent[] cs, float alignment) {
        for (JComponent c : cs) {
            c.setAlignmentX(alignment);
        }
    }

    /**
     * Makes the same alignment on YAxis for group of elements
     *
     * @param cs        components to align
     * @param alignment type of alignment
     */
    public static void setGroupAlignmentY(JComponent[] cs, float alignment) {
        for (JComponent c : cs) {
            c.setAlignmentY(alignment);
        }
    }

}
