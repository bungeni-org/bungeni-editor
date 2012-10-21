package org.bungeni.editor.panels.toolbar;

//~--- non-JDK imports --------------------------------------------------------

import java.io.UnsupportedEncodingException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import org.bungeni.editor.config.ToolbarActionsReader;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.utils.CommonEditorXmlUtils;

/**
 * Reads the toolbar xml config file for the document type
 * and loads them as tabbed panels
 * @author Ashok Hariharan
 */
public class BungeniToolbarParser {

    private static org.apache.log4j.Logger log      =
        org.apache.log4j.Logger.getLogger(BungeniToolbarParser.class.getName());

    private static BungeniToolbarParser instance = null ; 

    private Document       document = null;


    private String               TOOLBAR_ROOT = "/toolbar/root";
    /**
     * actionGroups provide top level place holders (parent grouping of tabs)
     */
    private String                TOOLBAR_GROUPS = "/toolbar/root/actionGroup";
    private String                TOOLBAR_BLOCKS_FOR_GROUP = "/blockAction";
    /**
     * blockActions allow sub-Grouping of actions as sub-tabs
     */
    private String                TOOLBAR_BLOCKS        = "/toolbar/root/actionGroup/blockAction";
    private String                TOOLBAR_BLOCK_ACTIONS = "/action";
    private String                TOOLBAR_XML_FILE      =
        "JUST SOME DUMMY TEXT/toolbar_debate_b.xml";

    private BungeniToolbarParser() {
        //saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",validate);
        this.TOOLBAR_XML_FILE = ToolbarActionsReader.TOOLBAR_ACTIONS_FILE;
        this.document = getToolbarXMLFile(TOOLBAR_XML_FILE);
    }

    public static BungeniToolbarParser getInstance(){
        if (null == instance) {
            instance = new BungeniToolbarParser();
        }
        return instance;
    }
    

    private Document getToolbarXMLFile(String pathToXmlFile) {
        Document doc = null;
        try {
            
          doc = CommonEditorXmlUtils.loadFile(pathToXmlFile);

        } catch (FileNotFoundException ex) {
            log.error("Error while loading toolbar action file : " + pathToXmlFile, ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("Error while loading toolbar action file : " + pathToXmlFile, ex);
        } catch (JDOMException ex) {
             log.error("Error while loading toolbar action file : " + pathToXmlFile, ex);
        } catch (IOException ex) {
            log.error("Error while loading toolbar action file : " + pathToXmlFile, ex);
        }

        return doc;
       
    }

    public ArrayList<Element> getTabElements() {
        ArrayList<Element> blockElements = new ArrayList<Element>(0);

        try {
            XPath blocksXpath = XPath.newInstance(TOOLBAR_BLOCKS);

            blockElements = (ArrayList<Element>) blocksXpath.selectNodes(document);
        } catch (JDOMException ex) {
            log.error("getTabElements : " + ex.getMessage());
        } finally {
            return blockElements;
        }
    }

    /**
     *
     * @param groupElement - an actionGroup element
     * @return
     */
    public ArrayList<Element> getTabElements(Element groupElement) {
        ArrayList<Element> blockElements = new ArrayList<Element>(0);
        try {
             blockElements = (ArrayList<Element>) XPath.selectNodes(groupElement, "blockAction");
        } catch (JDOMException ex) {
            log.error("getTabElements : " + ex.getMessage());
        }
        return blockElements;
    }

    public Element getToolbarRoot() {
        Element rootNode = null;
        try {
            XPath rootPath = XPath.newInstance(TOOLBAR_ROOT);
            rootNode = (Element) rootPath.selectSingleNode(document);
        } catch (JDOMException ex) {
            log.error("Unable to get toolbar root", ex);
        }
        return rootNode;
    }

    public ArrayList<Element> getTabActionGroups() {
        ArrayList<Element> actionGrpElements = new ArrayList<Element>(0);

        try {
            XPath grpPath = XPath.newInstance(TOOLBAR_GROUPS);
            actionGrpElements = (ArrayList<Element>) grpPath.selectNodes(document);
        } catch (JDOMException ex) {
            log.error("getTabElements : " + ex.getMessage());
        } 
            return actionGrpElements;
    }

    public ArrayList<Element> getTabActionElements(Element tabElement) {
        ArrayList<Element> actionElements = new ArrayList<Element>(0);
        try {
            System.out.println("processing : " + tabElement.getAttributeValue("name"));
            actionElements = (ArrayList<Element>) XPath.selectNodes(tabElement, "action | subaction");
        } catch (JDOMException ex) {
            log.error("getTabActionElements : " + ex.getMessage());
        }
        return actionElements;
    }

    public Element getTabActionElementByName(String sName) {
        Element actionNode = null;
        try {
            StringBuilder sXpath = new StringBuilder();
            sXpath.append("//action[@name='");
            sXpath.append(sName);
            sXpath.append("']");
            actionNode = (Element) XPath.selectSingleNode(document, sXpath.toString());
        } catch (JDOMException ex) {
            log.error("Error while getting action Element by nanme");
        }
        return actionNode;
    }


}

