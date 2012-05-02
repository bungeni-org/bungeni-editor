package org.bungeni.extutils;

import java.io.File;
import org.apache.log4j.Logger;
import org.bungeni.db.DefaultInstanceFactory;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.util.Locale;

/**
 *
 */
public class CommonEditorFunctions {

   private static org.apache.log4j.Logger log = Logger.getLogger(CommonEditorFunctions.class.getName());

    public static String getSettingsFolder(){
        return DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "settings";
    }

    public static String getPathRelativeToRoot(String thisPath) {
        String runtimeRoot = System.getProperty("user.dir");
        String appendPath = thisPath.replace('/', File.separatorChar);
        log.info("getPathRelativetoRoot for (" + thisPath + ") is " + runtimeRoot + File.separator + appendPath );
        return runtimeRoot + File.separator + appendPath ;
    }

    /**
     * Changes the Orientation of the components depending on the Locale
     *
     * @param comp Component handle
     */
     public static void compOrientation(Component comp)  
     {
       //!+ORIENTATION(ah, 03-05-2012) - original version by smr.ayesh was appling orientation
       //as Container, added a fallback if it isnt a container, to use the regular Component class
       //apply orientation api
       if (comp instanceof Container) {
            ((Container)comp).applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
       } else {
            comp.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
       }

     }
     
}
