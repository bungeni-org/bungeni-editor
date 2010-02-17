package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import java.text.SimpleDateFormat;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.odftoolkit.odfdom.doc.text.OdfTextDeletion;
import org.odftoolkit.odfdom.doc.text.OdfTextInsertion;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.odftoolkit.odfdom.type.DateTime;

/**
 * This class assists in extracting tracked changes from a ODF document
 * @author Ashok Hariharan
 */
public class BungeniOdfTrackedChangesHelper {
    private static String PRESENTATION_DATE_FORMAT = "EEE, MMM d, yyyy, h:mm:ss a";
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
        log.debug("BungeniOdfTrackedChangesHelper : in constructor");
    }

    /**
     * Retrieves the container for the changes header text:tracked-changes
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

        log.debug("node name = " + elementChange.getLocalName());

        if (elementChange.getLocalName().equals(OdfTextDeletion.ELEMENT_NAME.getLocalName())) {
            scType.changetype    = "deletion";
            scType.elementChange = elementChange;

            // scType.changeRegion = textChangedRegion;
            return scType;
        } else if (elementChange.getNodeName().equals(OdfTextInsertion.ELEMENT_NAME.getLocalName())) {
            scType.changetype    = "insertion";
            scType.elementChange = elementChange;

            // scType.changeRegion = textChangedRegion;
            return scType;
        } else {
            return null;
        }
    }

    /***
     * Returncs change info as a hashmap
     * @param scChangeType
     * @return
     */
    public HashMap<String, String> getChangeInfo(StructuredChangeType scChangeType) {
        HashMap<String, String> changeInfo = new HashMap<String, String>();
        OdfElement elemInsOrDel = (OdfElement) scChangeType.elementChange;
        String     dcCreator    = getChangeInfoDcCreator(elemInsOrDel);
        String     dcDate       = getChangeInfoDcDate(elemInsOrDel);
        changeInfo.put("changeType", scChangeType.changetype);
        changeInfo.put("dcCreator", dcCreator);
        changeInfo.put("dcDate", dcDate);

        if (scChangeType.changetype.equals("deletion")) {
            String     deletedText  = getDeletedText(elemInsOrDel);
            changeInfo.put("deletedText", deletedText);
        } 

        return changeInfo;
    }


    /*The xml for text:deletion element looks like below -- [1]
     * we want the deleted text which is all the child elements outside the
     * <office:change-info..> block so we use the xpath to match all children excep
     * the change-info element
     *
     * [1] --
     * <text:deletion>
     * <office:change-info>
     * <dc:creator>Ashok Hariharan</dc:creator>
     * <dc:date>2010-02-09T12:38:00</dc:date>
     * </office:change-info>
     * <text:h text:style-name="Heading_20_1" text:outline-level="1">Triffids</text:h>
     * </text:deletion>
     *
     * @param elemDeletion - the text:deletion element
     * @return
     */
    private String getDeletedText(OdfElement elemDeletion) {
        StringBuffer sbfDeletedText = new StringBuffer("");
        try {
            /*
             * Note that here we have to use the ./ context for xpath since we
             * want to search from the context node onwards... otherwise XPath will return
             * results from the document root
             */
            String xPathExpr = "./child::*[local-name() != 'change-info']";
            NodeList matchedNodes = (NodeList) this.m_docXpath.evaluate(xPathExpr,elemDeletion, XPathConstants.NODESET);
            for (int i = 0; i < matchedNodes.getLength(); i++) {
                Node foundNode = matchedNodes.item(i);
                sbfDeletedText.append(foundNode.getTextContent());
            }
        } catch (XPathExpressionException ex) {
            log.error("getDeletedText : " + ex.getMessage(), ex);
        }
        return sbfDeletedText.toString();
    }

    /**
     * Get dc:creator for the change
     * @param elem
     * @return
     */
    private String getChangeInfoDcCreator(OdfElement elem) {
        NodeList changeElems = (NodeList) elem.getElementsByTagName("dc:creator");

        if (changeElems.getLength() > 0) {
            Element dcCreator = (Element) changeElems.item(0);

            return dcCreator.getTextContent();
        }

        return null;
    }

    /**
     * get dc:date for change info
     * @param elem
     * @return
     */
    private String getChangeInfoDcDate(OdfElement elem) {
        NodeList changeElems = (NodeList) elem.getElementsByTagName("dc:date");
        String outDate = "";
        if (changeElems.getLength() > 0) {
            Element dcDate = (Element) changeElems.item(0);
            /*
             * all dates in odf uses the ODFDOM data type DateTime
             * we convert to the XML date type and then to Java date type
             */
            DateTime dtdcDate = DateTime.valueOf(dcDate.getTextContent());
            Date ddcDate = dtdcDate.getXMLGregorianCalendar().toGregorianCalendar().getTime();
            SimpleDateFormat dfFormat =  new SimpleDateFormat(PRESENTATION_DATE_FORMAT) ;
            outDate  = dfFormat.format(ddcDate);
        }

        return outDate;
    }

    public class StructuredChangeType {
        public OdfTextChangedRegion changeRegion;
        public String               changetype;    // deletion, insertion
        public OdfElement           elementChange;
    }
}
