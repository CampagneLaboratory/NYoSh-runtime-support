package org.campagnelab.nyosh.environment.parsers;

import org.campagnelab.gobyweb.clustergateway.jobs.simulator.Option;

/**
 * An environment variable read by a Parser.
 */
public class ScriptVariable implements Comparable<ScriptVariable> {

    public static enum Kind {
        FILE, DIRECTORY, NUMBER, STRING
    }

    public final String name;
    public final String value;
    public final Kind kind;

    protected ScriptVariable(String name, String value, Kind kind) {
        this.name = name;
        this.value = value;
        this.kind = kind;
    }

    @Override
    public int compareTo(ScriptVariable variable) {
        return this.name.compareTo(variable.name);
    }

    protected static ScriptVariable fromOption(Option option) {
      return new ScriptVariable(option.name, option.value,Kind.valueOf(option.kind.name()));
    }

}
