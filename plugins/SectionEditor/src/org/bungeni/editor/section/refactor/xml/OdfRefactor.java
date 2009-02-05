/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.openoffice.odf.pkg.OdfPackage;

/**
 *
 * @author Ashok Hariharan
 */
public class OdfRefactor {
    /**
     * package object used to browse ODF file
     */
    OdfPackage odfPackage;
    /**
     * JDom document to browse content.xml pack
     */
    org.jdom.Document xmlDocument ;
    org.w3c.dom.Document domDocument;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdfPackage.class.getName());

    private final static String ODF_CONTENT_FILE="content.xml";
    /**
     * Sets up the OdfRefactor object
     * @param odfFile - Path to ODF file
     */
    public OdfRefactor(String odfFile) {
        try {
            odfPackage = OdfPackage.loadPackage(odfFile);
            //Set<String> setOfFileEntries = odfPackage.getFileEntries();
            //OdfFileEntry fileEntry  = odfPackage.getFileEntry(ODF_CONTENT_FILE);
            InputStream is = odfPackage.getInputStream(ODF_CONTENT_FILE);
            SAXBuilder saxBuilder = new SAXBuilder();
            xmlDocument = saxBuilder.build(is);
            DOMOutputter converter = new DOMOutputter();
            domDocument = converter.output(xmlDocument);
        } catch (Exception ex) {
          log.error("OdfRefactor() = " + odfFile);
        }
    }




    /*
    private Node findSectionByName(String sectionName) {
        Node sourceSectionNode = null;
        NodeList sectionsList = domDocument.getElementsByTagName(ODF_SECTION_TAG);
        for (int i=0 ; i < sectionsList.getLength(); i++) {
            Node sectionNode = sectionsList.item(i);
            NamedNodeMap sectionAttribs = sectionNode.getAttributes();
            Node nameAttrib = sectionAttribs.getNamedItem(ODF_SECTION_NAME_ATTRIBUTE);
            String foundSectionName = nameAttrib.getNodeValue();
            if (foundSectionName.equals(sectionName)) {
                sourceSectionNode = sectionNode;
                break;
            }
        }
        return sourceSectionNode;
    }
    */


    private Element getSectionByName(String lookForThisSection) {
        Element matchingElement = null;
        try {

            ElementAttributeFilter eaf = new ElementAttributeFilter(SECTION_ELEMENT_WITHOUT_NS);
            eaf.filterBy(SECTION_NAME_ATTR_WITHOUT_NS, lookForThisSection );
            Iterator  sectionIter = xmlDocument.getRootElement().getDescendants(eaf) ;
            while (sectionIter.hasNext()) {
                matchingElement = (Element) sectionIter.next();
                log.info("getSectionByName, found section " + lookForThisSection );
                break;
            }
        } catch (Exception ex) {
            log.error("getSectionByName : " + ex.getMessage());
        } finally {
            return matchingElement;
        }
     }


    private static String SECTION_ELEMENT = "text:section";
    private static String SECTION_NAME_ATTR = "text:name";
    private static final String SECTION_ELEMENT_WITHOUT_NS = "section";
    private static final String SECTION_NAME_ATTR_WITHOUT_NS = "name";

    /**
     * Refactors a source section and places it before the target section (the parent element remaining the same)
     * @param sourceSection
     * @param targetSectionBefore
     * @return
     */
    public boolean moveSectionBefore(String sourceSection, String targetSectionBefore) throws Exception {
        boolean bState = false;
        try {
            //get the source element
            Element elemSourceSection = this.getSectionByName(sourceSection);
            if (elemSourceSection == null ) {
                throw new Exception("Section sourceSection : " + sourceSection + " was not found");
            }
            //get the target element
            Element elemTargetSection = this.getSectionByName(targetSectionBefore);
            if (elemTargetSection == null ) {
                throw new Exception("Section targetSectionNode : " + targetSectionBefore + " was not found");
            }

            //detach the source elements so that it can be refactored.
            elemSourceSection.detach();

            //Now refactor the source into the target element's parent
            //getParent().getContent(), returns a "live" list
            List contentList = elemTargetSection.getParent().getContent();
            //iterate the live list 
            for (int i=0; i < contentList.size(); i++) {
                Object elemObject = contentList.get(i);
                //check if the object found is an "Element"
                if (elemObject.getClass().getName().equals(Element.class.getName())) {
                    Element contentElem = (Element) elemObject;
                    if (contentElem.getName().equals(SECTION_ELEMENT_WITHOUT_NS)) {
                        Attribute nameAttr = contentElem.getAttribute(SECTION_NAME_ATTR_WITHOUT_NS, contentElem.getNamespace());
                        if (nameAttr != null) {
                            if (nameAttr.getValue().equals(targetSectionBefore)) {
                                contentList.add(i, elemSourceSection);
                                System.out.println("preamble found");
                                break;
                            }
                        }
                     }
               }
             }

            /*
            DOMOutputter converter = new DOMOutputter();
            org.w3c.dom.Document domDoc = converter.output(xmlDocument);
            //get source section node
            Node sourceSectionNode = null;
            sourceSectionNode = findSectionByName(sourceSection);
            if (sourceSectionNode == null ) {
                throw new Exception("Section sourceSection : " + sourceSection + " was not found");
            }
            //get target section node
            Node targetSectionNode = null;
            targetSectionNode = findSectionByName(targetSectionBefore);
            if (targetSectionNode == null ) {
                throw new Exception("Section targetSectionNode : " + targetSectionBefore + " was not found");
            }
            Node parentOfTargetNode = targetSectionNode.getParentNode();
            Node insertedNode = parentOfTargetNode.insertBefore(sourceSectionNode, targetSectionNode);
            //display the refactored xml
            Source source = new DOMSource(domDoc);
            StringWriter swriter = new StringWriter();
            Result result = new StreamResult(swriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            System.out.println(swriter.getBuffer().toString());
             */

            bState = true;
        } catch (Exception ex) {
            log.error("moveSectionBefore : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    public void getXmlString(){
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(xmlDocument, System.out);
        } catch (IOException ex) {
            Logger.getLogger(OdfRefactor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public boolean moveSectionAfter(String sourceSection, String targetSectionAfter) {
        return true;
    }

    public static void main(String[] args) {
        try {
            OdfRefactor ref = new OdfRefactor("/Users/ashok/NetbeansProjects/SectionRefactorPlugin/testdocs/ken_bill_2009_1_10_eng_main.odt");
            ref.moveSectionBefore("part1", "preamble");
            //ref.getXmlString();
        } catch (Exception ex) {
            Logger.getLogger(OdfRefactor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
