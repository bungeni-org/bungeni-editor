/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;

/**
 *
 * @author undesa
 */
public class BungeniResourceBundleFactory {
    public static HashMap<String,ResourceBundle> messageBundles = new HashMap<String, ResourceBundle>();
    private static boolean bundlesLoaded = false;
    private static String messageBundlesFolder  = "";
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniResourceBundleFactory.class.getName());
  
    /**
     * Invoke only after loadMessageBundles(), requires messageBundlesFolder variable to be set prior to call
     * 
     */
    public static class BungeniResourceBundleClassLoader extends ClassLoader {
        @Override
        public URL findResource (String name) {
                URL urlFile = null;
            try {
                String prefPath = messageBundlesFolder + File.separator + name;
                File fProps = new File(prefPath);
                urlFile = fProps.toURI().toURL();
            } catch (MalformedURLException ex) {
                log.error("findResource : " + ex.getMessage());
            } finally {
                return urlFile;
            }
                
        }
        
    }
    public static Locale getActiveLocale() {
        String langC = BungeniEditorProperties.getEditorProperty("locale.Language");
        String countryC = BungeniEditorProperties.getEditorProperty("locale.Country");
        Locale lc = new Locale(langC, countryC);
        return lc;
    }
    
    
    public static void loadMessageBundles(){
        try {
          Locale activeLocale = getActiveLocale();
          messageBundlesFolder  = loadPathToMessageBundles();
          BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
          db.Connect();
          QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_MESSAGE_BUNDLES());
          String[] bundles = qr.getSingleColumnResult("BUNDLE_NAME");
          db.EndConnect();
          for (String bundle : bundles ) {
              ResourceBundle rBundle = ResourceBundle.getBundle(bundle, activeLocale, new BungeniResourceBundleClassLoader());
              messageBundles.put(bundle, rBundle);
          }
          bundlesLoaded = true;
        }catch (Exception ex) {
            log.error("loadMessageBundles : " + ex.getMessage());
            bundlesLoaded = false;
        }
    }
    
    public static String loadPathToMessageBundles(){
       String bundlePath = BungeniEditorProperties.getEditorProperty("messageBundlesPath");
       String msgBundlesFolder = CommonFileFunctions.convertRelativePathToFullPath(bundlePath);
       return msgBundlesFolder;
    }
    
    
    public static String getString(String bundleName, String msgString) {
        if (!bundlesLoaded)
            loadMessageBundles();
        if (messageBundles.containsKey(bundleName)) {
            String foundString = "";
            try {
            ResourceBundle bundle = messageBundles.get(bundleName);
            foundString = bundle.getString(msgString);
            } catch (MissingResourceException ex) {
                foundString = msgString;
            }
            return foundString;
         } else {
            return msgString;
         }
        
    }
}
