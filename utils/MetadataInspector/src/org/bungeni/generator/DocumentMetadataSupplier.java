/*
 * DocumentMetadataFactory.java
 *
 * Created on October 26, 2007, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.generator;

import com.sun.star.beans.Property;
import com.sun.star.beans.UnknownPropertyException;
import java.util.HashMap;
import java.util.Iterator;

import java.util.TreeMap;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class DocumentMetadataSupplier {
    
    private TreeMap<String, DocumentMetadata> metadataMap = new TreeMap<String, DocumentMetadata>();
    private OOComponentHelper ooDocument;
  
    
    /** Creates a new instance of DocumentMetadataFactory */
    public DocumentMetadataSupplier(OOComponentHelper ooDoc) {
        ooDocument = ooDoc;
        // initDocumentMetadataVariables();
    }
   
    public int getVisibleCount(){
        return metadataMap.size();
        /*int counter = 0;
        Iterator keyIterator = metadataMap.keySet().iterator();
        while (keyIterator.hasNext()) {
             String key = (String) keyIterator.next();
             DocumentMetadata metadata = metadataMap.get(key);
             if (metadata.IsVisible())
                 counter++;
        }
        return counter;*/
    }
    
    public DocumentMetadata[] getDocumentMetadata(){
     
        return metadataMap.values().toArray(new DocumentMetadata[metadataMap.size()]);
    }
    
  
    
    /*
     *Set ooDocument OOComponentHelper Object
     */
    
    public void setOOComponentHelper(OOComponentHelper ooDoc){
        this.ooDocument = ooDoc;
    }
    
    /*
     *Get metadata values from document into metadata map
     */
    public void loadMetadataFromDocument(){
        try {
        Property[] docProps = ooDocument.getDocumentProperties();
        metadataMap.clear();
        for (Property prop : docProps) {
            if (prop.Name.startsWith("Bungeni")){
                DocumentMetadata meta = new DocumentMetadata(prop.Name, ooDocument.getPropertyValue(prop.Name));
                metadataMap.put(prop.Name,  meta);
            }
        }
        
        } catch (Exception ex) {
        }
    }
    
    
    
    /*
     *Get metadata values from map into document
     */
    public void updateMetadataToDocument(){
         if (!metadataMap.isEmpty()) {
            Iterator metaIterator = metadataMap.keySet().iterator();
            while (metaIterator.hasNext()) {
                String metaName = (String) metaIterator.next();
                updateMetadataToDocument(metaName);
            }
        }
    }
    public void updateMetadataToDocument(String name) {
        if (metadataMap.containsKey(name)){
            DocumentMetadata metadata = metadataMap.get(name);
            if (ooDocument.propertyExists(metadata.getName())) {
               ooDocument.setPropertyValue(metadata.getName(), metadata.getValue());
            } else {
                //property does not exist, so add it
               ooDocument.addProperty(metadata.getName(), metadata.getValue());
            }
        }
    }
    
    
    
}
