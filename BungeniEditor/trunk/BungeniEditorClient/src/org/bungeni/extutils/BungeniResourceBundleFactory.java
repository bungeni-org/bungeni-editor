package org.bungeni.extutils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.bungeni.editor.config.BundlesReader;
import org.jdom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniResourceBundleFactory {
    public static HashMap<String,ResourceBundle> messageBundles = new HashMap<String, ResourceBundle>();
    private static boolean bundlesLoaded = false;
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
                String prefPath = BundlesReader.getBundlesFolder() + File.separator + name;
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
        String langC = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-1");
        String countryC = BungeniEditorProperties.getEditorProperty("locale.Country.iso3166-1-a2");
        Locale lc = new Locale(langC, countryC);
        return lc;
    }
    
    /**
     * Loads the message bundles for the section type names and document metanames and error messages.
     * The other bundles are loaded by the individual dialog classes
     */
    public static void loadMessageBundles(){
        try {
          Locale activeLocale = getActiveLocale();

          List<Element> listBundles = BundlesReader.getInstance().getBundles();

          if (null != listBundles) {
              for (Element bundleElem : listBundles) {
                   ResourceBundle rBundle = ResourceBundle.getBundle(bundleElem.getAttributeValue("name"), activeLocale, new BungeniResourceBundleClassLoader());
                   messageBundles.put(bundleElem.getAttributeValue("name"), rBundle);
              }
          }
          bundlesLoaded = true;
        }catch (Exception ex) {
            log.error("loadMessageBundles : " + ex.getMessage());
            bundlesLoaded = false;
        }
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
