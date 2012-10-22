
package org.bungeni.extutils;

import java.util.UUID;
import org.apache.commons.codec.binary.Base64;



/**
 *
 * @author Administrator
 */
public class BungeniUUID {
    
    /** Creates a new instance of BungeniUUID */
    public BungeniUUID() {
    }
    
    public static String getStringUUID() {
            UUID uuid = UUID.randomUUID();
            return uuid.toString();
    }

    /**
     * Adapted from
     * http://stackoverflow.com/questions/772802/storing-uuid-as-base64-string
     * @return
     */
    public static String getShortUUID(){
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = toByteArray(uuid);
        //convert UUID to base64 encoded string
        Base64 encoder = new Base64();
        String sbyteArrAsB64 = new String(encoder.encode(uuidBytes));
        //strip the ==
        sbyteArrAsB64 = sbyteArrAsB64.split("=")[0];
        return sbyteArrAsB64;
    }

     private static byte[] toByteArray(UUID uuid) {

        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
                buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
                buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }

        return buffer;
    }

}
