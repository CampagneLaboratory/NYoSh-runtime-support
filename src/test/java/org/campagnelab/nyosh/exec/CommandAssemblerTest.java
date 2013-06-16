package org.campagnelab.nyosh.exec;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Fabien Campagne
 *         Date: 6/15/13
 *         Time: 7:37 PM
 */
public class CommandAssemblerTest {
    @Test
    public void testAppendCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("ls -ltr");
        assembler.appendOperator(";");
        assembler.appendCommand("echo end");
        assembler.finishAssembly();
        assertEquals(0, assembler.getCommandExecutionPlan().run());
    }

    @Test
    public void testPipeCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("ls -ltr");
        assembler.appendOperator("|");
        assembler.appendCommand("grep src");
        assertEquals(0, assembler.getCommandExecutionPlan().run());
    }

    @Test
    public void testPipeCommand2() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("ls -ltr");
        assembler.appendOperator("|");
        assembler.appendCommand("grep 23434"); // no file exists in the directory with this name, grep returns 1:
        assertEquals(1, assembler.getCommandExecutionPlan().run());
    }

    @Test
    public void testPipeStdOutCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-echo-error.sh");
        assembler.appendOperator("|");
        assembler.appendCommand("grep error");
        // the error word is not found on stdout, grep exits with 1:
        assertEquals(1, assembler.getCommandExecutionPlan().run());
    }


    @Test
    public void testPipeStdErrCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-echo-error.sh");
        assembler.appendOperator("|&");
        assembler.appendCommand("grep error");
        // the error word is found on stderr, grep exits with 0:
        assertEquals(0, assembler.getCommandExecutionPlan().run());
    }

    @Test
    public void testAndListCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-success.sh");
        assembler.appendOperator("&&");
        assembler.appendCommand("test-data/script-success.sh");
        assertEquals(0, assembler.getCommandExecutionPlan().run());
    }

    @Test
    public void testAndListCommand2() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-fail.sh");
        assembler.appendOperator("&&");
        assembler.appendCommand("test-data/script-success.sh");
        assertEquals(1, assembler.getCommandExecutionPlan().run());
    }


    @Test
    public void testSemiListCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-fail.sh");
        assembler.appendOperator(";");
        assembler.appendCommand("test-data/script-success.sh");
        assertEquals(0, assembler.getCommandExecutionPlan().run());
    }

    @Test
    public void testAndListCommand3() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-fail.sh");
        assembler.appendOperator("||");   // || requires non zero status to continue
        assembler.appendCommand("test-data/script-success.sh");
        final CommandExecutionPlan executionPlan = assembler.getCommandExecutionPlan();
        assertEquals(0, executionPlan.run());
        assertTrue(executionPlan.executedCompletely());
    }

    @Test
    public void testAndListCommand4() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-success.sh");
        assembler.appendOperator("||");   // || requires non zero status to continue
        assembler.appendCommand("test-data/script-success.sh");
        final CommandExecutionPlan executionPlan = assembler.getCommandExecutionPlan();
        assertEquals(0, executionPlan.run());
        assertFalse(executionPlan.executedCompletely());

    }

    @Test
    public void testAndListCommand5() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-success.sh");
        assembler.appendOperator("&&");
        assembler.appendCommand("test-data/script-success.sh");
        assembler.appendOperator("&&");
        assembler.appendCommand("test-data/script-success.sh");
        assembler.appendOperator(";");
        assembler.appendCommand("test-data/script-success.sh");
        assembler.appendOperator("&&");
        assembler.appendCommand("test-data/script-success.sh");
        final CommandExecutionPlan executionPlan = assembler.getCommandExecutionPlan();
        assertEquals(0, executionPlan.run());
        assertTrue(executionPlan.executedCompletely());

    }

    @Test
    public void testConsumeOutput() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("ls -ltr");
        assembler.appendOperator(";");
        assembler.appendCommand("echo end");
        final StringBuffer output = new StringBuffer();
        assembler.consumeStandardOutput(new OutputConsumer() {

            public void consume(InputStream inputStream) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                try {
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        assembler.finishAssembly();
        assertEquals(0, assembler.getCommandExecutionPlan().run());
        assertEquals("end", output.toString());
    }
}
