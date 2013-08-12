package org.campagnelab.nyosh.exec;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * An interface for callbacks that consume input streams.
 *
 * @author Fabien Campagne
 *         Date: 6/16/13
 *         Time: 11:51 AM
 */
public abstract class OutputConsumer {
    /**
     * Consume is called with the input stream to consume.
     *
     * @param inputStream
     */
    public abstract void consume(InputStream inputStream);


    private ExceptionHandler handler=null;

    /**
     * Set an implementation of exception handler.
     *
     * @param handler
     */
    public void setHandler(ExceptionHandler handler) {
        this.handler = handler;
    }

    /**
     * A handler method that delegates to the handler, if present.
     */
    protected void handleException(Exception e) {
        if (handler != null) {
            handler.handleException(e);
        }
    }
}
