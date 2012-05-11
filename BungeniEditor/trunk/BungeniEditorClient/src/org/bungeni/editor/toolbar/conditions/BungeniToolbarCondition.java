/*
 * BungeniToolbarCondition.java
 *
 * Created on January 26, 2008, 10:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions;

import java.util.HashMap;
import org.bungeni.editor.config.ConditionsReader;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Administrator
 */
public class BungeniToolbarCondition {
    private String conditionName;
    private String conditionValue;
    private String conditionClass;
    private boolean negationCondition = false;
    private static HashMap<String,String> conditionNameforClassMap = new HashMap<String,String>();
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniToolbarCondition.class.getName());
 

    /** Creates a new instance of BungeniToolbarCondition */
    public BungeniToolbarCondition(String fullCondition) {
        if (fullCondition.indexOf(":") != -1){
            String[] full = fullCondition.trim().split("[:]");
            this.conditionName = extractConditionName(full[0].trim());
            this.conditionValue = full[1].trim();
            try {
                setConditionClass(getConditionClassFromName(conditionName));
            } catch (Exception ex) {
                log.error("BungeniToolbarCondition constructor: possibly the toolbar condition has not been setup for this document type (" +fullCondition + " )"  + ex.getMessage() );
            }
        }
        
    }
     
    private final static String NEGATION_OPERATOR = "!";
    
    private String extractConditionName(String condition ) {
        //look for prefix operators 
        if (condition.startsWith(NEGATION_OPERATOR)) {
            negationCondition = true;
            condition  = condition.substring(1).trim();
        } 
        return condition;
    }
     
   private String getConditionClassFromName(String conditionName) {
        if (conditionNameforClassMap.containsKey(conditionName)) {
            return conditionNameforClassMap.get(conditionName);
        } else {
            Element conditionElem = null;
            try {
                conditionElem = ConditionsReader.getInstance().getConditionByName(BungeniEditorPropertiesHelper.getCurrentDocType(), conditionName);
            } catch (JDOMException ex) {
                log.error("Error while loading condition !", ex);
            }
            if (null != conditionElem) {
                conditionNameforClassMap.put(conditionName, conditionElem.getAttributeValue("class"));
            }
            return conditionNameforClassMap.get(conditionName);
        }
   } 
    public BungeniToolbarCondition(String name, String value ) {
        this.setConditionName(name);
        this.setConditionValue(value);
    }

    public String getConditionName() {
        return conditionName;
    }

    public final void setConditionName(String conditionName) {
        this.conditionName = conditionName.trim();
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public final void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue.trim();
    }

    public String getConditionClass() {
        return conditionClass;
    }
    
    public boolean hasNegationCondition(){
        return this.negationCondition;
    }

    public void setConditionClass(String conditionClass) {
        this.conditionClass = conditionClass.trim();
    }
    
 
}
