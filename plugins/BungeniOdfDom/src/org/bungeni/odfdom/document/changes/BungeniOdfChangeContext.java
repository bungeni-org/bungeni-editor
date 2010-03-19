package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------


import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.apache.log4j.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.odfdom.utils.XPathComponent;
import org.w3c.dom.Node;

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
    Node changeElement;
    String elementXpath;
    String     formatString;

    public BungeniOdfChangeContext(Node changeElement, BungeniOdfDocumentHelper odfHelper) {
        this.changeElement = changeElement;
        this.elementXpath = BungeniOdfNodeHelper.getXPath(changeElement);
        this.m_docHelper = odfHelper;
    }

    public String getReportPath() {
        String xPathString = this.elementXpath;
        //"office:document-content/office:body[1]/office:text[1]/text:section[1]/text:section[5]/text:section[1]/text:section[2]/text:p[4]/text:change-start[3]";    // BungeniOdfNodeHelper.getXPath(changeElement);
        // office:document-content/office:body[1]/office:text[1]/text:section[1]/text:section[5]/text:section[1]/text:section[2]/text:p[4]/text:change-start[3]
        String[] xpathComponents = xPathString.split("/");
        StringBuffer sReportMessage = new StringBuffer();
        List<String> surrounds = getSurroundingText();
        for (int i = 0; i < surrounds.size(); i++) {
            String string = surrounds.get(i);
            if (i == 0 ) {
                if (string.trim().length() > 0) {
                    sReportMessage.append(" After :\n");
                    sReportMessage.append(string + "\n");
                }
            } else {
                if (string.trim().length() > 0) {
                    sReportMessage.append(" Before :\n");
                    sReportMessage.append(string + "\n");
                }
            }
        }
        for (String string : surrounds) {
            if (string.trim().length() > 0 ) {
                sReportMessage.append(string);
            }
            
        }
        for (int i = xpathComponents.length - 2; i >= 0; i--) {
            String xpathComponent = xpathComponents[i];
            sReportMessage.append(parsePathComponent(xpathComponent));
        }

        return sReportMessage.toString();
    }

    private List<String> getSurroundingText(){
        ArrayList<String> listSurrounding = new ArrayList<String>(0);
        try {
            String precedingXpath = this.elementXpath + "/preceding-sibling::text()";
            String followingXpath = this.elementXpath + "/following-sibling::text()";
            XPath xPath = m_docHelper.getOdfDocument().getXPath();
            String precedingText = (String) xPath.evaluate(precedingXpath, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.STRING);
            String followingText = (String) xPath.evaluate(followingXpath, m_docHelper.getOdfDocument().getContentDom(), XPathConstants.STRING);
            listSurrounding.add(precedingText);
            listSurrounding.add(followingText);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return listSurrounding;
    }

    private String parsePathComponent(String xpathComponent) {
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
