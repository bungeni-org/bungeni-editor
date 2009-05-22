/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * base class for Rule Parser
 * @author Ashok Hariharan
 */
public class RuleParser {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RuleParser.class.getName());


        Document xmlDocument ;
        String pathToXmlFile ;

        public RuleParser(){

        }

        /**
         *Loads the rules Xml file and instantiates the JDom document
         */
        public void loadXml() {
        try {
            File xmlfile = new File(pathToXmlFile);
            SAXReader builder = new SAXReader();
            xmlDocument = builder.read(xmlfile);
        } catch (DocumentException ex) {
            log.error("loadXml:" + ex.getMessage());
        }
      }
}
