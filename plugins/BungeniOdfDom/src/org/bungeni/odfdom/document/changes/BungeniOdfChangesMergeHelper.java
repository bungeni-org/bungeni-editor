package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * This class merges changes (accepts / rejects changes) into a document
 * Additionally it allows merging changes from one user into another user's changes.
 * @author Ashok Hariharan
 */
public class BungeniOdfChangesMergeHelper {
    private static org.apache.log4j.Logger log             =
        Logger.getLogger(BungeniOdfChangesMergeHelper.class.getName());
    private BungeniOdfTrackedChangesHelper m_changesHelper = null;
    private BungeniOdfDocumentHelper m_docHelper = null;
    private XPath                          m_docXpath      = null;



    public BungeniOdfChangesMergeHelper(BungeniOdfTrackedChangesHelper docH) {
        m_changesHelper = docH;
        m_docXpath      = m_changesHelper.getOdfDocumentHelper().getOdfDocument().getXPath();
        m_docHelper = m_changesHelper.getOdfDocumentHelper();
    }

    /**
     * This API is used to merge one users changes into another. The source user's changes are appropriated into the
     * target user's changes. The standard use case involves merely substituting all changes by 'source user' into 'target user' changes.
     * The non-typical use case is the classical user-> review user workflow, where a user marks changes on the document, a review user marks
     * further changes on the document - and may also 'change the changes' made by the original user.
     * @param sourceUser - the review user
     * @param targetUser - the user whose changes are being reviewed
     * @return
     */
    public boolean mergeChanges(String sourceUser, String targetUser) {
       //Get all the changed regions of the sourceUser
        ArrayList<OdfTextChangedRegion> changesByTarget =
            m_changesHelper.getChangedRegionsByCreator(m_changesHelper.getTrackedChangeContainer(), sourceUser);
        for (OdfTextChangedRegion odfTextChangedRegion : changesByTarget) {
            processChange(odfTextChangedRegion, sourceUser, targetUser);
        }

        return true;
    }

    private void processChange (OdfTextChangedRegion odfTextChangedRegion, String sourceUser, String targetUser ) {
            try {
                //First get the id of the change region
                String changeId  = odfTextChangedRegion.getTextIdAttribute();
                //Then check if the change id has an immediately preceding change-end
                //the XPath expression will eliminate preceding text() nodes.
                String xPathExpr = "//text:change-start[@text:change-id='"+ changeId +"']/preceding-sibling::node()[1][name()='text:change-end']";
                Node nodePrecedingChangeEnd = (Node) m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                if (nodePrecedingChangeEnd != null ) {
                    //Get the id of the changed end element
                    OdfTextChangeEnd endElement = (OdfTextChangeEnd) nodePrecedingChangeEnd;
                    String changeEndId = endElement.getTextChangeIdAttribute();
                    //Using the id of the change-end element get the changed-region for the id.
                    //and check if the dc:creator of the changed-region is the user being merged into (the target user)
                    String xPathChangeRegion = "//text:changed-region[@text:id='" +changeEndId + "']/descendant::dc:creator='" + targetUser + "'";
                    Boolean bResult = (Boolean) m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.BOOLEAN);
                    //if the user matches, the adjacent change is a merge-able change.
                     if (bResult) {
                        //get the node content between change-start and change-end for the sourceUser and place it into the change of the
                        //target user.
                        mergeAdjacentChange(sourceUser, targetUser, changeId, changeEndId);
                    } else {
                        //change is by Unknonw user.
                    }

                }
            } catch (Exception ex) {
               log.error("processChange:" + sourceUser + ", " + targetUser + ", " + ex.getMessage(), ex);
            }
    }

   private void mergeAdjacentChange(String sourceUser, String targetUser, String sourceUserChangeid, String targetUserChangeid) {

        OdfTextChangedRegion sourceChangeRegion = this.m_changesHelper.getChangedRegionById(sourceUserChangeid);
        OdfTextChangedRegion targetChangeRegion = this.m_changesHelper.getChangedRegionById(targetUserChangeid);
        StructuredChangeType scSourceType = this.m_changesHelper.getStructuredChangeType(sourceChangeRegion);
        StructuredChangeType scTargetType = this.m_changesHelper.getStructuredChangeType(targetChangeRegion);
        //Merge only if the change types match and the change is an insert
        if (scSourceType.changetype.equals(scTargetType.changetype)  && scSourceType.changetype.equals("insertion")) {

            OdfTextChangeStart sourceStart = m_changesHelper.getChangeStartItem(sourceUserChangeid);
            OdfTextChangeEnd sourceEnd = m_changesHelper.getChangeEndItem(sourceUserChangeid);
            Node sourceNextSibling = sourceStart.getNextSibling();
            //First build  a cloned array of change nodes
            ArrayList<Node> clonedNodes = new ArrayList<Node>(0);
            while (!sourceNextSibling.isSameNode(sourceEnd)) {
                Node clonedNode = sourceNextSibling.cloneNode(true);
                clonedNodes.add(clonedNode);
                System.out.println(sourceNextSibling.getNodeName());
                sourceNextSibling = sourceNextSibling.getNextSibling();
            }
            //Now we delete the changed nodes which we have cloned
            sourceNextSibling = sourceStart.getNextSibling();
            while(!sourceNextSibling.isSameNode(sourceEnd)) {
                sourceNextSibling.getParentNode().removeChild(sourceNextSibling);
                sourceNextSibling = sourceStart.getNextSibling();
            }
            //Remove the bounding start and end nodes
            sourceStart.getParentNode().removeChild(sourceStart);
            sourceEnd.getParentNode().removeChild(sourceEnd);
            //Remove the source change region
            sourceChangeRegion.getParentNode().removeChild(sourceChangeRegion);

            //Get the ending node of the target node
            Node targetEnd = m_changesHelper.getChangeEndItem(targetUserChangeid);
            //Now Iterate through the cloned nodes and add them before the change-end element of the target.
            for (Node node : clonedNodes) {
                targetEnd.getParentNode().insertBefore(node, targetEnd);
            }
        }
   }

    /*
    private void mergeAdjacentChange(String sourceUser, String targetUser, String sourceUserChangeid, String targetUserChangeid) {
        ///If the source change is an insert and the target change is an insert they can be successfully merged.
        OdfTextChangedRegion sourceChangeRegion = this.m_changesHelper.getChangedRegionById(sourceUserChangeid);
        OdfTextChangedRegion targetChangeRegion = this.m_changesHelper.getChangedRegionById(targetUserChangeid);
        StructuredChangeType scSourceType = this.m_changesHelper.getStructuredChangeType(sourceChangeRegion);
        StructuredChangeType scTargetType = this.m_changesHelper.getStructuredChangeType(targetChangeRegion);
        if (scSourceType.changetype.equals(scTargetType.changetype)  && scSourceType.changetype.equals("insertion")) {
            NodeList sourceChangesList = m_changesHelper.getInsertedNodes(sourceUserChangeid);
            NodeList targetChangesList = m_changesHelper.getInsertedNodes(targetUserChangeid);
            Node lastTargetItem = targetChangesList.item(targetChangesList.getLength() -1 );
            //the next sibling of the last item should get you the <text:change-end ... /> element
            Node nodeChangeEnd = lastTargetItem.getNextSibling();
            for (int i = 0; i < sourceChangesList.getLength(); i++) {
                try {
                    Node sourceNode = sourceChangesList.item(i);
                    if (sourceNode.getNodeType() == Node.TEXT_NODE) {
                        Text srcText = (Text) sourceNode;
                        nodeChangeEnd.getParentNode().insertBefore(srcText, nodeChangeEnd);
                    }
                } catch (Exception ex) {
                    log.error("mergeAdjacentChange :" + ex.getMessage(), ex);
                }
            }
        }


    }
     *
     */

}
