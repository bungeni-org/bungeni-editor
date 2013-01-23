
package org.bungeni.translators.utility.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;
import javax.xml.transform.stream.StreamSource;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.xml.sax.InputSource;

/**
 * Safely closes file handles
 * @author Ashok Hariharan
 */
public class CloseHandle {
   
    private static org.apache.log4j.Logger log              =
        org.apache.log4j.Logger.getLogger(CloseHandle.class.getName());

    
    public static void closeQuietly(InputStream is){
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
            log.error("closeQuietly : error while closing stream", ex);
        }
    }
    
    public static void closeQuietly(Writer w){
        try {
            if (w != null) {
                w.close();
            }
        } catch (IOException ex) {
            log.error("closeQuietly : error while closing writer", ex);
        }
    }

    public static void closeQuietly(Reader r){
        try {
            if (r != null) {
                r.close();
            }
        } catch (IOException ex) {
            log.error("closeQuietly : error while closing writer", ex);
        }
    }

    
    public static void closeQuietly(StreamSource is){
        try {
            if (is != null) {
                InputStream isource = is.getInputStream();
                if (isource != null ) {
                    isource.close();
                }
            }
        } catch (IOException ex) {
            log.error("closeQuietly : error while closing stream", ex);
        }
    }
    
    public static void closeQuietly(InputSource is){
        try {
            if (is != null) {
                InputStream isource = is.getByteStream();
                if (isource != null ) {
                    isource.close();
                }
            }
        } catch (IOException ex) {
            log.error("closeQuietly : error while closing inputsource", ex);
        }
    }
    
    public static void closeQuietly(Channel channel){
        try {
            if (channel != null) {
               channel.close();
            }
        } catch (IOException ex) {
            log.error("closeQuietly : error while closing channel", ex);
        }
    }
    
    
    public static void closeQuietly(OdfDocument doc) {
        try {
            if (doc != null) {
               doc.close();
            }
        } catch (Exception ex) {
            log.error("closeQuietly : error while closing odf document", ex);
        }
        
    }
    

}
