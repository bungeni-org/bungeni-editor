package org.bungeni.utils;

import java.io.File;
import org.bungeni.uri.BungeniManifestationName;
import org.bungeni.uri.BungeniURI;

/**
 * Returns a file saving path out of a URI
 * @author Ashok
 */
public class BungeniFileSavePathFormat {
    
    BungeniURI workURI;
    BungeniURI expURI;
    BungeniManifestationName fileNameFormat;

    private static org.apache.log4j.Logger log =
      org.apache.log4j.Logger.getLogger(
                BungeniFileSavePathFormat.class.getName()
                );
  
    public BungeniFileSavePathFormat(String wURI, String eURI, String fnFormat){
        workURI = new BungeniURI(wURI);
        expURI = new BungeniURI(eURI);
        fileNameFormat = new BungeniManifestationName(fnFormat);
    }

    /**
     * API to set the URI components , the URI components are then used to generate
     * the URI by calling parseComponents()
     * @param compName
     * @param compValue
     */
    public void setSaveComponent(String compName, String compValue) {
        try {
            setURIFormatComponent(workURI, compName, compValue );
            setURIFormatComponent(expURI, compName, compValue );
            setURIFormatComponent(fileNameFormat, compName, compValue );
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.error("setSaveComponent missing compName : " + compName, ex);
        }
    }
    
    public void parseComponents(){
        try {
            log.debug("parseComponents: parsing expURI");
            this.expURI.parse();
            log.debug("parseComponents: parsing workURI");
            this.workURI.parse();
            log.debug("parseComponents: parsing filename format");
            this.fileNameFormat.parse();
        } catch (Exception ex) {
            log.error("parseComponents : " + ex.getMessage(), ex);
        }
    }

    /**
     * Returns the generated URI
     * @return
     */
    public String getExpressionURI(){
        return this.expURI.get();
    }

    private String _getFilePath(BungeniURI aURI) {
        return aURI.get().replace(BungeniURI.separator(), File.separator);
    }

    /**
     * Returns the generated file path of the expression
     * @return
     */
    public String getExpressionFilePath(){
        return _getFilePath(this.expURI);
    }

    /**
     * Returns the generated work URI
     * @return
     */
    public String getWorkURI(){
        return this.workURI.get();
    }

    /**
     * Returns the generated file path of the work URI
     * @return
     */
    public String getWorkFilePath(){
        return _getFilePath(this.expURI);
    }

    /**
     * Returns the name of the manifestation
     * @return
     */
    public String getManifestationName(){
        return this.fileNameFormat.get();
    }
    
    public void setSaveComponent(String compName , int compValue) {
        String sCompValue = "";
        sCompValue = Integer.toString(compValue);
        setSaveComponent(compName, sCompValue);
    }

    private void setURIFormatComponent(BungeniURI uriComp, String compName, String compValue) {
        try {
            uriComp.setURIComponent(compName, compValue);
        } catch (ArrayIndexOutOfBoundsException ex) {
           log.debug("setURIFormatComponent (BungeniURI) " + ex.getMessage()); 
        }
    }
    
    private void setURIFormatComponent(BungeniManifestationName uriComp, String compName, String compValue) {
        try {
            uriComp.setURIComponent(compName, compValue);
        } catch (ArrayIndexOutOfBoundsException ex) {
           log.debug("setURIFormatComponent (BungeniManifestationName) " + ex.getMessage()); 
        }
    }
   
}
