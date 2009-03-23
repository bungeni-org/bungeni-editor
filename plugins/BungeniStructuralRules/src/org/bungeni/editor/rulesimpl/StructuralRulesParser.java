/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * Class to parse section structural rules
 * @author Ashok Hariharan
 */
public class StructuralRulesParser {
        Document xmlDocument ;
        String pathToXmlFile ;
        

        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StructuralRulesParser.class.getName());

        /**
         * Path to rules Xml file
         * @param xmlRules
         */
        public StructuralRulesParser(String xmlRules){
            pathToXmlFile = StructuralRulesConfig.DOCSTRUCTURE_RULES_PATH_PREFIX +  xmlRules;
        }

        private static final  String GET_DOC_STRUCTURE_TYPE = "/DocumentStructure/@type";

        private final static String GET_SECTION_TYPE_NAME = "/DocumentStructure/sectionType[@name='{thisSectionType}']";

        private final static String GET_CHILD_REF_TYPES = "allowedTypes/ref";

        private final static String GET_CHILD_SECTION_TYPES = "allowedTypes/ref[@type='sectionType']";

        private final static String GET_CHILD_SECTION_TYPES_BY_NAME = "allowedTypes/ref[@type='sectionType' and @name='{refSectionType}']";

        

        /**
         * Get the structure type for the document
         * @return
         */
        public String getDocStructureType(){
        String typeValue = "";
        try {
            XPath selectPath = XPath.newInstance(GET_DOC_STRUCTURE_TYPE);
            Object foundNode =  selectPath.selectSingleNode(xmlDocument);
            typeValue = ((Attribute) foundNode).getValue();
        } catch (Exception ex) {
            log.error("getDocStructureType :" + ex.getMessage());
        } finally {
            return typeValue;
        }
        }

       

        /**
         * Gets a section type element from the document;
         * @param sectionTypeName
         * @return an Element object matching the <sectionType /> element
         */
        public Element getSectionType (String sectionTypeName) {
            Element elemSecType = null;
           try {
                String xpathSectionType = GET_SECTION_TYPE_NAME.replaceAll( "\\{thisSectionType\\}", sectionTypeName);
                System.out.println("this = " + xpathSectionType);
                XPath selectPath = XPath.newInstance(xpathSectionType);
                elemSecType = (Element) selectPath.selectSingleNode(this.xmlDocument);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                return elemSecType;
            }
        }

        /**
         * Returns all allowed types for a section type, returns <ref /> elements
         * @param sectionTypeName
         * @return
         */
        public ArrayList<Element> getAllowedTypesForSectionType(String sectionTypeName) {
            ArrayList<Element> matchingNodes = new ArrayList<Element>(0);
            try {
                //first get the section type element
                Element thisSectionType = getSectionType(sectionTypeName);
                XPath selectPath = XPath.newInstance(GET_CHILD_REF_TYPES);
                matchingNodes = (ArrayList<Element>) selectPath.selectNodes(thisSectionType);

            } catch (JDOMException ex) {
                System.out.println("getAllowedSectionTypeForSectionType :" + ex.getMessage());
            } finally {
                return matchingNodes;
            }
        }

        /**
         * Get the allowed section types for a parent section type
         * @param sectionTypeName
         * @return an ArrayList of <sectionType /> elements
         */
        public ArrayList<Element> getAllowedSectionTypesForType (String sectionTypeName ) {
        ArrayList<Element> foundTypes = new ArrayList<Element>(0);
             try {
                Element foundSectionType = this.getSectionType(sectionTypeName);
                XPath selectPath = XPath.newInstance(GET_CHILD_SECTION_TYPES);
                ArrayList<Element> foundSectionTypes = (ArrayList<Element>) selectPath.selectNodes(foundSectionType);
                for (Element elemrefSecType : foundSectionTypes) {
                    String nameOfAllowedSectionType = elemrefSecType.getAttributeValue("name");
                    Element secType =  this.getSectionType(nameOfAllowedSectionType);
                    if (secType != null) {
                        foundTypes.add(secType);
                    } else {
                        log.error("getAllowedSectionTypes : " + sectionTypeName + " has a reference to allowed section-type : " +
                                nameOfAllowedSectionType +  " which does not exist");
                    }
                }

            } catch (Exception ex) {
                log.error("getAllowedSectionTypes : "  + ex.getMessage());
            } finally {
                return foundTypes;
            }
        }

         public boolean isAllowedSectionTypeForType (String sectionTypeName, String lookForThisType ) {
             Element matchingChildSectionType = null;
             try {
                //Element foundSectionType = this.getSectionType(sectionTypeName);
                String xpathGetparentSection = GET_SECTION_TYPE_NAME.replaceAll("\\{thisSectionType\\}", sectionTypeName);
                String xpathGetChildSectionTypes = GET_CHILD_SECTION_TYPES_BY_NAME.replaceAll( "\\{refSectionType\\}", lookForThisType);
                XPath selectPath = XPath.newInstance(xpathGetparentSection + "/" + xpathGetChildSectionTypes);
                matchingChildSectionType = (Element) selectPath.selectSingleNode(xmlDocument);
            } catch (Exception ex) {
                log.error("isAllowedSectionTypeForType : "  + ex.getMessage());
            } finally {
                return (matchingChildSectionType == null) ? false : true;
            }
        }

        /**
         *Loads the rules Xml file and instantiates the JDom document
         */
        public void loadXml() {
        try {
            File xmlfile = new File(pathToXmlFile);
            SAXBuilder builder = new SAXBuilder();
            xmlDocument = builder.build(xmlfile);
        } catch (JDOMException ex) {
            log.error("loadXml  : "+ex.getMessage());
        } catch (IOException ex) {
            log.error("loadXml  : "+ex.getMessage());
        }
      }

        public static void main(String[] args){
            StructuralRulesParser srp = new
                    StructuralRulesParser("/home/undesa/Projects/Bungeni/BungeniStructuralRules/editorDocRules.xml");
            srp.loadXml();
            System.out.println(srp.getDocStructureType());
            Element pref = srp.getSectionType("Preface");
            System.out.println("found == " + srp.isAllowedSectionTypeForType("XXClause", "XXPart"));
        }
}
