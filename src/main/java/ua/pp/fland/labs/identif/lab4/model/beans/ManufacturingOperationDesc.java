package ua.pp.fland.labs.identif.lab4.model.beans;

/**
 * @author Maxim Bondarenko
 * @version 1.0 10/22/11
 */

public class ManufacturingOperationDesc {
    private final int durationMin;

    private final int temperatureDeviation;

    public ManufacturingOperationDesc(int durationMin, int temperatureDeviation) {
        this.durationMin = durationMin;
        this.temperatureDeviation = temperatureDeviation;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public int getTemperatureDeviation() {
        return temperatureDeviation;
    }
}
