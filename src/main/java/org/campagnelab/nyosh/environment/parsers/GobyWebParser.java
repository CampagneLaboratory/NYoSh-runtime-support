package org.campagnelab.nyosh.environment.parsers;

import org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for GobyWeb variables.
 * @author manuele
 */
public class GobyWebParser implements Parser {

    static Map<String,String> variables = new HashMap<String, String>();

    static {
        //static initialization of variable known to be used by GobyWeb on a cluster node
        //values are used at design time by the editor allowing to load other Environment Sources from the local filesystem
        variables.put("JOB_DIR",System.getProperty("user.home")+"/plugins-SDK-cache");
        variables.put("TMPDIR",System.getProperty("user.home")+"/plugins-SDK-cache");
        variables.put("SGE_O_WORKDIR",System.getProperty("user.home")+"/plugins-SDK-cache");
    }

    /**
     * Parses the source and populates the {@link org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment}
     *
     * @param source
     */
    @Override
    public void parseAtRunTime(String source) {
       //the variables already exist in the process environment (at least JOB_DIR), here we just populate the nyosh env
       //with them to avoid to rely on a previous loaded JVM Source
        NYoShRuntimeEnvironment environment = NYoShRuntimeEnvironment.getEnvironment();
        for (Map.Entry<String,String> variable : variables.entrySet())
            environment.addVariable(variable.getKey(), System.getenv().get(variable.getKey()));
    }

    /**
     * Parses the source.
     *
     * @param source
     * @return the list of variables found in the source.
     */
    @Override
    public Map<String, String> parseAtDesignTime(String source) {
        return variables;
    }
}
