package org.bungeni.odfdom.document.properties;

//~--- non-JDK imports --------------------------------------------------------

import java.util.HashMap;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.odftoolkit.odfdom.OdfFileDom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Gives access to meta.xml
 * API provides access to user defined properties.
 * @author undesa
 */
public class BungeniOdfPropertiesHelper {
    private BungeniOdfDocumentHelper m_docHelper  = null;
    private XPath                    m_docXpath   = null;
    private OdfFileDom               m_metaDom    = null;



    public BungeniOdfPropertiesHelper(BungeniOdfDocumentHelper docHelper) {
        try {
            this.m_docHelper = docHelper;
            this.m_metaDom   = this.m_docHelper.getOdfDocument().getMetaDom();
            m_docXpath       = m_docHelper.getOdfDocument().getXPath();
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BungeniOdfDocumentHelper getOdfDocumentHelper() {
        return m_docHelper;
    }

    public String getMetaInitialCreator() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:initial-creator";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                nodeValue = foundNode.getNodeValue();
            } catch (XPathExpressionException ex) {
                Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            return nodeValue;
    }

    public String getMetaCreationDate() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:creation-date";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                nodeValue = foundNode.getNodeValue();
            } catch (XPathExpressionException ex) {
                Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            return nodeValue;
    }

    public String getMetaEditingDuration() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:editing-duration";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                nodeValue = foundNode.getNodeValue();
            } catch (XPathExpressionException ex) {
                Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            return nodeValue;
    }

    public String getMetaEditingCycles() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:editing-cycles";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                nodeValue = foundNode.getNodeValue();
            } catch (XPathExpressionException ex) {
                Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            return nodeValue;
    }




    public HashMap<String,String> getUserDefinedPropertyValues() {
        HashMap<String, String> propertyValues = new HashMap<String, String>();
    
        try {
            String xPathExpression = "//meta:user-defined";
            NodeList foundNodes = (NodeList) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODESET);
            for (int i = 0; i < foundNodes.getLength(); i++) {
                Element aNode = (Element) foundNodes.item(i);
                String attrName = aNode.getAttribute("meta:name");
                String attrValue = aNode.getTextContent();
                propertyValues.put(attrName, attrValue);
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
            return propertyValues;

    }

    public String getUserDefinedPropertyValue(String propName) {
        String nodeValue       = "";
        String xPathExpression = "//meta:user-defined[@meta:name='" + propName + "']";

        try {
            Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
          nodeValue = foundNode.getTextContent();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nodeValue;
    }
}
