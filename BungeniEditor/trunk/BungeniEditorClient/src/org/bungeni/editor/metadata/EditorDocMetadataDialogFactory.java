/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;

/**
 *
 * @author undesa
 */
public class EditorDocMetadataDialogFactory {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditorDocMetadataDialogFactory.class.getName());
    public static String WORK_URI = "";
    public static String EXP_URI = "";
    public static String MANIFESTATION_FORMAT = "";
    
    public static IEditorDocMetadataDialog getInstance(String docType) {
        IEditorDocMetadataDialog iInstance = null;
        String metadataModelClass= "";
        String metadataModelTitle = "";
        String workURI = "";
        String expURI = "";
        String fileSavePathFormat = "";
        try {
             BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
             db.Connect();
             QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_DOCUMENT_TYPE_BY_NAME(docType));
             db.EndConnect();
             if (qr.hasResults()){
                   Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
                   resultRows = qr.theResults();
                   for (Vector<String> resultRow: resultRows) {
                       metadataModelClass = qr.getField(resultRow, "METADATA_MODEL_EDITOR");
                       metadataModelTitle = qr.getField(resultRow, "METADATA_EDITOR_TITLE");
                       WORK_URI =  qr.getField(resultRow, "WORK_URI");
                       EXP_URI = qr.getField(resultRow, "EXP_URI");
                       MANIFESTATION_FORMAT = qr.getField(resultRow, "FILE_NAME_SCHEME");
                       break;
                   } 
                 }
           if (metadataModelClass.length() > 0 ) {
              Class modelClass;
              modelClass = Class.forName(metadataModelClass);
              iInstance = (IEditorDocMetadataDialog)modelClass.newInstance();
              iInstance.setTabTitle(metadataModelTitle);
              //iInstance.setUriFormats(workURI, expURI, fileSavePathFormat);
           }
        } catch (Exception ex) {
            log.error("getInstance : "+ ex.getMessage());
        } finally {
            return iInstance;
        }
    }
}
