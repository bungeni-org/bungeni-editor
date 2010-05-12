/*
 * GeneralQueryFactory.java
 *
 * Created on August 24, 2007, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.db;

/**
 *
 * @author Administrator
 */
public class GeneralQueryFactory {
    
    /** Creates a new instance of GeneralQueryFactory */
    public GeneralQueryFactory() {
    }
      public static String Q_FETCH_ALL_MPS (){
        String query = new String("" +
                "select id, first_name, last_name, uri from persons");
        return query;
    }
    
    public static String Q_FETCH_METADATA_SOURCES(){
        String query= new String("" +
                "select * from metadata_sources");
        return query;
    }
    
    public static String Q_FETCH_MACRO_DATA(String programming_lang, 
                                             String library_name, 
                                             String macro_name ){
        String query = new String(""+ 
                "select prog_language, library_name, macro_name, no_of_params from editor_macros where " +
                "prog_language='"+programming_lang+"' and " +
                "library_name= '"+library_name+"' and " +
                "macro_name = '"+macro_name+"' ");
        return query; 
    }
    
    public static String Q_FETCH_ALL_QUESTIONS() {
        return new String("select * from questions");
    }

    public static String Q_FETCH_PERSON_BY_URI (String URI) {
        return new String("Select * from persons where " +
                "uri='"+URI +"'");
    }
}
