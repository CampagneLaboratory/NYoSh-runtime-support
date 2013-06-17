package org.campagnelab.nyosh.exec;

import java.io.*;
import java.util.concurrent.CountDownLatch;


/**
 * Class copied and adapted from http://stackoverflow.com/questions/4157303/how-to-execute-cmd-commands-via-java
 *
 * @author Fabien Campagne
 *         Date: 1/10/13
 *         Time: 4:06 PM
 */

public class SyncPipe implements Runnable {


    private CountDownLatch closeOutputCountDown;

    public SyncPipe(InputStream istrm, OutputStream ostrm) {

        istrm_ = new BufferedInputStream(istrm, 10000);
        ostrm_ = new BufferedOutputStream(ostrm, 10000);

    }

    private String debug;

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public void run() {
        try {
            final byte[] buffer = new byte[1024];
            while (istrm_.available() > 0) {
                for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {

                    ostrm_.write(buffer, 0, length);
                }
                ostrm_.flush();
            }

        } catch (java.io.IOException e) {
            //if (!e.getMessage().equals("Stream closed")) {
            System.err.println("e:" + e);
            //}
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // indicate that the output can be closed so the next process knows processing needs to finish..

            closeOutputCountDown.countDown();

            // close immediately if we reached 0:
            try {

                closeOutputCountDown.await();
                ostrm_.close();

            } catch (IOException e) {

            } catch (InterruptedException e) {


            }
        }
    }


    private final BufferedOutputStream ostrm_;
    private final BufferedInputStream istrm_;

    public void setCloseOutputCountDown(CountDownLatch closeOutputCountDown) {
        this.closeOutputCountDown = closeOutputCountDown;
    }


}