package org.bungeni.trackchanges.init;

import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;

/**
 *
 * @author Ashok Hariharan
 */
public class TrackChangesInit {
    
    /** Creates a new instance of TrackChangesManager2 */
    public TrackChangesInit() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // get the remote office component context
            XComponentContext xContext = Bootstrap.bootstrap();
            if (xContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
            } else
                System.out.println("SUCCESS: xContext was initialized");
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
        finally {
            System.exit( 0 );
        }
    }
    
}
