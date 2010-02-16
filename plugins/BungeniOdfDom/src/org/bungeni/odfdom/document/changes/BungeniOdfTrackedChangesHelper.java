package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.odftoolkit.odfdom.doc.text.OdfTextDeletion;
import org.odftoolkit.odfdom.doc.text.OdfTextInsertion;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import org.odftoolkit.odfdom.type.Date;

/**
 * This class assists in extracting tracked changes from a ODF document
 * @author Ashok
 */
public class BungeniOdfTrackedChangesHelper {
    private static org.apache.log4j.Logger log         =
        Logger.getLogger(BungeniOdfTrackedChangesHelper.class.getName());
    private BungeniOdfDocumentHelper       m_docHelper = null;
    private XPath                          m_docXpath  = null;

    /**
     * Track changes helper is initiialized using a Document Helper object
     * @param docH
     */
    public BungeniOdfTrackedChangesHelper(BungeniOdfDocumentHelper docH) {
        m_docHelper = docH;
        m_docXpath  = m_docHelper.getOdfDocument().getXPath();
    }

    /**
     * Retrieves the container for the changes header text:tracked-changes
     * @return
     */
    public Element getTrackedChangeContainer() {
        Element textTrackedChanges = null;

        try {
            NodeList trackedChanges =
                m_docHelper.getOdfDocument().getContentDom().getElementsByTagName("text:tracked-changes");

            for (int i = 0; i < trackedChanges.getLength(); i++) {

                // this is the tracked change element container
                textTrackedChanges = (Element) trackedChanges.item(i);

                break;

                // get all the changed regions
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return textTrackedChanges;
    }

    /**
     * Get changed regions in a text change container
     * @param changeContainer
     * @return
     */
    public ArrayList<OdfTextChangedRegion> getChangedRegions(Element changeContainer) {
        ArrayList<OdfTextChangedRegion> textChangedRegions = new ArrayList<OdfTextChangedRegion>(0);
        NodeList                        changedRegions     =
            changeContainer.getElementsByTagName("text:changed-region");

        for (int i = 0; i < changedRegions.getLength(); i++) {
            OdfTextChangedRegion textChangedRegion = (OdfTextChangedRegion) (Element) changedRegions.item(i);

            textChangedRegions.add(textChangedRegion);
        }

        return textChangedRegions;
    }

    /**
     * Helper function for getStructuredChangeType()
     * gets the text:deletion or text:insertion element ...
     * @param textChangedRegion
     * @return
     */
    private Element getChange(OdfTextChangedRegion textChangedRegion) {
        Element  textChange = null;
        NodeList nn         = textChangedRegion.getChildNodes();

        for (int i = 0; i < nn.getLength(); i++) {
            textChange = (Element) nn.item(i);

            return textChange;
        }

        return textChange;
    }

    /**
     * identifies and returns a text:deletion or text:insertion element from a change region
     * @param textChangedRegion
     * @return
     */
    public StructuredChangeType getStructuredChangeType(OdfTextChangedRegion textChangedRegion) {
        StructuredChangeType scType        = new StructuredChangeType();
        OdfElement           elementChange = (OdfElement) getChange(textChangedRegion);

        System.out.println("node name = " + elementChange.getLocalName());

        if (elementChange.getLocalName().equals(OdfTextDeletion.ELEMENT_NAME.getLocalName())) {
            scType.changetype    = "deletion";
            scType.elementChange = elementChange;

            return scType;
        } else if (elementChange.getNodeName().equals(OdfTextInsertion.ELEMENT_NAME.getLocalName())) {
            scType.changetype    = "insertion";
            scType.elementChange = elementChange;

            return scType;
        } else {
            return null;
        }
    }


    public void  getChangeInfo(StructuredChangeType scChangeType) {
        if (scChangeType.changetype.equals("deletion")) {
            OdfTextDeletion elemDeletion = (OdfTextDeletion) scChangeType.elementChange;

        } else if (scChangeType.changetype.equals("insertion")) {
            OdfTextInsertion elemInsertion = (OdfTextInsertion) scChangeType.elementChange;
        }
    }

    private String getChangeInfoDcCreator(OdfElement elem) {
        Element dcCreator = (Element) elem.getElementsByTagName("dc:creator");
        return dcCreator.getTextContent();
    }

    private Date getChangeInfoDcDate(OdfElement elem) {
        Element dcDate = (Element) elem.getElementsByTagName("dc:date");
        return Date.valueOf(dcDate.getTextContent());
    }

    public class StructuredChangeType {
        public String     changetype;    // deletion, insertion
        public OdfElement elementChange;
    }
}
