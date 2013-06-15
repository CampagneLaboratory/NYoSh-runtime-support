package org.campagnelab.nyosh.exec;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 7:05 PM
 */
public class CmdErrorHandler {
    /**
     * Determines if the step succeeded.
     * @param exitCode
     * @return
     */
    public boolean success(int exitCode) {
        return  (exitCode==0) ;
    }

}
