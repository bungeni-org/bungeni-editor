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
     
  
 
    public static String Q_FETCH_PERSON_BY_URI (String URI) {
        return "Select * from persons where " + "uri='" + URI + "'";
    }
}
