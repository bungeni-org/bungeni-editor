package org.bungeni.trackchanges.init;

import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.trackchanges.trackChangesMain;
import org.bungeni.trackchanges.utils.RuntimeProperties;

/**
 *
 * @author Ashok Hariharan
 */
public class TrackChangesInit {

      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TrackChangesInit.class.getName());



    private static XComponentContext OOoContext = null;
    private static BungenioOoHelper  openOfficeObject = null;

    /** Creates a new instance of TrackChangesManager2 */
    public TrackChangesInit() {
    }


       private static ILookAndFeel getLAF() {
        ILookAndFeel lafObject = null;

        try {
            String lafClassName = RuntimeProperties.getDefaultProp("DefaultTheme");
            Class  lafClass     = Class.forName(lafClassName); //"org.bungeni.editor.themes.SaharaLAF");

            lafObject = (ILookAndFeel) lafClass.newInstance();
        } catch (InstantiationException ex) {
            log.error("getLAF : " + ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            log.error("getLAF : " + ex.getMessage(), ex);
        } catch (ClassNotFoundException ex) {
            log.error("getLAF : " + ex.getMessage(), ex);
        }

        return lafObject;
    }

    private static  void initUI() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            UIManager.put("ClassLoader", TrackChangesInit.class.getClassLoader());
            ILookAndFeel iFeel = getLAF();
            LookAndFeel laf = iFeel.newLAFInstance();
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException ex) {
            log.error("initUI : " + ex.getMessage(), ex);
        }
    }

     public static void createAndShowGUI() {
                if (trackChangesMain.parentFrame == null) {
                    trackChangesMain.parentFrame = new JFrame("Track Changes Manager");
                    trackChangesMain.parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    trackChangesMain.parentFrame.getContentPane().add(new trackChangesMain());
                    trackChangesMain.parentFrame.pack();
                    trackChangesMain.parentFrame.setVisible(true);
                }
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
                //create openoffice context
                openOfficeObject = new BungenioOoHelper(OOoContext);
                openOfficeObject.initoOo();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        initUI();
                        createAndShowGUI();
                    }
                });
                //trackChangesMain.createAndShowGUI();
            }
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
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
