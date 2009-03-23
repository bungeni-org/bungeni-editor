package org.bungeni.editor.rulesimpl;

import java.util.ArrayList;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * Class to parse section structural rules
 * @author Ashok Hariharan
 */
public class RuleEngineParser extends RuleParser {

        

        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RuleEngineParser.class.getName());

        /**
         * Path to rules Xml file
         * @param xmlRules
         */
        public RuleEngineParser(String xmlRules){
            pathToXmlFile = StructuralRulesConfig.getRuleEnginesPath() +  xmlRules;
        }

     
        /**
         * Returns an array of <engine /> elements
         * @return
         */
        public ArrayList<Element> getRules(){
            ArrayList<Element> engineElements = new ArrayList<Element>(0);
            try {
                XPath selectPath = XPath.newInstance("rules/engine");
                engineElements =  (ArrayList<Element>) selectPath.selectNodes(xmlDocument);
            }
            catch (JDOMException ex) {
                log.error("getRules : " + ex.getMessage());
            } finally {
                return engineElements;
            }
        }

}
