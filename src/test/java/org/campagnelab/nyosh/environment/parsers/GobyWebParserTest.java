package org.campagnelab.nyosh.environment.parsers;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.SortedSet;

/**
 * Tester for {@link GobyWebParser}
 * @author manuele
 */
@RunWith(JUnit4.class)
public class GobyWebParserTest {

    @Test
    public void testParseAtRunTime() throws Exception {

    }

    @Test
    public void testParseAtDesignTime() throws Exception {
       GobyWebParser parser = new GobyWebParser();
       SortedSet<ScriptVariable> variables = parser.parseAtDesignTime("TRINITY", "resources",new File("test-data/plugins-root").getAbsolutePath());
       Assert.assertNotNull(variables);

    }
}
