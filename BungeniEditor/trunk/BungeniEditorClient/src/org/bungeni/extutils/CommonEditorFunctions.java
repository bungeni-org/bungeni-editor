/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * @author Ashok Hariharan
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
    
     public static void compOrientation(Component comp)  
     {  
            if(comp instanceof Component)  
            {  
                  comp.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
            }  
            if(comp instanceof Container)  
            {  
                Component[] comps = ((Container)comp).getComponents();  
                for(int x = 0, y = comps.length; x < y; x++)  
                {  
                    compOrientation(comps[x]);  
                }  
            }  
     }  
     
}
