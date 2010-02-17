package org.bungeni.editor.rulesimpl;

//~--- non-JDK imports --------------------------------------------------------

import org.odftoolkit.odfdom.doc.OdfDocument;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

/**
 *
 * @author undesa
 */
public abstract class BaseStructuralRule implements IStructuralRule {
    protected OdfDocument                odfDocument      = null;
    protected String                     ruleName         = null;
    protected StructuralRulesParser      ruleParserEngine = null;
    protected String                     ruleSource       = null;
    protected ArrayList<StructuralError> errorLog         = new ArrayList<StructuralError>(0);

    public BaseStructuralRule() {}

    public String getName() {
        return ruleName;
    }

    public void setName(String sName) {
        ruleName = sName;
    }

    public boolean setupRule(StructuralRulesParser engine, OdfDocument ooDoc) {
        try {
            System.out.println("setupRule 1");
            this.ruleParserEngine = engine;
            System.out.println("setupRule 2");
            this.odfDocument = ooDoc;

            // also clear the error log
            System.out.println("setupRule 3");
            this.errorLog.clear();
            System.out.println("setting up rule for :" + ooDoc.getBaseURI());
        } catch (Exception ex) {
            System.out.println("setupRule exception");
        }

        return true;
    }

    public StructuralError[] getErrors() {
        return errorLog.toArray(new StructuralError[errorLog.size()]);
    }

    public abstract boolean applyRule(String forThisSectionName);

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
    protected StructuralError makeStructuralError(boolean state, String psecType, String csecType, String psecName,
            String csecName, String errType, String errMsg) {
        StructuralError err = new StructuralError();

        err.errorState        = state;
        err.parentSectionType = psecType;
        err.childSectionType  = csecType;
        err.parentSectionName = psecName;
        err.childSectionName  = csecName;
        err.failRuleType      = errType;
        err.errorMessage      = errMsg;

        return err;
    }
}
