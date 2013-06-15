package org.campagnelab.nyosh.exec;

import java.io.InputStream;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:47 PM
 */
public class ThreadExecutor extends SequentialStepExecutor implements Runnable {
    public ThreadExecutor(String command, boolean needsForwardPipe, boolean needsForwardErrPipe) {
        super(command,  needsForwardPipe,  needsForwardErrPipe);
    }

    @Override
    public void executeStep() {
        new Thread(this).start();
    }

    public void run() {
        // execute the sequential execution step, since we are now in a new thread.
        super.executeStep();
    }
}
