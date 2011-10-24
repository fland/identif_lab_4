package ua.pp.fland.labs.identif.lab4.gui;

import java.awt.*;

/**
 * @author Maxim Bondarenko
 * @version 1.0 6/27/11
 */

public enum StandardDimension {

    VER_RIGID_AREA(new Dimension(0, 12)),
    VER_HALF_RIGID_AREA(new Dimension(0, 6)),
    HOR_RIGID_AREA(new Dimension(12, 0)),
    HOR_HALF_RIGID_AREA(new Dimension(6, 0));

    private final Dimension value;

    private StandardDimension(Dimension value) {
        this.value = value;
    }

    public Dimension getValue() {
        return value;
    }
}
