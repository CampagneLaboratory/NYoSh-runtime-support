package org.campagnelab.nyosh.environment.parsers;

import org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parser for the Java environment
 * @author manuele
 */
public class JVMEnvParser implements Parser {

    /**
     * Parses the Java environment and populates the {@link NYoShRuntimeEnvironment}
     *
     * @param source not used
     */
    @Override
    public void parseAtRunTime(String source) {
        NYoShRuntimeEnvironment environment = NYoShRuntimeEnvironment.getEnvironment();
        for (Map.Entry<String,String> entry : System.getenv().entrySet()) {
            environment.addVariable(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Parses the Java environment.
     *
     * @param source
     * @return the list of variables found in the Java environment plus the ones we know will be visible at runtime.
     */
    @Override
    public List<String> parseAtDesignTime(String source) {
        List<String> returnedKeys = new ArrayList<String>();
        for (Map.Entry<String,String> entry : System.getenv().entrySet()) {
            returnedKeys.add(entry.getKey());
        }
        return returnedKeys;
    }
}
