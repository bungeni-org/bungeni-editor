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
}
