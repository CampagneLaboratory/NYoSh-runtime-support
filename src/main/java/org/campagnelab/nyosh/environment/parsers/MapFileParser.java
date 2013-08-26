package org.campagnelab.nyosh.environment.parsers;

import org.apache.log4j.Logger;
import org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment;
import org.campagnelab.nyosh.logging.Log4JInitializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Parser for files in which each row is in the format KEY=VALUE.
 * @author manuele
 */
public class MapFileParser implements Parser {

    protected static Logger logger;

    static {
        Log4JInitializer.init();
        logger = Logger.getLogger(MapFileParser.class);
    }
    /**
     * Parses the file and populates the {@link NYoShRuntimeEnvironment}
     *
     * @param file
     */
    @Override
    public void parseAtRunTime(String ... file) {
        logger.debug("Loading " + file[0]);
        NYoShRuntimeEnvironment environment = NYoShRuntimeEnvironment.getEnvironment();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file[0]));
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
            logger.error("Invalid source",e);
        }
    }

    /**
     * Parses the file.
     * @param file
     * @return the list of designTimeDefaults found in the file.
     */
    @Override
    public SortedSet<ScriptVariable> parseAtDesignTime(String ... file) {
        SortedSet<ScriptVariable> returnedVariables = new TreeSet<ScriptVariable>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file[0]));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    returnedVariables.add(new ScriptVariable(tokens[0], tokens[1], ScriptVariable.Kind.STRING));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            logger.error("Invalid source",e);
        }
        return returnedVariables;

    }


}
