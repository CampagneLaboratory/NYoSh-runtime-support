package org.campagnelab.nyosh.exec;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Redirect the output of a command to a file.
 * @author Fabien Campagne
 *         Date: 7/30/13
 *         Time: 1:16 PM
 */
public class RedirectToFile extends OutputConsumer {
    private File file;

    public RedirectToFile(File file) {
        this.file = file;
    }

    public void consume(InputStream inputStream) {
        FileOutputStream output = null;

        try {
            output = new FileOutputStream(file);
            IOUtils.copy(inputStream, output);
        } catch (Exception e) {
            handleException(e);
        } finally {
            try {
                if (output!=null) {
                    output.close();
                }
            } catch (IOException e) {
                handleException(e);
            }
        }
    }

}
