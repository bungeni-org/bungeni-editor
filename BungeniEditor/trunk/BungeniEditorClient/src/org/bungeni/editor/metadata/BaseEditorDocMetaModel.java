/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Defines the base metadata attributes applicable to all Bungeni documents
 * This is extended for specific document types
 * @author undesa
 */
public class BaseEditorDocMetaModel implements IEditorDocMetaModel {
    HashMap<String,String>docMeta = new HashMap<String,String>();
    
    public void setup(){
        //document related
        docMeta.put("BungeniDocType", "");
        docMeta.put("BungeniPublicationName", "");
        docMeta.put("BungeniPublicationNameFull", "");
        docMeta.put("BungeniPublicationDate", "");
        docMeta.put("BungeniDocPart", "");
        docMeta.put("BungeniCountryCode", "");
        docMeta.put("BungeniLanguageCode", "");
        //FRBR Items
        //work
        docMeta.put("BungeniWorkAuthor", "");
        docMeta.put("BungeniWorkAuthorURI", "");
        docMeta.put("BungeniWorkDate", "");
        docMeta.put("BungeniWorkDateName","");
        docMeta.put("BungeniWorkURI", "");
        //expression
        docMeta.put("BungeniExpAuthor", "");
        docMeta.put("BungeniExpAuthorURI", "");
        docMeta.put("BungeniExpDate", "");
        docMeta.put("BungeniExpDateName","");
        docMeta.put("BungeniExpURI", "");
        //manifestation
        docMeta.put("BungeniManAuthor", "");
        docMeta.put("BungeniManAuthorURI", "");
        docMeta.put("BungeniManDate", "");
        docMeta.put("BungeniManDateName","");
        docMeta.put("BungeniManURI", "");
        //metadata exists
        //docMeta.put("__BungeniDocMeta", "");
    }             
   
    
    public BaseEditorDocMetaModel(){
        
    }

    public HashMap<String, String> getModelMap() {
        return docMeta;
    }
    
    public void updateItem(String itemName, String itemValue) {
        if (docMeta.containsKey(itemName)) {
            docMeta.put(itemName, itemValue);
        }
    }
   
    public String getItem(String itemName) {
        if (docMeta.containsKey(itemName)) {
            return docMeta.get(itemName);
        } else 
            return null;
    }
    
    public Set<String> getMetaNames(){
        return docMeta.keySet();
    }
    
    public void saveItem(String itemName, OOComponentHelper ooDoc) {
        ooDocMetadata docM = new ooDocMetadata(ooDoc);
        if (docMeta.containsKey(itemName)) {
            docM.AddProperty(itemName, docMeta.get(itemName));
        }
    }
    
    public void saveModel(OOComponentHelper ooDoc) {
        ooDocMetadata docM = new ooDocMetadata(ooDoc);
        Iterator<String> iterKeys = docMeta.keySet().iterator();
        while (iterKeys.hasNext()) {
            String nextKey = iterKeys.next();
            docM.AddProperty(nextKey,docMeta.get(nextKey));
        }
    }
    
    public void loadModel(OOComponentHelper ooDoc){
        ooDocMetadata docM = new ooDocMetadata(ooDoc);
        Iterator<String> iterKeys = docMeta.keySet().iterator();
        while (iterKeys.hasNext()) {
            String nextKey = iterKeys.next();
            if (docM.PropertyExists(nextKey)) {
                String docPropValue = docM.GetProperty(nextKey);
                docMeta.put(nextKey, docPropValue);
            }
        }
    }
    
}
