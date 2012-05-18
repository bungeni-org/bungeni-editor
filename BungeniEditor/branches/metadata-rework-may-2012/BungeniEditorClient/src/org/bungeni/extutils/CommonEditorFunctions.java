package org.bungeni.extutils;

import java.io.File;
import org.apache.log4j.Logger;
import org.bungeni.db.DefaultInstanceFactory;

/**
 *
 * Helper functions for Editor internals
 *
 */
public class CommonEditorFunctions {

   private static org.apache.log4j.Logger log = Logger.getLogger(CommonEditorFunctions.class.getName());

   /**
    * Get the settings folder
    * @return
    */
    public static String getSettingsFolder(){
        return DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "settings";
    }

    /**
     * Gets the relative path of a file in working folder of the editor
     * @param thisPath
     * @return
     */
    public static String getPathRelativeToRoot(String thisPath) {
        String runtimeRoot = System.getProperty("user.dir");
        String appendPath = thisPath.replace('/', File.separatorChar);
        log.info("getPathRelativetoRoot for (" + thisPath + ") is " + runtimeRoot + File.separator + appendPath );
        return runtimeRoot + File.separator + appendPath ;
    }
     
}
