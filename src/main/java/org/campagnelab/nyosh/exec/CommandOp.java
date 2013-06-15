package org.campagnelab.nyosh.exec;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:24 PM
 */
class CommandOp {
    String command;
    String operator;
    boolean sequentialStep;
    CommandExecutor executor;
    private boolean sequential;

    CommandOp(String command, String operator) {
        this.command = command;
        this.operator = operator;
        if (";".equals(operator) || "&&".equals(operator) || "||".equals(operator)) {
            sequentialStep = true;
        }
        if ("|".equals(operator)) {
            sequentialStep = false;
        }
    }


    public boolean isSequential() {
        return sequentialStep;
    }

    /**
     * Returns true if the command needs a pipe to the next step.
     */
    public boolean needsForwardPipe() {
        return "|".equals(operator);
    }

    /**
     * Returns true if the command needs a pipe of standard error to the next step.
     */
    public boolean needForwardErrPipe() {
        return "|&".equals(operator);
    }
}
