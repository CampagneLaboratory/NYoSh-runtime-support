package org.campagnelab.nyosh.exec;

import java.util.List;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:22 PM
 */
public class CommandExecutionPlan {
    private List<CommandExecutor> executors;
    private int globalExitCode;
    private boolean executedCompletely;

    /**
     * Run the execution plan. Return the execution status of the plan.
     */
    public int run() {
        CommandExecutor previousExecutor = null;
        // connect the pipes as needed:
        for (CommandExecutor executor : executors) {
            executor.prepare(previousExecutor);
            previousExecutor = executor;
        }
        //run the commands:
        CommandExecutor lastExecutor = null;
        int execCount=0;
        for (CommandExecutor executor : executors) {
            lastExecutor = executor;
            executor.executeStep();
            globalExitCode = executor.exitCode();
            if (executor.requestsEarlyStop()) {

                break;
            }
            execCount++;
        }
        executedCompletely=execCount==executors.size();
        return globalExitCode;
    }

    public void setCommandExecutors(List<CommandExecutor> executors) {
        this.executors = executors;
    }


    public boolean executedCompletely() {
        return executedCompletely;
    }
}
