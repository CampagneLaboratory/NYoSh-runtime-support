package org.campagnelab.nyosh.exec;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.List;

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


    // @Test
    public void testPipeStdErrCommand() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("test-data/script-echo-error.sh");
        assembler.appendOperator("|&");
        assembler.appendCommand("grep e");
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
    public void testChangeDirectory() throws Exception {
        String workingDir = System.getProperty("user.dir");
        CommandAssembler assembler = new CommandAssembler();
        assembler.changeDirectory("test-data");
        assembler.appendCommand("./script-success.sh");
        assembler.appendOperator("&&");
        assembler.appendCommand("./script-success.sh");
        assembler.appendOperator("&&");
        assembler.changeDirectory(workingDir);
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

                }
            }
        });
        assembler.finishAssembly();
        assertEquals(0, assembler.getCommandExecutionPlan().run());
        assertEquals("end", output.toString());
    }

    @Test
    public void testConsumeOutput2() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("ls /bin");
        assembler.appendOperator("|&");
        assembler.appendCommand("grep vcf ");
        assembler.consumeStandardOutput(new OutputConsumer() {
            public void consume(InputStream stream) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // replace this
                        System.out.println("line: " + line);
                    }
                } catch (Exception e) {
                    //  ignore all exceptions
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {

                    }
                }
            }
        });
        assembler.finishAssembly();
        assembler.getCommandExecutionPlan().run();
    }


    @Test
    public void testBashFragment() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendBashFragment("a=\"1\"; eval echo '${a}'\n");
        OutputConsumerToString var = new OutputConsumerToString();
        assembler.consumeStandardOutput(var);
        assembler.finishAssembly();
        CommandExecutionPlan commandExecutionPlan = assembler.getCommandExecutionPlan();
        commandExecutionPlan.run();
        assertTrue(commandExecutionPlan.executedCompletely());
        assertEquals("1",var.getValue());

    }

    @Test
    public void testRedirectToFile() throws Exception {
        CommandAssembler assembler = new CommandAssembler();
        assembler.appendCommand("ls -ltr");
        assembler.appendOperator(";");
        assembler.appendCommand("echo end");
        final StringBuffer output = new StringBuffer();
        String filename = "rest-results/redirect-to-file/out-1.txt";
        assembler.consumeStandardOutput(new RedirectToFile(new File(filename)));
        assembler.finishAssembly();
        assertEquals(0, assembler.getCommandExecutionPlan().run());
        List<String> lines = FileUtils.readLines(new File(filename));
        assertEquals("end", lines.get(lines.size() - 1));
    }

    @BeforeClass
    public static void before() {
        new File("rest-results/redirect-to-file").mkdirs();
    }

    @AfterClass
    public static void after() {
        new File("rest-results/redirect-to-file").delete();

    }
}
