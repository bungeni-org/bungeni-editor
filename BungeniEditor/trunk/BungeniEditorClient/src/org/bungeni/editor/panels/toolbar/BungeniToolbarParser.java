package org.bungeni.editor.panels.toolbar;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.extutils.BungeniEditorProperties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import org.bungeni.editor.config.ToolbarActionsReader;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonFileFunctions;

/**
 * Reads the toolbar xml config file for the document type
 * and loads them as tabbed panels
 * @author Ashok Hariharan
 */
public class BungeniToolbarParser {
    private static boolean                 validate = false;
    private static byte[]                  xml      = new byte[] {};
    private static org.apache.log4j.Logger log      =
        org.apache.log4j.Logger.getLogger(BungeniToolbarParser.class.getName());
    private static Document       document;
    private static BufferedReader reader;
    private static SAXBuilder     saxBuilder;
    private static String SAX_PARSER_IMPL = "org.apache.xerces.parsers.SAXParser";
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
        "/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/settings/toolbar_debate_b.xml";

    public BungeniToolbarParser() {
        saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",validate);
        this.TOOLBAR_XML_FILE = ToolbarActionsReader.TOOLBAR_ACTIONS_FILE;
        /**
        String          activeDocumentMode = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String          toolbarquery       = SettingsQueryFactory.Q_FETCH_TOOLBAR_CONFIG_FILE(activeDocumentMode);
        BungeniClientDB instance           = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(),
                                                 DefaultInstanceFactory.DEFAULT_DB());

        instance.Connect();

        QueryResults qr = instance.QueryResults(toolbarquery);

        instance.EndConnect();

        String[] toolbarXml            = qr.getSingleColumnResult("TOOLBAR_XML");
        String   xmlConfigRelativePath = toolbarXml[0];

        xmlConfigRelativePath = xmlConfigRelativePath.replace('/', File.separatorChar);
        this.TOOLBAR_XML_FILE = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator
                                + xmlConfigRelativePath; */
        
    }

    private String getToolbarXMLFile(String pathToXmlFile) {
        String strResult = null;

        try {
            FileReader fr;

            fr = new FileReader(pathToXmlFile);

            BufferedReader xmlReader = new BufferedReader(fr);
            String         line      = "";
            StringBuilder  result    = new StringBuilder();

            while ((line = xmlReader.readLine()) != null) {
                result.append(line);
            }

            strResult = result.toString();
        } catch (FileNotFoundException ex) {
            log.error("getToolbarXMLFile: toolbar.xml not found in path : " + ex.getMessage());
        } finally {
            return strResult;
        }
    }

    public void buildToolbar() {
        try {
            StringReader stringReader = new StringReader(getToolbarXMLFile(TOOLBAR_XML_FILE));
            document = saxBuilder.build(stringReader);
            

        } catch (JDOMException ex) {
            log.error("buildToolbar : " + ex.getMessage());
        } catch (IOException ex) {
            log.error("buildToolbar : " + ex.getMessage());
        }
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
        } finally {
            return blockElements;
        }

    }

    public ArrayList<Element> getTabActionGroups() {
        ArrayList<Element> actionGrpElements = new ArrayList<Element>(0);

        try {
            XPath grpPath = XPath.newInstance(TOOLBAR_GROUPS);
            actionGrpElements = (ArrayList<Element>) grpPath.selectNodes(document);
        } catch (JDOMException ex) {
            log.error("getTabElements : " + ex.getMessage());
        } finally {
            return actionGrpElements;
        }

    }

    public ArrayList<Element> getTabActionElements(Element tabElement) {
        ArrayList<Element> actionElements = new ArrayList<Element>(0);
        try {
            System.out.println("processing : " + tabElement.getAttributeValue("name"));
            actionElements = (ArrayList<Element>) XPath.selectNodes(tabElement, "action | subaction");
        } catch (JDOMException ex) {
            log.error("getTabActionElements : " + ex.getMessage());
        } finally {
            return actionElements;
        }
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

