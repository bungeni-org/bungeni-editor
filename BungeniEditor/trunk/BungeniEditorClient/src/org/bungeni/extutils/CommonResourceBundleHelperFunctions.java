/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

/**
 *
 * @author undesa
 */
public  class CommonResourceBundleHelperFunctions {
    
    public static String getSectionMetaString(String name) {
        if (name == null ) return new String("");
        if (name.length() == 0) return new String("");
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

}
