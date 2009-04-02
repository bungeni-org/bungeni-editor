/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.ArrayList;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.IQueryResultsIterator;
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


    public static class metadataModelResultsIterator implements IQueryResultsIterator {
        public ArrayList<IEditorDocMetadataDialog> dlgLists = new ArrayList<IEditorDocMetadataDialog>(0);

        public boolean iterateRow(QueryResults mQr, Vector<String> rowData) {
                       String metadataModelClass = mQr.getField(rowData, "METADATA_MODEL_EDITOR");
                       String metadataModelTitle = mQr.getField(rowData, "METADATA_EDITOR_TITLE");
                       if (metadataModelClass.length() > 0 ) {
                                IEditorDocMetadataDialog iInstance = newInstance(metadataModelClass, metadataModelTitle);
                                dlgLists.add(iInstance);
                           }
                       return true;
        }
    }

    public static ArrayList<IEditorDocMetadataDialog> getInstances(String docType) {
        ArrayList<IEditorDocMetadataDialog> dlgLists = new ArrayList<IEditorDocMetadataDialog>(0);
        String workURI = "";
        String expURI = "";
        String fileSavePathFormat = "";
        try {
             BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
             db.Connect();
             QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_METADATA_MODEL_EDITORS(docType));
             db.EndConnect();
             metadataModelResultsIterator resultsIter = new metadataModelResultsIterator();
             qr.resultsIterator(resultsIter);
             dlgLists = resultsIter.dlgLists;
             /*
             if (qr.hasResults()){
                   Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
                   resultRows = qr.theResults();
                   for (Vector<String> resultRow: resultRows) {
                       metadataModelClass = qr.getField(resultRow, "METADATA_MODEL_EDITOR");
                       metadataModelTitle = qr.getField(resultRow, "METADATA_EDITOR_TITLE");
                       if (metadataModelClass.length() > 0 ) {
                                IEditorDocMetadataDialog iInstance = newInstance(metadataModelClass, metadataModelTitle);
                                dlgLists.add(iInstance);
                           }
                   }
             }*/
        } catch (Exception ex) {
            log.error("getInstances : " + ex.getMessage());
        } finally {
            return dlgLists;
        }
    }

    public static IEditorDocMetadataDialog newInstance (String metaClassName, String metaTabTitle) {
        IEditorDocMetadataDialog iInstance = null;
        try {
            Class modelClass;
            modelClass = Class.forName(metaClassName);
            iInstance = (IEditorDocMetadataDialog) modelClass.newInstance();
            iInstance.setTabTitle(metaTabTitle);
        } catch (ClassNotFoundException ex) {
            log.error("newInstance() : " + ex.getMessage());
        } catch (InstantiationException ex) {
            log.error("newInstance() : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("newInstance() : " + ex.getMessage());
        } finally {
            return iInstance;
        }
    }


    /*
    public static void getManifestationInfo(String docType) {
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
    } */

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
