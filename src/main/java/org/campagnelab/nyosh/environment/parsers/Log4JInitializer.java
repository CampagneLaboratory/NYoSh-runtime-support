package org.campagnelab.nyosh.environment.parsers;

import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Properties;

/**
 * Initializer for Log4J framework.
 *
 * @author manuele
 */
public class Log4JInitializer {

    public static void init() {
        String logFolder;
        if (System.getenv().containsKey("JOB_DIR"))
           logFolder = System.getenv("JOB_DIR");
        else
            logFolder = System.getProperty("user.home");
        Properties prop = new Properties();
        try {
            prop.load(Log4JInitializer.class.getClass().getResourceAsStream("/nyosh-log4j.properties"));
            prop.setProperty("log4j.appender.NYOSHFILE.File", logFolder + "/nyosh-runtime.log");
            PropertyConfigurator.configure(prop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
