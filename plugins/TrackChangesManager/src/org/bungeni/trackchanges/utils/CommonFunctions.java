package org.bungeni.trackchanges.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonFunctions {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonFunctions.class.getName());


    public static String getRootPath() {    
        String sRootPath = "";
        try {
            sRootPath =  CommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException ex) {
            log.error("getRootPath : " + ex.getMessage(), ex);
        }
        return sRootPath;
    }
}
