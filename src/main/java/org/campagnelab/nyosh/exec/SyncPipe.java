package org.campagnelab.nyosh.exec;

import java.io.*;


/**
 * Class copied and adapted from http://stackoverflow.com/questions/4157303/how-to-execute-cmd-commands-via-java
 *
 * @author Fabien Campagne
 *         Date: 1/10/13
 *         Time: 4:06 PM
 */

public class SyncPipe implements Runnable {
    private final boolean quiet;

    public SyncPipe(InputStream istrm, OutputStream ostrm) {
        this(false, istrm, ostrm);
    }

    public SyncPipe(boolean quiet, InputStream istrm, OutputStream ostrm) {
        this.quiet = quiet;
        istrm_ = new BufferedInputStream(istrm);
        ostrm_ = new BufferedOutputStream(ostrm);

    }

    public void run() {
        try {
            final byte[] buffer = new byte[1024];

            for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {
                if (!quiet) {
                    ostrm_.write(buffer, 0, length);
                }
            }
            if (!quiet) {
                ostrm_.flush();
               // Thread.sleep(1000);
            }
        } catch (java.io.IOException e) {
            System.err.println("e:" + e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
          // close the output so the next process knows processing needs to finish..
                  try {

                      ostrm_.close();
                  } catch (IOException e) {

                  }
        }
    }

    @Override
    protected void finalize() throws Throwable {

    }

    private final BufferedOutputStream ostrm_;
    private final BufferedInputStream istrm_;
}