package org.campagnelab.nyosh.exec;

import java.util.ArrayList;
import java.util.List;

/**
 * Assemble a command execution plan with BASH semantics.
 * Given a sequence of commands to execute and BASH operators,
 * produce an ExecutableCommand instance ready to execute.
 * Supported operators include:
 * <ul>
 * <li>wait-then-continue (BASH: ;) This operator executes preceeding commands,
 * wait until they complete, then execute the next command.
 * <li>pipe (BASH: |). This operator runs commands in parallel, piping the output of the command before
 * into the input of the command following.
 * <li>wait-success-continue (BASH: &amp;&amp;). An &amp;&amp; list has the form command1 && command2.
 * The &amp;&amp; operator checks if the previous command status was zero, continues if so, or fails.
 * When used to separate multiple commands, any failure will fail the entire execution.
 * <li>wait-fail-continue (BASH: ||).  An OR list has the form command1 || command2. command2 is executed
 * if, and only if, command1 returns a non-zero exit status.
 *
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 9:21 AM
 */
public class CommandAssembler {

    private List<CommandOp> commandList = new ArrayList<CommandOp>();
    private String currentCommand = null;
    private String currentOp = null;

    public void appendCommand(String cmd) {
        assert currentCommand == null : "Two commands cannot follow one another without a separating operator.";
        push();
        currentCommand = cmd;
        currentOp = null;
    }

    private void push() {
        if (currentCommand != null && currentOp != null) {
            commandList.add(new CommandOp(currentCommand, currentOp));
            currentCommand = null;
            currentOp = null;
        }
    }

    public void appendOperator(String operator) {
        assert currentOp == null : "Two commands cannot follow one another without a separating operator.";
        currentOp = operator;
        push();
    }

    public CommandExecutionPlan getCommandExecutionPlan() {
        finishAssembly();
        List<CommandExecutor> executors = new ArrayList<CommandExecutor>();
        CommandExecutionPlan result = new CommandExecutionPlan();
        for (CommandOp op : commandList) {

            CommandExecutor executor = op.isSequential() ? new SequentialStepExecutor(op.command, op.needsForwardPipe(),
                    op.needForwardErrPipe()) :
                    new ThreadExecutor(op.command,
                            op.needsForwardPipe(),
                            op.needForwardErrPipe());

            if ("||".equals(op.operator)) {
                // the || operator consider exitCode!=0 as success.
                executor.setErrorHandler(new CmdErrorHandler() {
                    @Override
                    public boolean success(int exitCode) {
                        return exitCode != 0;
                    }
                });

            }
            if (";".equals(op.operator)) {
                // the ; operator does not check exit code between commands.
                executor.setErrorHandler(new CmdErrorHandler() {
                    @Override
                    public boolean success(int exitCode) {
                        return true;
                    }
                });

            }
            executors.add(executor);
        }
        result.setCommandExecutors(executors);
        return result;
    }

    public void finishAssembly() {
        if (currentOp == null) currentOp = ";";
        push();
    }
}
