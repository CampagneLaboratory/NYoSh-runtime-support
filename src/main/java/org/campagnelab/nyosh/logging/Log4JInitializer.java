package org.campagnelab.nyosh.logging;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

/**
 * Initializer for Log4J framework.
 *
 * @author manuele
 */
public class Log4JInitializer {

    public static void init() {
        try {
            //apparently MPS java stubs does not import properties file included in a JAR, so we set here the properties.
            Properties prop = new Properties();
            prop.setProperty("log4j.rootLogger","trace, FILE");
            prop.setProperty("log4j.appender.FILE","org.apache.log4j.FileAppender");
            prop.setProperty("log4j.appender.FILE.Append","true");
            prop.setProperty("log4j.appender.FILE.layout","org.apache.log4j.PatternLayout");
            prop.setProperty("log4j.appender.FILE.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss} %5r %p [%-7t] [%-15c{1}] %-34C{2} - %m%n");
            String logFolder;
            if (System.getenv().containsKey("JOB_DIR"))
                logFolder = System.getenv("JOB_DIR");
            else
                logFolder = System.getProperty("user.home");
            prop.setProperty("log4j.appender.FILE.File", logFolder + "/nyosh-runtime-output.log");
            PropertyConfigurator.configure(prop);
        } catch (Exception e) {
            //can't open the logger. the only way to trace the error is on a file.
            try {
                File file = new File(System.getProperty("user.home") + "/nyosh-ex.log");
                if (!file.exists())
                    file.createNewFile();
                PrintStream p = new PrintStream(file);
                e.printStackTrace(p);
                p.close();
            }catch (Exception e2) {e2.printStackTrace();}
        }
    }
}
