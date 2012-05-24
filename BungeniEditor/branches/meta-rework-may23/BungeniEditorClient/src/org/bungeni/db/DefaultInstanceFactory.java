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
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultInstanceFactory.class.getName());

    /** Creates a new instance of DefaultInstanceFactory */
    public DefaultInstanceFactory() {
    }
    
    public static String DEFAULT_INSTANCE() {
        String installDirectory = DEFAULT_INSTALLATION_PATH();
        String defaultInstance =  installDirectory + File.separator + "settings" + File.separator + "db" + File.separator;
        log.info("DEFAULT_INSTANCE : " + defaultInstance);
        return defaultInstance;
  
    }

    public static String DEFAULT_INSTALLATION_PATH() {
        Installation install = new Installation();
        String installDirectory = install.getAbsoluteInstallDir();
        log.info("DEFAULT_INSTALLATION_PATH : " + installDirectory);
        return installDirectory;
    }
}
