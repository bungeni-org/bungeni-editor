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
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class addSectionInsideSection implements Command {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(addSectionInsideSection.class.getName());
 
    /** Creates a new instance of replaceTextWithField */
    public addSectionInsideSection() {
    }

    public boolean execute(Context context) throws Exception {
        boolean bRet = false;
        try {
        BungeniFormContext formContext = (BungeniFormContext) context;
        //IBungeniForm iForm = formContext.getBungeniForm();
        //ooDocFieldSet fieldSet = formContext.getFieldSets().get(0);
        log.debug("executing addSectionInsideSection");
        String targetSection = (String) formContext.popObjectFromFieldSet("container_section");
        String currentSection =  (String) formContext.popObjectFromFieldSet("current_section");
        System.out.println(" target = " + targetSection + " current = " + currentSection);
        String selSectionActionCommand = "";
        if (formContext.hasFieldSet("selected_section_action_command")) {
            selSectionActionCommand = (String) formContext.popObjectFromFieldSet("selected_section_action_command");
        }                                                                         

        if (selSectionActionCommand.length() > 0 ) { // then we need to check execution condition
            if (selSectionActionCommand.equals("INSIDE_SECTION")) {
                bRet = CommonActions.action_addSectionInsideSection(formContext.getOoDocument(), 
                        targetSection, 
                        currentSection );
            } else {
                //bRet = true to simulate api success return
                bRet = true;
            }
        } else {
                bRet = CommonActions.action_addSectionInsideSection(formContext.getOoDocument(), 
                        targetSection, 
                        currentSection );
        }
        
        } catch (Exception ex) {
            log.error("Command: addSectionInsideSection : "+ ex.getMessage());
            log.error("Command: addSectionInsideSection : "+ CommonExceptionUtils.getStackTrace(ex));
            bRet = true;
        } finally {
            System.out.println("returning bRet = " + !bRet);
            return !bRet;
        }
    }
    
}
