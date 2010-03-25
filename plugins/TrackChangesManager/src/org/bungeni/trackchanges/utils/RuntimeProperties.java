package org.bungeni.trackchanges.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ini4j.Ini;

/**
 * Read properties from runtime.properties
 * @author undesa
 */
public class RuntimeProperties {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RuntimeProperties.class.getName());


    public static final String PROPERTIES_FILE = "app.ini";
    private static Ini runtimeProperties = null;
    private static Ini.Section defaultsSection = null;
    
    private static void loadProperties(){
        FileInputStream fsProps = null;
        try {
            runtimeProperties = new Ini();
            log.debug("Loading properties from : " + CommonFunctions.getRootPath() + File.separator + PROPERTIES_FILE);
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

    public static String getDefaultProp(String key) {
        if (runtimeProperties == null) {
            loadProperties();
        }
        if (defaultsSection == null ) {
            defaultsSection = runtimeProperties.get("default");
        }
        return defaultsSection.get(key);
    }

    public static List<String> getSectionProp(String section, String key) {
        if (runtimeProperties == null) {
            loadProperties();
        }
        Ini.Section aSection = runtimeProperties.get(section);
        int noOfValuesForKey = aSection.length(key);
        List<String> valuesList = new ArrayList<String>(0);
        for (int i = 0; i < noOfValuesForKey; i++) {
            valuesList.add(aSection.get(key, i, String.class));
        }
        return valuesList;
    }


}
