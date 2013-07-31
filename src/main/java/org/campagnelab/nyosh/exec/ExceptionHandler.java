package org.campagnelab.nyosh.exec;

/**
 * Handle an exception in some way. Implementations would typically log the exception somewhere.
 * @author Fabien Campagne
 *         Date: 7/30/13
 *         Time: 1:27 PM
 */
public interface ExceptionHandler {
    public void handleException(Exception e);
}
