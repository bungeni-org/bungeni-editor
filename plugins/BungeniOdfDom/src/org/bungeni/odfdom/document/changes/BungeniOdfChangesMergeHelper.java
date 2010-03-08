package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.w3c.dom.Node;

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

    /**
     * User whose changes are merged into
     */
    String origUser;

    /**
     * User providing the changes to be merged
     */
    String replWithUser;

    public BungeniOdfChangesMergeHelper(BungeniOdfTrackedChangesHelper docH) {
        m_changesHelper = docH;
        m_docXpath      = m_changesHelper.getOdfDocumentHelper().getOdfDocument().getXPath();
        m_docHelper = m_changesHelper.getOdfDocumentHelper();
    }

    public boolean mergeChanges(String sourceUser, String targetUser) {
   // find all changed regions by sourceUser
        ArrayList<OdfTextChangedRegion> changesByTarget =
            m_changesHelper.getChangedRegionsByCreator(m_changesHelper.getTrackedChangeContainer(), targetUser);
        for (OdfTextChangedRegion odfTextChangedRegion : changesByTarget) {
            processChange(odfTextChangedRegion, sourceUser, targetUser);
        }

        return true;
    }

    private void processChange (OdfTextChangedRegion odfTextChangedRegion, String sourceUser, String targetUser ) {
            try {
                //get the id of the change region
                String changeId  = odfTextChangedRegion.getTextIdAttribute();
                //1 check if the change id has an immediately preceding change-end
                String xPathExpr = "//text:change-start[@text:change-id='"+ changeId +"']/preceding-sibling::node()[1][name()='text:change-end']";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                if (foundNode != null ) {
                    //if it does get the id of the change-end
                    OdfTextChangeEnd endElement = (OdfTextChangeEnd) foundNode;
                    String changeEndId = endElement.getTextChangeIdAttribute();
                    //find the change-region for this id.
                    String xPathChangeRegion = "//text:changed-region[@text:id='" +changeEndId + "']/descendant::dc:creator='" + sourceUser + "'";
                    Boolean bResult = (Boolean) m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.BOOLEAN);
                    if (bResult) {
                        //change is by Mp user
                        //merge the changes
                    } else {
                        //change is by Unknonw user.
                    }

                }
            } catch (Exception ex) {
               log.error("processChange:" + sourceUser + ", " + targetUser + ", " + ex.getMessage(), ex);
            }

    }
}
