package org.campagnelab.nyosh.logging;

import org.campagnelab.stepslogger.FileStepsLogger;

import java.io.File;
import java.io.IOException;

/**
 * Helper class to setup a steps logger and log script events.
 * @author Fabien Campagne
 *         Date: 9/2/13
 *         Time: 11:13 AM
 */

public class StepsLoggerHelper {

    public static void assertTrue(boolean mustBeTrue, String reason, int statusCode) {
        if (!(mustBeTrue)) {
            if (_steps != null) {
                _steps.error(reason);
            }
            try {
                if (_steps != null) {
                    _steps.close();
                    _steps = null;
                }
            } catch (IOException e) {
                //  we tried to close stepslogger. Giving up now.
            }
        }
    }

    private static FileStepsLogger _steps;

    public static void createLogFile() {
        if (_steps == null) {
            _steps = new FileStepsLogger(new File("./"));
        }
    }

    public static void assertTrue(boolean mustBeTrue, String reason) {
        assertTrue(mustBeTrue, reason, 1);
    }

    public static void done(String stepDescription, int statusCode) {
        _steps.step(stepDescription, statusCode);
    }


}