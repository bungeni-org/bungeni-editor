/*
 * DefaultInstanceFactory.java
 *
 * Created on August 24, 2007, 3:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.db;

import java.io.File;
import org.bungeni.utils.Installation;

/**
 *
 * @author Administrator
 */
public class DefaultInstanceFactory {
    
    /** Creates a new instance of DefaultInstanceFactory */
    public DefaultInstanceFactory() {
    }
    
    public static String DEFAULT_INSTANCE() {
        String installDirectory = DEFAULT_INSTALLATION_PATH();
        return installDirectory + File.separator + "settings" + File.separator + "db" + File.separator;
  
    }
    
    public static String DEFAULT_DB() {
        return new String("settings.db");
    }
    
    
    public static String DEFAULT_INSTALLATION_PATH() {
        Installation install = new Installation();
        String installDirectory = install.getAbsoluteInstallDir();
        return installDirectory;
    }
}
