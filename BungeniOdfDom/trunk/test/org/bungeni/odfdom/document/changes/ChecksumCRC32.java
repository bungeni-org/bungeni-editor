
package org.bungeni.odfdom.document.changes;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class ChecksumCRC32 {

    public static long doChecksum(InputStream iStream) {
        long checksum = 0;
        try {

            CheckedInputStream cis = null;
            long fileSize = 0;

                // Computer CRC32 checksum
                cis = new CheckedInputStream(
                        iStream, new CRC32());


            byte[] buf = new byte[128];
            while(cis.read(buf) >= 0) {
            }

            checksum = cis.getChecksum().getValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return checksum;

    }



}

