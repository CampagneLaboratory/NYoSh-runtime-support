package org.campagnelab.nyosh.exec;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

/**
 * A thread that consumes the output produces by another command.
 * @author Fabien Campagne
 *         Date: 6/16/13
 *         Time: 12:10 PM
 */
public class OutputConsumerRunnable implements Runnable {
    private final CountDownLatch countDownCloseOutput;
    OutputConsumer consumer;
    private InputStream inputStream;

    public OutputConsumerRunnable(OutputConsumer consumer, InputStream inputStream,
                                  CountDownLatch countDownCloseOutput) {
        this.consumer = consumer;
        this.inputStream = inputStream;
        this.countDownCloseOutput=countDownCloseOutput;
    }

    public void run() {
        try {
        consumer.consume(inputStream);
        }finally {
            try {
                try {
                    countDownCloseOutput.countDown();
                    countDownCloseOutput.await();
                } catch (InterruptedException e) {

                }
                inputStream.close();
            } catch (IOException e) {

            }
        }
    }
}
