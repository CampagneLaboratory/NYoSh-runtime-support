package org.campagnelab.nyosh.exec;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:32 PM
 */
public abstract class CommandExecutor {
    protected String command;
    private boolean needsForwardPipe;
    private boolean needsForwardErrPipe;
    protected int exitCode = 127;
    protected CommandExecutor previous;
    private boolean needPipeBackward;
    private OutputStream outputStream;
    private CmdErrorHandler errorHandler=new CmdErrorHandler();
    protected boolean earlyStop;
    private InputStream inputStreamForStdErr;

    public CommandExecutor(String command, boolean needsForwardPipe, boolean needsForwardErrPipe) {
        this.command = command;
        this.needsForwardPipe = needsForwardPipe;
        this.needsForwardErrPipe = needsForwardErrPipe;
    }

    private boolean needPipeOutBackward;
    private boolean needPipeErrBackward;

    protected void makePipes(CommandExecutor previous, OutputStream outputStream) {
        if (needPipeOutBackward) {
            new Thread(new SyncPipe(previous.getInputStreamForStdOut(), outputStream)).start();
        }
        if (needPipeErrBackward) {
            new Thread(new SyncPipe(previous.getInputStreamForStdErr(), outputStream)).start();
        }
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
        if (previousExecutor!=null && previousExecutor.needPipeForward()) {
            needPipeBackward = true;
            needPipeOutBackward = true;
        }
        if (previousExecutor!=null && previousExecutor.needPipeErrForward()) {

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
