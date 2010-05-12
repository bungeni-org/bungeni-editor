/*
 * replaceTextWithField.java
 *
 * Created on February 4, 2008, 12:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;
import org.bungeni.ooo.ooDocFieldSet;

/**
 *
 * @author Administrator
 */
public class insertSectionAfterSection implements Command {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(insertSectionAfterSection.class.getName());
 
    /** Creates a new instance of replaceTextWithField */
    public insertSectionAfterSection() {
    }

    public boolean execute(Context context) throws Exception {
        boolean bRet = false;
        try {
            BungeniFormContext formContext = (BungeniFormContext) context;
            log.debug("executing command : insertSectionAfterSection");
            String selSectionActionCommand = "";
            if (formContext.hasFieldSet("selected_section_action_command")) {
                selSectionActionCommand = (String) formContext.popObjectFromFieldSet("selected_section_action_command");
            }

            if (selSectionActionCommand.length() > 0 ) { // then we need to check execution condition
                if (selSectionActionCommand.equals("AFTER_SECTION")) {        
                         bRet = CommonActions.action_insertSectionAfterSection(formContext.getOoDocument(), 
                                (String) formContext.popObjectFromFieldSet("current_section"), 
                                (String) formContext.popObjectFromFieldSet("target_section") );
                } else {
                    //set bRet to true to simulate a successful api return
                    bRet = true;
                }
            } else {
                         bRet = CommonActions.action_insertSectionAfterSection(formContext.getOoDocument(), 
                                (String) formContext.popObjectFromFieldSet("current_section"), 
                                (String) formContext.popObjectFromFieldSet("target_section") );
            }
        } catch (Exception ex) {
            log.error("insertSectionAfterSection : "+ ex.getMessage());            
            bRet = true;
        } finally {
            return !bRet;
        }
    }
    
}
