package org.bungeni.editor;

//~--- non-JDK imports --------------------------------------------------------
import org.apache.log4j.BasicConfigurator;

import org.bungeni.editor.dialogs.editorApplicationController;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import org.bungeni.editor.ui.LookAndFeelFactory;
import org.bungeni.extutils.CommonTreeFunctions;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.Installation;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Locale;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import org.bungeni.editor.noa.BungeniNoaApp;
import org.bungeni.extutils.BungeniRuntimeProperties;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniEditorClient {

    // private static XComponentContext m_xContext;
    private static String __WINDOW_TITLE__ = "BungeniEditor Launcher";
    public static BungeniEditorClientCmdOptions cmdOptions = null;
    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(BungeniEditorClient.class.getName());
    private static JFrame frame = null;
    private static editorApplicationController panel;
 
    /** Creates a new instance of BungeniEditorClient */
    public BungeniEditorClient() {
    }

    private static void initOOo(){
        //just calling getINstance() on BungeniNoaApp to initialize the OpenOffice libraries.
        BungeniNoaApp.getInstance();
    }

    /**
     * Set the default language and locale for the Editor
     * Command line options for locale are mandatory -- however this can be overriden by
     * specifying alternate default locale in editor.ini
     */
    private static void setIniProperties() throws FileNotFoundException, IOException {

        // check if the default language has been specified in a ini file
        String lang = cmdOptions.getLang();
        String region = cmdOptions.getRegion();

        File dir = Installation.getInstallDirectory(BungeniEditorClient.class);

        log.info("setLangAndLocale : current directory =  " + dir.getAbsolutePath());

        BungeniRuntimeProperties.setProperty("EDITOR_ROOT_FOLDER", dir.getAbsolutePath());
        BungeniRuntimeProperties.setProperty("EDITOR_INI", dir.getAbsolutePath() + File.separator + "editor.ini");

        File fini = new File(BungeniRuntimeProperties.getProperty("EDITOR_INI"));

        if (fini.exists()) {
            log.info("Found editor.ini - attempting to set locale from ini");
            //setting locale
            // if the ini file exists use the defaults in the ini file
            Properties pini = new Properties();
            pini.load(new FileInputStream(fini));
            if (pini.containsKey("lang") && pini.containsKey("region")) {

                // if the keys exist in editor.ini use them for setting lang and region
                lang = pini.getProperty("lang");
                region = pini.getProperty("region");
            } else {
                log.info("editor.ini has missing key for lang or region - ignoring ini setting - using commandline");
            }
            //getting metadata property
            //Refering to OOComponentHelper here yeilds a class not found exception here
            //because UNO isnt fully initialized yet
            if (pini.containsKey("metadata_format")) {
                String metaFormat = pini.getProperty("metadata_format");
                if (metaFormat.trim().equals("rdf")) {
                    BungeniRuntimeProperties.setProperty("METADATA_FORMAT", "RDF");
                } else {
                    BungeniRuntimeProperties.setProperty("METADATA_FORMAT", "OLD_STYLE");
                }
            } else {
                log.info("editor.ini metadata_format not set using rdf metadata");
            }

            Locale locale = new Locale(lang, region);
            Locale.setDefault(locale);
        }

    }

    /**
     * @param args the command line arguments
     */
    private static void createAndShowGUI() {

        // Use the Java look and feel.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        initUI();

        // Instantiate the controlling class.
        if (frame == null)
            frame = new JFrame(__WINDOW_TITLE__);

        ImageIcon iconApp = CommonTreeFunctions.loadIcon("bungeni-icon.png");

        frame.setIconImage(iconApp.getImage());
        frame.setResizable(false);

        // force prompting of exit message
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        WindowListener panelListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(frame, "Really Exit? This will close all Editor panels",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        null, null);

                if (confirm == 0) {
                    panel.cleanup();
                    frame.dispose();
                    System.exit(0);
                }
            }
        };

        frame.addWindowListener(panelListener);
        preLaunch();
        panel = new editorApplicationController(frame);
        panel.init();
        frame.add(panel);
        frame.setSize(615, 400);

        // Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);    // center it

        // if launch doctype = gui launch it as before
        if (cmdOptions.getDocType().equals("gui")) {
            frame.setVisible(true);
        } else {

            // otherwise hide the launch frame and process the doc type specific arguments
            frame.setVisible(false);

            // do the other launch processing here
            panel.launchDocumentType(cmdOptions.getDocType(), cmdOptions.getLaunchMode());
        }
    }

    /**
     * Function to initialize UI
     */
    private static void initUI() {
        try {

            // invoke look and feel from UI manager
            // set the class loader to be used by the UI manager, so that the UI manager
            // uses the appropriate thread context class loader
            UIManager.put("ClassLoader", BungeniEditorClient.class.getClassLoader());

            // access the deafault look and feel via the interface
            // instantiate the look and feel
            // LookAndFeel laf = new CafeCremeLAF();
            ILookAndFeel iFeel = LookAndFeelFactory.getDefaultLookAndFeel();
            LookAndFeel lafInstance = iFeel.newLAFInstance();

            if (lafInstance == null) {
                log.error("lafInstance is null");
            }

            UIManager.setLookAndFeel(lafInstance);

            // override theme colors if required
            // BungeniUIManager bungeniUI = new BungeniUIManager();
            // bungeniUI.loadBungeniUI();
        } catch (Exception ex) {
            log.error("initUI : " + ex.getMessage());
            log.error("InitUI : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }

    private static void preLaunch() {

        // do prelaunch stuff here...
        // 1 create the log file if it doesnt exist...
        String curDir = System.getProperty("user.dir");

        curDir = curDir + File.separator + "logs" + File.separator + "log.txt";

        File f = new File(curDir);

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

            // parse the command line options
            cmdOptions = new BungeniEditorClientCmdOptions();
            BasicConfigurator.configure();
            cmdOptions.doMain(args);

            // launch the editor
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        // set the default language and locale
                        setIniProperties();
                        initOOo();
                    } catch (FileNotFoundException ex) {
                        log.error("editor.ini not found", ex);
                    } catch (IOException ex) {
                        log.error("editor.ini not found", ex);
                    }
                    // show the UI
                    createAndShowGUI();
                }
            });
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

}
