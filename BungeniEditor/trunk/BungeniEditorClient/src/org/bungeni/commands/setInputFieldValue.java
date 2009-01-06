/*
 * setInputFieldValue.java
 *
 * Created on December 20, 2007, 9:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;
import org.bungeni.editor.selectors.IBungeniForm;
import org.bungeni.commands.CommonActions;
import org.bungeni.ooo.ooDocFieldSet;

/**
 *
 * @author Administrator
 */
public class setInputFieldValue implements Command {
    
    /**
     * Creates a new instance of setInputFieldValue
     * Requires: ooDocFieldSet object to be set in the BungeniFormContext from the caller class
     */

     public boolean execute(Context context) throws Exception {
        BungeniFormContext formContext = (BungeniFormContext) context;
        //IBungeniForm iForm = formContext.getBungeniForm();
        ooDocFieldSet fieldSet = (ooDocFieldSet) formContext.popObjectFromFieldSet("document_field_set");
        boolean bRet = CommonActions.action_setInputFieldValue(formContext.getOoDocument(), fieldSet.getFieldName(), 
                fieldSet.getFieldValue(), fieldSet.getFieldContainer());
        //formContext.getFieldSets("document_field_set").remove(fieldSet);
        
        return false;
     }
}