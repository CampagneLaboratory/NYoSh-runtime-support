package org.campagnelab.nyosh.exec;

/**
 * @author Fabien Campagne
 *         Date: 6/18/13
 *         Time: 6:29 PM
 */
public interface IArgumentParser {
    IParsedArguments parse(String[] args);

}
