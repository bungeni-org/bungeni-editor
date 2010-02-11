/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.odfdom.document.properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.odftoolkit.odfdom.OdfFileDom;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *Gives access to meta.xml
 * API provides access to user defined properties.
 * @author undesa
 */
public class BungeniOdfPropertiesHelper {
    private BungeniOdfDocumentHelper m_docHelper = null;
    private OdfFileDom m_metaDom = null;
    private XPath m_docXpath = null;
    private XPath m_metaXpath = null;
    private Document m_metaDomDoc = null;

    public BungeniOdfPropertiesHelper(BungeniOdfDocumentHelper docHelper ) {
        try {
            this.m_docHelper = docHelper;
            this.m_metaDom = this.m_docHelper.getOdfDocument().getMetaDom();
            m_docXpath = m_docHelper.getOdfDocument().getXPath();
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


   

    public String getUserDefinedPropertyValue (String propName ) {
        String nodeValue = "";
        String xPathExpression = "//meta:user-defined[@meta:name='"+  propName + "']";
        try {
            Node foundNode  = (Node)m_docXpath.evaluate(xPathExpression, m_metaDom,XPathConstants.NODE);
            nodeValue = foundNode.getTextContent();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(BungeniOdfPropertiesHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nodeValue;
    }


}
