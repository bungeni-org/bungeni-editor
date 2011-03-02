package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.odftoolkit.odfdom.dom.element.text.TextChangeElement;
import org.odftoolkit.odfdom.dom.element.text.TextChangeEndElement;
import org.odftoolkit.odfdom.dom.element.text.TextChangeStartElement;
import org.odftoolkit.odfdom.dom.element.text.TextChangedRegionElement;

/**
 * This class merges changes (accepts / rejects changes) into a document
 * Additionally it allows merging changes from one user into another user's changes.
 * @author Ashok Hariharan
 */
public class BungeniOdfChangesMergeHelper {
    private static org.apache.log4j.Logger log             =
        Logger.getLogger(BungeniOdfChangesMergeHelper.class.getName());
    private BungeniOdfTrackedChangesHelper m_changesHelper = null;
    private BungeniOdfDocumentHelper       m_docHelper     = null;
    private XPath                          m_docXpath      = null;

    /**
     * Sets up the change merge helper, uses the ODF tracked changes helper 
     * @param docH
     */
    public BungeniOdfChangesMergeHelper(BungeniOdfTrackedChangesHelper docH) {
        m_changesHelper = docH;
        try {
            m_docXpath = m_changesHelper.getOdfDocumentHelper().getOdfDocument().getContentDom().getXPath();
        } catch (Exception ex) {
           log.error("Exception while trying to get XPath handle");
        }
        m_docHelper     = m_changesHelper.getOdfDocumentHelper();
    }

    /**
     * <p>This API is used to merge one users changes into another. The source user's
     * changes are appropriated into the target user's changes. The standard use case
     * involves merely substituting all changes by 'source user' into 'target user' changes.
     * The non-typical use case is the classical user-> review user workflow, where a user
     * marks changes on the document, a review user marks further changes on the document -
     * and may also 'change the changes' made by the original user.</p>
     * @param sourceUser - the review user
     * @param targetUser - the user whose changes are being reviewed
     * @return
     */
    public boolean mergeChanges(String sourceUser, String targetUser) {

        // Get all the changed regions of the sourceUser
        ArrayList<TextChangedRegionElement> changesByTarget =
            m_changesHelper.getChangedRegionsByCreator(m_changesHelper.getTrackedChangeContainer(), sourceUser);

        for (TextChangedRegionElement odfTextChangedRegion : changesByTarget) {
            System.out.println("changed region id = " + odfTextChangedRegion.getTextIdAttribute());
            processChange(odfTextChangedRegion, sourceUser, targetUser);
        }

        processUserSubstitutionMerge(sourceUser, targetUser);

        return true;
    }

    /**
     * Private API processes a single changed region - 'insetion' / 'replacement' merges are processed
     * by this api.
     * @param odfTextChangedRegion
     * @param sourceUser
     * @param targetUser
     */
    private void processChange(TextChangedRegionElement odfTextChangedRegion, String sourceUser, String targetUser) {
        StructuredChangeType scType = null;

        try {
            scType = m_changesHelper.getStructuredChangeType(odfTextChangedRegion);

            if (scType.changetype.equals("insertion")) {

                // A "insertion" merge was detected
                processInsertionMerge(scType, odfTextChangedRegion, sourceUser, targetUser);
            } else if (scType.changetype.equals("replacement")) {

                // A "replacement" pattern was detected
                System.out.println(scType.changetype);
                processReplacementMerge(scType, odfTextChangedRegion, sourceUser, targetUser);
            }
        } catch (Exception ex) {
            log.error("processChange:" + sourceUser + ", " + targetUser + ", " + " change =  "
                      + odfTextChangedRegion.getTextIdAttribute() + " - " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param scType
     * @param odfTextChangedRegion
     * @param sourceUser
     * @param targetUser
     */
    private void processInsertionMerge(StructuredChangeType scType, TextChangedRegionElement odfTextChangedRegion,
                                       String sourceUser, String targetUser) {
        try {

            // First get the id of the change region
            String changeId = odfTextChangedRegion.getTextIdAttribute();

            // Then check if the change id has an immediately preceding change-end
            // the XPath expression will eliminate preceding text() nodes.
            String xPathExpr = "//text:change-start[@text:change-id='" + changeId
                               + "']/preceding-sibling::node()[1][name()='text:change-end']";
            Node nodePrecedingChangeEnd = (Node) m_docXpath.evaluate(xPathExpr,
                                              m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);

            if (nodePrecedingChangeEnd != null) {

                // Get the id of the changed end element
                TextChangeEndElement endElement  = (TextChangeEndElement) nodePrecedingChangeEnd;
                String           changeEndId = endElement.getTextChangeIdAttribute();

                // Using the id of the change-end element get the changed-region for the id.
                // and check if the dc:creator of the changed-region is the user being merged into (the target user)
                String xPathChangeRegion = "//text:changed-region[@text:id='" + changeEndId
                                           + "']/descendant::dc:creator='" + targetUser + "'";
                Boolean bResult = (Boolean) m_docXpath.evaluate(xPathExpr,
                                      m_docHelper.getOdfDocument().getContentDom(), XPathConstants.BOOLEAN);

                // if the user matches, the adjacent change is a merge-able change.
                if (bResult) {

                    // get the node content between change-start and change-end for the
                    // sourceUser and place it into the change of the
                    // target user.
                    int nRet = mergeAdjacentInsertChange(sourceUser, targetUser, changeId, changeEndId);

                    if (nRet == -1) {
                        log.debug("Insert change was dropped for " + changeId + " processing as userSubstitutionMerge");
                    }
                } else {

                    // change is by Unknonw user.
                }
            }
        } catch (Exception ex) {
            log.error("processInsertionMerge : " + ex.getMessage(), ex);
        }
    }

    /**
     * Replacement pattern is one where the reviewer deletes some changes submitted by a user.
     * the replacement change is a <text:deletion> element
     * the original change is a <text:insertion> with a reference to the user information
     * The merge for the replacement pattern works like this ... :
     * 1) remove the <text:change > for the change id in the document i.e. remove the reference tot he delete
     * 2) remove the changed region from the document
     * 3) merge any post-adjacent i.e. after the delete <text:insert > with any pre-adjacent <text:insert>
     *
     * @param scType
     * @param odfTextChangedRegion
     * @param sourceUser
     * @param targetUser
     */
    private void processReplacementMerge(StructuredChangeType scType, TextChangedRegionElement odfTextChangedRegion,
            String sourceUser, String targetUser) {

        // Get the <text:change> element and delete it
        TextChangeElement changeItem = m_changesHelper.getChangeItem(scType.changeId);

        changeItem.getParentNode().removeChild(changeItem);
        odfTextChangedRegion.getParentNode().removeChild(odfTextChangedRegion);
    }

    /**
     * Substitution Merge searches and replaces the user name for changes with another user.
     * This is done for changes which are inserted by the clerk which dont overlap changes
     * made by a mp user.
     * @param sourceUser
     * @param targetUser
     */
    private void processUserSubstitutionMerge(String sourceUser, String targetUser) {
        try {

            // First process insert changes
            String xPathExprIns = "//dc:creator[text()='" + sourceUser
                                  + "' and ancestor::node()[name()='text:insertion']]";
            NodeList insertNodesDcCreators = (NodeList) this.m_docXpath.evaluate(xPathExprIns,
                                                 m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);
            int nInsertedNodesasCreator = insertNodesDcCreators.getLength();

            log.debug("inserted nodes as reviewer :" + nInsertedNodesasCreator);

            for (int i = 0; i < nInsertedNodesasCreator; i++) {
                Node insertNodeDcCreator = insertNodesDcCreators.item(i);

                insertNodeDcCreator.setTextContent(targetUser);
                log.debug("processUserSubstitutionMerge insert node dc:creator new - value :"
                          + insertNodeDcCreator.getTextContent());
            }

            // Next process delete changes
            String xPathExprDel = "//dc:creator[text()='" + sourceUser
                                  + "' and ancestor::node()[name()='text:deletion']]";
            NodeList deleteNodesDcCreators = (NodeList) this.m_docXpath.evaluate(xPathExprDel,
                                                 m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);
            int nDeletedNodesasCreator = deleteNodesDcCreators.getLength();

            log.debug("delete nodes as reviewer :" + nDeletedNodesasCreator);

            for (int i = 0; i < nDeletedNodesasCreator; i++) {
                Node deleteNodeDcCreator = deleteNodesDcCreators.item(i);

                deleteNodeDcCreator.setTextContent(targetUser);
                log.debug("processUserSubstitutionMerge insert node dc:creator new - value :"
                          + deleteNodeDcCreator.getTextContent());
            }
        } catch (Exception ex) {
            log.error("processUserSubstitutionMerge, sourceUser = " + sourceUser + ", targetUser = " + targetUser, ex);
        }
    }

    /**
     * This API merges adjacent changes into one change
     * @param sourceUser
     * @param targetUser
     * @param sourceUserChangeid
     * @param targetUserChangeid
     */
    private int mergeAdjacentInsertChange(String sourceUser, String targetUser, String sourceUserChangeid,
            String targetUserChangeid) {
        TextChangedRegionElement sourceChangeRegion = this.m_changesHelper.getChangedRegionById(sourceUserChangeid);
        TextChangedRegionElement targetChangeRegion = this.m_changesHelper.getChangedRegionById(targetUserChangeid);
        StructuredChangeType scSourceType       = this.m_changesHelper.getStructuredChangeType(sourceChangeRegion);
        StructuredChangeType scTargetType       = this.m_changesHelper.getStructuredChangeType(targetChangeRegion);

        // Merge only if the change types match and the change is an insert
        if (scSourceType.changetype.equals(scTargetType.changetype) && scSourceType.changetype.equals("insertion")) {
            TextChangeStartElement sourceStart       = m_changesHelper.getChangeStartItem(sourceUserChangeid);
            TextChangeEndElement   sourceEnd         = m_changesHelper.getChangeEndItem(sourceUserChangeid);
            Node               sourceNextSibling = sourceStart.getNextSibling();

            // First build  a cloned array of change nodes
            ArrayList<Node> clonedNodes = new ArrayList<Node>(0);

            while (!sourceNextSibling.isSameNode(sourceEnd)) {
                Node clonedNode = sourceNextSibling.cloneNode(true);

                clonedNodes.add(clonedNode);
                System.out.println(sourceNextSibling.getNodeName());
                sourceNextSibling = sourceNextSibling.getNextSibling();

                if (sourceNextSibling == null) {

                    // sourceNextSibling should never return null --
                    // it returns null only when the next sibling is an
                    // unclosed element e.g. a </p> occuring between change
                    // start and change-end elements. In this case we do a
                    // user substitution merge.
                    return -1;
                }
            }

            // Now we delete the changed nodes which we have cloned
            sourceNextSibling = sourceStart.getNextSibling();

            while (!sourceNextSibling.isSameNode(sourceEnd)) {
                sourceNextSibling.getParentNode().removeChild(sourceNextSibling);
                sourceNextSibling = sourceStart.getNextSibling();
            }

            // Remove the bounding start and end nodes
            sourceStart.getParentNode().removeChild(sourceStart);
            sourceEnd.getParentNode().removeChild(sourceEnd);

            // Remove the source change region
            sourceChangeRegion.getParentNode().removeChild(sourceChangeRegion);

            // Get the ending node of the target node
            Node targetEnd = m_changesHelper.getChangeEndItem(targetUserChangeid);

            // Now Iterate through the cloned nodes and add them before the change-end element of the target.
            for (Node node : clonedNodes) {
                targetEnd.getParentNode().insertBefore(node, targetEnd);
            }
        }

        return 0;
    }
}
