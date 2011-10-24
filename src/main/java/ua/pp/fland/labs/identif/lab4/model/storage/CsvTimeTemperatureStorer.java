package ua.pp.fland.labs.identif.lab4.model.storage;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Maxim Bondarenko
 * @version 1.0 10/22/11
 */

public class CsvTimeTemperatureStorer implements TimeTemperatureStorer {
    private static final Logger log = Logger.getLogger(CsvTimeTemperatureStorer.class);

    private final CSVWriter writer;

    public CsvTimeTemperatureStorer(String outputFilePath) throws IOException {
        this(outputFilePath, '\t', '\"');
    }

    public CsvTimeTemperatureStorer(String outputFilePath, char separator, char quoteCharacter) throws IOException {
        writer = new CSVWriter(new FileWriter(outputFilePath), separator, quoteCharacter);
    }

    @Override
    public void store(Map<Integer, Float> timeTemperature, int timeStepMin) throws IOException {
        log.debug("Forming data to store");
        Map<Integer, Float> formattedData = new HashMap<Integer, Float>(getFormattedData(timeTemperature, timeStepMin));
        log.debug("Formatted data got. Storing...");
        writer.writeAll(formWriteableList(formattedData));

        writer.close();
    }

    private List<String[]> formWriteableList(Map<Integer, Float> formattedData) {
        List<Integer> timeStamps = new ArrayList<Integer>(formattedData.keySet());
        Collections.sort(timeStamps);
        List<String[]> res = new ArrayList<String[]>();
        res.add(new String[]{"Time, min", "Temperature, C"});
        for (int timeStamp : timeStamps) {
            res.add(new String[]{String.valueOf(timeStamp), String.valueOf(formattedData.get(timeStamp))});
        }

        return res;
    }

    private Map<Integer, Float> getFormattedData(Map<Integer, Float> timeTemperature, int timeStepMin) {
        Map<Integer, Float> formattedData = new HashMap<Integer, Float>();

        List<Integer> timeStamps = new ArrayList<Integer>(timeTemperature.keySet());
        Collections.sort(timeStamps);
        Iterator<Integer> timeStampsIterator = timeStamps.iterator();
        int startTime = timeStampsIterator.next();
        while (timeStampsIterator.hasNext()) {
            int endTime = timeStampsIterator.next();
            if ((endTime - startTime) > timeStepMin) {
                int endTimeStamp = endTime - startTime;
                float tempStep = (timeTemperature.get(endTime) - timeTemperature.get(startTime)) /
                        (endTime - startTime);
                for (int i = 0; i <= endTimeStamp; i++) {
                    formattedData.put(i + startTime, (timeTemperature.get(startTime) + tempStep * i));
                }

                startTime = endTime;
            } else if ((endTime - startTime) == timeStepMin) {
                formattedData.put(startTime, timeTemperature.get(startTime));
                formattedData.put(endTime, timeTemperature.get(endTime));
                startTime = endTime;
            }
        }
        log.debug("Reached end of timeTemperature data");

        return formattedData;
    }
}
