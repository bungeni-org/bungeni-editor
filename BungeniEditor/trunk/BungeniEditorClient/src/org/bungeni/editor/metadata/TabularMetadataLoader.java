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
        } 
      return returnmeta;
        
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

    public static Vector fetchTabularMetadata(OOComponentHelper ooDocument, String findByPrefix, int[] indices){
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
            for (int filterIndex : indices ) {
                vRow.remove(filterIndex);
            }
            vTableModel.add(vRow);
            //add first name, last name ,
        }
        return vTableModel;
    }
  
  
  public static class TabularMetadataModel {
      public Vector columnVector;
      public Vector dataVector;
      public DefaultTableModel tabularModel;
      
      public TabularMetadataModel(){
          
      }
  }
  /**
   * 
   * @param ooDocument
   * @param findByPrefix
   * @return Object array with 3 elements - 
   * element 0 - column config Vector
   * element 1 - data vector
   * element 2 - defautablemodel composed of element 0 and 1 
   */
  public static TabularMetadataModel getTabularMetadataTableModel(OOComponentHelper ooDocument, String findByPrefix) {
        TabularMetadataModel metamodel = new TabularMetadataModel();

        /*
        String findMetaPrefix = findByPrefix.replaceAll(":", "");
        //get column config vector
        DocumentMetadata meta = fetchDocumentMetadataConfig(findMetaPrefix);
        Vector vColumnConfig = meta.getTabularConfig();
        metamodel.columnVector = vColumnConfig;
        //get thet tabular metadata for the pattern
        Vector vMetadata = fetchTabularMetadata(ooDocument, findMetaPrefix);
        metamodel.dataVector = vMetadata;
        */
        int[] filterIndices = {} ; // no filters
        Vector[] arrMetadata = getMetadataVector(findByPrefix, ooDocument, filterIndices);
 
        DefaultTableModel mdl = new DefaultTableModel(arrMetadata[1], arrMetadata[0]);
        metamodel.tabularModel = mdl;
        return metamodel;

  }

  /**
   *
   * @param metaPrefix
   * @param ooDocument
   * @return index 0 - column config, 1 - data vector
   */
  private static Vector[] getMetadataVector(String metaPrefix, OOComponentHelper ooDocument, int[] filterIndices) {
        String findMetaPrefix = metaPrefix.replaceAll(":", "");
        //get column config vector
        DocumentMetadata meta = fetchDocumentMetadataConfig(findMetaPrefix);
        Vector vColumnConfig = meta.getTabularConfig();
        for (int filterIndex : filterIndices) {
            vColumnConfig.remove(filterIndex);
        }
        Vector vMetadata = fetchTabularMetadata(ooDocument, findMetaPrefix, filterIndices);
        Vector[] vData = {vColumnConfig, vMetadata};
        return vData;
  }

  public static TabularMetadataModel getTabularMetadataTableModel(OOComponentHelper ooDocument, String findByPrefix, int[] filterTheseIndices) {
        TabularMetadataModel metamodel = new TabularMetadataModel();
        Vector[] arrMetadata = getMetadataVector(findByPrefix, ooDocument, filterTheseIndices);
   
        DefaultTableModel mdl = new DefaultTableModel(arrMetadata[1], arrMetadata[0]);
        metamodel.tabularModel = mdl;
        return metamodel;
  }
  
}
