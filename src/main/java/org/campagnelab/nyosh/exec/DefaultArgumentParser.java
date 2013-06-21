package org.campagnelab.nyosh.exec;

/**
 * @author Fabien Campagne
 *         Date: 6/21/13
 *         Time: 6:33 PM
 */
public class DefaultArgumentParser implements IArgumentParser {
    private String[] args;

    public IParsedArguments parse(String[] args) {
        this.args = args;
        return new DefaultParsedArguments(args);
    }
}
