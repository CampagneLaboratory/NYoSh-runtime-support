package org.campagnelab.nyosh.exec;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:32 PM
 */
public abstract class CommandExecutor {
    protected final OutputConsumer stdErrConsumer;
    protected final OutputConsumer stdOutConsumer;
    protected String command;
    private boolean needsForwardPipe;
    private boolean needsForwardErrPipe;
    protected int exitCode = 127;
    protected CommandExecutor previous;
    private boolean needPipeBackward;
    private OutputStream outputStream;
    private CmdErrorHandler errorHandler = new CmdErrorHandler();
    protected boolean earlyStop;
    private InputStream inputStreamForStdErr;

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    protected String workingDirectory;

    public CommandExecutor(String command, boolean needsForwardPipe, boolean needsForwardErrPipe,
                           OutputConsumer stdOutConsumer, OutputConsumer stdErrConsumer) {
        this.command = command;
        this.needsForwardPipe = needsForwardPipe;
        this.needsForwardErrPipe = needsForwardErrPipe;
        this.stdOutConsumer = stdOutConsumer;
        this.stdErrConsumer = stdErrConsumer;
    }

    private boolean needPipeOutBackward;
    private boolean needPipeErrBackward;
    protected CountDownLatch countDownCloseOutput;

    protected void makePipes(CommandExecutor previous, OutputStream outputStream) {

        if (needPipeOutBackward) {
            final SyncPipe syncPipe = new SyncPipe(previous.getInputStreamForStdOut(), outputStream);
            syncPipe.setDebug(command);
            syncPipe.setCloseOutputCountDown(countDownCloseOutput);
            new Thread(syncPipe).start();
        }
        if (needPipeErrBackward) {
            final SyncPipe syncPipe = new SyncPipe(previous.getInputStreamForStdErr(), outputStream);
            syncPipe.setDebug(command);
            syncPipe.setCloseOutputCountDown(countDownCloseOutput);
            new Thread(syncPipe).start();
        }

    }

    protected void setupCountDown() {
        int countDownCount = 0;
               if( stdOutConsumer!=null) {
                   countDownCount++;
               }
               if( stdErrConsumer!=null) {
                           countDownCount++;
                       }
               if (needPipeOutBackward) {
                   countDownCount++;
               }
               if (needPipeErrBackward) {
                   countDownCount++;
               }
               countDownCloseOutput = new CountDownLatch(countDownCount);
    }

    /**
     * Execute a command, in a thread or sequentially.
     */
    public abstract void executeStep();

    /**
     * Prepare this step, setting up a pipe with the previous step if necessary.
     *
     * @param previousExecutor
     */
    public void prepare(CommandExecutor previousExecutor) {
        previous = previousExecutor;
        if (previousExecutor != null && previousExecutor.needPipeForward()) {
            needPipeBackward = true;
            needPipeOutBackward = true;
        }
        if (previousExecutor != null && previousExecutor.needPipeErrForward()) {

            needPipeErrBackward = true;
        }
    }


    /**
     * Return the exit code of the command.
     *
     * @return
     */
    public int exitCode() {
        return exitCode;
    }

    public boolean needPipeForward() {
        return needsForwardPipe;
    }

    protected boolean needPipeErrForward() {
        return needsForwardErrPipe;
    }

    /**
     * Get the output of the step as an inputStream.
     *
     * @return
     */
    public abstract InputStream getInputStreamForStdOut();

    public void setErrorHandler(CmdErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public CmdErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void forceEarlyStop() {
        earlyStop = true;
    }

    public boolean requestsEarlyStop() {
        return earlyStop;
    }

    public InputStream getInputStreamForStdErr() {
        return inputStreamForStdErr;
    }

    public void setInputStreamForStdErr(InputStream inputStreamForStdErr) {
        this.inputStreamForStdErr = inputStreamForStdErr;
    }
}
