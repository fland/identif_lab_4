package ua.pp.fland.labs.identif.lab4.model;

import org.apache.log4j.Logger;
import ua.pp.fland.labs.identif.lab4.model.beans.ManufacturingOperationDesc;
import ua.pp.fland.labs.identif.lab4.model.tools.ArgumentGuard;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxim Bondarenko
 * @version 1.0 10/22/11
 */

public class TemperatureCalculator {

    private static final Logger log = Logger.getLogger(TemperatureCalculator.class);

    private final int castIronMassDeviation;
    private final int movingToSlagRemovingTimeDeviation;
    private final int movingToMixerTimeDeviation;
    private final int desulfurationCount;
    private final int ladleCountDeviation;

    private final ManufacturingOperationDesc castIronDischarge;
    private final ManufacturingOperationDesc desulfurationDepartment;
    private final ManufacturingOperationDesc slagRemovingDepartment;
    private final ManufacturingOperationDesc mixerDepartment;
    private final ManufacturingOperationDesc movingToDesulfurationDepartment;
    private final ManufacturingOperationDesc movingToSlagRemovingDepartment;
    private final ManufacturingOperationDesc movingToMixerDepartment;
    private final ManufacturingOperationDesc movingToSlagRemovingDepartmentDeviation;
    private final ManufacturingOperationDesc movingToMixerDepartmentDeviation;
    private final Map<Integer, ManufacturingOperationDesc> blowingOff;

    private final static int BLOWING_OFF_MAX_COUNT = 3;

    public TemperatureCalculator(int castIronMassDeviation, int movingToSlagRemovingTimeDeviation,
                                 int movingToMixerTimeDeviation, int desulfurationCount, int ladleCountDeviation) {
        //todo add parameters checking
        ArgumentGuard.checkIntLessOrEqualThan(desulfurationCount, "desulfurationCount", BLOWING_OFF_MAX_COUNT);

        this.castIronMassDeviation = castIronMassDeviation;
        this.movingToMixerTimeDeviation = movingToMixerTimeDeviation;
        this.movingToSlagRemovingTimeDeviation = movingToSlagRemovingTimeDeviation;
        this.desulfurationCount = desulfurationCount;
        this.ladleCountDeviation = ladleCountDeviation;

        castIronDischarge = new ManufacturingOperationDesc(14, -48);
        desulfurationDepartment = new ManufacturingOperationDesc(25, -16);
        slagRemovingDepartment = new ManufacturingOperationDesc(19, -6);
        mixerDepartment = new ManufacturingOperationDesc(29, -5);
        movingToDesulfurationDepartment = new ManufacturingOperationDesc(70, -14);
        movingToSlagRemovingDepartment = new ManufacturingOperationDesc(30, -6);
        movingToSlagRemovingDepartmentDeviation = new ManufacturingOperationDesc(60, -8);
        movingToMixerDepartmentDeviation = new ManufacturingOperationDesc(60, -19);
        movingToMixerDepartment = new ManufacturingOperationDesc(20, -4);
        blowingOff = new HashMap<Integer, ManufacturingOperationDesc>();
        blowingOff.put(1, new ManufacturingOperationDesc(0, 0));
        blowingOff.put(2, new ManufacturingOperationDesc(0, -4));
        blowingOff.put(3, new ManufacturingOperationDesc(0, -7));
    }

    public Map<Integer, Float> calculateTemperature(int startCastIronTemperature) {
        log.debug("Start cast iron temp: " + startCastIronTemperature);
        Map<Integer, Float> timeTemperature = new HashMap<Integer, Float>();

        float currCastIronTemp = startCastIronTemperature;
        int currTime = 0;
        currCastIronTemp = currCastIronTemp + (0.5f * castIronMassDeviation);
        currCastIronTemp = currCastIronTemp + (3.0f * ladleCountDeviation);
        timeTemperature.put(currTime, currCastIronTemp);

        log.debug("Cast iron discharging...");
        currCastIronTemp = currCastIronTemp + castIronDischarge.getTemperatureDeviation();
        currTime = currTime + castIronDischarge.getDurationMin();
        timeTemperature.put(currTime, currCastIronTemp);
        log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);

        if (desulfurationCount > 0) {
            log.debug("Moving to desulfuration...");
            currCastIronTemp = currCastIronTemp + movingToDesulfurationDepartment.getTemperatureDeviation();
            currTime = currTime + movingToDesulfurationDepartment.getDurationMin();
            timeTemperature.put(currTime, currCastIronTemp);
            log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);

            log.debug("Desulfuration...");
            currTime = currTime + desulfurationDepartment.getDurationMin();
            currCastIronTemp = currCastIronTemp + desulfurationDepartment.getTemperatureDeviation();
            for (int i = 1; i <= desulfurationCount; i++) {
                log.debug("Starting " + i + " blowing off");
                currTime = currTime + blowingOff.get(i).getDurationMin();
                currCastIronTemp = currCastIronTemp + blowingOff.get(i).getTemperatureDeviation();
                timeTemperature.put(currTime, currCastIronTemp);
            }
            log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);
        } else {
            log.debug("No desulfuration...");
//            currCastIronTemp = currCastIronTemp + 16;
        }

        log.debug("Moving to slag removal department...");
        currTime = currTime + movingToSlagRemovingDepartment.getDurationMin();
        currTime = currTime + movingToSlagRemovingTimeDeviation;
        currCastIronTemp = currCastIronTemp + movingToSlagRemovingDepartment.getTemperatureDeviation();
        currCastIronTemp = currCastIronTemp + (movingToSlagRemovingTimeDeviation /
                (float) movingToSlagRemovingDepartmentDeviation.getDurationMin() *
                movingToSlagRemovingDepartmentDeviation.getTemperatureDeviation());
        timeTemperature.put(currTime, currCastIronTemp);
        log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);

        log.debug("Slug removing...");
        currTime = currTime + slagRemovingDepartment.getDurationMin();
        currCastIronTemp = currCastIronTemp + slagRemovingDepartment.getTemperatureDeviation();
        timeTemperature.put(currTime, currCastIronTemp);
        log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);

        log.debug("Moving to mixer department");
        currTime = currTime + movingToMixerDepartment.getDurationMin();
        currTime = currTime + movingToMixerTimeDeviation;
        currCastIronTemp = currCastIronTemp + movingToMixerDepartment.getTemperatureDeviation();
        currCastIronTemp = currCastIronTemp + (movingToMixerTimeDeviation /
                (float) movingToMixerDepartmentDeviation.getDurationMin() *
                movingToMixerDepartmentDeviation.getTemperatureDeviation());
        timeTemperature.put(currTime, currCastIronTemp);
        log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);

        log.debug("Mixer department");
        currTime = currTime + mixerDepartment.getDurationMin();
        currCastIronTemp = currCastIronTemp + mixerDepartment.getTemperatureDeviation();
        timeTemperature.put(currTime, currCastIronTemp);
        log.debug("Final time: " + currTime + " final temp: " + currCastIronTemp);

        return timeTemperature;
    }
}
