/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.util.ArrayList;
import org.openoffice.odf.doc.OdfDocument;

/**
 *
 * @author undesa
 */
public abstract class BaseStructuralRule implements IStructuralRule {

    protected String ruleName;
    protected String ruleSource;
    protected StructuralRulesParser ruleParserEngine;
    protected OdfDocument odfDocument;
    protected ArrayList<StructuralError> errorLog = new ArrayList<StructuralError>(0);

    public String getName() {
        return ruleName;
    }

    public void setName(String sName) {
        ruleName = sName;
    }
   

    public boolean setupRule(StructuralRulesParser engine, OdfDocument ooDoc) {
        this.ruleParserEngine = engine;
        this.odfDocument = ooDoc;
        //also clear the error log
        this.errorLog.clear();
        return true;
    }

    public StructuralError[] getErrors() {
        return errorLog.toArray(new StructuralError[errorLog.size()]);
    }

    public abstract boolean applyRule(String forThisSectionName) ;

    /**
     * Creates a structural error object to log error messages from rule engine
     * @param state - state of the error, success or failue (true / false)
     * @param psecType - parent section type
     * @param csecType - child section type
     * @param psecName - parent section name
     * @param csecName - child section name
     * @param errType - error type
     * @return
     */
    protected StructuralError makeStructuralError(boolean state, String psecType,
            String csecType, String psecName, String csecName, String errType ) {
        StructuralError err = new StructuralError();
        err.errorState = state;
        err.parentSectionType = psecType;
        err.childSectionType = csecType;
        err.parentSectionName = psecName;
        err.childSectionName  = csecName;
        err.failRuleType = errType;
        return err;

    }
}
