package org.bungeni.extutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
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
  
  
    public static Locale getActiveLocale() {
        String langC = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-1");
        String countryC = BungeniEditorProperties.getEditorProperty("locale.Country.iso3166-1-a2");
        Locale lc = new Locale(langC, countryC);
        return lc;
    }

    /**
     *
     *
     * Loads the message bundles for the section type names and document metanames and error messages.
     * The other bundles are loaded by the individual dialog classes
     */
    public static void loadMessageBundles(){
        try {
          Locale activeLocale = getActiveLocale();

          List<Element> listBundles = BundlesReader.getInstance().getBundles();

          if (null != listBundles) {
              for (Element bundleElem : listBundles) {
                   String sFormat = bundleElem.getAttributeValue("format");
                   ResourceBundle rBundle = null;
                   if (sFormat.equals("xml")) {
                       rBundle = ResourceBundle.getBundle(bundleElem.getAttributeValue("name"), activeLocale, BungeniResourceBundleClassLoader.getInstance(),  new XMLResourceBundleControl());
                   } else {
                       rBundle = ResourceBundle.getBundle(bundleElem.getAttributeValue("name"), activeLocale, BungeniResourceBundleClassLoader.getInstance());
                  }
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
