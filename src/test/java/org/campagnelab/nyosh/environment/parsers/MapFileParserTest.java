package org.campagnelab.nyosh.environment.parsers;

import org.campagnelab.nyosh.environment.NYoShRuntimeEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.List;
import java.util.Map;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Tester for {@link MapFileParser}
 * @author manuele
 */
@RunWith(JUnit4.class)
public class MapFileParserTest {

    @Test
    public void testParseAtRunTime() throws Exception {
        MapFileParser parser = new MapFileParser();
        parser.parseAtRunTime(new File("test-data", "auto-options.sh").getAbsolutePath());
        NYoShRuntimeEnvironment env = NYoShRuntimeEnvironment.getEnvironment();
        assertTrue(env.getVariableValue("PLUGINS_ALIGNER_SAMPLE_ALIGNER_WITH_MPS_FILES_SCRIPT").length()>0);
        assertTrue(env.getVariableValue("foo").length()==0);
    }

    @Test
    public void testParseAtDesignTime() throws Exception {
        MapFileParser parser = new MapFileParser();
        Map<String,String> keys= parser.parseAtDesignTime(new File("test-data", "auto-options.sh").getAbsolutePath());
        assertEquals(7, keys.size());
    }
}
