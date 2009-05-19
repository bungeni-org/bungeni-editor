/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.io.File;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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
            SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
            xmlDocument = builder.build(xmlfile);
        } catch (JDOMException ex) {
            log.error("loadXml  : "+ex.getMessage());
        } catch (IOException ex) {
            log.error("loadXml  : "+ex.getMessage());
        }
      }
}
