package org.bungeni.xmlmerge;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.BasicConfigurator;

import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.QueryResults;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.xmlmerge.queries.xmlMergeQueries;
import org.bungeni.xmlmerge.utils.xmlMergeUtils;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;
import org.odftoolkit.odfdom.doc.text.OdfTextSequenceDecls;
import org.odftoolkit.odfdom.doc.text.OdfTextTrackedChanges;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * This is the main class for the ODF n-merge process.
 *
 * @author Ashok Hariharan
 */
public class xmlMergeMain {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(xmlMergeMain.class.getName());

    /**
     * The different document versions to be merged.
     */
    private List<OdfDocument> fileVersions = new ArrayList<OdfDocument>(0);

    /**
     * The odf merge database
     */
    private BungeniClientDB mergeDB = null;

    /**
     * The original odf document file
     */
    private OdfDocument originalFile;

    public xmlMergeMain(OdfDocument origFile, List<OdfDocument> versions) {
        this.originalFile = origFile;
        this.fileVersions = versions;

        // buildMergeWorkspace();
        this.mergeDB = getDBhandle();
    }

    private BungeniClientDB getDBhandle() {
        if (mergeDB == null) {
            BungeniClientDB mdb = new BungeniClientDB(System.getProperty("user.dir") + File.separator + "merge"
                                      + File.separator, "merge.db");

            return mdb;
        } else {
            return mergeDB;
        }
    }

    /**
     * Build the change metadata stack
     */
    public void buildChangeInfo() {
        try {

            // create an xpath object to process
            // the document
            XPath xPath = originalFile.getXPath();

            // iterate through each version document
            // extrat the change information for each node
            // capture the node order and info into the document
            buildChangeNodePoints(xPath);
            //now process in order 
            processChangesInOrder(xPath);
        } catch (Exception ex) {
            log.error("buildChangeInfo : " + ex.getMessage(), ex);
        }
    }

    List<nodeAddress> getParentNodesInOrder() {
        QueryResults      qr          = mergeDB.ConnectAndQuery(xmlMergeQueries.GET_CHANGE_PARENTS());
        List<nodeAddress> parentNodes = new ArrayList<nodeAddress>(0);

        if (qr.hasResults()) {
            for (Vector<String> aResult : qr.theResults()) {
                String aNodeAddr = qr.getField(aResult, "CHANGE_PARENT");

                parentNodes.add(new nodeAddress(aNodeAddr));
            }
        }

        Collections.sort(parentNodes);

        return parentNodes;
    }

    public OdfDocument getVersionDocByPath(String fileURIpath) {
        for (OdfDocument odfDocument : fileVersions) {
            if (odfDocument.getBaseURI().equals(fileURIpath)) {
                return odfDocument;
            }
        }

        return null;
    }

    /**
     * Start with the shallowest change and go to the higher one
     * @param xPath
     */
    private void processChangesInOrder(XPath xPath) {

        // first get the parent nodes and sort them by document order
        // then for each parent node process the changes
        List<nodeAddress> orderedParentNodes = getParentNodesInOrder();
        boolean           bFirstChange       = false;
        int univChangeOrder = 0;
        //process changes in order of parent node
        
        for (int i = 0; i < orderedParentNodes.size(); i++) {
            nodeAddress  anAddress = orderedParentNodes.get(i);
            QueryResults qr        = mergeDB.ConnectAndQuery(xmlMergeQueries.GET_CHANGE_BY_PARENT(anAddress.xpathAddr));

            if (qr.hasResults()) {

                // all these changes have the same parent
                // sort them by preceding address points
                changePositions changePos = buildChangeNodeArray(i, qr);

                changePos.sort();

                // now process the change pos elements
                // keep track of the last incmoing change
                changePosition lastChange = null;

                for (int j = 0; j < changePos.getChangePositions().size(); j++) {
                    changePosition cp = changePos.getChangePositions().get(j);

                    if (bFirstChange == false) {
                        try {

                            // this is the very first change
                            bFirstChange = true;
                            log.debug("First preceding change Position = " + cp);

                            // get the first change preceding siblings
                            // we get only the preceding siblings -- since the siblings themselves may containe
                            // other nodes -- if we used preceding:: instead it would get parent and child nodes,
                            // we are interested only in the parents
                            //if it is the first change in the
                            String strPrecedingXML = extractFirstChangePrecedingSiblings(xPath,
                                                         getVersionDocByPath(cp.docName), cp.changeId, cp.changeType);
                            String strChangeXML = extractChangeContent(xPath, getVersionDocByPath(cp.docName),
                                                      cp.changeId, cp.changeType);
                            String updateChangeXml = xmlMergeQueries.UPDATE_CHANGE_FRAGMENTS(cp.docName, cp.changeId, strPrecedingXML, strChangeXML, ++univChangeOrder);

                            log.debug("Query == " + updateChangeXml + "\n\n");
                            mergeDB.Connect();
                            mergeDB.Update(updateChangeXml);
                            mergeDB.EndConnect();
                            File fprecXml = new File(getMergeWorkspace() + File.separator + j+ "_prec_" + xmlMergeUtils.getFileNameWithoutExtension(xmlMergeUtils.getFileNameFromPath(cp.docName))+ ".xml");
                            FileWriter fw = new FileWriter(fprecXml);
                            fw.write(strPrecedingXML);
                            fw.close();
                            File fchangeXML = new File(getMergeWorkspace() + File.separator +  j+ "_chg_" + xmlMergeUtils.getFileNameWithoutExtension(xmlMergeUtils.getFileNameFromPath(cp.docName))+ ".xml");
                            fw = new FileWriter(fchangeXML);
                            fw.write(strChangeXML);
                            fw.close();

                        } catch (Exception ex) {
                            log.error("processChangesInOrder : " + ex.getMessage());
                        }
                    } else {
                        try {

                            // process next change
                            // for the next change we capture the text between the end point of the
                            // previous change and the starting point of the 2md change
                            String nextChangePrecedingXml = this.extractNextChangePrecedingSiblings(xPath, cp,
                                                                lastChange);
                            String nextChangeXml = this.extractChangeContent(xPath, getVersionDocByPath(cp.docName),
                                                       cp.changeId, cp.changeType);
                            String updateChangeXml = xmlMergeQueries.UPDATE_CHANGE_FRAGMENTS(cp.docName, cp.changeId, nextChangePrecedingXml, nextChangeXml, ++univChangeOrder);
                            mergeDB.Connect();
                            mergeDB.Update(updateChangeXml);
                            mergeDB.EndConnect();
                            File fprecXml = new File(getMergeWorkspace() + File.separator +  j+ "_prec_" + xmlMergeUtils.getFileNameWithoutExtension(xmlMergeUtils.getFileNameFromPath(cp.docName)) + ".xml");
                            FileWriter fw = new FileWriter(fprecXml);
                            fw.write(nextChangePrecedingXml);
                            fw.close();
                            File fchangeXML = new File(getMergeWorkspace() + File.separator +  j+ "_chg_" + xmlMergeUtils.getFileNameWithoutExtension(xmlMergeUtils.getFileNameFromPath(cp.docName))+ ".xml") ;
                            fw = new FileWriter(fchangeXML);
                            fw.write(nextChangeXml);
                            fw.close();
                        } catch (java.lang.Exception ex) {
                            log.error("Error while processing next change " + cp + " exception " + ex.getMessage(), ex);
                        }
                    }

                    lastChange = cp;
                }

                try {
                    String strLastChange = extractLastChangeFollowingSiblings(xPath, lastChange, anAddress.xpathAddr);
                    String updLastQuery = xmlMergeQueries.UPDATE_FOLLOWING_FRAGMENT(lastChange.docName, lastChange.changeId, strLastChange);
                    mergeDB.Connect();
                    mergeDB.Update(updLastQuery);
                    mergeDB.EndConnect();
                    log.debug("Last change xml = \n\n" + strLastChange);
                } catch (Exception ex) {
                    log.error("Error while processing last change " + lastChange + " exception", ex);
                }
            }
        }
    }

    /**
     * 
     * @param cpObject
     */
    private void processShallowestNodes(changePositions cpObject) {

        // find the shallowest node
        // step 1
        // get the immediately preceding node
        // compare document positions of the parent node of the change node
        // an actual text:insert change can insert a whole section ... which may affect
        // the hierarchy... so ideally we have to collapse all insert hierachys ..store
        // them temporarily in a file and then recreate them
        changePosition lastNode = null;

        for (changePosition cp : cpObject.getChangePositions()) {
            log.debug("node address = " + cp.nodeAddressStart.substring(0, cp.nodeAddressStart.lastIndexOf("/")));

            if (lastNode != null) {}
        }
    }

    /**
     * 
     * @param norder
     * @param qr
     * @return
     */
    private changePositions buildChangeNodeArray(int norder, QueryResults qr) {
        changePositions cpos = new changePositions(norder);

        for (Vector<String> dataRow : qr.theResults()) {
            String         docname      = qr.getField(dataRow, "DOC_NAME");
            String         changeId     = qr.getField(dataRow, "CHANGE_ID");
            String         changeType   = qr.getField(dataRow, "CHANGE_TYPE");
            String         changeBefore = qr.getField(dataRow, "CHANGE_POS_BEFORE");
            String         changeAfter  = qr.getField(dataRow, "CHANGE_POS_AFTER");
            changePosition cpObject     = new changePosition(docname, changeId, changeBefore, changeAfter, changeType);

            cpos.add(cpObject);
        }

        return cpos;
    }

    /**
     *
     * @return
     */
    private String getMergeWorkspace() {
        BungeniOdfDocumentHelper docH    = new BungeniOdfDocumentHelper(this.originalFile);
        String                   sFolder =
            xmlMergeUtils.getFileNameWithoutExtension(xmlMergeUtils.getFileNameFromPath(docH.getDocumentPath()));

        return System.getProperty("user.dir") + File.separator + "merge_ws" + File.separator + sFolder + File.separator
               + "fragments";
    }

    /**
     * 
     * @param fragmentType
     * @param filePath
     * @return
     */
    private File getOutputFragmentFile(String fragmentType, String filePath) {
        String fragmentSuffix = xmlMergeUtils.getFileNameWithoutExtension(xmlMergeUtils.getFileNameFromPath(filePath));
        File   ffragment      = new File(getMergeWorkspace() + File.separator + fragmentType + "_" + fragmentSuffix
                                         + ".xml");

        return ffragment;
    }


    /**
     *
     * @param xPath
     * @throws Exception
     */
    private void buildChangeNodePoints(XPath xPath) throws Exception {
        List<String> addQueries   = new ArrayList<String>(0);
        NodeList     nodeListPrev = null;

        for (OdfDocument document : fileVersions) {
            try {
                BungeniOdfDocumentHelper dochelper = new BungeniOdfDocumentHelper(document);

                extractAutomaticStyles(document);

                extractFontFaceDecls(document);

                extractOfficeScripts(document);

                // extract track changes container
                extractTrackChangesContainer(document);

                // extract sequence decls
                extractTextSequenceDecls(document);

                // find all change nodes
                NodeList changeNodes = dochelper.getChangesHelper().getAllChangeNodes();

                // iterate through the change nodes
                for (int i = 0; i < changeNodes.getLength(); i++) {
                    String xpathStart  = "",
                           xpathEnd    = "";
                    Node   changeStart = changeNodes.item(i);
                    Node   changeEnd   = null;
                    String changeId    = changeStart.getAttributes().getNamedItem("text:change-id").getTextContent();

                    xpathStart = BungeniOdfNodeHelper.getXPath(changeStart);

                    // check if the node is a change start node
                    String changeType = "";

                    if (changeStart.getNodeName().equals(OdfTextChangeStart.ELEMENT_NAME.getQName())) {

                        // get the change id
                        // get the node path to the change-start item
                        // get the xpath to the expresion end
                        String xpathExprEnd = "//text:change-end[@text:change-id='" + changeId + "']";

                        changeEnd  = (Node) xPath.evaluate(xpathExprEnd, document.getContentDom(), XPathConstants.NODE);
                        xpathEnd   = BungeniOdfNodeHelper.getXPath(changeEnd);
                        changeType = BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_INSERTION__;
                    } else {
                        changeType = BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__;
                    }

                    String parentNodeAddr = xmlMergeUtils.parentNodeFromAddress(xpathStart);
                    String addQuery       = xmlMergeQueries.ADD_CHANGE(document.getBaseURI(), changeId, changeType,
                                                xpathStart, xpathEnd, "", Integer.toString(i), parentNodeAddr);

                    addQueries.add(addQuery);
                    log.debug("buildChangeInfo : " + addQuery);
                }
            } catch (XPathExpressionException ex) {
                log.error("buildChangeInfo : " + ex.getMessage());
            }
        }

        this.mergeDB.Connect();
        this.mergeDB.Update(addQueries, true);
        this.mergeDB.EndConnect();
    }


    /**
     * Extracts the font-face declarations in a ODF file -- these also need to be merged
     * @param document
     */
    private void extractFontFaceDecls(OdfDocument document) {
        try {
            BungeniOdfDocumentHelper odoc          = new BungeniOdfDocumentHelper(document);
            File                     outputFile    = getOutputFragmentFile("font-face-decls", odoc.getDocumentPath());
            XPath                    xpath         = document.getXPath();
            Node                     fontFaceDecls = (Node) xpath.evaluate("//office:font-face-decls",
                                                         document.getContentDom(), XPathConstants.NODE);

            BungeniOdfNodeHelper.outputNodeAsXML(fontFaceDecls, outputFile);

            // write to disk
        } catch (Exception ex) {
            log.error("extractFontFaceDecls " + ex.getMessage(), ex);
        }
    }


    /**
     * extract automatic styles in a odf document - these also need to be merged
     * @param document
     */
    private void extractAutomaticStyles(OdfDocument document) {
        try {
            BungeniOdfDocumentHelper odoc            = new BungeniOdfDocumentHelper(document);
            File                     outputFile      = getOutputFragmentFile("automatic-styles",
                                                           odoc.getDocumentPath());
            XPath                    xpath           = document.getXPath();
            Node                     automaticStyles = (Node) xpath.evaluate("//office:automatic-styles",
                                                           document.getContentDom(), XPathConstants.NODE);

            BungeniOdfNodeHelper.outputNodeAsXML(automaticStyles, outputFile);

            // write to disk
        } catch (Exception ex) {
            log.error("extractAutomaticStyles :" + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param document
     */
    private void extractOfficeScripts(OdfDocument document) {
        try {
            BungeniOdfDocumentHelper odoc            = new BungeniOdfDocumentHelper(document);
            File                     outputFile      = getOutputFragmentFile("scripts", odoc.getDocumentPath());
            XPath                    xpath           = document.getXPath();
            Node                     automaticStyles = (Node) xpath.evaluate("//office:automatic-styles",
                                                           document.getContentDom(), XPathConstants.NODE);

            BungeniOdfNodeHelper.outputNodeAsXML(automaticStyles, outputFile);

            // write to disk
        } catch (Exception ex) {
            log.error("extractOfficeScripts " + ex.getMessage(), ex);
        }
    }

    /**
     * 
     * @param document
     */
    private void extractTrackChangesContainer(OdfDocument document) {
        try {
            BungeniOdfDocumentHelper odoc       = new BungeniOdfDocumentHelper(document);
            File                     outputFile = getOutputFragmentFile("track-changes-container",
                                                      odoc.getDocumentPath());
            OdfTextTrackedChanges    tcElement  =
                (OdfTextTrackedChanges) odoc.getChangesHelper().getTrackedChangeContainer();

            BungeniOdfNodeHelper.outputNodeAsXML(tcElement, outputFile);

            // write to disk
        } catch (TransformerConfigurationException ex) {
            log.error("extractTrackChangesContainer " + ex.getMessage(), ex);
        } catch (TransformerException ex) {
            log.error("extractTrackChangesContainer " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param document
     * @throws XPathExpressionException
     * @throws Exception
     */
    private void extractTextSequenceDecls(OdfDocument document) throws XPathExpressionException, Exception {
        XPath                    xPath    = document.getXPath();
        BungeniOdfDocumentHelper odoc     = new BungeniOdfDocumentHelper(document);
        OdfTextSequenceDecls     seqDecls = (OdfTextSequenceDecls) xPath.evaluate("//text:sequence-decls",
                                                document.getContentDom(), XPathConstants.NODE);
        File outputFile = getOutputFragmentFile("sequence-decls", odoc.getDocumentPath());

        BungeniOdfNodeHelper.outputNodeAsXML(seqDecls, outputFile);
    }

    /**
     *
     * @param xPath
     * @param document
     * @param changeId
     * @param changeType
     * @return
     * @throws Exception
     */
    private String extractChangeContent(XPath xPath, OdfDocument document, String changeId, String changeType)
            throws Exception {
        StringWriter buff = new StringWriter();

        if (changeType.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_INSERTION__)) {

            // generate the element prefixes :
            Node startNode = (Node) xPath.evaluate("//text:change-start[@text:change-id='" + changeId + "']",
                                 document.getContentDom(), XPathConstants.NODE);
            Node endNode = (Node) xPath.evaluate("//text:change-end[@text:change-id='" + changeId + "']",
                               document.getContentDom(), XPathConstants.NODE);
            String xpathForBetweenNodes = "./following-sibling::*[following::text:change-end[@text:change-id='"
                                          + changeId + "']]";

            // do an xpath relative to the startnode
            NodeList xpathNodeList = (NodeList) xPath.evaluate(xpathForBetweenNodes, startNode, XPathConstants.NODESET);

            BungeniOdfNodeHelper.outputNodeAsXML(startNode, buff);
            BungeniOdfNodeHelper.outputNodesAsXML(xpathNodeList, buff);
            BungeniOdfNodeHelper.outputNodeAsXML(endNode, buff);

            // "//text:change-start[@text:change-id='ct472670048']/following-sibling::*[following::text:change-end[@text:change-id='ct472670048']]"
        } else {    // it is a deletion
        }           // we dont handle replacement scenarios yet

        return buff.toString();
    }

    /**
     * Extracts the preceding sibling nodes from the first change upto the start of the document
     * @param xPath
     * @param document
     * @param changeId
     * @param changeType
     * @return
     * @throws Exception
     */
    private String extractFirstChangePrecedingSiblings(XPath xPath, OdfDocument document, String changeId,
            String changeType)
            throws Exception {
        NodeList     nnodes;
        StringBuffer sbf = new StringBuffer();

        try {
            if (changeType.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__)) {
                sbf.append(precedingSiblingsForFirstChangeDelete(xPath, document, changeId));
            } else {
                sbf.append(precedingSiblingsForFirstChangeInsert(xPath, document, changeId));
            }

        } catch (Exception ex) {
            log.error("extractFirstChange : " + ex.getMessage());

            throw ex;
        }

        return sbf.toString();
    }

    /**
     * 
     * @param xPath
     * @param thisChange
     * @param prevChange
     * @return
     * @throws Exception
     */
    private String extractNextChangePrecedingSiblings(XPath xPath, changePosition thisChange, changePosition prevChange)
            throws Exception {
        NodeList     nnodes;
        StringBuffer sbf = new StringBuffer();

        try {
            if (thisChange.changeType.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__)) {
                sbf.append(precedingSiblingsForNextChangeDelete(xPath, thisChange, prevChange));
            } else {
                sbf.append(precedingSiblingsForNextChangeInsert(xPath, thisChange, prevChange));
            }

            // BungeniOdfNodeHelper.outputNodesAsXML(nnodes, sw);
        } catch (Exception ex) {
            log.error("extractFirstChange : " + ex.getMessage());

            throw ex;
        }

        return sbf.toString();
    }

    /**
     *
     * @param xPath
     * @param document
     * @param changeId
     * @return
     * @throws Exception
     */
    private String precedingSiblingsForFirstChangeInsert(XPath xPath, OdfDocument document, String changeId)
            throws Exception {
        String xpathExpr = "//text:change-start[@text:change-id='" + changeId
                           + "']/preceding-sibling::*[preceding::text:sequence-decls]";
        List<String> xmlNodes   = new ArrayList<String>(0);
        NodeList     foundNodes = (NodeList) xPath.evaluate(xpathExpr, document.getContentDom(),
                                      XPathConstants.NODESET);
        log.info("preceding nodes for " + document.getBaseURI() + " for " + changeId + " are " + foundNodes.getLength());
        StringWriter sw         = new StringWriter();

        BungeniOdfNodeHelper.outputNodesAsXML(foundNodes, sw);

        return sw.toString();
    }

    /**
     *
     * @param xPath
     * @param document
     * @param changeId
     * @return
     * @throws Exception
     */
    private String precedingSiblingsForFirstChangeDelete(XPath xPath, OdfDocument document, String changeId)
            throws Exception {
        String xpathExpr = "//text:change[@text:change-id='" + changeId
                           + "']/preceding-sibling::*[preceding::text:sequence-decls]";
        List<String> xmlNodes   = new ArrayList<String>(0);
        NodeList     foundNodes = (NodeList) xPath.evaluate(xpathExpr, document.getContentDom(),
                                      XPathConstants.NODESET);
        StringWriter sw         = new StringWriter();

        BungeniOdfNodeHelper.outputNodesAsXML(foundNodes, sw);

        return sw.toString();
    }

    /**
     *
     * @param xPath
     * @param cp
     * @param lastChange
     * @return
     * @throws Exception
     */
    private String precedingSiblingsForNextChangeInsert(XPath xPath, changePosition cp, changePosition lastChange)
            throws Exception {

        // -- first process the last change end point --
        // get the node address of the node after the last change end
        String lastEndNodeXpath = "";

        if (lastChange.changeType.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_INSERTION__)) {

            // if the lastnode was a insertion we use the insertion end point
            lastEndNodeXpath = lastChange.nodeAddressEnd + "/following::node()[1]";
        } else {

            // if the lastnode was a deletion we use the deletion end point
            lastEndNodeXpath = lastChange.nodeAddressStart + "/following::node()[1]";
        }
        /// WE HAVE TO COLLAPSE INSERT NODES !!!! make a copy of the document and collapse the insert node

        Node lastEndNode = (Node) xPath.evaluate(lastEndNodeXpath,
                               getVersionDocByPath(lastChange.docName).getContentDom(), XPathConstants.NODE);

        // now we have the node address of the end point with respect to the original document
        // this node address should be valid even in the change document
        String xpathNodeAddress = BungeniOdfNodeHelper.getXPath(lastEndNode);

        // -- now process the start point of the current change
        Node changeStartThis = (Node) xPath.evaluate(cp.nodeAddressStart,
                                   getVersionDocByPath(cp.docName).getContentDom(), XPathConstants.NODE);
        Node endNodeThis = (Node) xPath.evaluate(xpathNodeAddress, getVersionDocByPath(cp.docName).getContentDom(),
                               XPathConstants.NODE);
        String xpathPrecedingNext = xpathNodeAddress
                                    + "/following-sibling::*[following::text:change-start[@text:change-id='"
                                    + cp.changeId + "']]";
        NodeList precedingNextList = (NodeList) xPath.evaluate(xpathPrecedingNext,
                                         getVersionDocByPath(cp.docName).getContentDom(), XPathConstants.NODESET);
        StringWriter sw = new StringWriter();

        BungeniOdfNodeHelper.outputNodeAsXML(endNodeThis, sw);
        BungeniOdfNodeHelper.outputNodesAsXML(precedingNextList, sw);
        log.debug("Next change xml = \n\n " + sw.toString());
        return sw.toString();
    }

    /**
     *
     * @param xPath
     * @param cp
     * @param lastChange
     * @return
     * @throws Exception
     */
    private String precedingSiblingsForNextChangeDelete(XPath xPath, changePosition cp, changePosition lastChange)
            throws Exception {

        // -- first process the last change end point --
        // get the node address of the node after the last change end
        String lastEndNodeXpath = "";

        if (lastChange.changeType.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_INSERTION__)) {

            // if the lastnode was a insertion we use the insertion end point
            lastEndNodeXpath = lastChange.nodeAddressEnd + "/following::node()[1]";
        } else {

            // if the lastnode was a deletion we use the deletion end point
            lastEndNodeXpath = lastChange.nodeAddressStart + "/following::node()[1]";
        }

        Node lastEndNode = (Node) xPath.evaluate(lastEndNodeXpath,
                               getVersionDocByPath(lastChange.docName).getContentDom(), XPathConstants.NODE);

        // now we have the node address of the end point with respect to the original document
        // this node address should be valid even in the change document
        String xpathNodeAddress = BungeniOdfNodeHelper.getXPath(lastEndNode);

        // -- now process the start point of the current change
        Node changeStartThis = (Node) xPath.evaluate(cp.nodeAddressStart,
                                   getVersionDocByPath(cp.docName).getContentDom(), XPathConstants.NODE);
        Node endNodeThis = (Node) xPath.evaluate(xpathNodeAddress, getVersionDocByPath(cp.docName).getContentDom(),
                               XPathConstants.NODE);
        String xpathPrecedingNext = xpathNodeAddress + "/following-sibling::*[following::text:change[@text:change-id='"
                                    + cp.changeId + "']]";
        NodeList precedingNextList = (NodeList) xPath.evaluate(xpathPrecedingNext,
                                         getVersionDocByPath(cp.docName).getContentDom(), XPathConstants.NODESET);
        StringWriter sw = new StringWriter();

        BungeniOdfNodeHelper.outputNodeAsXML(endNodeThis, sw);
        BungeniOdfNodeHelper.outputNodesAsXML(precedingNextList, sw);
        log.debug("Next change xml = \n\n " + sw.toString());

        return sw.toString();       
    }

    /**
     * Clean up the changes table
     */
    private void cleanupChangeInfo() {
        this.mergeDB.Connect();
        this.mergeDB.Update(xmlMergeQueries.DELETE_ALL_CHANGES());
        this.mergeDB.EndConnect();
    }

    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();

            final String userDir      = System.getProperty("user.dir");
            final String originalFile = userDir + "/testfiles/original.odt";
            List<String> versions     = new ArrayList<String>() {
                {
                    add(userDir + "/testfiles/version-1.odt");
                    add(userDir + "/testfiles/version-2.odt");
                }
            };
            OdfDocument xmlOrigFile = OdfDocument.loadDocument(new File(originalFile));

            log.debug("File Path = " + xmlOrigFile.getPackage().getBaseURI());

            List<OdfDocument> xmlVersions = new ArrayList<OdfDocument>(0);

            for (String version : versions) {
                xmlVersions.add(OdfDocument.loadDocument(new File(version)));
            }

            xmlMergeMain mergeMain = new xmlMergeMain(xmlOrigFile, xmlVersions);

            mergeMain.cleanupChangeInfo();
            mergeMain.buildChangeInfo();
        } catch (java.lang.Exception ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Get the last following sibling xml nodes 
     * @param xPath
     * @param lastChange
     * @param parentXpath
     * @return
     * @throws java.lang.Exception
     */
    private String extractLastChangeFollowingSiblings(XPath xPath, changePosition lastChange, String parentXpath)
            throws java.lang.Exception {
        NodeList nnodes            = null;
        String   changeElementType = "";

        if (lastChange.changeType.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_INSERTION__)) {
            changeElementType = "//text:change-end";
        } else {
            changeElementType = "//text:change";
        }

        String xPathExpr = changeElementType + "[@text:change-id='" + lastChange.changeId + "']/following-sibling::*["+  parentXpath +"/child::node()[last()]]";

        nnodes = (NodeList) xPath.evaluate(xPathExpr, this.getVersionDocByPath(lastChange.docName).getContentDom(),
                                           XPathConstants.NODESET);

        StringWriter sw = new StringWriter();

        BungeniOdfNodeHelper.outputNodesAsXML(nnodes, sw);

        return sw.toString();
    }

    /**
     * This class implements a the properties of a change node
     * It does not cache the actual XML node for memory usage and performance reasons
     */
    class changePosition implements Comparable {
        String changeId;
        String changeType;
        String docName;
        String nodeAddressEnd;
        String nodeAddressStart;

        public changePosition(String dName, String cId, String nAddressS, String nAddressE, String cType) {
            this.nodeAddressStart = nAddressS;
            this.nodeAddressEnd   = nAddressE;
            this.changeId         = cId;
            this.docName          = dName;
            this.changeType       = cType;
        }

        @Override
        public String toString() {
            return "changeId=" + changeId + ";" + " docName=" + docName + ";" + " nodeAddressStart=" + nodeAddressStart
                   + ";" + " nodeAddressEnd=" + nodeAddressEnd + ";" + " changeType=" + changeType;
        }

        public boolean parentsEqual(changePosition cp) {
            String parentCompareNode = xmlMergeUtils.parentNodeFromAddress(cp.nodeAddressStart);
            String parentThisNode    = xmlMergeUtils.parentNodeFromAddress(this.nodeAddressStart);

            return parentCompareNode.equals(parentThisNode);
        }

        private String getPrecedingNodeAddress(String xpathStr) {
            return xpathStr + "/preceding::node()[1]";
        }

        public int compareTo(Object objcp) {
            changePosition cp = (changePosition) objcp;

            try {

                // get the odfdocument handles for the 2 documents containing the change positions
                OdfDocument thisDocument = getVersionDocByPath(this.docName);
                OdfDocument compDocument = getVersionDocByPath(cp.docName);
                XPath       xPath        = thisDocument.getXPath();

                // get the preceding for the current node
                Node thisPreceding = (Node) xPath.evaluate(getPrecedingNodeAddress(this.nodeAddressStart),
                                         thisDocument.getContentDom(), XPathConstants.NODE);
                String xpathThisPreceding = BungeniOdfNodeHelper.getXPath(thisPreceding);

                // get the preceding for the change node
                Node compPreceding = (Node) xPath.evaluate(getPrecedingNodeAddress(cp.nodeAddressStart),
                                         compDocument.getContentDom(), XPathConstants.NODE);
                String xpathCompPreceding = BungeniOdfNodeHelper.getXPath(compPreceding);

                // if the preceding node paths are different, the nodes are at the same positions
                // this should never happen !!
                if (xpathThisPreceding.equals(xpathCompPreceding)) {
                    log.debug("compareTo :  the node changes are at the same positions");

                    return 0;
                } else {

                    // get the preceding nodes in the original document .. and use the document position
                    // api to determine order
                    // this will work if both the
                    Node thisNode = (Node) xPath.evaluate(xpathThisPreceding, originalFile.getContentDom(),
                                        XPathConstants.NODE);
                    Node compNode = (Node) xPath.evaluate(xpathCompPreceding, originalFile.getContentDom(),
                                        XPathConstants.NODE);
                    int nPosition = thisNode.compareDocumentPosition(compNode);

                    if (nPosition == Node.DOCUMENT_POSITION_FOLLOWING) {

                        // compared Node is after this node
                        return -1;
                    } else {    // if (nPosition == Node.DOCUMENT_POSITION_PRECEDING) {

                        // compared node is before this node
                        return 1;
                    }
                }
            } catch (java.lang.Exception ex) {
                log.error("compareTo " + ex.getMessage());
            }

            return 0;
        }
    }


    class changePositions {
        List<changePosition> arrPositions = new ArrayList<changePosition>(0);
        int                  changeOrder;

        public changePositions(int order) {
            this.changeOrder = order;
        }

        public void add(changePosition pPos) {
            this.arrPositions.add(pPos);
        }

        public List<changePosition> getChangePositions() {
            return arrPositions;
        }

        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();

            for (changePosition position : arrPositions) {
                buf.append(position.toString() + "\n");
            }

            return buf.toString();
        }

        public void sort() {
            Collections.sort(arrPositions);
        }
    }


    class nodeAddress implements Comparable {
        String xpathAddr;

        public nodeAddress(String xPath) {
            this.xpathAddr = xPath;
        }

        public int compareTo(Object o) {
            nodeAddress compObj   = (nodeAddress) o;
            int         nComparer = 0;

            if (xpathAddr.equals(compObj.xpathAddr)) {
                nComparer = 0;
            } else {
                try {
                    XPath xPath    = originalFile.getXPath();
                    Node  thisNode = (Node) xPath.evaluate(this.xpathAddr, originalFile.getContentDom(),
                                         XPathConstants.NODE);
                    Node otherNode = (Node) xPath.evaluate(compObj.xpathAddr, originalFile.getContentDom(),
                                         XPathConstants.NODE);
                    int nComparison = thisNode.compareDocumentPosition(otherNode);

                    switch (nComparison) {
                        case Node.DOCUMENT_POSITION_PRECEDING:
                            nComparer = -1;
                            break;
                        case Node.DOCUMENT_POSITION_FOLLOWING:
                            nComparer = 1;
                            break;

                        case Node.DOCUMENT_POSITION_CONTAINED_BY:
                            nComparer = 1;
                            break;
                        case Node.DOCUMENT_POSITION_CONTAINS:
                            nComparer = -1;
                            break;
                        default :
                            nComparer = 1;
                            break;
                    }
                    
                } catch (Exception ex) {
                    log.error("compareTo :" + ex.getMessage());
                }
            }

            return nComparer;
        }
    }
}
