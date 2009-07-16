package org.bungeni.extutils;

import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;

/**
 * Allows getting and setting of editor properties from the settings db's
 * general_editor_properties table
 * @author Ashok Hariharan
 */
public class BungeniEditorProperties {
    private static HashMap<String,String> propertiesMap = new HashMap<String,String>();
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniEditorProperties.class.getName());
    public static final String ODF_URI_PREFIX = "uri:";
    public static String SAX_PARSER_DRIVER = "org.apache.xerces.parsers.SAXParser";
    /** Creates a new instance of BungeniEditorProperties */
    public BungeniEditorProperties() {
    }

    private static int PROPERTY_NAME_COLUMN=0;
    private static int PROPERTY_VALUE_COLUMN=1;
    
    public static void setEditorProperty(String propertyName, String propertyValue ) {
        BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        String query = SettingsQueryFactory.Q_FETCH_ALL_DOCUMENT_TYPES();
        instance.Connect();
        int nRow = instance.Update(SettingsQueryFactory.Q_SET_EDITOR_PROPERTY(propertyName, propertyValue));
        instance.EndConnect();
        if (nRow > 0 ) {
            //update was successful
            propertiesMap.put(propertyName, propertyValue);
        }
    }
    
    public static void setPropertyInMap(String propName, String propValue) {
        propertiesMap.put(propName, propValue);
    }
    
    public static String getEditorProperty(String propertyName) {
        String propertyValue = "";
            if (propertiesMap.containsKey(propertyName) ) {
               log.debug("getEditorProperty : property found in map");
               return (String) propertiesMap.get(propertyName);
            } else {
                log.debug("getEditorProperty: property not cached, querying");
            }

            String settingsInstance = DefaultInstanceFactory.DEFAULT_INSTANCE();
            BungeniClientDB db = new BungeniClientDB(settingsInstance, "");
            db.Connect();
            HashMap<String,Vector<Vector<String>>> resultsMap = db.Query(SettingsQueryFactory.Q_FETCH_EDITOR_PROPERTY(propertyName));
            db.EndConnect();
            QueryResults results = new QueryResults(resultsMap);
            if (results.hasResults() ) {
               Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
               resultRows = results.theResults();
               //it should always return a single row....
               //so we process the first row and brea
               Vector<java.lang.String> tableRow = new Vector<java.lang.String>();

                   for (int i = 0 ; i < resultRows.size(); i++ ) {
                       //get the results row by row into a string vector
                       tableRow = resultRows.elementAt(i);
                       break;
                   }
               if (tableRow.size() > 0) {
                propertyValue = tableRow.elementAt(PROPERTY_VALUE_COLUMN);
                propertiesMap.put(propertyName, propertyValue);

               }
            }
            return propertyValue;
    }
    
}
