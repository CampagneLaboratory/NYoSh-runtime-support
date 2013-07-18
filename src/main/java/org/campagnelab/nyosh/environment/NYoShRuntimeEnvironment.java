package org.campagnelab.nyosh.environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Runtime environment visible to NyoSh scripts.
 * @author manuele
 */
public class NYoShRuntimeEnvironment {

    private static NYoShRuntimeEnvironment env = new NYoShRuntimeEnvironment();
    private final Map<String, String> variables;


    private NYoShRuntimeEnvironment() {
        variables = new HashMap<String, String>();
    }


    public static NYoShRuntimeEnvironment getEnvironment() {
        return env;
    }

    public void addVariable(String name, String value) {
        variables.put(name,value);
    }

    public String getVariableValue(String name) {
        return (variables.containsKey(name)) ? variables.get(name) : "";
    }

}

