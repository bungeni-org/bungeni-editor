/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

/**
 *
 * @author undesa
 */
public class CommonStringFunctions {
      
    public static String stripNonAlphanumeric(String s) { 
         return s.replaceAll("[^a-zA-Z0-9]", ""); 
      }

      public static String stripNonAlphanumeric(String s, String sExcept) {
         return s.replaceAll("[^a-zA-Z0-9"+ "\\"+ sExcept + "]", "");
      }

    public static String convertUriForAttrUsage(String s) {
         return s.replaceAll("[/]", ".");
      }

    public static String makeReferenceFriendlyString(String s) {
        return stripNonAlphanumeric(s);
    }

    public static String makeReferenceFriendlyString(String s, String excl) {
        return stripNonAlphanumeric(s, excl);
    }
    
    public static boolean emptyOrNull(String s) {
        if (s == null ) return true;
        if (s.trim().length() == 0 ) return true;
        return false;
    }

}
