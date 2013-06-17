package org.campagnelab.nyosh.exec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
        this(command, needsForwardPipe, needsForwardErrPipe, null, null);
    }

    public SequentialStepExecutor(String command, boolean needsForwardPipe, boolean needsForwardErrPipe, OutputConsumer stdOutConsumer, OutputConsumer stdErrConsumer) {
        super(command, needsForwardPipe, needsForwardErrPipe, stdOutConsumer, stdErrConsumer);

    }

    @Override
    public void executeStep() {
        Process p = null;
        try {
            //    System.out.println("Starting command: "+command);
            String[] envp = new String[0];
            p = Runtime.getRuntime().exec(command, envp, new File(workingDirectory));
            setupCountDown();
            installConsumers(p);
            makePipes(previous, p.getOutputStream());
            inputStreamForStdOut = p.getInputStream();
            inputStreamForStdErr = p.getErrorStream();
            //   System.out.println("Waiting for "+command+"..");
            exitCode = p.waitFor();
            closeAllOutputs(p);
            System.out.println("Command " + command + " finished with exit code: " + exitCode() +" in directory "+workingDirectory);

            if (!getErrorHandler().success(exitCode())) {
                forceEarlyStop();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

            closeAllOutputs(p);

        }
    }

    private void closeAllOutputs(Process p) {
        try {
          if (countDownCloseOutput!=null){
              countDownCloseOutput.await();
          }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            if (previous != null && previous.getInputStreamForStdErr()!=null) {
                previous.getInputStreamForStdErr().close();
            }
            if (previous != null &&  previous.getInputStreamForStdOut()!=null) {
                previous.getInputStreamForStdOut().close();
            }
        } catch (IOException e) {

        }

    }

    private void installConsumers(Process p) {
        if (stdOutConsumer != null) {
            new Thread(new OutputConsumerRunnable(stdOutConsumer, p.getInputStream(), countDownCloseOutput)).start();
        }
        if (stdErrConsumer != null) {
            new Thread(new OutputConsumerRunnable(stdErrConsumer, p.getErrorStream(), countDownCloseOutput)).start();
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
