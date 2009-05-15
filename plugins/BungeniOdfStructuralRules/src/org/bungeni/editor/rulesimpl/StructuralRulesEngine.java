/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.util.ArrayList;
import java.util.Arrays;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.odfdom.section.IBungeniOdfSectionIterator;
import org.jdom.Element;
import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.element.text.OdfSection;

/**
 *
 * @author undesa
 */
public class StructuralRulesEngine {
    StructuralRulesParser rulesParser = null;
    RuleEngineParser ruleEngineParser = null;
    ArrayList<IStructuralRule> rulesToApply = new ArrayList<IStructuralRule>(0);
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StructuralRulesEngine.class.getName());
    ArrayList<StructuralError> errorLog = new ArrayList<StructuralError>(0);


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
            //set name for rule engine
            iEngine.setName(engineName);
            } catch (Exception ex) {
                log.error("loadRulesForDocumentType , during engine instantiation of "+ 
                        engineName +  " : " + ex.getMessage());
            }
            if (iEngine != null) {
                rulesToApply.add(iEngine);
            }
        }
    }

    class IterativeRulesForSections implements IBungeniOdfSectionIterator {
        public boolean nextSection(BungeniOdfSectionHelper helper, OdfSection nSection) {
            for (IStructuralRule iStructuralRule : rulesToApply) {
                iStructuralRule.setupRule(rulesParser, helper.getDocument());
                iStructuralRule.applyRule(nSection.getName());
                StructuralError[] errors = iStructuralRule.getErrors();
                  if (errors != null ) {
                      if (errors.length > 0 ){
                          errorLog.addAll(Arrays.asList(errors));
                      }
                  }
            }
            return true;
        }
    }

    public boolean processRulesForDocument(OdfDocument ooDocument, String rootSection) {
        BungeniOdfSectionHelper sectionhelper = new BungeniOdfSectionHelper(ooDocument);
        sectionhelper.iterateSections(new IterativeRulesForSections());
        return true;
    }

    public boolean processRules(OdfDocument ooDocument, String sectionName) {
        for (IStructuralRule rule : rulesToApply) {
          rule.setupRule(this.rulesParser, ooDocument);
          rule.applyRule(sectionName);
          StructuralError[] errors = rule.getErrors();
          if (errors != null ) {
              if (errors.length > 0 ){
                  errorLog.addAll(Arrays.asList(errors));
              }
          }
        }
        return true;
    }

    public ArrayList<StructuralError> getErrors(){
        return errorLog;
    }

    public static void main(String[] args) {
       String docrules = "/home/undesa/Projects/Bungeni/BungeniOdfStructuralRules/structural_rules/doc_rules/debaterecord.xml";
       String enginerules = "/home/undesa/Projects/Bungeni/BungeniOdfStructuralRules/structural_rules/engine_rules/debaterecord.xml";
        //configure the source files
        String ruleEnginesFile = enginerules;
        String docRulesFile  =  docrules;
        //initalize the rules engine
        StructuralRulesEngine sre = new
                StructuralRulesEngine(docRulesFile,
                                        ruleEnginesFile);
        //iterate through the document and process the rules for every section
        //processSections(sre);*/
    }



}
