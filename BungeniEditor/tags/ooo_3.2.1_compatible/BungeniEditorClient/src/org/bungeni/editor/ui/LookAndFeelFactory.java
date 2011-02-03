package org.bungeni.editor.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;

/**
 * Look and Feel Factory returns active look and feel from the settings file
 * @author Ashok Hariharan
 */
public class LookAndFeelFactory {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LookAndFeelFactory.class.getName());

    /**
     * Returns the active look and feel
     * @return
     */
    public static ILookAndFeel getDefaultLookAndFeel() {
            ILookAndFeel lafObject = null;
            String strUserDirPath = System.getProperty("user.dir");
            log.info("getDefaultLookAndFeel (user.dir) : " + strUserDirPath );
            String themePropsFile = strUserDirPath + File.separator + "settings" + File.separator + "bungenitheme.properties";
            File fPropsFile = new File(themePropsFile);
            String DEFAULT_LAF_CLASS = "org.bungeni.editor.themes.CafeCremeLAF";
            if (fPropsFile.exists()) {
                    FileInputStream fPropsStream = null ;
                    try {
                        fPropsStream = new FileInputStream(fPropsFile);
                        Properties themeProps = new Properties();
                        themeProps.load(fPropsStream);
                        String foundDefault = themeProps.getProperty("DefaultTheme");
                        if (foundDefault != null) {
                            DEFAULT_LAF_CLASS = foundDefault;
                        }
                    } catch (FileNotFoundException ex) {
                            log.error("getDefaultLookAndFeel : " + ex.getMessage());
                         } catch (IOException ex) {
                            log.error("getDefaultLookAndFeel : " + ex.getMessage());
                         }
            }
            try {
                Class lafClass = Class.forName(DEFAULT_LAF_CLASS);
                lafObject = (ILookAndFeel) lafClass.newInstance();
             } catch (InstantiationException ex) {
               log.debug("getDefaultLookAndFeel :"+ ex.getMessage());
               } catch (IllegalAccessException ex) {
               log.debug("getDefaultLookAndFeel :"+ ex.getMessage());
               }  catch (ClassNotFoundException ex) {
               log.debug("getDefaultLookAndFeel :"+ ex.getMessage());
              } catch (NullPointerException ex) {
               log.debug("getDefaultLookAndFeel :"+ ex.getMessage());
              } finally {
                  log.error("getDefaultLookAndFeel : returning instance :" + (lafObject == null) );
                  return lafObject;
              }
        }
}
