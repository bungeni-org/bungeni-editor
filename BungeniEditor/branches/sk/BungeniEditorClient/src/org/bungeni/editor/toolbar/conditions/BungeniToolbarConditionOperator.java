/*
 * BungeniToolbarConditionOperator.java
 *
 * Created on January 26, 2008, 4:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions;

/**
 *
 * @author Administrator
 */
public class BungeniToolbarConditionOperator {
    private String conditionName;
    private String condition;
    private String conditionProcessorClass;
    /** Creates a new instance of BungeniToolbarConditionOperator */
    public BungeniToolbarConditionOperator(){
        conditionName = "";
        condition = "";
        conditionProcessorClass="";
    }
    public BungeniToolbarConditionOperator(String conditionName, String condition, String conditionProc ) {
        this.setConditionName(conditionName);
        this.setCondition(condition);
        this.setConditionProcessorClass(conditionProc);
    }

    /**
     * Get condition " and ", " or "...
     */
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition.trim();
    }

    /**
     * Get the class to handle the operator condition
     */
    public String getConditionProcessorClass() {
        return conditionProcessorClass;
    }

    public void setConditionProcessorClass(String conditionProcessorClass) {
        this.conditionProcessorClass = conditionProcessorClass.trim();
    }

    /**
     * get descriptive name of condition
     */
    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName.trim();
    }
    
}
