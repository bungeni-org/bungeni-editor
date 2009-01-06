/*
 * MacroFactory.java
 *
 * Created on August 28, 2007, 4:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.macro;

import java.util.HashMap;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.GeneralQueryFactory;
import org.bungeni.db.QueryResults;

/**
 *
 * @author Administrator
 */
public class ExternalMacroFactory {
    private static String DEFAULT_LANGUAGE = "Basic";
    private static String DEFAULT_LIBRARY = "BungeniLibs.Common" ;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExternalMacroFactory.class.getName());
  
    public static void setDefaultLanguage (String lang) {
        DEFAULT_LANGUAGE = lang;
    }
    
    public static void setDefaultLibrary (String library){
        DEFAULT_LIBRARY = library;
    }
    
    /** Creates a new instance of MacroFactory */
    public ExternalMacroFactory() {
    }

    public static ExternalMacro getMacroDefinition (String macro_name) {
        String query = GeneralQueryFactory.Q_FETCH_MACRO_DATA(DEFAULT_LANGUAGE, 
                DEFAULT_LIBRARY, macro_name);
         String propertyValue = "";
         /*
        if (propertiesMap.containsKey(propertyName) ) {
           log.debug("getEditorProperty : property found in map");
           return (String) propertiesMap.get(propertyName);
        } else {
            log.debug("getEditorProperty: property not cached, querying");
        }*/
        log.debug("getMacroDefinition :"+macro_name + " query = "+ query);
        String settingsInstance = DefaultInstanceFactory.DEFAULT_INSTANCE();
        BungeniClientDB db = new BungeniClientDB(settingsInstance, "");
        db.Connect();
        HashMap<String,Vector<Vector<String>>> resultsMap = db.Query(query);
        db.EndConnect();
        QueryResults results = new QueryResults(resultsMap);
        String programmingLang= "", libraryName = "", noParams = "";
       
        if (results.hasResults() ) {
          HashMap columnsMap = results.columnNameMap();
          log.debug("getMacroDefiniton: hasResults()");
           Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
           resultRows = results.theResults();
           //it should always return a single row.... 
           //so we process the first row and brea
           Vector<java.lang.String> tableRow = new Vector<java.lang.String>();
               for (int i = 0 ; i < resultRows.size(); i++ ) {
                   //get the results row by row into a string vector
                   tableRow = (Vector<String>) resultRows.elementAt(i);
                   break;
               }
          if (tableRow.size() > 0) {
            programmingLang = tableRow.elementAt(results.getColumnIndex("PROG_LANGUAGE")-1); 
            libraryName = tableRow.elementAt(results.getColumnIndex("LIBRARY_NAME")-1);
            noParams = tableRow.elementAt(results.getColumnIndex("NO_OF_PARAMS")-1);
           }
           ExternalMacro macro = new ExternalMacro(programmingLang, libraryName,
                   macro_name, Integer.parseInt(noParams));
           return macro;
        } else {
            return null;
        } 
    }


}
