package org.campagnelab.nyosh.exec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Consume the output and put the first line as a String value into a variable.
 * @author Fabien Campagne
 *         Date: 7/29/13
 *         Time: 5:51 PM
 */
public class OutputConsumerToString implements OutputConsumer {
    String value=null;
    public void consume(InputStream stream) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

      try {
       value = reader.readLine();
      } catch (Exception e) {
        // ignore all exceptions
      }
    }

    /**
     * Get the first line of the output.
     * @return
     */
    public String getValue() {
        return value;
    }
}
