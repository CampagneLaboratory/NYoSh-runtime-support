package org.campagnelab.nyosh.exec;

import java.io.IOException;
import java.io.InputStream;

/**
 * A thread that consumes the output produces by another command.
 * @author Fabien Campagne
 *         Date: 6/16/13
 *         Time: 12:10 PM
 */
public class OutputConsumerRunnable implements Runnable {
    OutputConsumer consumer;
    private InputStream inputStream;

    public OutputConsumerRunnable(OutputConsumer consumer, InputStream inputStream) {
        this.consumer = consumer;
        this.inputStream = inputStream;
    }

    public void run() {
        try {
        consumer.consume(inputStream);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {

            }
        }
    }
}
