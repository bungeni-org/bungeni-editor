/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.impl;

import java.util.Iterator;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;

/**
 *
 * @author undesa
 */
public class TransformerConfigurationFactory {
   
    public static class Transformer {
        public String documentType;
        public String configName;
        public String configFile;
        public Transformer(){}
    }
    
    public static Transformer getConfiguration(String docType) {
        BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        QueryResults qr = instance.ConnectAndQuery(SettingsQueryFactory.Q_FETCH_TRANSFORM_CONFIG(docType));
        Transformer tf = new Transformer();
        if (qr.hasResults()) {
                Iterator<Vector<String>> resultsIterator = qr.theResultsIterator();
                while (resultsIterator.hasNext()) {
                    Vector<String> resultRow = resultsIterator.next();
                    tf.configFile =qr.getField(resultRow, "CONFIG_FILE");
                    tf.configName = qr.getField(resultRow, "CONFIG_NAME");
                    tf.documentType = qr.getField(resultRow, "DOC_TYPE");
                    break;
                }
        }
        return tf;  
    }  

}
