/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.bungeni.editor.section.refactor.changelog.ChangeLog;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.openoffice.odf.pkg.OdfPackage;
import org.openoffice.odf.pkg.manifest.OdfFileEntry;

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
     * path to open odt file
     */
    String fullPathToOdfFile ;
    /**
     * 
     */
    OdfFileEntry packageFileEntry ;
    /**
     * JDom document to browse content.xml pack
     */
    org.jdom.Document xmlDocument ;
   // org.w3c.dom.Document domDocument;
    /**
     * backup creator class
     */
    OdfPackageBackup packageBackup ;

    private String commitLog = "";
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdfPackage.class.getName());

    private final static String ODF_CONTENT_FILE="content.xml";
    /**
     * Sets up the OdfRefactor object
     * @param odfFile - Path to ODF file
     */
    public OdfRefactor(String odfFile) {
        try {
            //get full path to file
            fullPathToOdfFile = odfFile;
            //load the odf package
            odfPackage = OdfPackage.loadPackage(odfFile);
            //create the package backup object
            packageBackup = new OdfPackageBackup(odfPackage);
            //get an inputstream into content.xml
            InputStream is = odfPackage.getInputStream(ODF_CONTENT_FILE);
            //get the file entry object
            packageFileEntry = odfPackage.getFileEntry(ODF_CONTENT_FILE);
            //get the JDom dom of the odf document
            SAXBuilder saxBuilder = new SAXBuilder();
            xmlDocument = saxBuilder.build(is);
        } catch (Exception ex) {
          log.error("OdfRefactor() = " + odfFile);
        }
    }

    public void updateCommitLog(String commitLog) {
       this.commitLog = commitLog;
    }

        

        
    /**
     * Wrapper function to generate incremental backups of document
     * @return
     */
    private boolean backupPackage(String sourceSection, String targetSection, int moveBeforeOrAfter){
       //pass change log info to backup
        packageBackup.updateChangeInfo(sourceSection, 
               targetSection, 
               ((moveBeforeOrAfter == MOVE_BEFORE)?ChangeLog.CHANGE_ACTION_MOVE_BEFORE:ChangeLog.CHANGE_ACTION_MOVE_AFTER), commitLog);       
       //generate backup
       File fbackupFile =  packageBackup.generateBackup();
       if (fbackupFile == null) {
            return false;
       } else
           return true;
    }

    /**
     * use for generating debug ooutput of xml
     * @param fileName
     */
    private void debugOuput(String fileName) {
        FileOutputStream fs = null;
        try {
            XMLOutputter xo = new XMLOutputter();
            fs = new FileOutputStream(fileName);
            xo.output(xmlDocument, fs);
            fs.close();
        } catch (Exception ex) {
            Logger.getLogger(OdfRefactor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }



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

    private final int MOVE_BEFORE = 0;
    private final int MOVE_AFTER = 1;

    private boolean moveSection(String sourceSection, String targetSection, int moveBeforeOrAfter) {
        boolean bState = false;
        //make a backup package first
        if (!backupPackage(sourceSection, targetSection, moveBeforeOrAfter)) {
            log.error("moveSectionBefore : backup of package failed");
        }
        try {
            //get the source element
            Element elemSourceSection = this.getSectionByName(sourceSection);
            if (elemSourceSection == null ) {
                throw new Exception("Section sourceSection : " + sourceSection + " was not found");
            }
            //get the target element
            Element elemTargetSection = this.getSectionByName(targetSection);
            if (elemTargetSection == null ) {
                throw new Exception("Section targetSectionNode : " + targetSection + " was not found");
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
                            if (nameAttr.getValue().equals(targetSection)) {
                                if (MOVE_BEFORE == moveBeforeOrAfter)
                                    contentList.add(i, elemSourceSection);
                                else if (MOVE_AFTER == moveBeforeOrAfter) {
                                    contentList.add(i+1, elemSourceSection);
                                }

                                break;
                            }
                        }
                     }
               }
             }

            bState = true;
        } catch (Exception ex) {
            log.error("moveSection : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    /**
     * Refactors a source section and places it before the target section (the parent element remaining the same)
     * @param sourceSection
     * @param targetSectionBefore
     * @return
     */
    public boolean moveSectionBefore(String sourceSection, String targetSectionBefore) throws Exception {
        boolean bState = false;
        bState = moveSection(sourceSection, targetSectionBefore, MOVE_BEFORE) ;
        return bState;
    }

    /**
     * Refactors a source section and places it before the target section (the parent element remaining the same)
     * @param sourceSection
     * @param targetSectionBefore
     * @return
     */
    public boolean moveSectionAfter(String sourceSection, String targetSectionAfter) throws Exception {
        boolean bState = false;
        bState = moveSection(sourceSection, targetSectionAfter, MOVE_AFTER) ;
        return bState;
    }
    
    public Document getXmlDocument(){
        return this.xmlDocument;
    }

    public boolean saveDocument(){
        boolean bstate = false;
        try {
            String packMediaType = packageFileEntry.getMediaType();
            //we transform the dom document to a byte array and then pass it into odfdom to repackage the content xml
            //we could have passed the w3c dom document directly into odfdom, but transforming to bytes removes version
            //dependency on xerces
            DOMOutputter output = new DOMOutputter();
            // here we have a DOM-document:
            org.w3c.dom.Document dom = output.output(xmlDocument);
            Source source = new DOMSource(dom);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            byte[] xmlBytes = out.toByteArray();
            odfPackage.remove(ODF_CONTENT_FILE);
            odfPackage.insert(xmlBytes, "content.xml", packMediaType);
            odfPackage.save(this.fullPathToOdfFile);
            bstate = true;
        } catch (Exception ex) {
            log.error("saveDocument : " + ex.getMessage());
        } finally {
            return bstate;
        }
    }


 

    public static void main(String[] args) {
        try {
            OdfRefactor ref = new OdfRefactor("/Users/ashok/NetbeansProjects/SectionRefactorPlugin/testdocs/ken_bill_2009_1_10_eng_main.odt");
            ref.moveSectionBefore("preamble", "part1");
            ref.saveDocument();
            //ref.getXmlString();
        } catch (Exception ex) {
            Logger.getLogger(OdfRefactor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
