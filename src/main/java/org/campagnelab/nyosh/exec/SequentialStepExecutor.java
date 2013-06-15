package org.campagnelab.nyosh.exec;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Execute a Runtime command sequentially.
 *
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:33 PM
 */
public class SequentialStepExecutor extends CommandExecutor {


    private InputStream inputStreamForStdOut;
    private InputStream inputStreamForStdErr;


    public SequentialStepExecutor(String command, boolean needsForwardPipe, boolean needsForwardErrPipe) {
        super(command, needsForwardPipe, needsForwardErrPipe);
    }

    @Override
    public void executeStep() {
        try {
        //    System.out.println("Starting command: "+command);
            Process p = Runtime.getRuntime().exec(command);
            makePipes(previous, p.getOutputStream());
            inputStreamForStdOut = p.getInputStream();
            inputStreamForStdErr = p.getErrorStream();
         //   System.out.println("Waiting for "+command+"..");
            exitCode = p.waitFor();
            System.out.println("Command "+command+" finished with exit code: "+exitCode());
            if (!getErrorHandler().success(exitCode())) {
                forceEarlyStop();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public InputStream getInputStreamForStdOut() {
        return inputStreamForStdOut;
    }

    @Override
    public InputStream getInputStreamForStdErr() {
        return inputStreamForStdErr;
    }


}
