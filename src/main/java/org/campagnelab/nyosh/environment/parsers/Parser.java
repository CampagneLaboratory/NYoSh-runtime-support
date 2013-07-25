package org.campagnelab.nyosh.environment.parsers;

import java.util.Map;

/**
 * Interface for parsers of Environment Sources.
 * @author manuele
 */
public interface Parser {

    /**
     * Parses the source and populates the {@link org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment}
     * @param source
     */
    public void parseAtRunTime(String source);

    /**
     * Parses the source.
     * @param source
     * @return the list of variables found in the source.
     */
    public Map<String,String> parseAtDesignTime(String source);

}
