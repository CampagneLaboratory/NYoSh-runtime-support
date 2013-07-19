package org.campagnelab.nyosh.environment.parsers;

import org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for files in which each row is in the format KEY=VALUE.
 * @author manuele
 */
public class MapFileParser implements Parser {

    /**
     * Parses the file and populates the {@link org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment}
     *
     * @param file
     */
    @Override
    public void parseAtRunTime(String file) {
        NYoShRuntimeEnvironment environment = NYoShRuntimeEnvironment.getEnvironment();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    environment.addVariable(tokens[0], tokens[1].trim().replaceAll("(^\")|(\"$)", ""));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Invalid source");
            e.printStackTrace();
        }
    }

    /**
     * Parses the file.
     * //TODO: the implementation of this method will change when it will be integrated with the plugins-SDK
     * @param file
     * @return the list of variables found in the file.
     */
    @Override
    public List<String> parseAtDesignTime(String file) {
        List<String> returnedKeys = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    returnedKeys.add(tokens[0]);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Invalid source");
            e.printStackTrace();
        }

        return returnedKeys;

    }


}
