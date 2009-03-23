/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.util.HashMap;

/**
 *
 * @author undesa
 */
public class StructuralRulesEngine {
    StructuralRulesParser rulesParser = null;
    RuleEngineParser ruleEngineParser = null;
    HashMap<String, IStructuralRule> rulesToApply = new HashMap<String, IStructuralRule>(0);

    public StructuralRulesEngine(StructuralRulesParser pParser){
        rulesParser = pParser;
    }

    public StructuralRulesEngine(String documentStructureFile, String ruleEngineFile) {
        rulesParser = new StructuralRulesParser(documentStructureFile);
        rulesParser.loadXml();
        ruleEngineParser = new RuleEngineParser(ruleEngineFile);
        ruleEngineParser.loadXml();
    }

    private void loadRulesForDocumentType(){
        String docType = rulesParser.getDocStructureType();
        //open the rule engine file and load all the engines
    }

    
    


}
