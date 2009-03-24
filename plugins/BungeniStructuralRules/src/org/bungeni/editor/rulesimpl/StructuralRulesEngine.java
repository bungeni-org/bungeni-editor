/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author undesa
 */
public class StructuralRulesEngine {
    StructuralRulesParser rulesParser = null;
    RuleEngineParser ruleEngineParser = null;
    ArrayList<IStructuralRule> rulesToApply = new ArrayList<IStructuralRule>(0);
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StructuralRulesEngine.class.getName());


    public StructuralRulesEngine(StructuralRulesParser pParser){
        rulesParser = pParser;
    }

    public StructuralRulesEngine(String documentStructureFile, String ruleEngineFile) {
        rulesParser = new StructuralRulesParser(documentStructureFile);
        rulesParser.loadXml();
        ruleEngineParser = new RuleEngineParser(ruleEngineFile);
        ruleEngineParser.loadXml();
        //rule objects loaded into Map
        loadRulesForDocumentType();
    }

    private void loadRulesForDocumentType(){
        //open the rule engine file and load all the engines
        //returns a list of <engine /> elements
        ArrayList<Element> rules = ruleEngineParser.getRules();
        for (Element rule : rules) {
            String engineName = rule.getAttributeValue("name");
            String engineSource = rule.getAttributeValue("source");
            IStructuralRule iEngine = null;
            try {
            iEngine = StructuralRuleFactory.getStructuralRule(engineSource);
            } catch (Exception ex) {
                log.error("loadRulesForDocumentType , during engine instantiation of "+ 
                        engineName +  " : " + ex.getMessage());
            }
            if (iEngine != null) {
                rulesToApply.add(iEngine);
            }
        }
    }

    
    


}
