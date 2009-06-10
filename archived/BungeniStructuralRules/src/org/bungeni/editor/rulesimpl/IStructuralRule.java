/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public interface IStructuralRule {
    public String getName();
    public void setName(String sName);
    
    public boolean setupRule(StructuralRulesParser rulesParsingEngine, OOComponentHelper ooDoc);
    public boolean applyRule(String sectionName);
    public StructuralError[] getErrors();
}
