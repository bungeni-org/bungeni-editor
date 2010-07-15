package org.bungeni.editor.ui;

import java.awt.Color;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

/**
 * Abstracts acces to the Swing UI manager - loads default colors from a properties file.
 * This sets system wide colors - to change colors of individual panels, call CommonUIFunctions.setBackground()
 * in the specific panel class.
 * @author Ashok Hariharan
 */
public class BungeniUIManager {

    String pathToUIPropertiesFile = "";
    File fPropsFile = null;
    boolean uiPropertyFileExists = false;
    Properties uiProps = new Properties();

    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniUIManager.class.getName());

    /**
     * Constructor looks for the UI properties file, if it is available opens it
     */
    public BungeniUIManager() {
        //get handle to ui properties file if it exists.
        String strUserDirPath = System.getProperty("user.dir");
        this.pathToUIPropertiesFile = strUserDirPath + File.separator + "settings" + File.separator + "bungeniui.properties";
        this.fPropsFile = new File(pathToUIPropertiesFile);
        if (fPropsFile.exists()) {
            this.uiPropertyFileExists = true;
            }
        }

    /**
     * Load the Bungeni UI
     * @return
     */
    public boolean loadBungeniUI(){
        boolean bState = false;
        if (this.uiPropertyFileExists) {
                try {
                    //load properties file
                    FileInputStream fPropsStream = new FileInputStream(this.fPropsFile);
                    uiProps.load(fPropsStream);
                } catch (IOException ex) {
                    log.error("loadBungeniUI : " + ex.getMessage());
                }
                //load properties
                Enumeration emKeys = uiProps.keys();
                while (emKeys.hasMoreElements()) {
                    try {
                        String emProp = (String) emKeys.nextElement();
                        //convert value to color
                        String emValue = uiProps.getProperty(emProp);
                        Object convertedValue = parsePropertyValue(emValue);
                        if (convertedValue != null)
                            UIManager.put(emProp, convertedValue);
                    } catch (Exception ex) {
                        log.error("loadBungeniUI: " + ex.getMessage());
                    }
                }
                bState = true;
        }
        return bState;
    }

    /**
     * Parses property values into different object types
     * @param emValue
     * @return
     */
    private Object parsePropertyValue(String emValue) {
        String[] emProps = emValue.split("[:]");
        String subPropName = emProps[0].trim();
        String subPropValue = emProps[1].trim();
        if (subPropName.equals("Color")) {
            return parsePropColor(subPropValue);
        } else
        if (subPropName.equals("Boolean")) {
            return parsePropColor(subPropValue);
        } else
        if (subPropName.equals("Insets")) {
            return parsePropInsets(subPropValue);
        }
        return null;
    }

    /**
     * Parses a property value as a java.awt.Color
     * @param subPropValue
     * @return
     */
    private Color parsePropColor(String subPropValue) {
            Color bgColor = Color.decode(subPropValue);
            return bgColor;
    }

    /**
     * Parses a property value as  a java.lang.Boolean
     * @param subPropValue
     * @return
     */
    private Boolean parsePropBoolean(String subPropValue) {
            Boolean bState = Boolean.parseBoolean(subPropValue);
            return bState;
        
    }

    /**
     * Parses a property value as an Insets object
     * @param subPropValue
     * @return
     */
    private Insets parsePropInsets(String subPropValue) {
            String[] insetProps = subPropValue.split("[,]");
            Integer[] intInsetProps = new Integer[insetProps.length];
            for (int i = 0; i < insetProps.length; i++) {
                 intInsetProps[i] = Integer.parseInt(insetProps[i]);
            }
            Insets objInset = new Insets(intInsetProps[0],intInsetProps[1], intInsetProps[2], intInsetProps[3] );
            return objInset;
    }
}
