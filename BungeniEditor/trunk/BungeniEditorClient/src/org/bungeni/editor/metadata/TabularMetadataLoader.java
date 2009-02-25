/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.ooDocMetadataFieldSet;

/**
 *
 * @author undesa
 */
public class TabularMetadataLoader {
  
       private static org.apache.log4j.Logger log = Logger.getLogger(TabularMetadataLoader.class.getName());
 

    
  public static DocumentMetadata fetchDocumentMetadataConfig(String metadataVariable){
        DocumentMetadata returnmeta = null;
        try {
            String query = SettingsQueryFactory.Q_FETCH_DOCUMENT_METADATA_VARIABLE(metadataVariable);
            log.debug("fetchDocumentMetadataConfig :query = "+ query);
            String settingsInstance = DefaultInstanceFactory.DEFAULT_INSTANCE();
            BungeniClientDB db = new BungeniClientDB(settingsInstance, "");
            db.Connect();
            HashMap<String,Vector<Vector<String>>> resultsMap = db.Query(query);
            db.EndConnect();
            Vector<String> tableRow = new Vector<String>();
            QueryResults results = new QueryResults(resultsMap);
            if (results.hasResults() ) {
               Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
               resultRows = results.theResults();
               //it should always return a single row.... 
               //so we process the first row and brea
               log.debug("resultRows = "+ resultRows.size());
               for (int i = 0 ; i < resultRows.size(); i++ ) {
                       //get the results row by row into a string vector
                       tableRow = resultRows.elementAt(i);
                       returnmeta  = DocumentMetadataSupplier.convertVectorToDocumentMetadata(tableRow);
                       break;
               }
            }
        } catch (Exception ex) {
            log.error("fetchDocumentMetadataConfig : " + ex.getMessage());
        } finally {
            return returnmeta;
        }
  }  
    
  public static Vector fetchTabularMetadata(OOComponentHelper ooDocument, String findByPrefix){
        String findMetaPrefix = findByPrefix.replaceAll(":", "");
        ArrayList<ooDocMetadataFieldSet> metaObjectByType = ooDocMetadata.getMetadataObjectsByType(ooDocument, findMetaPrefix);
        Vector vTableModel = new Vector();
        for (Iterator<ooDocMetadataFieldSet> it = metaObjectByType.iterator(); it.hasNext();) {
            ooDocMetadataFieldSet docMetadataFieldSet = it.next();
            String metaName = docMetadataFieldSet.getMetadataName();
            String metaValue = docMetadataFieldSet.getMetadataValue();
            //fields are delimited by ~
            String[] judgeMetaValues = metaValue.split("~");
            Vector vRow = new Vector(Arrays.asList(judgeMetaValues));
            vTableModel.add(vRow);
            //add first name, last name , 
        }
        return vTableModel;
    }
  
  public static DefaultTableModel getTabularMetadataTableModel(OOComponentHelper ooDocument, String findByPrefix) {
        String findMetaPrefix = findByPrefix.replaceAll(":", "");
        //get column config vector
        DocumentMetadata meta = fetchDocumentMetadataConfig(findMetaPrefix);
        Vector vColumnConfig = meta.getTabularConfig();
        //get thet tabular metadata for the pattern
        Vector vMetadata = fetchTabularMetadata(ooDocument, findMetaPrefix);
        DefaultTableModel mdl = new DefaultTableModel(vMetadata, vColumnConfig);
        return mdl;

  }
  
}
