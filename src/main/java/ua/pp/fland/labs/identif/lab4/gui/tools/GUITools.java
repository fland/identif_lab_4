package ua.pp.fland.labs.identif.lab4.gui.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUITools {
    /**
     * Get the array of buttons and makes necessary margins from left and right
     *
     * @param buttons array of buttons
     */
    public static void createRecommendedMargin(JButton... buttons) {
        for (JButton button : buttons) {
            Insets margin = button.getMargin();
            margin.left = 12;
            margin.right = 12;
            button.setMargin(margin);
        }
    }

    /**
     * Fixes size of text field (usually its height)
     *
     * @param field JTextField to fix its size
     */
    public static void fixTextFieldSize(JTextField field) {
        Dimension size = field.getPreferredSize();
        size.width = field.getMaximumSize().width;
        field.setMaximumSize(size);
    }

    /**
     * Gets maximum value from array of values
     *
     * @param array array of values
     * @return maximum value
     */
    private static int maximumElementPosition(int[] array) {
        int maxPos = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxPos]) maxPos = i;
        }
        return maxPos;
    }


    /**
     * Makes same size (min, preferred, max) for group of elements. Components
     * take size of the widest element in the group
     *
     * @param components components to remake size of
     */
    public static void makeSameSize(JComponent... components) {
        int[] sizes = new int[components.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = components[i].getPreferredSize().width;
        }
        int maxSizePos = maximumElementPosition(sizes);
        Dimension maxSize =
                components[maxSizePos].getPreferredSize();
        for (JComponent component : components) {
            component.setPreferredSize(maxSize);
            component.setMinimumSize(maxSize);
            component.setMaximumSize(maxSize);
        }
    }

    public static Color determineDisabledColorByWitchCraft() {
        // this is little 'block' character..
        JButton b = new JButton(String.valueOf('\u2586'));
        b.setSize(b.getPreferredSize());
        b.setEnabled(false);
        BufferedImage biDisabled = new BufferedImage(
                b.getWidth(),
                b.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g2 = biDisabled.getGraphics();
        b.paint(g2);

        // get the middle pixel..
        int x = b.getWidth() / 2;
        int y = b.getHeight() / 2;

        return new Color(biDisabled.getRGB(x, y));
    }

}
