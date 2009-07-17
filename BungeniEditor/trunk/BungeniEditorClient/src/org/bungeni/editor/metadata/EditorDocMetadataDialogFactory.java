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
 * @author Ashok Hariharan
 */
public class EditorDocMetadataDialogFactory {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditorDocMetadataDialogFactory.class.getName());
    public static String WORK_URI = "";
    public static String EXP_URI = "";
    public static String MANIFESTATION_FORMAT = "";


    public static ArrayList<IEditorDocMetadataDialog> getInstances(String docType) {
        ArrayList<IEditorDocMetadataDialog> dlgLists = new ArrayList<IEditorDocMetadataDialog>(0);
    //    String workURI = "";
   //     String expURI = "";
   //     String fileSavePathFormat = "";
        try {
            log.info("getInstance : getting db handle");
             BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
             db.Connect();
            log.info("getInstance : after connect");
             QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_METADATA_MODEL_EDITORS(docType));
             db.EndConnect();
             metadataModelResultsIterator resultsIter = new metadataModelResultsIterator();
             qr.resultsIterator(resultsIter);
             log.info("getInstance : number of dialog lists = " + resultsIter.dlgLists.size());
             dlgLists = resultsIter.dlgLists;
        } catch (Exception ex) {
            log.error("getInstances : " + ex.getMessage());
        } finally {
            return dlgLists;
        }
    }


    public static class metadataModelResultsIterator implements IQueryResultsIterator {
        public ArrayList<IEditorDocMetadataDialog> dlgLists = new ArrayList<IEditorDocMetadataDialog>(0);

        public boolean iterateRow(QueryResults mQr, Vector<String> rowData) {
                       try {
                       String metadataModelClass = mQr.getField(rowData, "METADATA_MODEL_EDITOR");
                       String metadataModelTitle = mQr.getField(rowData, "METADATA_EDITOR_TITLE");
                       if (metadataModelClass.length() > 0 ) {
                                log.info("iterateRow : creating instance for class = " + metadataModelClass);
                                IEditorDocMetadataDialog iInstance = newInstance(metadataModelClass, metadataModelTitle);
                                dlgLists.add(iInstance);
                           }
                       } catch (Exception ex) {
                           log.error("iterateRow : " + ex.getMessage());
                       }
                       return true;
        }
    }


    public static IEditorDocMetadataDialog newInstance (String metaClassName, String metaTabTitle) {
        IEditorDocMetadataDialog iInstance = null;
        try {
            log.info("newInstance for class : " + metaClassName);
            Class modelClass;
            modelClass = Class.forName(metaClassName);
            log.info("newInstance created class");
            iInstance = (IEditorDocMetadataDialog) modelClass.newInstance();
            log.info("newInstance created instance from class");
            iInstance.setTabTitle(metaTabTitle);
            log.info("newInstance set title = " + metaTabTitle);
        } catch (ClassNotFoundException ex) {
            log.error("newInstance() : " + ex.getMessage());
        } catch (InstantiationException ex) {
            log.error("newInstance() : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("newInstance() : " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("newInstance() (null pointer) = " + ex.getMessage());
        } finally {
            return iInstance;
        }
    }

}
