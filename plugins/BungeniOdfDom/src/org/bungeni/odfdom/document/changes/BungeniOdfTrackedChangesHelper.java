package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import java.util.logging.Level;
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
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;

/**
 * <p>This class assists in extracting tracked changes from a ODF document</p>
 * @author Ashok Hariharan
 */
public class BungeniOdfTrackedChangesHelper {

    private static org.apache.log4j.Logger log                      =
        Logger.getLogger(BungeniOdfTrackedChangesHelper.class.getName());
    private BungeniOdfDocumentHelper       m_docHelper              = null;
    private XPath                          m_docXpath               = null;
    private BungeniOdfChangesMergeHelper m_changesMergeHelper = null;

    /**
     * <p>Track changes helper is initialized using a Document Helper object</p>
     * @param docH
     */
    public BungeniOdfTrackedChangesHelper(BungeniOdfDocumentHelper docH) {
        m_docHelper = docH;
        m_docXpath  = m_docHelper.getOdfDocument().getXPath();
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


    public BungeniOdfChangesMergeHelper getChangesMergeHelper(){
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
     * <p>Get changed regions in a text change container by a specific user.
     * Changes are filtered by the user name specified in dc:creator
     * text:tracked-changes/text:changed-regions</p>
     * @param changeContainer
     * @return
     */
    public ArrayList<OdfTextChangedRegion> getChangedRegionsByCreator(Element changeContainer, String dcCreator) {
        ArrayList<OdfTextChangedRegion> textChangedRegions = new ArrayList<OdfTextChangedRegion>(0);
       try {
           // to fix issue 70 - http://code.google.com/p/bungeni-editor/issues/detail?id=70
            String xPathExpr = "./child::text:changed-region[" +
                                "(descendant::text:insertion/office:change-info[@office:chg-author='"+ dcCreator  + "'])" +
                                " or " +
                                "(descendant::dc:creator='"+ dcCreator + "')]"  ;

            NodeList changedRegions = (NodeList) this.m_docXpath.evaluate(xPathExpr, changeContainer, XPathConstants.NODESET);
            for (int i = 0; i < changedRegions.getLength(); i++) {
                OdfTextChangedRegion textChangedRegion = (OdfTextChangedRegion) (Element) changedRegions.item(i);
                textChangedRegions.add(textChangedRegion);
            }
        } catch (XPathExpressionException ex) {
            log.error("getChangedRegionsByCreator :" + ex.getMessage(), ex);
        }
                    return textChangedRegions;

    }

    public OdfTextChangedRegion getChangedRegionById(String changeId ) {
        OdfTextChangedRegion foundRegion = null;
        try {
            String xPathExpr = "./child::text:changed-region[@text:id='" + changeId + "']";
            Node foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, this.getTrackedChangeContainer(), XPathConstants.NODE);
            if (foundNode != null) {
                foundRegion = (OdfTextChangedRegion) foundNode;
            }
        } catch (XPathExpressionException ex) {
            log.error("getChangedRegionsById : " + ex.getMessage() , ex);
        }
        return foundRegion;
    }

    /**
     * <p>Helper function for getStructuredChangeType()
     * gets the text:deletion or text:insertion element</p>
     * @param textChangedRegion
     * @return
     */
    private ArrayList<Element> getChange(OdfTextChangedRegion textChangedRegion) {
        ArrayList<Element> textChanges = new ArrayList<Element>(0);

        NodeList nn         = textChangedRegion.getChildNodes();

        for (int i = 0; i < nn.getLength(); i++) {
            Element textChange = (Element) nn.item(i);
            textChanges.add(textChange);
       }

        return textChanges;
    }

    /**
     * <p>identifies and returns a text:deletion or text:insertion element from a change region</p>
     * @param textChangedRegion
     * @return
     */
    public StructuredChangeType getStructuredChangeType(OdfTextChangedRegion textChangedRegion) {
        StructuredChangeType scType        = new StructuredChangeType();
        String               changedId     = textChangedRegion.getAttribute("text:id");
        ArrayList<Element>   elementChanges = getChange(textChangedRegion);

        log.debug("node name = " + elementChanges.size());


        String elemInsertion  = OdfTextInsertion.ELEMENT_NAME.getLocalName();
        String elemDeletion   = OdfTextDeletion.ELEMENT_NAME.getLocalName();
        if (elementChanges.size() == 2) {
            // this is a replacement change a deletion + insertion
            scType.changetype = "replacement";
            scType.changeId = changedId;
            scType.elementChange.add((OdfElement) elementChanges.get(0));
            scType.elementChange.add((OdfElement) elementChanges.get(1));
        } else {
            OdfElement elementChange = (OdfElement) elementChanges.get(0);
            String elemChangeName = elementChange.getLocalName();
            scType.changeId = changedId;
            if (elemChangeName.equals(elemDeletion)) {
                //this is the deletion pattern
                scType.changetype    = "deletion";
                scType.elementChange.add(elementChange);
            } else if (elemInsertion.equals(elemChangeName)) {
                //this is the insertion pattern
                scType.changetype    = "insertion";
                scType.elementChange.add(elementChange);
            }
        }
        return scType;
     }

    /**
     * <p>Returncs change info as a hashmap</p>
     * @param scChangeType
     * @return
     */
    public HashMap<String, String> getChangeInfo(StructuredChangeType scChangeType) {
        HashMap<String, String> changeInfo   = new HashMap<String, String>();
        ArrayList<OdfElement> changes = scChangeType.elementChange;
        //in the case of the replacement patter this will be the text:deletion element
        OdfElement              elemInsOrDel =  changes.get(0);
        String                  dcCreator    = getChangeInfoDcCreator(elemInsOrDel);
        String                  dcDate       = getChangeInfoDcDate(elemInsOrDel);

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

    /***
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
     * <p>Helper function on getDeletedNodes, returns a String with the text content
     * of all the nodes in the deleted nodes NodeList</p>
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

            sbInsText.append(foundNode.getTextContent());
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
    private String getChangeInfoDcCreator(OdfElement elem) {
        NodeList changeElems = (NodeList) elem.getElementsByTagName("dc:creator");

        if (changeElems.getLength() > 0) {
            Element dcCreator = (Element) changeElems.item(0);

            return dcCreator.getTextContent();
        }

        return null;
    }

    /**
     * <p>Get dc:date for change info</p>
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
            outDate = BungeniOdfDateHelper.odfDateToPresentationDate(dcDate.getTextContent());
        }

        return outDate;
    }

    public BungeniOdfDocumentHelper getOdfDocumentHelper() {
        return m_docHelper;
    }

    public OdfTextChangeStart getChangeStartItem(String changeId) {
        OdfTextChangeStart startNode = null;
        try {
            String xPathExpr = "//text:change-start[@text:change-id='" + changeId + "']";
            Node foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
            if (foundNode != null) {
                startNode = (OdfTextChangeStart) foundNode;
            }
        } catch (Exception ex) {
           log.error("getChangeStartitem : " + changeId + ":" + ex.getMessage(), ex);
        }
        return startNode;
    }


     public OdfTextChangeEnd getChangeEndItem(String changeId) {
        OdfTextChangeEnd endNode = null;
        try {
            String xPathExpr = "//text:change-end[@text:change-id='" + changeId + "']";
            Node foundNode = (Node) this.m_docXpath.evaluate(xPathExpr, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
            if (foundNode != null) {
                endNode = (OdfTextChangeEnd) foundNode;
            }
        } catch (Exception ex) {
           log.error("getChangeStartitem : " + changeId + ":" + ex.getMessage(), ex);
        }
        return endNode;
    }

    
    public class StructuredChangeType {
        public String               changeId;
        //public OdfTextChangedRegion changeRegion;
        public String               changetype;    // deletion, insertion
        public ArrayList<OdfElement>           elementChange = new ArrayList<OdfElement>(0);
    }
}
