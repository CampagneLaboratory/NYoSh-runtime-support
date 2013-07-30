package org.campagnelab.nyosh.environment.parsers;

import java.util.Map;
import java.util.SortedSet;

/**
 * Interface for parsers of Environment Sources.
 * @author manuele
 */
public interface Parser {

    /**
     * Parses the source and populates the {@link org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment}
     * @param parameters actual parameters for the parsing (depend on the parser implementation)
     */
    public void parseAtRunTime(String ... parameters);

    /**
     * Parses the source.
     * @param parameters actual parameters for the parsing (depend on the parser implementation)
     * @return the list of designTimeDefaults found in the source.
     */
    public SortedSet<ScriptVariable> parseAtDesignTime(String ... parameters);

}
