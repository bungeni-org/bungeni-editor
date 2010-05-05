package org.bungeni.db;

import java.io.File;

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
        String defaultInstance =  installDirectory + File.separator + "merge" + File.separator ;
        log.info("DEFAULT_INSTANCE : " + defaultInstance);
        return defaultInstance;
  
    }
    
    public static String DEFAULT_DB() {
        return new String("merge.db");
    }
    
    
    public static String DEFAULT_INSTALLATION_PATH() {
      String installDirectory = System.getProperty("user.dir");
        return installDirectory;
    }
}
