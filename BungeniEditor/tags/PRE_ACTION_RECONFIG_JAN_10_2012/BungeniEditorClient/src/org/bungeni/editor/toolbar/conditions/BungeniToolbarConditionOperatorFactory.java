/*
 * BungeniToolbarConditionOperatorFactory.java
 *
 * Created on January 29, 2008, 11:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions;

import java.util.HashMap;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;

/**
 *
 * @author Administrator
 */
public class BungeniToolbarConditionOperatorFactory {
    
    /** Creates a new instance of BungeniToolbarConditionOperatorFactory */
    public BungeniToolbarConditionOperatorFactory() {
    }
    private static HashMap<String, BungeniToolbarConditionOperator> conditionOperatorMap= new HashMap<String,BungeniToolbarConditionOperator>();
    
    public static HashMap<String, BungeniToolbarConditionOperator> getObjects(){
        if (conditionOperatorMap.isEmpty()) {
            HashMap<String,BungeniToolbarConditionOperator> toolMap = new HashMap<String,BungeniToolbarConditionOperator>();
            BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
            db.Connect();
            QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_CONDITIONAL_OPERATORS());
            if (qr.hasResults()) {
               Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
               resultRows = qr.theResults();
               for (Vector<String> resultRow: resultRows) {
                   String conditionName = resultRow.elementAt(qr.getColumnIndex("CONDITION_NAME")-1);
                   String conditionSyntax = resultRow.elementAt(qr.getColumnIndex("CONDITION_SYNTAX")-1);
                   String conditionClass = resultRow.elementAt(qr.getColumnIndex("CONDITION_CLASS")-1);
                   toolMap.put(conditionName, new BungeniToolbarConditionOperator(conditionName, conditionSyntax, conditionClass ));
               } 
            }
            db.EndConnect();
            //cache the operator map
            conditionOperatorMap = toolMap;
            return conditionOperatorMap;
        } else {
            //operator map was already cached, return the cached map
            return conditionOperatorMap;
        }
            
    }       
   
   public static void main(String[] args) {
       HashMap<String, BungeniToolbarConditionOperator> map = getObjects();
       java.util.Set<String> ops = map.keySet();
       for (String s : ops) {
          BungeniToolbarConditionOperator m =  map.get(s);
          System.out.println(m.toString());
       }
       String fullConditionValue = "sectionNotExists:root";
        java.util.Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            BungeniToolbarConditionOperator condition = map.get(key);
            if (fullConditionValue.indexOf(condition.getCondition()) != -1) {
                System.out.println("matched condition ");
                //individualConditions = fullConditionValue.split(condition.getCondition());
                return;
            }
        } 
   }
}
