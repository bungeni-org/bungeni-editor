package org.bungeni.trackchanges.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Read properties from runtime.properties
 * @author undesa
 */
public class RuntimeProperties {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RuntimeProperties.class.getName());


    public static final String PROPERTIES_FILE = "runtime.properties";
    private static Properties runtimeProperties = null;

    private static void loadProperties(){
        FileInputStream fsProps = null;
        try {
            runtimeProperties = new Properties();
            System.out.println("Loading properties from : " + CommonFunctions.getRootPath() + File.separator + PROPERTIES_FILE);
            File fProps = new File(CommonFunctions.getRootPath() + File.separator + PROPERTIES_FILE);
            fsProps = new FileInputStream(fProps);
            runtimeProperties.load(fsProps);
        } catch (IOException ex) {
            log.error("loadProperties : " + ex.getMessage());
        }  finally {
            try {
                fsProps.close();
            } catch (IOException ex) {
                log.error("loadProperties : " + ex.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        if (runtimeProperties == null) {
            loadProperties();
        }
        return runtimeProperties.getProperty(key, "");
    }


}
