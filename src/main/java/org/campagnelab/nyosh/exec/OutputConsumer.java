package org.campagnelab.nyosh.exec;

import java.io.InputStream;

/**
 * An interface for callbacks that consume input streams.
 *
 * @author Fabien Campagne
 *         Date: 6/16/13
 *         Time: 11:51 AM
 */
public interface OutputConsumer {
    /**
     * Consume is called with the input stream to consume.
     *
     * @param inputStream
     */
    public void consume(InputStream inputStream);
}
