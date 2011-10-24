package ua.pp.fland.labs.identif.lab4.model.storage;

import java.io.IOException;
import java.util.Map;

/**
 * @author Maxim Bondarenko
 * @version 1.0 10/22/11
 */

public interface TimeTemperatureStorer {

    /**
     * Stores interpolated temperature at every time moment, set by time step
     *
     * @param timeTemperature pairs of time stamps and temperatures at current time
     * @param timeStepMin     time step to store in minutes
     * @throws java.io.IOException on I/O errors
     */
    void store(Map<Integer, Float> timeTemperature, int timeStepMin) throws IOException;
}
