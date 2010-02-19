package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.odftoolkit.odfdom.doc.text.OdfTextDeletion;
import org.odftoolkit.odfdom.doc.text.OdfTextInsertion;
import org.odftoolkit.odfdom.type.DateTime;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 * This class assists in extracting tracked changes from a ODF document
 * @author Ashok Hariharan
 */
public class BungeniOdfTrackedChangesHelper {
    private static String                  PRESENTATION_DATE_FORMAT = "EEE, MMM d, yyyy, h:mm:ss a";
    private static org.apache.log4j.Logger log                      =
        Logger.getLogger(BungeniOdfTrackedChangesHelper.class.getName());
    private BungeniOdfDocumentHelper       m_docHelper              = null;
    private XPath                          m_docXpath               = null;

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
     * text:tracked-changes/text:changed-regions
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
        String               changedId     = textChangedRegion.getAttribute("text:id");
        OdfElement           elementChange = (OdfElement) getChange(textChangedRegion);

        log.debug("node name = " + elementChange.getLocalName());

        String elemInsertion  = OdfTextInsertion.ELEMENT_NAME.getLocalName();
        String elemDeletion   = OdfTextDeletion.ELEMENT_NAME.getLocalName();
        String elemChangeName = elementChange.getLocalName();

        scType.changeId = changedId;

        if (elemChangeName.equals(elemDeletion)) {
            scType.changetype    = "deletion";
            scType.elementChange = elementChange;

            // scType.changeRegion = textChangedRegion;
            return scType;
        } else if (elemInsertion.equals(elemChangeName)) {
            scType.changetype    = "insertion";
            scType.elementChange = elementChange;

            // scType.changeRegion = textChangedRegion;
            return scType;
        } else {
            return null;
        }
    }

    /**
     * Returncs change info as a hashmap
     * @param scChangeType
     * @return
     */
    public HashMap<String, String> getChangeInfo(StructuredChangeType scChangeType) {
        HashMap<String, String> changeInfo   = new HashMap<String, String>();
        OdfElement              elemInsOrDel = (OdfElement) scChangeType.elementChange;
        String                  dcCreator    = getChangeInfoDcCreator(elemInsOrDel);
        String                  dcDate       = getChangeInfoDcDate(elemInsOrDel);

        changeInfo.put("changeType", scChangeType.changetype);
        changeInfo.put("dcCreator", dcCreator);
        changeInfo.put("dcDate", dcDate);

        if (scChangeType.changetype.equals("deletion")) {
            String deletedText = getDeletedText(elemInsOrDel);

            changeInfo.put("changeText", deletedText);
        }

        if (scChangeType.changetype.equals("insertion")) {
            String insertedText = getInsertedText(elemInsOrDel, scChangeType.changeId);

            changeInfo.put("changeText", insertedText);
        }

        return changeInfo;
    }

    /***
     * <br />
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
     * <br />
     *
     * @param elemDeletion - the text:deletion element<br />
     * @return NodeList of al the deleted nodes<br />
     */
    public NodeList getDeletedNodes(OdfElement elemDeletion) {
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
     * Helper function on getDeletedNodes, returns a String with the text content
     * of all the nodes in the deleted nodes NodeList
     * @param elemDeletion the text:deletion element
     * @return String content of all the deleted nodes
     */
    public String getDeletedText(OdfElement elemDeletion) {
        StringBuffer sbfDeletedText = new StringBuffer("");
        NodeList     matchedNodes   = getDeletedNodes(elemDeletion);

        for (int i = 0; i < matchedNodes.getLength(); i++) {
            Node foundNode = matchedNodes.item(i);

            sbfDeletedText.append(foundNode.getTextContent());
        }

        return sbfDeletedText.toString();
    }

    /**
     * Helper function on getInsertedNodes - converts the NodeList to a string,
     * by evaluation text content of nodes.
     * @param elemInsertion
     * @param changeId
     * @return
     */
    public String getInsertedText(OdfElement elemInsertion, String changeId) {
        StringBuffer sbInsText    = new StringBuffer();
        NodeList     matchedNodes = getInsertedNodes(elemInsertion, changeId);

        for (int i = 0; i < matchedNodes.getLength(); i++) {
            Node foundNode = matchedNodes.item(i);

            sbInsText.append(foundNode.getTextContent());
        }

        return sbInsText.toString();
    }

    /**
     * This function extracts inserted text corresponding to a change mark.<br />
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
     * <br />
     * &lt;text:changed-region text:id=&quot;ct472232592&quot;&gt;<br />
     * &lt;text:insertion&gt;<br />
     *   &lt;office:change-info&gt;<br />
     *       &lt;dc:creator&gt;Ashok Hariharan&lt;/dc:creator&gt;<br />
     *       &lt;dc:date&gt;2010-02-17T17:10:00&lt;/dc:date&gt;<br />
     *   &lt;/office:change-info&gt;<br />
     * &lt;/text:insertion&gt;<br />
     * &lt;/text:changed-region&gt;<br />
     * <br />
     * <br />
     * <br />
     * [2] --<br />
     * <br />
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
     * @param elemInsertion - text:insertion element<br />
     * @param changeId - id of the changed-region containing the text:insertion element<br />
     * @return a NodeList of all the newly inserted text() nodes
     */
    public NodeList getInsertedNodes(OdfElement elemInsertion, String changeId) {
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
     * This is a version of the inserted text API that uses DOM traversal
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
        String   outDate     = "";

        if (changeElems.getLength() > 0) {
            Element dcDate = (Element) changeElems.item(0);

            /*
             * all dates in odf uses the ODFDOM data type DateTime
             * we convert to the XML date type and then to Java date type
             */
            DateTime         dtdcDate = DateTime.valueOf(dcDate.getTextContent());
            Date             ddcDate  = dtdcDate.getXMLGregorianCalendar().toGregorianCalendar().getTime();
            SimpleDateFormat dfFormat = new SimpleDateFormat(PRESENTATION_DATE_FORMAT);

            outDate = dfFormat.format(ddcDate);
        }

        return outDate;
    }

    public class StructuredChangeType {
        public String               changeId;
        public OdfTextChangedRegion changeRegion;
        public String               changetype;    // deletion, insertion
        public OdfElement           elementChange;
    }
}
