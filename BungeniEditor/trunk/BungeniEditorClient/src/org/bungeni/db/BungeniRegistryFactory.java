/*
 * BungeniRegistryFactory.java
 *
 * Created on August 24, 2007, 3:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.db;

import java.io.File;
import java.util.HashMap;
import org.bungeni.editor.BungeniEditorProperties;
import org.bungeni.utils.Installation;

/**
 *
 * @author Administrator
 */
public class BungeniRegistryFactory {
     /** Creates a new instance of BungeniRegistryFactory */
    private static String JDBCdriver = "";
    private static String JDBCDriverPrefix  = "";
    private static String registryDB = "";
    private static String localRegistry  = "";
    private static String localRegistryFolder = "" ;
    private static String registryUser = "";
    private static String registryPassword = "";
    private static HashMap<String,String> registryMap = new HashMap<String, String>();
    
    public BungeniRegistryFactory() {
    
    }
    
      public static String DEFAULT_INSTANCE() {
        String installDirectory = DEFAULT_INSTALLATION_PATH();
        return installDirectory + File.separator + "registry" + File.separator + "db" + File.separator;
  
    }
    
    public static String DEFAULT_INSTALLATION_PATH() {
        Installation install = new Installation();
        String installDirectory = install.getAbsoluteInstallDir();
        return installDirectory;
    }
    
    public static HashMap<String, String> fullConnectionString () {
     
     String fullConnectionString = "";
   
     if (JDBCdriver.length() == 0 ) {
       JDBCdriver = BungeniEditorProperties.getEditorProperty("registryJDBCdriver");
       JDBCDriverPrefix = BungeniEditorProperties.getEditorProperty("registryJDBCdriverPrefix");
       registryDB = BungeniEditorProperties.getEditorProperty("registryDB");
       localRegistry = BungeniEditorProperties.getEditorProperty("localRegistry");
       localRegistryFolder = BungeniEditorProperties.getEditorProperty("localRegistryFolder");
       registryUser = BungeniEditorProperties.getEditorProperty("registryUser");
       registryPassword = BungeniEditorProperties.getEditorProperty("registryPassword");
     }
     
     if (localRegistry.equals("yes")) {
           //get path to local registry db
           String path = DEFAULT_INSTALLATION_PATH(); 
           path = path + File.separator + localRegistryFolder ;
           path = path + File.separator + registryDB;
           fullConnectionString = JDBCDriverPrefix + path; 
          
        } else {
           fullConnectionString = JDBCDriverPrefix + registryDB;
        }
     
       registryMap.put("ConnectionString", fullConnectionString);
       registryMap.put("UserName", registryUser);
       registryMap.put("Password", registryPassword);
       registryMap.put("Driver", JDBCdriver);
      
       return registryMap; 
    }
}
