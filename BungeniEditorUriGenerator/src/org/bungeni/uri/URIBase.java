package org.bungeni.uri;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Base class for building URIs based on input components
 * @author Ashok
 */
public abstract class URIBase {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(URIBase.class.getName());

    public final static String URI_SEPARATOR = "/";
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
            put("ActType", "");
            put("ActY", "");
            put("ActNo", "");
            put("BungeniActReleaseDate", "");
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
         try {
         String sURIorder = this.uriOrderString;
         log.info("parse: original uri order string : " + uriOrderString);
         java.util.Iterator<String> uriKeys = uriComponents.keySet().iterator();
         while (uriKeys.hasNext()) {
            String key = uriKeys.next();
            if (sURIorder.indexOf(key) != -1 ) {
                //uri contains this component
                sURIorder = sURIorder.replaceAll(key, uriComponents.get(key));
                log.info("parse: parsed order string " + sURIorder);
            }
         }
         this.outputUriOrderString = sURIorder;
         } catch (Exception ex) {
                log.error("parse: "+ ex.getMessage());
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                log.error("parse :" + sw.toString());
         }
     }
     
             
     public void setURIComponentOrder (String sUriComponentOrder) {
         this.uriOrderString = sUriComponentOrder;
     }
     
     public String get(){
        return this.outputUriOrderString;
     }

     public static String separator(){
         return Matcher.quoteReplacement(URIBase.URI_SEPARATOR);
     }
     
     
}
