package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.practicalxml.util.NodeListIterator;

import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;


import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.odftoolkit.odfdom.dom.element.text.TextChangeElement;
import org.odftoolkit.odfdom.dom.element.text.TextChangeEndElement;
import org.odftoolkit.odfdom.dom.element.text.TextChangeStartElement;
import org.odftoolkit.odfdom.dom.element.text.TextChangedRegionElement;
import org.odftoolkit.odfdom.dom.element.text.TextDeletionElement;
import org.odftoolkit.odfdom.dom.element.text.TextInsertionElement;

/**
 * <p>This class assists in extracting tracked changes from a ODF document</p>
 * @author Ashok Hariharan
 */
public class BungeniOdfTrackedChangesHelper {
    public static final String             __CHANGE_TYPE_DELETION__    = "deletion";
    public static final String             __CHANGE_TYPE_INSERTION__   = "insertion";
    public static final String             __CHANGE_TYPE_REPLACEMENT__ = "replacement";
    private static org.apache.log4j.Logger log                         =
        Logger.getLogger(BungeniOdfTrackedChangesHelper.class.getName());
    private BungeniOdfChangesMergeHelper   m_changesMergeHelper        = null;
    private BungeniOdfDocumentHelper       m_docHelper                 = null;
    private XPath                          m_docXpath                  = null;

    /**
     * <p>Track changes helper is initialized using a Document Helper object</p>
     * @param docH
     */
    public BungeniOdfTrackedChangesHelper(BungeniOdfDocumentHelper docH) {
        m_docHelper = docH;
        try {
        m_docXpath  = m_docHelper.getOdfDocument().getContentDom().getXPath();
        } catch (Exception ex) {
            log.error("Exception while trying to get XPath handle");
        }
        log.debug("BungeniOdfTrackedChangesHelper : in constructor");
    }

    /**
     * <p>Retrieves the container for the changes header text:tracked-changes</p>
     * @return
     */
    public Element getTrackedChangeContainer() {
        Element textTrackedChanges = null;

        try {
            log.debug("BungeniOdfTrackedChangesHelper.getTrackedChangeContainer");

            NodeList trackedChanges =
                m_docHelper.getOdfDocument().getContentDom().getElementsByTagName("text:tracked-changes");

            for (int i = 0; i < trackedChanges.getLength(); i++) {

                // this is the tracked change element container
                textTrackedChanges = (Element) trackedChanges.item(i);

                break;

                // get all the changed regions
            }
        } catch (Exception ex) {
            log.error("getTrackedChangeContainer : " + ex.getMessage(), ex);
        }

        return textTrackedChanges;
    }

    /**
     *
     * @return
     */
    public BungeniOdfChangesMergeHelper getChangesMergeHelper() {
        if (this.m_changesMergeHelper == null) {
            m_changesMergeHelper = new BungeniOdfChangesMergeHelper(this);
        }

        return m_changesMergeHelper;
    }

    /**
     * <p>Get changed regions in a text change container
     * text:tracked-changes/text:changed-regions</p>
     * @param changeContainer
     * @return
     */
    public ArrayList<TextChangedRegionElement> getChangedRegions(Element changeContainer) {
        ArrayList<TextChangedRegionElement> textChangedRegions = new ArrayList<TextChangedRegionElement>(0);
        NodeList                        changedRegions     =
            changeContainer.getElementsByTagName("text:changed-region");

        for (int i = 0; i < changedRegions.getLength(); i++) {
            TextChangedRegionElement textChangedRegion = (TextChangedRegionElement) changedRegions.item(i);

            textChangedRegions.add(textChangedRegion);
        }

        return textChangedRegions;
    }

    /**
     * <p>Get changed regions in a text change container by a specific user.
     * Changes are filtered by the user name specified in dc:creator
     * text:tracked-changes/text:changed-regions</p>
     * @param changeContainer
     * @return
     */
    public ArrayList<TextChangedRegionElement> getChangedRegionsByCreator(Element changeContainer, String dcCreator) {
        ArrayList<TextChangedRegionElement> textChangedRegions = new ArrayList<TextChangedRegionElement>(0);

        try {

            // to fix issue 70 - http://code.google.com/p/bungeni-editor/issues/detail?id=70
            String xPathExpr = "./child::text:changed-region["
                               + "(descendant::text:insertion/office:change-info[@office:chg-author='" + dcCreator
                               + "'])" + " or " + "(descendant::dc:creator='" + dcCreator + "')]";
            NodeList changedRegions = (NodeList) this.m_docXpath.evaluate(xPathExpr, changeContainer,
                                          XPathConstants.NODESET);

            for (int i = 0; i < changedRegions.getLength(); i++) {
                TextChangedRegionElement textChangedRegion = (TextChangedRegionElement) (Element) changedRegions.item(i);

                textChangedRegions.add(textChangedRegion);
            }
        } catch (XPathExpressionException ex) {
            log.error("getChangedRegionsByCreator :" + ex.getMessage(), ex);
        }

        return textChangedRegions;
    }

    /**
     * <p>Retrieves the changed-region by change id </p>
     * @param changeId
     * @return TextChangedRegionElement
     */
    public TextChangedRegionElement getChangedRegionById(String changeId) {
        TextChangedRegionElement foundRegion = null;

        try {
            String xPathExpr = "./child::text:changed-region[@text:id='" + changeId + "']";
            Node   foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, this.getTrackedChangeContainer(),
                                   XPathConstants.NODE);

            if (foundNode != null) {
                foundRegion = (TextChangedRegionElement) foundNode;
            }
        } catch (XPathExpressionException ex) {
            log.error("getChangedRegionsById : " + ex.getMessage(), ex);
        }

        return foundRegion;
    }



    /**
     * Reverse all track change markings in a document by a user and restores original text.
     * The API requies a user name to be specified as input, and also a list of changes to be ignored.
     * If no changes to be ignored are provided, all the the changes by the user are reversed.
     * It is the responsibility of the caller to save the doument after the API has been called.
     * Failure to save the document before further processing may result in unpredictable results
     * or crashes
     *
     * @param dcCreator
     * @param exceptTheseChangeIds
     * @return
     * @throws Exception
     */
    public boolean revertAllChangesByCreatorWithException(String dcCreator, List<String> exceptTheseChangeIds)
            throws Exception {
        String xPathExpr = "./child::text:changed-region["
                           + "(descendant::text:insertion/office:change-info[@office:chg-author='" + dcCreator + "'])"
                           + " or " + "(descendant::dc:creator='" + dcCreator + "')]";

        log.info("revertAllChangesByCreatorWithException  : " + xPathExpr);

        NodeList foundNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr,
                                  this.m_docHelper.getChangesHelper().getTrackedChangeContainer(),
                                  XPathConstants.NODESET);
        NodeListIterator foundNodesIterator = new NodeListIterator(foundNodes);

        // iterate through the individual changes int he document
        while (foundNodesIterator.hasNext()) {
            TextChangedRegionElement changeRegion = (TextChangedRegionElement) foundNodesIterator.next();
            StructuredChangeType scType       = this.getStructuredChangeType(changeRegion);
            boolean              bException   = false;

            // check if the id is one of the exceptions
            // if it is we dont process the exceptions
            for (String exceptThisId : exceptTheseChangeIds) {
                if (exceptThisId.equals(scType.changeId)) {
                    bException = true;
                }
            }

            // process only if it is not an exception
            if (false == bException) {
                if (scType.changetype.equals(__CHANGE_TYPE_INSERTION__)) {

                    // insert changes are very straightforward to handle
                    // the markers are removed and the header entries are removed.
                    revertInsertChange(scType);
                } else {
                    log.debug("removing deletion for change id :" + scType.changeId);
                    ///revertDeleteChange(scType, changeRegion);
                    DeletedNodeReverter delRevert = new DeletedNodeReverter(scType, changeRegion);
                    delRevert.initialize();
                    delRevert.revert();
                }

                 changeRegion.getParentNode().removeChild(changeRegion);
            }
        }

        return true;
    }

    /**
     * Reverts an insert change - this is a private API
     * @param scType
     * @return
     */
    private boolean revertInsertChange(StructuredChangeType scType) {
        boolean bstate = false;

        try {
            log.debug("removing insertion for change id :" + scType.changeId);

            // if it is an insertion we remove the nodes between the markers
            // first delete all the nodes between the markers and then delete
            // the marker nodes
            NodeList insertNodes = getNodesBetweenInsertMarkers(scType.changeId);

            for (int j = 0; j < insertNodes.getLength(); j++) {
                Node insNode = insertNodes.item(j);

                insNode.getParentNode().removeChild(insNode);
            }

            NodeList markerNodes = getMarkerNodesForChange(scType.changeId);

            for (int k = 0; k < markerNodes.getLength(); k++) {
                Node markerNode         = markerNodes.item(k);
                Node parentofMarkerNode = markerNode.getParentNode();

                parentofMarkerNode.removeChild(markerNode);

                // if the parent of the marker node is empty -- remove it too
                // since its a boundary marker
                // if (!parentofMarkerNode.hasChildNodes()) {
                // parentofMarkerNode.getParentNode().removeChild(parentofMarkerNode);
                // }
            }

            bstate = true;
        } catch (Exception ex) {
            log.error("revertInsertChange : " + ex.getMessage(), ex);
        }

        return bstate;
    }



    /**
     * This is a special internal class to handle reversion of delete nodes
     * A special class has been setup because the delete reversal use case
     * features many exceptions and unusal scenarios for reversion.
     *
     * To use the API - create a DeletedNodeRevertr object,
     * call initialize() and then,
     * call revert() to revert the change
     */
    private class DeletedNodeReverter {

        StructuredChangeType scType = null;
        TextChangedRegionElement changeRegion = null;

        /** nodes used during processing **/
        Node m_deleteMarker = null;
        Node m_parentofDeleteMarker = null;
        Node m_nodeAfterParentOfDeleteMarker = null;
        Node m_lastDeletedNode = null;

        /*** deleted nodes iterator **/
        NodeListIterator  m_deletedNodesIterator = null;

        /** Boolean flags **/
            /** initialized flags **/
            Boolean m_bmarkerHasNoSiblings        = false;
            Boolean m_bmarkerOnlyElementInParent  = false;
            Boolean m_bmarkerFirstElementInParent = false;

            /** runtime flags **/
            Boolean r_bfirstdeletedNode           = false;
            Boolean r_brestoreNodesAfterParentofParentMarker = false;
            Boolean r_bfollowingnodeshavedifferentParent = false;


        DeletedNodeReverter(StructuredChangeType sType, TextChangedRegionElement cRegion ) {
            this.scType = sType;
            this.changeRegion = cRegion;
        }

        void initialize() {
            init();
            init_flags();
        }

        void init_flags (){
            m_bmarkerHasNoSiblings        = !hasNodeGotSiblings(this.m_deleteMarker);
            m_bmarkerOnlyElementInParent  = (this.m_parentofDeleteMarker.getChildNodes().getLength() == 1)
                                          ? true
                                          : false;
            m_bmarkerFirstElementInParent = (m_bmarkerOnlyElementInParent)
                                          ? true
                                          : this.m_parentofDeleteMarker.getChildNodes().item(0).isSameNode(
                                              m_deleteMarker);
        }

        /**
         * Initialize the deleted nodes processor
         */
        void init(){
            try {

            NodeList deletdNodes = changeRegion.getElementsByTagName("text:deletion");
            // gets the text:change, text:change-start, text:change-end elements for a change id
            NodeList markerNodes = getMarkerNodesForChange(scType.changeId);
            // gets all the deleted nodes whcih need to be restored
            NodeList allDeletedNodes = getDeletedNodes(deletdNodes.item(0));

            // this is the delete marker <text:change ... />
            m_deleteMarker = markerNodes.item(0);
            // the parent of the delete marker
            m_parentofDeleteMarker  = m_deleteMarker.getParentNode();
            //the node after the parent of the deleted marker
            m_nodeAfterParentOfDeleteMarker = m_parentofDeleteMarker.getNextSibling();

            // now we process the deleted nodes one by one starting from the bottom to the top since that
            // is the order we are going to add the nodes using insertBefore();
            m_deletedNodesIterator  = new NodeListIterator(allDeletedNodes);
            } catch (Exception ex) {
                log.debug(ex);
            }
        }



        boolean revert(){
            Node currentDeletedNode = null;
            boolean bState = false;
            int i=0;
            try {
            while (m_deletedNodesIterator.hasNext()) {
                currentDeletedNode = m_deletedNodesIterator.next();
                if (0 == i ) {
                    this.r_bfirstdeletedNode = true;
                    i++;
                } else {
                    this.r_bfirstdeletedNode = false;
                }
                currentDeletedNode.getParentNode().removeChild(currentDeletedNode);

                if (r_bfirstdeletedNode) {
                    revertFirstDeletedNode(currentDeletedNode);
                } else {
                    revertNextDeletedNode(currentDeletedNode);
                }
                m_lastDeletedNode = currentDeletedNode;
            }
            revertPostProcess();
            bState = true;
            } catch (Exception ex) {
                log.error("revert error : " + ex.getMessage(), ex);
            }
            return bState;
        }



        boolean revertFirstDeletedNode(Node aDeletedNode){
            boolean currentAndParentNodeSignaturesEqual =  compareNodeSignatures (aDeletedNode, this.m_parentofDeleteMarker);
             if (r_brestoreNodesAfterParentofParentMarker) {
                    this.m_nodeAfterParentOfDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_nodeAfterParentOfDeleteMarker);
                    // no more processsing ---
                    return true;
             }
            if ((true== this.m_bmarkerHasNoSiblings) && currentAndParentNodeSignaturesEqual) {
                      m_parentofDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_parentofDeleteMarker);
            }
            if ((false == this.m_bmarkerHasNoSiblings))  {
                if (aDeletedNode.hasChildNodes() && currentAndParentNodeSignaturesEqual) {
                        NodeList childrenofThis = aDeletedNode.getChildNodes();
                        NodeListIterator childrenofThisIterator = new NodeListIterator(childrenofThis);
                            while (childrenofThisIterator.hasNext()) {
                            Node childNode = childrenofThisIterator.next();
                            this.m_deleteMarker.getParentNode().insertBefore(childNode, m_deleteMarker);
                            }
                    }
                if (!aDeletedNode.hasChildNodes() && currentAndParentNodeSignaturesEqual) {
                     Node firstchildofparentofChangeMarker = this.m_parentofDeleteMarker.getFirstChild();
                     if (compareNodeSignatures (firstchildofparentofChangeMarker, m_deleteMarker)) {
                            //they are the same -- this is what this scneario could look like
                            // this is the change in the documenmt
                            // <text:p><text:change ... /> ,......</text:p>
                            //this is the first deleted node :
                            // <text:p/> .. i.e. add a new paragrp before the parent of the change marker
                            m_parentofDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_parentofDeleteMarker);
                     }

                }
                if (!currentAndParentNodeSignaturesEqual) {
                            Node parentofNodeAfterParentMarker = this.m_nodeAfterParentOfDeleteMarker.getParentNode();
                            m_nodeAfterParentOfDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_nodeAfterParentOfDeleteMarker);
                            this.r_brestoreNodesAfterParentofParentMarker = true;
                            this.r_bfollowingnodeshavedifferentParent = true;
                }

            }

            return true;
        }

        boolean revertNextDeletedNode(Node aDeletedNode){
            boolean currentAndParentNodeSignaturesEqual =  compareNodeSignatures (aDeletedNode, this.m_parentofDeleteMarker);
            if (r_brestoreNodesAfterParentofParentMarker) {
                    this.m_nodeAfterParentOfDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_nodeAfterParentOfDeleteMarker);
                    // no more processsing ---
                    return true;
            }
            if ((true== this.m_bmarkerHasNoSiblings) && currentAndParentNodeSignaturesEqual) {
                      m_parentofDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_parentofDeleteMarker);
            }
            if ((false == this.m_bmarkerHasNoSiblings))  {
                if (aDeletedNode.hasChildNodes() && currentAndParentNodeSignaturesEqual) {
                        NodeList childrenofThis = aDeletedNode.getChildNodes();
                        NodeListIterator childrenofThisIterator = new NodeListIterator(childrenofThis);
                            while (childrenofThisIterator.hasNext()) {
                            Node childNode = childrenofThisIterator.next();
                            this.m_deleteMarker.getParentNode().insertBefore(childNode, m_deleteMarker);
                            }
                    } else
                if (!aDeletedNode.hasChildNodes() && currentAndParentNodeSignaturesEqual) {
                                // this is the change in the documenmt
                                    // <text:p>....<text:change ... /> ,......</text:p>
                                    //this is the not the first de;etenode :
                                    // <text:p/> .. i.e. add a new paragrp before the parent of the change marker

                                    m_nodeAfterParentOfDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_nodeAfterParentOfDeleteMarker);
                                    this.r_brestoreNodesAfterParentofParentMarker = true;


                } 
                if (!currentAndParentNodeSignaturesEqual) {
                            Node parentofNodeAfterParentMarker = this.m_nodeAfterParentOfDeleteMarker.getParentNode();
                            m_nodeAfterParentOfDeleteMarker.getParentNode().insertBefore(aDeletedNode, m_nodeAfterParentOfDeleteMarker);
                            this.r_brestoreNodesAfterParentofParentMarker = true;
                            this.r_bfollowingnodeshavedifferentParent = true;
                }

            }
            return true;
        }

        private boolean revertPostProcess() throws Exception {
                boolean bState = false;
                if (this.r_brestoreNodesAfterParentofParentMarker) {
                       TextChangeElement elemDelChangeMark = (TextChangeElement) m_deleteMarker;
                List<Node> repatriateTheseNodes = new ArrayList<Node>(0);
                Node nodeNext = null;
                /**
                 * There are various use cases here
                 * - if the last deleted node of the change has the same node signature
                 * as the next sibling of the change mark - we simply add it to the parent clone of the
                 * changemarker
                 * - if the last deleted node is a text:p and the next sibling is a span, the span is subsumed into the
                 * last deleted node.
                 */
                boolean lastdeletednodeisaParagraph = false;
                boolean nextsiblingofchangeMarkisaSpan = false;

                if (m_lastDeletedNode.getNodeName().equals("text:p")) {
                    lastdeletednodeisaParagraph = true;
                }
                Node nodeAfterChangeMark = elemDelChangeMark.getNextSibling();
                if (nodeAfterChangeMark != null ) {
                    if (nodeAfterChangeMark.getNodeName().equals("text:span")) {
                        nextsiblingofchangeMarkisaSpan = true;
                    }
                }
                if (lastdeletednodeisaParagraph && nextsiblingofchangeMarkisaSpan ) {
                    for (nodeNext = elemDelChangeMark.getNextSibling(); nodeNext != null;) {
                        repatriateTheseNodes.add(nodeNext);
                        nodeNext = nodeNext.getNextSibling();
                    }
                     for (Node repatriatenode : repatriateTheseNodes) {
                        repatriatenode.getParentNode().removeChild(repatriatenode);
                        m_lastDeletedNode.appendChild(repatriatenode);
                    }

                    //append these repatriated nodes to the lastDeletedNode

                } else {
                Node copyofParent = this.m_parentofDeleteMarker.cloneNode(false);
                for (nodeNext = elemDelChangeMark.getNextSibling(); nodeNext != null;) {
                    repatriateTheseNodes.add(nodeNext);
                    nodeNext = nodeNext.getNextSibling();
                }
                //test current preceding node to node after parent marker ... if it is NOT an empty paragrph
                //we simply append these nodes as children of that node
                Node precedingofparentMarker = this.m_nodeAfterParentOfDeleteMarker.getPreviousSibling();
                if (compareNodeSignatures(precedingofparentMarker, this.m_parentofDeleteMarker) && precedingofparentMarker.hasChildNodes()) {
                    for (Node repatriatenode : repatriateTheseNodes) {
                        repatriatenode.getParentNode().removeChild(repatriatenode);
                        precedingofparentMarker.appendChild(repatriatenode);
                    }
                } else {
                    for (Node repatriatenode : repatriateTheseNodes) {
                        repatriatenode.getParentNode().removeChild(repatriatenode);
                        copyofParent.appendChild(repatriatenode);
                    }
                    this.m_nodeAfterParentOfDeleteMarker.getParentNode().insertBefore(copyofParent, m_nodeAfterParentOfDeleteMarker);
                }
                }
                this.m_deleteMarker.getParentNode().removeChild(m_deleteMarker);
            } else if ((this.m_bmarkerHasNoSiblings)) {
                m_deleteMarker.getParentNode().getParentNode().removeChild(m_deleteMarker.getParentNode());
            } else {
                m_deleteMarker.getParentNode().removeChild(m_deleteMarker);
            }
            bState = true;
            return bState;
            }
      
    }

    /**
     * Checks if a node has any sibling nodes - if there no sibling nodes -- this is the only
     * node contained by the parent node
     * @param markerNode
     * @return
     */
    private boolean hasNodeGotSiblings(Node markerNode) {
        boolean prevSibling = false,
                nextSibling = false;

        if (markerNode.getNextSibling() == null) {
            nextSibling = false;
        } else {
            nextSibling = true;
        }

        if (markerNode.getPreviousSibling() == null) {
            prevSibling = false;
        } else {
            prevSibling = true;
        }

        return prevSibling | nextSibling;
    }

    /**
     * Adapted from DOMUtils - compares node signatures in terms of node name,
     * namespace, attributes - but NOT content
     *
     * @param aNode
     * @param bNode
     * @return
     */
    private boolean compareNodeSignatures(Node aNode, Node bNode) {
        if ((aNode == null) || (bNode == null)) {
            return false;
        }

        if (aNode.getNodeType() != bNode.getNodeType()) {
            return false;
        }

        if (aNode instanceof Element) {
            Element aElement = (Element) aNode;
            Element bElement = (Element) bNode;

            // compare local names
            if (!aElement.getLocalName().equals(bElement.getLocalName())) {
                log.info("compareNodeSignatures : local names did not match");

                return false;
            }

            // compare NS uris
            String aElementNSUri = aElement.getNamespaceURI();
            String bElementNSUri = bElement.getNamespaceURI();

            if (((bElementNSUri == null) && (aElementNSUri != null))
                    || ((bElementNSUri != null) &&!bElementNSUri.equals(aElementNSUri))) {
                log.info("compareNodeSignatures : namespace URIs did not match");

                return false;
            }

            String elementName = "{" + bElement.getNamespaceURI() + "}" + aElement.getLocalName();

            // compare attributes
            NamedNodeMap aAttrs = aElement.getAttributes();
            NamedNodeMap bAttrs = bElement.getAttributes();

            // first count attributes
            if (this.countNonNamespaceAttribures(aAttrs) != this.countNonNamespaceAttribures(bAttrs)) {
                return false;
            }

            // then match attributes if the count is equal
            for (int i = 0; i < bAttrs.getLength(); i++) {
                Attr expectedAttr = (Attr) bAttrs.item(i);

                if (expectedAttr.getName().startsWith("xmlns")) {
                    continue;
                }

                Attr actualAttr = null;

                if (expectedAttr.getNamespaceURI() == null) {
                    actualAttr = (Attr) aAttrs.getNamedItem(expectedAttr.getName());
                } else {
                    actualAttr = (Attr) aAttrs.getNamedItemNS(expectedAttr.getNamespaceURI(),
                            expectedAttr.getLocalName());
                }

                if (actualAttr == null) {
                    log.info("compareNodeSignatures " + elementName + ": No attribute found:" + expectedAttr);

                    return false;
                }

                if (!expectedAttr.getValue().equals(actualAttr.getValue())) {
                    log.info("compareNodeSignatures " + elementName + ": Attribute values do not match: "
                             + expectedAttr.getValue() + " " + actualAttr.getValue());

                    return false;
                }
            }

            return true;
        } else {
            log.info("Problem : currently only parent nodes which are elements are handled");

            return false;
        }
    }

    /**
     * Counts the non -namespaced attributes in an attribute map
     * @param attrs
     * @return
     */
    private int countNonNamespaceAttribures(NamedNodeMap attrs) {
        int n = 0;

        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);

            if (!attr.getName().startsWith("xmlns")) {
                n++;
            }
        }

        return n;
    }

    /**
     * Gets all the sibling nodes between a start and end change marker
     * @param changeId
     * @return
     * @throws Exception
     */
    public NodeList getNodesBetweenInsertMarkers(String changeId) throws Exception {
        String xpathExpr = "//text:change-start[@text:change-id='" + changeId
                           + "']/following-sibling::node()[following::text:change-end[@text:change-id='" + changeId
                           + "']]";
        NodeList insertNodes = (NodeList) this.m_docXpath.evaluate(xpathExpr,
                                   this.m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);

        return insertNodes;
    }

    /**
     * Gets a change mark based on the change id - can be used to return both insert and delete change marks
     * in the case of delete change marks a NodeList wiht a single item will be returned.
     * @param changeId
     * @return
     * @throws Exception
     */
    public NodeList getMarkerNodesForChange(String changeId) throws Exception {
        String   xpathExpr   = "//node()[@text:change-id='" + changeId + "']";
        NodeList markerNodes = (NodeList) this.m_docXpath.evaluate(xpathExpr,
                                   this.m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);

        return markerNodes;
    }

    /**
     * <p>Helper function for getStructuredChangeType()
     * gets the text:deletion or text:insertion element</p>
     * @param textChangedRegion
     * @return
     */
    private ArrayList<Node> getChange(TextChangedRegionElement textChangedRegion) {
        ArrayList<Node> textChanges = new ArrayList<Node>(0);
        NodeList        nn          = textChangedRegion.getChildNodes();

        for (int i = 0; i < nn.getLength(); i++) {
            Node textChange = (Node) nn.item(i);

            // ignore text nodes and other kinds of nodes
            if (textChange.getNodeType() == Node.ELEMENT_NODE) {
                textChanges.add(textChange);
            }
        }

        return textChanges;
    }

    /**
     * <p>identifies and returns a text:deletion or text:insertion element from a change region</p>
     * @param textChangedRegion
     * @return
     */
    public StructuredChangeType getStructuredChangeType(TextChangedRegionElement textChangedRegion) {
        StructuredChangeType scType         = new StructuredChangeType();
        String               changedId      = textChangedRegion.getAttribute("text:id");
        ArrayList<Node>      elementChanges = getChange(textChangedRegion);

        log.debug("node name = " + elementChanges.size());

        String elemInsertion = TextInsertionElement.ELEMENT_NAME.getLocalName();
        String elemDeletion  = TextDeletionElement.ELEMENT_NAME.getLocalName();

        if (checkIfReplacementChange(elementChanges)) {

            // this is a replacement change a deletion + insertion
            scType.changetype = "replacement";
            scType.changeId   = changedId;
            scType.elementChange.add(elementChanges.get(0));
            scType.elementChange.add(elementChanges.get(1));
        } else {
            Node   elementChange  = elementChanges.get(0);
            String elemChangeName = elementChange.getLocalName();

            scType.changeId = changedId;

            if (elemChangeName.equals(elemDeletion)) {

                // this is the deletion pattern
                scType.changetype = "deletion";
                scType.elementChange.add(elementChange);
            } else if (elemInsertion.equals(elemChangeName)) {

                // this is the insertion pattern
                scType.changetype = "insertion";
                scType.elementChange.add(elementChange);
            }
        }

        return scType;
    }

    /**
     * A replacement change has both deletion and insertion nodes
     * @param changeNodes
     * @return
     */
    private boolean checkIfReplacementChange(ArrayList<Node> changeNodes) {
        boolean bTrue      = false;
        boolean bInsertion = false;
        boolean bDeletion  = false;

        for (Node changeNode : changeNodes) {
            if (changeNode.getNodeName().equals("text:insertion")) {
                bInsertion = true;
            }

            if (changeNode.getNodeName().equals("text:deletion")) {
                bDeletion = true;
            }
        }

        if (bInsertion && bDeletion) {
            return true;
        }

        return false;
    }

    /**
     * <p>Returncs change info as a hashmap</p>
     * <p>The following properties are available in the HashMap :</p>
     * <ul>
     * <li>changeType - the type of the change</li>
     * <li>dcCreator - the author of the change </li>
     * <li>dcDate - the date of the change</li>
     * <li>changeId - the change id</li>
     * <li>changeText - the inserted or deleted text of the change</li>
     * </ul>
     * @param scChangeType
     * @return
     */
    public HashMap<String, Object> getChangeInfo(StructuredChangeType scChangeType) {
        HashMap<String, Object> changeInfo = new HashMap<String, Object>();
        ArrayList<Node>         changes    = scChangeType.elementChange;

        // in the case of the replacement patter this will be the text:deletion element
        Node   elemInsOrDel = changes.get(0);
        String dcCreator    = getChangeInfoDcCreator(elemInsOrDel);
        String dcDate       = getChangeInfoDcDate(elemInsOrDel);

        changeInfo.put("changeType", scChangeType.changetype);
        changeInfo.put("dcCreator", dcCreator);
        changeInfo.put("dcDate", dcDate);
        changeInfo.put("changeId", scChangeType.changeId);

        if (scChangeType.changetype.equals("deletion")) {
            String deletedText = getDeletedText(elemInsOrDel);

            changeInfo.put("changeText", deletedText);
        }

        if (scChangeType.changetype.equals("insertion")) {
            String insertedText = getInsertedText(scChangeType.changeId);

            changeInfo.put("changeText", insertedText);
        }

        if (scChangeType.changetype.equals("replacement")) {
            String replacedText = getDeletedText(elemInsOrDel);

            changeInfo.put("changeText", replacedText);
        }

        return changeInfo;
    }

    /**
     * <p>
     * The xml for text:deletion element looks like below -- [1]<br />
     * we want the deleted text which is all the child elements outside the<br />
     * &lt;office:change-info..&gt; block so we use the xpath to match all children excep<br />
     * the change-info element<br />
     * <br />
     * [1] --<br />
     * &lt;text:deletion&gt;<br />
     * &lt;office:change-info&gt;<br />
     * &lt;dc:creator&gt;Ashok Hariharan&lt;/dc:creator&gt;<br />
     * &lt;dc:date&gt;2010-02-09T12:38:00&lt;/dc:date&gt;<br />
     * &lt;/office:change-info&gt;<br />
     * &lt;text:h text:style-name=&quot;Heading_20_1&quot; text:outline-level=&quot;1&quot;&gt;Triffids&lt;/text:h&gt;<br />
     * &lt;/text:deletion&gt;<br />
     * <br /></p>
     *
     * @param elemDeletion - the text:deletion element
     * @return NodeList of al the deleted nodes
     */
    public NodeList getDeletedNodes(Node elemDeletion) {
        NodeList matchedNodes = null;

        try {

            /*
             * Note that here we have to use the ./ context for xpath since we
             * want to search from the context node onwards... otherwise XPath will return
             * results from the document root
             */
            String xPathExpr = "./child::*[local-name() != 'change-info']";

            matchedNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr, elemDeletion, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            log.error("getDeletedText : " + ex.getMessage(), ex);
        }

        return matchedNodes;
    }

    /**
     * Adds an additional search filter to the getDeletedNodes API
     * @param elemDeletion the text:deletion element
     * @param xpathFilter any kind of xpathFilter extension e.g. //text:section
     * @return
     */
    public NodeList getDeletedNodesExt(Node elemDeletion, String xpathFilter) {
        NodeList matchedNodes = null;

        try {

            /*
             * Note that here we have to use the ./ context for xpath since we
             * want to search from the context node onwards... otherwise XPath will return
             * results from the document root
             */
            String xPathExpr = "./child::*[local-name() != 'change-info']" + xpathFilter;

            matchedNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr, elemDeletion, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            log.error("getDeletedText : " + ex.getMessage(), ex);
        }

        return matchedNodes;
    }

    /**
     * <p>Helper function on getDeletedNodes, returns a String with the text content
     * of all the nodes in the deleted nodes NodeList</p>
     * @param elemDeletion the text:deletion element
     * @return String content of all the deleted nodes
     */
    public String getDeletedText(Node elemDeletion) {
        StringBuffer sbfDeletedText = new StringBuffer("");
        NodeList     matchedNodes   = getDeletedNodes(elemDeletion);

        for (int i = 0; i < matchedNodes.getLength(); i++) {
            Node foundNode = matchedNodes.item(i);

            sbfDeletedText.append(foundNode.getTextContent() + "\n");
        }

        return sbfDeletedText.toString();
    }

    /**
     * <p>Helper function on getInsertedNodes - converts the NodeList to a string,
     * by evaluation text content of nodes.</p>
     * @param elemInsertion
     * @param changeId
     * @return
     */
    public String getInsertedText(String changeId) {
        StringBuffer sbInsText    = new StringBuffer();
        NodeList     matchedNodes = getInsertedNodes(changeId);

        for (int i = 0; i < matchedNodes.getLength(); i++) {
            Node foundNode = matchedNodes.item(i);

            sbInsText.append(foundNode.getTextContent() + "\n");
        }

        return sbInsText.toString();
    }

    /**
     * <p>This function extracts inserted text corresponding to a change mark.<br />
     * [1] - Is the change mark header for the text insertion. <br />
     * <br />
     * The text:id attribute of the changed-region element corresponds to the <br />
     * change-start and change-end element ids that appear in the actual text <br />
     * (See [2]). <br />
     * These markings demarcate the beginning and the end of the newly inserted <br />
     * text.<br />
     * <br />
     * The XPath expression to extract the changed text goes like this :<br />
     * <br />
     * //text:change-start[@text:change-id='ct-change-id']/following::text()<br />
     * [not(preceding::text:change-end[@text:change-id='ct-change-id'])]<br />
     * <br />
     * This translates to :<br />
     * -- get the change-start element having id 'ct-change-id<br />
     * -- get all the following text() elements from that element<br />
     * -- until the one preceding the change-ed element have the<br />
     * the id 'ct-change-id'<br />
     * <br />
     * <br />
     * [1] --<br />
     * <pre>
     * <br />
     * &lt;text:changed-region text:id=&quot;ct472232592&quot;&gt;<br />
     * &lt;text:insertion&gt;<br />
     *   &lt;office:change-info&gt;<br />
     *       &lt;dc:creator&gt;Ashok Hariharan&lt;/dc:creator&gt;<br />
     *       &lt;dc:date&gt;2010-02-17T17:10:00&lt;/dc:date&gt;<br />
     *   &lt;/office:change-info&gt;<br />
     * &lt;/text:insertion&gt;<br />
     * &lt;/text:changed-region&gt;<br />
     * </pre>
     * <br />
     * <br />
     * <br />
     * [2] --<br />
     * <br />
     * <pre>
     * &lt;text:p text:style-name=&quot;Standard&quot;&gt;Neque porro quisquam est, qui dolorem ipsum<br />
     * quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius<br />
     * modi &lt;text:change text:change-id=&quot;ct468041424&quot;/&gt;&lt;text:change-start<br />
     * text:change-id=&quot;ct472232592&quot;/&gt;quisquam est, qui dolorem&lt;/text:p&gt;<br />
     * &lt;text:p text:style-name=&quot;Standard&quot;/&gt;<br />
     * &lt;text:p text:style-name=&quot;Standard&quot;/&gt;<br />
     * &lt;text:p text:style-name=&quot;Standard&quot;&gt;Sed ut perspiciatis unde omnis iste natus<br />
     * error sit voluptatem accusantium doloremque laudantium, totam rem aperiam,<br />
     * nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas<br />
     * nulla pariatur?&lt;text:change-end text:change-id=&quot;ct472232592&quot;/&gt;&lt;text:change<br />
     * text:change-id=&quot;ct472209600&quot;/&gt;&lt;/text:p&gt;<br />
     * <br />
     * </pre>
     * </p>
     * @param elemInsertion - text:insertion element
     * @param changeId - id of the changed-region containing the text:insertion element
     * @return a NodeList of all the newly inserted text() nodes
     */
    public NodeList getInsertedNodes(String changeId) {
        NodeList matchedNodes = null;
        String   xPathExpr    = "//text:change-start[@text:change-id='" + changeId
                                + "']/following::text()[not(preceding::text:change-end[@text:change-id='" + changeId
                                + "'])]";

        try {
            matchedNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr,
                    this.m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);
        } catch (Exception ex) {
            log.error("getInsertedNodes :" + ex.getMessage(), ex);
        }

        return matchedNodes;
    }

    /*
     *
     * <p>This is a version of the inserted text API that uses DOM traversal
     *
     * private String getInsertedText(OdfElement elemInsertion, String changeId) {
     *   StringBuffer sbInsText = new StringBuffer();
     *   String xPathExpr1 =   "//text:change-start[@text:change-id='" + changeId +"']/following::text ()" ;
     *   String xPathExpr2 =   "//text:change-start[@text:change-id='"+changeId+"']/following::" +
     *             "*[@text:change-id='"+changeId+"'][1]/following::text()";
     *     try {
     *       NodeList matchedNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr1, this.m_docHelper.getOdfDocument().getContentDom(),XPathConstants.NODESET);
     *       NodeList exceptNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr2, this.m_docHelper.getOdfDocument().getContentDom(),XPathConstants.NODESET);
     *       for (int i = 0; i < matchedNodes.getLength(); i++) {
     *           Node foundNode = matchedNodes.item(i);
     *           boolean bDropThisNode = false;
     *           System.out.println("nname = " + foundNode.getLocalName());
     *           System.out.println("nvalue = " + foundNode.getTextContent());
     *           for (int j = 0; j < exceptNodes.getLength(); j++) {
     *               short pos =  exceptNodes.item(j).compareDocumentPosition(foundNode);
     *               System.out.println("\tenname = " + exceptNodes.item(j).getLocalName());
     *               System.out.println("\tenvalue = " + exceptNodes.item(j).getTextContent());
     *               System.out.println("\tenpos = " + pos);
     *               //we want matching nodes to be removed
     *               if (pos == 0) {
     *                   bDropThisNode = true;
     *               }
     *           }
     *           if (false == bDropThisNode)
     *               sbInsText.append(foundNode.getTextContent());
     *       }
     *   } catch (Exception ex) {
     *       log.error("getDeletedText : " + ex.getMessage(), ex);
     *   }
     *   return sbInsText.toString();
     * }
     */

    /**
     * <p>Get dc:creator for the change</p>
     * @param elem
     * @return
     */
    private String getChangeInfoDcCreator(Node elem) {
        try {
            String   xPathExpr   = "./descendant::dc:creator";
            NodeList changeElems = (NodeList) m_docXpath.evaluate(xPathExpr, elem, XPathConstants.NODESET);

            if (changeElems.getLength() > 0) {
                Node dcCreator = changeElems.item(0);

                return dcCreator.getTextContent();
            }
        } catch (XPathExpressionException ex) {
            log.error("getChangeInfoDcCreator : " + ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * <p>Get dc:date for change info</p>
     * @param elem
     * @return
     */
    private String getChangeInfoDcDate(Node elem) {
        String outDate = "";

        try {
            String   xPathExpr   = "./descendant::dc:date";
            NodeList changeElems = (NodeList) m_docXpath.evaluate(xPathExpr, elem, XPathConstants.NODESET);

            if (changeElems.getLength() > 0) {
                Node dcDate = changeElems.item(0);

                /*
                 * all dates in odf uses the ODFDOM data type DateTime
                 * we convert to the XML date type and then to Java date type
                 *
                 * Update : we dont do conversion .. the frontend application does the conversion
                 */
                outDate = dcDate.getTextContent();
            }
        } catch (XPathExpressionException ex) {
            java.util.logging.Logger.getLogger(BungeniOdfTrackedChangesHelper.class.getName()).log(Level.SEVERE, null,
                                               ex);
        }

        return outDate;
    }

    /**
     * <p>Returns the handle to the BungeniOdfDocumentHelper </p>
     * @return
     */
    public BungeniOdfDocumentHelper getOdfDocumentHelper() {
        return m_docHelper;
    }

    /**
     * <p>Returns the &lt;text:change-start&gt; element for a change id. </p>
     * @param changeId
     * @return TextChangeElementStart element
     */
    public TextChangeStartElement getChangeStartItem(String changeId) {
        TextChangeStartElement startNode = null;

        try {
            String xPathExpr = "//text:change-start[@text:change-id='" + changeId + "']";
            Node   foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(),
                                   XPathConstants.NODE);

            if (foundNode != null) {
                startNode = (TextChangeStartElement) foundNode;
            }
        } catch (Exception ex) {
            log.error("getChangeStartitem : " + changeId + ":" + ex.getMessage(), ex);
        }

        return startNode;
    }

    /**
     * <p>Returns the <text:change-end> element for a change id. </p>
     * @param changeId
     * @return
     */
    public TextChangeEndElement getChangeEndItem(String changeId) {
        TextChangeEndElement endNode = null;

        try {
            String xPathExpr = "//text:change-end[@text:change-id='" + changeId + "']";
            Node   foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(),
                                   XPathConstants.NODE);

            if (foundNode != null) {
                endNode = (TextChangeEndElement) foundNode;
            }
        } catch (Exception ex) {
            log.error("getChangeStartitem : " + changeId + ":" + ex.getMessage(), ex);
        }

        return endNode;
    }

    /**
     * <p>Returns the <text:change> item for a change id</p>
     * @param changeId
     * @return
     */
    public TextChangeElement getChangeItem(String changeId) {
        TextChangeElement changeNode = null;

        try {
            String xPathExpr = "//text:change[@text:change-id='" + changeId + "']";
            Node   foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(),
                                   XPathConstants.NODE);

            if (foundNode != null) {
                changeNode = (TextChangeElement) foundNode;
            }
        } catch (Exception ex) {
            log.error("getChangeStartitem : " + changeId + ":" + ex.getMessage(), ex);
        }

        return changeNode;
    }

    public NodeList getAllChangeNodes() throws Exception {
        NodeList nnodeList = null;

        try {
            String xpathExpr = "//node()[name()='text:change-start' or name()='text:change']";

            nnodeList = (NodeList) this.m_docXpath.evaluate(xpathExpr, m_docHelper.getOdfDocument().getContentDom(),
                    XPathConstants.NODESET);
        } catch (Exception ex) {
            log.error("getAllChangeNodes : " + ex.getMessage());

            throw ex;
        }

        return nnodeList;
    }

    /**
     * Helper API returns all changes in the document as change type objects
     * @return
     */
    public List<StructuredChangeType> getAllChanges() {
        List<TextChangedRegionElement> changeRegions = this.getChangedRegions(this.getTrackedChangeContainer());
        List<StructuredChangeType> changes       = new ArrayList<StructuredChangeType>(0);

        for (TextChangedRegionElement TextChangedRegionElement : changeRegions) {
            changes.add(this.getStructuredChangeType(TextChangedRegionElement));
        }

        return changes;
    }

    public class StructuredChangeType {

        /**
         * The child elements contained within a TextChangedRegionElement
         */
        public ArrayList<Node> elementChange = new ArrayList<Node>(0);

        /**
         * change id element for the change
         */
        public String changeId;

        /**
         * The type of change : "insertion" / "deletion" / "replacement"
         */
        public String changetype;
    }
}
