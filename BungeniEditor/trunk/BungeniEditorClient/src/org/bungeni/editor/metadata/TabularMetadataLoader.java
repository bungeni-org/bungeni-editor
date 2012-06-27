package org.bungeni.editor.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.DocumentMetadataReader;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.ooDocMetadataFieldSet;
import org.jdom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class TabularMetadataLoader {
  
       private static org.apache.log4j.Logger log = Logger.getLogger(TabularMetadataLoader.class.getName());
 

    
  public static DocumentMetadata fetchDocumentMetadataConfig(String metadataVariable){
        DocumentMetadata returnmeta = null;
        try {

            Element metadataElem = DocumentMetadataReader.getInstance().
                    getMetadataByName(
                        BungeniEditorPropertiesHelper.getCurrentDocType(),
                        metadataVariable
                    );

            if (null != metadataElem) {
                returnmeta = DocumentMetadataSupplier.convertElementToDocumentMetadata(metadataElem);
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
