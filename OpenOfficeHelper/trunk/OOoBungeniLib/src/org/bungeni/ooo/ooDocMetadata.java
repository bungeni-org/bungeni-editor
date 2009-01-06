/*
 * ooDocMetadata.java
 *
 * Created on September 27, 2007, 10:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo;

import com.sun.star.beans.Property;
import com.sun.star.beans.UnknownPropertyException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ooDocMetadata {
    private OOComponentHelper ooDocument;
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ooDocMetadata.class.getName());
  
    /** Creates a new instance of ooDocMetadata */
    public ooDocMetadata(OOComponentHelper docHandle) {
        ooDocument  = docHandle;
    }
    
    public void setOODocument(OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }
    public void AddProperty (String propertyName, String propertyValue ){
        if (!PropertyExists(propertyName)) {
        ooDocument.addProperty(propertyName, propertyValue);
        } else {
            SetProperty(propertyName, propertyValue);
        }
    }
    
    public void SetProperty (String propertyName, String propertyValue ) {
        ooDocument.setPropertyValue(propertyName, propertyValue);
    }
    
    public boolean PropertyExists (String propertyName) {
        return ooDocument.propertyExists(propertyName);
    }
    
    public String GetProperty (String propertyName)  {
        String propertyValue = null;
       try {
        if (PropertyExists (propertyName))
                propertyValue = ooDocument.getPropertyValue(propertyName);
       } catch (UnknownPropertyException ex) {
                log.error("GetProperty:"+propertyName+" "+ ex.getMessage());
               
       } finally {
       return propertyValue;
       }
    }
    /**
     * Returns document metadata properties by type
     * Properties are 'typed' based on a prefix e.g. rf: is an internal reference prefix
     * @param ooDoc OOComponentHelper object for the document whose properties need to be retreived
     * @param docMetadataType Type filter prefix (without ":" )
     * @return
     */
    public static ArrayList<ooDocMetadataFieldSet> getMetadataObjectsByType (OOComponentHelper ooDoc, String docMetadataType) {
        ArrayList<ooDocMetadataFieldSet> matchedProperties = new ArrayList<ooDocMetadataFieldSet>(0);
        Property[] docProps = ooDoc.getDocumentProperties();
        for (Property prop : docProps) {
            if (prop.Name.startsWith(docMetadataType)){
                try {
                    String value = ooDoc.getPropertyValue(prop.Name);
                    ooDocMetadataFieldSet fld = new ooDocMetadataFieldSet(prop.Name, value);
                    matchedProperties.add(fld);
                } catch (UnknownPropertyException ex) {
                    log.error("getMetadataObjectsByType : " + ex.getMessage());
                }
            }
        }
        return matchedProperties;
    }
    
}
