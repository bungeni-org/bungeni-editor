package org.bungeni.trackchanges.init;

import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import javax.swing.JFrame;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.trackchanges.trackChangesMain;

/**
 *
 * @author Ashok Hariharan
 */
public class TrackChangesInit {
    private static XComponentContext OOoContext = null;
    private static BungenioOoHelper  openOfficeObject = null;

    /** Creates a new instance of TrackChangesManager2 */
    public TrackChangesInit() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // get the remote office component context
            OOoContext = Bootstrap.bootstrap();
            if (OOoContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
            } else {
                openOfficeObject = new BungenioOoHelper(OOoContext);
                openOfficeObject.initoOo();
                JFrame frm = new JFrame("Track Changes Manager");
                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm.getContentPane().add(new trackChangesMain(frm));
                frm.pack();
                frm.setVisible(true);
            }
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
        finally {
            System.exit( 0 );
        }
    }

    /**
     * get UNO component context
     * @return
     */
    public static XComponentContext getOOoContext(){
        return TrackChangesInit.OOoContext;
    }

    /**
     * get Bungeni helper
     * @return
     */
    public static BungenioOoHelper getBungeniHelper(){
        return TrackChangesInit.openOfficeObject;
    }
}
