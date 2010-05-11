package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------



//~--- JDK imports ------------------------------------------------------------

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.apache.log4j.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.odfdom.utils.XPathComponent;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provides the context for a particular change in a document.
 * Context is provided in terms of depth of paragraph and no. of words from the start
 * of the paragraph.
 * @author Ashok Hariharan
 */
public class BungeniOdfChangeContext {

     private static org.apache.log4j.Logger log             =
        Logger.getLogger(BungeniOdfChangeContext.class.getName());


    BungeniOdfDocumentHelper m_docHelper;

    Node changeElementStart = null;
    Node changeElementEnd = null;
    String elementXpathStart;
    String elementXpathEnd;
    boolean singleElementBoundary = false;

    String     formatString;

    /**
     * This constructor is used when we need the preceding / following context for a single element node
     * @param changeElement
     * @param odfHelper
     */
    public BungeniOdfChangeContext(Node changeElement, BungeniOdfDocumentHelper odfHelper) {
        this.changeElementStart = changeElement;
        this.elementXpathStart = BungeniOdfNodeHelper.getXPath(changeElement);
        this.m_docHelper = odfHelper;
        this.singleElementBoundary = true;
    }

    /**
     * This constructor is used when we need the preceding / following context for spanned node boundaries,
     * the first one is used for the preceding context, the second one is used for the following context.
     * @param changeElementStart
     * @param changeElementEnd
     * @param odfHelper
     */
    public BungeniOdfChangeContext(Node changeElementStart, Node changeElementEnd, BungeniOdfDocumentHelper odfHelper) {
        this.changeElementStart = changeElementStart;
        this.changeElementEnd = changeElementEnd;
        this.elementXpathStart = BungeniOdfNodeHelper.getXPath(changeElementStart);
        this.elementXpathEnd = BungeniOdfNodeHelper.getXPath(changeElementEnd);
        this.m_docHelper = odfHelper;
        this.singleElementBoundary = false;
    }


    public Node getParentSection() {
        Node mainParent = null;
        mainParent  = changeElementStart.getParentNode();
        while (mainParent != null ) {
        if (mainParent.getNodeName().equals("text:section")) {
            break;
        } else {
            mainParent = mainParent.getParentNode();
        }
        }
        return mainParent;
    }

    public String getParentSectionType(){
        String sType = null;
        Node aNode = getParentSection();
        if (aNode != null ) {
           sType = m_docHelper.getSectionHelper().getSectionType((OdfTextSection) aNode);
        }
        return sType;
    }
    
    /**
     * Returns the content of the preceding text nodes 
     * @return
     */
    public String getPrecedingSiblingText() {
        StringBuffer precedingText = new StringBuffer();
        try {
            //preceding text boundary is always the start element
            String precedingXpath = this.elementXpathStart + "/preceding-sibling::text()";
            XPath xPath = m_docHelper.getOdfDocument().getXPath();
            NodeList textNodes = (NodeList) xPath.evaluate(precedingXpath, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);
            for (int i = 0; i < textNodes.getLength(); i++) {
                precedingText.append(textNodes.item(i).getTextContent());
            }
        } catch (Exception ex) {
            log.error("getPrecedingText : "+ ex.getMessage(), ex);
        }
        return precedingText.toString();
    }

    /**
     * Returns the content of the following text nodes (only siblings)
     * @return
     */
    public String getFollowingSiblingText() {
        StringBuffer followingText = new StringBuffer();
        try {
            //following text boundary can be the end element if it exists
            String precedingXpath = ((this.singleElementBoundary == true) ? this.elementXpathStart : this.elementXpathEnd) +
                    "/following-sibling::text()";
            XPath xPath = m_docHelper.getOdfDocument().getXPath();
            NodeList textNodes = (NodeList) xPath.evaluate(precedingXpath, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.NODESET);
            for (int i = 0; i < textNodes.getLength(); i++) {
                followingText.append(textNodes.item(i).getTextContent());
            }
            
        } catch (Exception ex) {
            log.error("getFollowingText : "+ ex.getMessage(), ex);
        }
        return followingText.toString();
    }


    public BungeniOdfDocumentHelper getDocHelper(){
        return this.m_docHelper;
    }

    /**
     * Parse the path component
     * @param xpathComponent
     * @return
     */
    public String parsePathComponent(String xpathComponent) {
        XPathComponent objComponent = new XPathComponent(xpathComponent);
        String strComponentElement = objComponent.getElement();
        StringBuffer friendlyPathMessage = new StringBuffer();
        if (strComponentElement.equals("p")) {
           friendlyPathMessage.append(pathMessage_p(objComponent));
        } else if (strComponentElement.equals("section")) {
            friendlyPathMessage.append(pathMessage_section(objComponent));
        } else if (strComponentElement.equals("text")) {
            friendlyPathMessage.append(pathMessage_text(objComponent));
        } else if (strComponentElement.equals("change-start")) {
            friendlyPathMessage.append(pathMessage_change_start(objComponent));
        } else if (strComponentElement.equals("body")) {
            friendlyPathMessage.append(pathMessage_body(objComponent));
        } else if (strComponentElement.equals("document-content")) {
            friendlyPathMessage.append(pathMessage_document_content(objComponent));
        } else {
            log.debug("Unknown component : " + xpathComponent);
        }
        return friendlyPathMessage.toString();

    }

    private String pathMessage_p(XPathComponent xpathComponent) {
        return " In Paragraph " + xpathComponent.getIndex() + " ;";
    }

    private String pathMessage_section(XPathComponent xpathComponent) {
        return " in Section " + xpathComponent.getIndex() + " ;";
    }

    private String pathMessage_change_start(XPathComponent xpathComponent) {
        return "";
    }

    private String pathMessage_document_content(XPathComponent xpathComponent) {
        return "";
    }

    private String pathMessage_body(XPathComponent xpathComponent) {
        return "";
    }

    private String pathMessage_text(XPathComponent xpathComponent) {
        return "";
    }



 
}
