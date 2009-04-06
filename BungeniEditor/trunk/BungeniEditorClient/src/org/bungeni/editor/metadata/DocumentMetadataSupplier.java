/*
 * DocumentMetadataFactory.java
 *
 * Created on October 26, 2007, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class DocumentMetadataSupplier {
    
    private HashMap<String, DocumentMetadata> metadataMap = new HashMap<String, DocumentMetadata>();
    private OOComponentHelper ooDocument;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentMetadataSupplier.class.getName());

    private static int METADATA_NAME_COLUMN = 0;
    private static int METADATA_DATATYPE_COLUMN = 1;
    private static int METADATA_TYPE_COLUMN = 2;
    private static int METADATA_DISPLAYNAME_COLUMN = 3;
    private static int METADATA_VISIBLE_COLUMN = 4;
    private static int METADATA_TABLE_CONFIG = 5;
    
    /** Creates a new instance of DocumentMetadataFactory */
    public DocumentMetadataSupplier(OOComponentHelper ooDoc) {
        ooDocument = ooDoc;
        log.debug("in Constructor()");
        initDocumentMetadataVariables();
    }
   
    public int getVisibleCount(){
        return metadataMap.size();
    }
    
    public DocumentMetadata[] getDocumentMetadata(){
       // debug();
        return metadataMap.values().toArray(new DocumentMetadata[metadataMap.size()]);
    }
    
    private void debug(){
        Iterator<String> iter = metadataMap.keySet().iterator();
        while (iter.hasNext()) {
            log.debug("DocumentMetadataSupplier : keyset value = " + iter.next());
        }
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
        log.debug("loadMetadataFromDocument: begin");    
        if (!metadataMap.isEmpty()) {
            Iterator metaIterator = metadataMap.keySet().iterator();
            while (metaIterator.hasNext()) {
                String metaName = (String) metaIterator.next();
                DocumentMetadata metadata = metadataMap.get(metaName);
                log.debug("loadMetadataFromDocument: metaName " + metaName);
                //if the property exists in the document, get the property value
                if (ooDocument.propertyExists(metadata.getName())) {
                   metadata.setValue(ooDocument.getPropertyValue(metaName));
                   log.debug("loadMetadataFromDocument : metaName:"+ metaName + ", value = "+ metadata.getValue());
                   metadataMap.put(metadata.getName(), metadata); 
                } else {
                   metadata.setValue("ERROR_PROP_DOES_NOT_EXIST");
                   metadataMap.put(metadata.getName(), metadata); 
                }
            }
        }
        } catch (Exception ex) {
            log.error("error in refreshmetadata() "+ ex.getMessage());
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
    
    private void initDocumentMetadataVariables () {
        try {
        //fetch only visible metadata
        String query = SettingsQueryFactory.Q_FETCH_DOCUMENT_METADATA_VARIABLES("1");
       //ArrayList<DocumentMetadata> arrayMeta = new ArrayList<DocumentMetadata>();
        log.debug("getDocumentMetadataVariables :query = "+ query);
        String settingsInstance = DefaultInstanceFactory.DEFAULT_INSTANCE();
        BungeniClientDB db = new BungeniClientDB(settingsInstance, "");
        db.Connect();
        HashMap<String,Vector<Vector<String>>> resultsMap = db.Query(query);
        db.EndConnect();
        Vector<String> tableRow = new Vector<String>();
        QueryResults results = new QueryResults(resultsMap);
        metadataMap.clear();
        if (results.hasResults() ) {
           Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
           resultRows = results.theResults();
           //it should always return a single row.... 
           //so we process the first row and brea
           log.debug("resultRows = "+ resultRows.size());
           for (int i = 0 ; i < resultRows.size(); i++ ) {
                   //get the results row by row into a string vector
                   tableRow = resultRows.elementAt(i);
                   DocumentMetadata meta = convertVectorToDocumentMetadata(tableRow);
                   this.metadataMap.put (meta.getName(), meta);
                  // arrayMeta.add(meta);
                  // break;
           }
        } else {
            log.debug(" no results found!");
        }
        } catch (Exception ex) {
            log.error("exception in DocumentMetadataSupplier :"+ ex.getMessage());
        }
    }
    
    public static DocumentMetadata convertVectorToDocumentMetadata(Vector<String> tableRow) {
                   String metaName = tableRow.elementAt(METADATA_NAME_COLUMN);
                   log.debug("fetching metaName = "+ metaName);
                   String metaDataType = tableRow.elementAt(METADATA_DATATYPE_COLUMN);
                   String metaType = tableRow.elementAt(METADATA_TYPE_COLUMN);
                   String metaDisplay = org.bungeni.extutils.CommonResourceBundleHelperFunctions.getDocMetaString(metaName);
                   String visible = tableRow.elementAt(METADATA_VISIBLE_COLUMN);
                   String tableConfig = tableRow.elementAt(METADATA_TABLE_CONFIG);
                   DocumentMetadata meta = new DocumentMetadata(metaName, metaType , metaDataType, metaDisplay, Integer.parseInt(visible), tableConfig);
                   return meta;
    }
}
