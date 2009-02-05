/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.openoffice.odf.pkg.OdfPackage;
import org.openoffice.odf.pkg.manifest.OdfFileEntry;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    /**
     * Sets up the OdfRefactor object
     * @param odfFile - Path to ODF file
     */
    public OdfRefactor(String odfFile) {
        try {
            odfPackage = OdfPackage.loadPackage(odfFile);
            Set<String> setOfFileEntries = odfPackage.getFileEntries();
            OdfFileEntry fileEntry  = odfPackage.getFileEntry("content.xml");
            InputStream is = odfPackage.getInputStream("content.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            xmlDocument = saxBuilder.build(is);
            DOMOutputter converter = new DOMOutputter();
            domDocument = converter.output(xmlDocument);
        } catch (Exception ex) {
          log.error("OdfRefactor() = " + odfFile);
        }
    }

    
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

    private static String ODF_SECTION_TAG = "text:section";
    private static String ODF_SECTION_NAME_ATTRIBUTE = "text:name";
    /**
     * Refactors a source section and places it before the target section (the parent element remaining the same)
     * @param sourceSection
     * @param targetSectionBefore
     * @return
     */
    public boolean moveSectionBefore(String sourceSection, String targetSectionBefore) throws Exception {
        boolean bState = false;
        try {
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
