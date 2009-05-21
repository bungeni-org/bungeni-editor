/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

/**
 *
 * @author undesa
 */
public class StructuralError {
    public String parentSectionType;
    public String childSectionType;
    public String parentSectionName;
    public String childSectionName;
    public String failRuleType;
    public boolean errorState;
    public boolean errorChecked;
    public String errorMessage;

    public StructuralError(){
        //default to fail
        errorState = false;
        errorChecked = false;
    }

    @Override
    public String toString(){
            return "\n{ parentSectionType : " + parentSectionType + "\n" +
                    "childSectionType : " + childSectionType + "\n" +
                    "parentSectionName : " + parentSectionName + "\n"+
                    "childSectionName : " + childSectionName + "\n" +
                    "failRuleType : " + failRuleType + "\n" +
                    "errorMessage : " + errorMessage +" } ";

    }
}
