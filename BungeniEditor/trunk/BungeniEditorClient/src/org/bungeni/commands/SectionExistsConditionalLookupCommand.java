/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.commands;

/**
 *
 * @author undesa
 */
public class SectionExistsConditionalLookupCommand extends ConditionalLookupCommand {
    public String getCondition(){
        return "section_exists";
    }
}
