/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;

/**
 *
 * @author Ashok
 */
public class LookAndFeelFactory {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LookAndFeelFactory.class.getName());


    public static ILookAndFeel getDefaultLookAndFeel() {
            ILookAndFeel lafObject = null;
            String strUserDirPath = System.getProperty("user.dir");
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
                //BungeniEditorClient.class.getClassLoader().
                Class lafClass = Class.forName(DEFAULT_LAF_CLASS);
                //Class lafClass = Class.forName(DEFAULT_LAF_CLASS);
                //String s = System.getProperty( "java.class.path" );
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
                  return lafObject;
              }
        }
}
