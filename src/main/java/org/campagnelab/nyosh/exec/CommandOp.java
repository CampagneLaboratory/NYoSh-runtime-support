package org.campagnelab.nyosh.exec;

/**
 * Models a command operation.
 *
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 6:24 PM
 */
class CommandOp {
    private final OutputConsumer currentStdOutConsumer;
    private final OutputConsumer currentStdErrConsumer;
    private String command;
    private String operator;
    private boolean sequentialStep;
    private CommandExecutor executor;
    private boolean sequential;
    /**
     * The working directory where the command should be executed.
     */
    private String workingDirectory;

    CommandOp(String command, String operator, OutputConsumer currentStdOutConsumer, OutputConsumer currentStdErrConsumer) {
        this.command = command;
        this.operator = operator;
        this.currentStdOutConsumer = currentStdOutConsumer;
        this.currentStdErrConsumer = currentStdErrConsumer;
        if ("|".equals("operator") && currentStdOutConsumer != null) {
            throw new IllegalArgumentException("You cannot consume the output of a command that is also piped to the next command.");
        }
        if ("|&".equals("operator") && (currentStdOutConsumer != null || currentStdErrConsumer != null)) {
            throw new IllegalArgumentException("You cannot consume the output or error of a command that is also piped to the next command.");
        }
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
     * Returns true if the command needs a stdout pipe to the next step.
     */
    public boolean needsForwardPipe() {
        return "|".equals(operator) ||needForwardErrPipe();
    }

    /**
     * Returns true if the command needs a pipe of standard error to the next step.
     */
    public boolean needForwardErrPipe() {
        return "|&".equals(operator);
    }

    public String getCommand() {
        return command;
    }

    public String getOperator() {
        return operator;
    }

    public boolean operatorIsPipe() {
        return "|".equals(operator);
    }
    public boolean operatorIsSemicolon() {
            return ";".equals(operator);
        }
    public boolean operatorIsAndList() {
           return "||".equals(operator);
       }


    public OutputConsumer getStdOutConsumer() {
        return currentStdOutConsumer;
    }
    public OutputConsumer getStdErrConsumer() {
            return currentStdErrConsumer;
        }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }
}
