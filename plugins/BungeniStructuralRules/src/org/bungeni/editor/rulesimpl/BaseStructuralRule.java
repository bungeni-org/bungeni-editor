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
public abstract class BaseStructuralRule implements IStructuralRule {

    String ruleName;
    String ruleSource;
    StructuralRulesEngine ruleEngine;
    OOComponentHelper ooDocument;


    public String getName() {
        return ruleName;
    }

    public void setName(String sName) {
        ruleName = sName;
    }
   

    public boolean setupRule(StructuralRulesEngine engine, OOComponentHelper ooDoc) {
        this.ruleEngine = engine;
        this.ooDocument = ooDoc;
        return true;
    }

    public abstract boolean applyRule(String forThisSectionName) ;

}
