/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import org.openoffice.odf.doc.OdfDocument;

/**
 *
 * @author undesa
 */
public interface IStructuralRule {
    public String getName();
    public void setName(String sName);
    
    public boolean setupRule(StructuralRulesParser rulesParsingEngine, OdfDocument ooDoc);
    public boolean applyRule(String sectionName);
    public StructuralError[] getErrors();
}
