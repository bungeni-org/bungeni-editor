
package org.bungeni.utils;

import org.bungeni.utils.BungeniResourceBundleFactory;

/**
 *
 * @author undesa
 */
public  class CommonResourceBundleHelperFunctions {
    
    public static String getSectionMetaString(String name) {
        if (name == null ) return "";
        if (name.length() == 0) return "";
        return BungeniResourceBundleFactory.getString("SectionMetaNames", name);
    }
    
    public static String getDocMetaString(String name) {
        return BungeniResourceBundleFactory.getString("DocMetaNames", name);
    }
    
    public static String getSectionTypeMetaString(String sectionType) {
        return BungeniResourceBundleFactory.getString("SectionTypeNames", sectionType);
    }
    
    public static String getErrorMsgString(String name) {
        return BungeniResourceBundleFactory.getString("ErrorMessages", name);
    }

    public static String getToolbarString(String name) {
        return BungeniResourceBundleFactory.getString("toolbar", name);
    }

}
