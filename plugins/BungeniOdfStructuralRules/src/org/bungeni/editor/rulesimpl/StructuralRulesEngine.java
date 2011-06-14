package org.bungeni.editor.rulesimpl;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.odfdom.section.IBungeniOdfSectionIterator;

import org.dom4j.Element;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author undesa
 */
public class StructuralRulesEngine {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(StructuralRulesEngine.class.getName());
    String[]                   m_runTheseRules  = null;
    RuleEngineParser           ruleEngineParser = null;
    StructuralRulesParser      rulesParser      = null;
    ArrayList<IStructuralRule> rulesToApply     = new ArrayList<IStructuralRule>(0);
    ArrayList<StructuralError> errorLog         = new ArrayList<StructuralError>(0);

    public StructuralRulesEngine(String documentStructureFile, String ruleEngineFile, String[] runTheseRules) {
        rulesParser = new StructuralRulesParser(documentStructureFile);
        rulesParser.loadXml();
        ruleEngineParser = new RuleEngineParser(ruleEngineFile);
        ruleEngineParser.loadXml();
        this.m_runTheseRules = runTheseRules;

        // rule objects loaded into Map
        loadRulesForDocumentType();
    }

    private void loadRulesForDocumentType() {

        // open the rule engine file and load all the engines
        // returns a list of <engine /> elements
        ArrayList<Element> rules = ruleEngineParser.getRules();

        for (Element rule : rules) {
            String engineName   = rule.attributeValue("name");
            String engineSource = rule.attributeValue("source");

            if (checkThisRule(engineName)) {
                IStructuralRule iEngine = null;

                try {
                    iEngine = StructuralRuleFactory.getStructuralRule(engineSource);

                    // set name for rule engine
                    iEngine.setName(engineName);
                } catch (Exception ex) {
                    System.out.println("error loading engines");
                    log.error("loadRulesForDocumentType , during engine instantiation of " + engineName + " : "
                              + ex.getMessage());
                }

                if (iEngine != null) {
                    rulesToApply.add(iEngine);
                }
            }
        }
    }

    private boolean checkThisRule(String engineName) {
        for (String engineRuleName : this.m_runTheseRules) {
            if (engineName.trim().equals(engineRuleName)) {
                return true;
            }
        }

        return false;
    }

    public boolean processRulesForDocument(OdfDocument ooDocument) {
        BungeniOdfSectionHelper sectionhelper = new BungeniOdfSectionHelper(ooDocument);

        sectionhelper.iterateSections(new IterativeRulesForSections());

        return true;
    }

    public boolean processRules(OdfDocument ooDocument, String sectionName) {
        for (IStructuralRule rule : rulesToApply) {
            rule.setupRule(this.rulesParser, ooDocument);
            rule.applyRule(sectionName);

            StructuralError[] errors = rule.getErrors();

            if (errors != null) {
                if (errors.length > 0) {
                    errorLog.addAll(Arrays.asList(errors));
                }
            }
        }

        return true;
    }

    public ArrayList<StructuralError> getErrors() {
        return errorLog;
    }

    public static void main(String[] args) {
        try {
            String docrules    = "/structural_rules/doc_rules/debaterecord.xml";
            String enginerules = "/structural_rules/engine_rules/debaterecord.xml";

            // configure the source files
            String   ruleEnginesFile = enginerules;
            String   docRulesFile    = docrules;
            String[] runTheseRules   = {};

            // initalize the rules engine
            StructuralRulesEngine sre  = new StructuralRulesEngine(docRulesFile, ruleEnginesFile, runTheseRules);
            OdfDocument           odoc =
                OdfDocument.loadDocument(
                    new File(
                        "/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/workspace/files/ke/debaterecord/2009-5-26/eng/ke_debaterecord_2009-5-26_eng.odt"));

            sre.processRulesForDocument(odoc);

            for (StructuralError serr : sre.getErrors()) {
                System.out.print(serr.parentSectionName);
                System.out.print(" , " + serr.errorState);
                System.out.print(" , " + serr.errorMessage);
                System.out.println(" , " + serr.failRuleType);
            }

            // iterate through the document and process the rules for every section
            // processSections(sre);*/
        } catch (Exception ex) {}

        // iterate through the document and process the rules for every section
        // processSections(sre);*/
    }

    class IterativeRulesForSections implements IBungeniOdfSectionIterator {
        public boolean nextSection(BungeniOdfSectionHelper helper, TextSectionElement nSection) {
            try {
                System.out.println("iterating rules for section : " + nSection.getTextNameAttribute());
                System.out.println("rules to apply = " + rulesToApply.size());

                for (IStructuralRule iStructuralRule : rulesToApply) {
                    OdfDocument oddoc = helper.getOdfDocument();

                    System.out.println("iterating rules for " + iStructuralRule.getName());

                    if (iStructuralRule.setupRule(rulesParser, helper.getOdfDocument())) {
                        System.out.println("finished setup of rule");
                        iStructuralRule.applyRule(nSection.getTextNameAttribute());
                    }

                    System.out.println("after setup of rule");

                    StructuralError[] errors = iStructuralRule.getErrors();

                    if (errors != null) {
                        if (errors.length > 0) {
                            errorLog.addAll(Arrays.asList(errors));
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Exception : " + ex.getMessage());
            }

            return true;
        }
    }
}
