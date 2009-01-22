/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.uri;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author undesa
 */
public class BungeniURI {

    /**
     * URI components
     */
    private HashMap<String,String> uriComponents = new HashMap<String,String>(){
        {
            put("CountryCode", "");
            put("LanguageCode", "");
            put("Year","");
            put("DocumentType", "");
            put("Month", "");
            put("Day", "");
            put("Identifier", "");
            put("PartName", "");
            put("FileName", "");
        }
    };
    /*
     CountryCode countryCode;
     LanguageCode languageCode;
     DocumentPart documentPart;
     */
    
     String uriOrderString;
     String fileUriOrderString;
     
     String outputUriOrderString;
     String outputFileUriOrderString;
     
     
     public BungeniURI(String orderOfURI, String orderOfFileNamingURI) {
         this.uriOrderString = orderOfURI;
         this.fileUriOrderString = orderOfFileNamingURI;
     }
     
     public void setURIComponent(String componentName, String componentValue) {
        if (uriComponents.containsKey(componentName)){
            uriComponents.put(componentName, componentValue);
        } else 
           throw new ArrayIndexOutOfBoundsException();
     }
     
     public void parseURIs(){
         //set output uri
         String sURIorder = new String(this.uriOrderString);
         String sFileURIorder = new String(this.fileUriOrderString);
         
         java.util.Iterator<String> uriKeys = uriComponents.keySet().iterator();
         while (uriKeys.hasNext()) {
            String key = uriKeys.next();
            if (sURIorder.indexOf(key) != -1 ) {
                //uri contains this component
                sURIorder = sURIorder.replaceAll(key, uriComponents.get(key));
            }
            if (sFileURIorder.indexOf(key) != -1) {
                //uri contains this component
                sFileURIorder = sFileURIorder.replaceAll(key, uriComponents.get(key));
            }
         }
         this.outputUriOrderString = sURIorder;
         this.outputFileUriOrderString = sFileURIorder;
     }
     
     public void setFileURIComponentOrder(String sFileUriComponentOrder) {
        this.fileUriOrderString = sFileUriComponentOrder;
     }
             
     public void setURIComponentOrder (String sUriComponentOrder) {
         this.uriOrderString = sUriComponentOrder;
     }
     
     public final String getURI(){
         return this.outputUriOrderString.replaceAll("~", File.separator);
     }
     
     public final String getFileURI(){
         return this.outputFileUriOrderString.replaceAll("~", File.separator);
     }
     
}
