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
     
     String outputUriOrderString;
     
     OutputType outputType ;
     
     public BungeniURI(String orderOfURI, OutputType output) {
         this.uriOrderString = orderOfURI;
         this.outputType = output;
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
         
         java.util.Iterator<String> uriKeys = uriComponents.keySet().iterator();
         while (uriKeys.hasNext()) {
            String key = uriKeys.next();
            if (sURIorder.indexOf(key) != -1 ) {
                //uri contains this component
                sURIorder = sURIorder.replaceAll(key, uriComponents.get(key));
            }
         }
         this.outputUriOrderString = sURIorder;
     }
     
             
     public void setURIComponentOrder (String sUriComponentOrder) {
         this.uriOrderString = sUriComponentOrder;
     }
     
     public final String get(){
         if (outputType == OutputType.FILE_NAME) {
             return this.outputUriOrderString.replaceAll("~", "_");
         } else {
             return this.outputUriOrderString.replaceAll("~", File.separator);
         }
     }
     
     
}
