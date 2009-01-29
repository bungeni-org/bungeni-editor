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
public abstract class URIBase {

    /**
     * URI components
     */
    protected HashMap<String,String> uriComponents = new HashMap<String,String>(){
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
    
     protected String uriOrderString;
     
     protected String outputUriOrderString;
     
      
     public URIBase(String orderOfURI) {
         this.uriOrderString = orderOfURI;
     }
     
     public void setURIComponent(String componentName, String componentValue) throws ArrayIndexOutOfBoundsException {
        if (uriComponents.containsKey(componentName)){
            uriComponents.put(componentName, componentValue);
        } else 
           throw new ArrayIndexOutOfBoundsException("componentName : " + componentName);
     }
     
     public void parse(){
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
     
     public String get(){
        return this.outputUriOrderString;
     }
     
     
}
