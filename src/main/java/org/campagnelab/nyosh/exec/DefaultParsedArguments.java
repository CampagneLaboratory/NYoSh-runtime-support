package org.campagnelab.nyosh.exec;

/**
 * @author Fabien Campagne
 *         Date: 6/21/13
 *         Time: 6:35 PM
 */
public class DefaultParsedArguments implements IParsedArguments {
    String[] args;

    public DefaultParsedArguments(String[] args) {
        this.args = args;
    }

    public String[] args() {
        return args;
    }
}
