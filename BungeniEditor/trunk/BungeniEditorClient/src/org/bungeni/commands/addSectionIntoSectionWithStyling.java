/*
 * addSectionIntoSectionWithStyling.java
 *
 * Created on December 20, 2007, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;


import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;
import org.bungeni.commands.CommonActions;
/**
 *
 * @author Administrator
 */
public class addSectionIntoSectionWithStyling implements Command {
    
    /**
     * Creates a new instance of addSectionIntoSectionWithStyling
     * Requires: the following variables to be set in the preInsertMap prior to invocation from the caller class:
     * container_section, current_section, section_back_color, section_left_margin
     */
 
    public boolean execute(Context context) throws Exception {
        BungeniFormContext formContext = (BungeniFormContext) context;
        //IBungeniForm iForm = formContext.getBungeniForm();
  
        //"container_section", "current_section", "section_back_color", "section_left_margin"
        String parentSection = (String)formContext.popObjectFromFieldSet("container_section"); //formContext.getPreInsertMap().get("container_section");
        String currentSection = (String)formContext.popObjectFromFieldSet("current_section"); //getPreInsertMap().get("current_section");
        String section_back_color = (String)formContext.popObjectFromFieldSet("section_back_color");//getPreInsertMap().get("section_back_color");
        String section_left_margin = (String)formContext.popObjectFromFieldSet("section_left_margin"); //getPreInsertMap().get("section_left_margin");
        //section_back_color = 
        long sectionBackColor = Long.parseLong(section_back_color, 16);
        float sectionLeftMargin = Float.parseFloat(section_left_margin);
        
        boolean bState = CommonActions.action_addSectionIntoSectionwithStyling(formContext.getOoDocument(),
                                                                        parentSection,
                                                                        currentSection,
                                                                        sectionBackColor, 
                                                                        sectionLeftMargin);
            
            if (bState == false ) {
                return false;
            } 
   
        return false;
    }
}
