package org.bungeni.odfdom.document.properties;

//~--- non-JDK imports --------------------------------------------------------

import com.catcode.odf.Duration;
import java.util.HashMap;
import java.util.List;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.odftoolkit.odfdom.dom.attribute.meta.MetaValueTypeAttribute.Value;
import org.w3c.dom.Node;

//~--- JDK imports ------------------------------------------------------------


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Gives access to meta.xml. ODFDOM trunk is required for this to work since getMetaDom() API
 * is provided only in ODFDOM trunk.
 * API provides access to user defined properties.
 * @author Ashok Hariharan
 */
public class BungeniOdfPropertiesHelper {

    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfPropertiesHelper.class.getName());

    private BungeniOdfDocumentHelper m_docHelper  = null;
    private XPath                    m_docXpath   = null;
    private OdfFileDom               m_metaDom    = null;



    public BungeniOdfPropertiesHelper(BungeniOdfDocumentHelper docHelper) {
        try {
            this.m_docHelper = docHelper;
            this.m_metaDom   = this.m_docHelper.getOdfDocument().getMetaDom();
            m_docXpath       = m_docHelper.getOdfDocument().getMetaDom().getXPath();
        } catch (Exception ex) {
                  log.error("BungeniOdfPropertiesHelper.BungeniOdfPropertiesHelper : " + ex.getMessage(), ex);
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
                nodeValue = foundNode.getTextContent();
            } catch (XPathExpressionException ex) {
                  log.error("BungeniOdfPropertiesHelper.getMetaInitialCreator : " + ex.getMessage(), ex);
            }
            return nodeValue;
    }

    public String getMetaCreationDate() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:creation-date";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                nodeValue = BungeniOdfDateHelper.odfDateToPresentationDate(foundNode.getTextContent());
            } catch (XPathExpressionException ex) {
                  log.error("BungeniOdfPropertiesHelper.getMetaCreationDate : " + ex.getMessage(), ex);
            }
            return nodeValue;
    }

    /**
     * Retrieve the editing duration
     * @return
     */
    public String getMetaEditingDuration() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:editing-duration";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                Duration objDuration = Duration.parseDuration(foundNode.getTextContent());
                String sSeconds = (""+objDuration.getSeconds());
                sSeconds = sSeconds.substring(0, sSeconds.indexOf("."));
                nodeValue = objDuration.getHours() + ":" + objDuration.getMinutes() + ":" + sSeconds;
            } catch (XPathExpressionException ex) {
                  log.error("BungeniOdfPropertiesHelper.getMetaEditingDuration : " + ex.getMessage(), ex);
            }
            return nodeValue;
    }

    /**
     * get the number of editing cycles the document has undergone.
     *
     * @return String containing the editing cycles property
     */
    public String getMetaEditingCycles() {
            String nodeValue = "";
            try {

                String xPathExpression = "//meta:editing-cycles";
                Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
                nodeValue = foundNode.getTextContent();
            } catch (XPathExpressionException ex) {
                  log.error("BungeniOdfPropertiesHelper.getMetaEditingCycles : " + ex.getMessage(), ex);
              }
            return nodeValue;
    }



    /**
     * Get all user defined property values.
     * User defined property values are defined as //meta:user-defined
     * @return HashMap of key value pairs containing the metadata names values
     */
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
            log.error("BungeniOdfPropertiesHelper.getUserDefinedPropertyValues : " + ex.getMessage(), ex);
        }
            return propertyValues;

    }

    /**
     * Retrieves a single property value by taking the name of the property as an input.
     * API uses the XPath : //meta:user-defined[@meta:name='propertyName']
     *
     * @param propName
     * @return String containing the property value
     */
    public String getUserDefinedPropertyValue(String propName) {
        String nodeValue       = "";
        String xPathExpression = "//meta:user-defined[@meta:name='" + propName + "']";

        try {
            Node foundNode = (Node) m_docXpath.evaluate(xPathExpression, m_metaDom, XPathConstants.NODE);
          nodeValue = foundNode.getTextContent();
        } catch (XPathExpressionException ex) {
              log.error("BungeniOdfPropertiesHelper.getUserDefinedPropertyValue : " + ex.getMessage(), ex);
            }

        return nodeValue;
    }

    public boolean setUserDefinedPropertyValue(String propName, String propValue) {
        boolean bState = false;
        try {

            OdfOfficeMeta odfMeta = this.getOdfDocumentHelper().getOdfDocument().getOfficeMetadata();
            odfMeta.setUserDefinedData(propName, Value.STRING.toString(), propValue);
            bState = true;
        } catch (Exception ex) {
            log.error("setUserDefinedPropertyValue : " + ex.getMessage());
        }
        return bState;

    }
}
