/*
 * BungeniEditorClient.java
 *
 * Created on 2007.04.30 - 10:30:04
 *
 */

package org.bungeni.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import org.bungeni.editor.dialogs.editorApplicationController;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import org.bungeni.editor.ui.LookAndFeelFactory;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class BungeniEditorClient {
 //   private static XComponentContext m_xContext;
    private static String __WINDOW_TITLE__="BungeniEditor Launcher";
    private static JFrame frame;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniEditorClient.class.getName());

    /** Creates a new instance of BungeniEditorClient */
    public BungeniEditorClient() {
    }
    
    /**
     * @param args the command line arguments
     */
    private static void createAndShowGUI() {
        //Use the Java look and feel.
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);
      initUI();
        //Instantiate the controlling class.
      frame = new JFrame(__WINDOW_TITLE__);
      frame.setResizable(false);
       //force prompting of exit message 
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      WindowListener panelListener = new WindowAdapter() {
           @Override
                public void windowClosing(WindowEvent e) {
                    int confirm = JOptionPane.showOptionDialog(frame, "Really Exit? This will close all Editor panels", "Exit Confirmation",
                                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                            null, null, null);
                    if (confirm == 0 ) {
                        frame.dispose();
                        System.exit(0);
                    }
                }
        };
        frame.addWindowListener(panelListener);
        preLaunch();
        editorApplicationController panel = new editorApplicationController();
        panel.init();
        frame.add(panel);
        frame.setSize(615,400);
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setVisible(true);
    }

    /**
     * Function to initialize UI
     */
    private  static void initUI(){
        try {
            //invoke look and feel from UI manager
            //set the class loader to be used by the UI manager, so that the UI manager
            //uses the appropriate thread context class loader
            UIManager.put("ClassLoader", BungeniEditorClient.class.getClassLoader());
            //access the deafault look and feel via the interface
            //instantiate the look and feel
            //LookAndFeel laf = new CafeCremeLAF();
            ILookAndFeel iFeel = LookAndFeelFactory.getDefaultLookAndFeel();
            LookAndFeel lafInstance = iFeel.newLAFInstance();
            if (lafInstance == null) log.error("lafInstance is null");
            UIManager.setLookAndFeel(lafInstance);
            //override theme colors if required
          //  BungeniUIManager bungeniUI = new BungeniUIManager();
          //  bungeniUI.loadBungeniUI();
        } catch (Exception ex) {
            log.error("initUI : " + ex.getMessage());
            log.error("InitUI : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }


    private static void preLaunch(){
       //do prelaunch stuff here... 
       //1 create the log file if it doesnt exist...
        String curDir = System.getProperty("user.dir");
        curDir = curDir + File.separator+ "logs" + File.separator + "log.txt";
        File f = new File (curDir);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                System.out.println("Log file could not be created");
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            // get the remote office component context
           // m_xContext = Bootstrap.bootstrap();
            javax.swing.SwingUtilities.invokeLater(
                        new Runnable() {
                                public void run() {
                                    createAndShowGUI();
                                }
                        }
            );
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
       // System.exit( 0 );
    }
    
}
