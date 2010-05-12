/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.commands;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.generic.LookupCommand;
import org.bungeni.editor.selectors.BungeniFormContext;

/**
 *
 * @author undesa
 */
public abstract class ConditionalLookupCommand extends LookupCommand {
    
    public abstract String getCondition();
    
    @Override
    public boolean execute(Context context) throws Exception{
        
        String condition = getCondition();
        
        BungeniFormContext formContext = (BungeniFormContext) context;
        //if the condition exists.. check the condition
        if (formContext.getConditionSet().conditionalsExist()) {
            String conditionValue = "";
            conditionValue = formContext.getConditionSet().getConditionValue(condition);
            if (conditionValue == null ) {
                return false;
            }
            //if the condition evaluates to true ... dont execute the condition as it has already been executed.
            //if false execute the conditional command
            if (conditionValue.equals("true")) {
                return false;
            } else {
                boolean bReturn = super.execute(context);
                if (!bReturn) formContext.getConditionSet().setConditionSetValue(getCondition(), "true");
                return bReturn;
            }
        } else {
            boolean bCondition = super.execute(context);
            if (!bCondition) formContext.getConditionSet().setConditionSetValue(getCondition(), "true");

            return bCondition;
        }
     //   formContext.getCondition()
    }
}
