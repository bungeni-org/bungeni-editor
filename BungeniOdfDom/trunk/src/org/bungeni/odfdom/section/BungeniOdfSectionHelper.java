package org.bungeni.odfdom.section;

//~--- non-JDK imports --------------------------------------------------------

import java.util.Iterator;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.odftoolkit.odfdom.dom.OdfSchemaDocument.OdfXMLFile;
import org.odftoolkit.odfdom.dom.element.style.StyleBackgroundImageElement;
import org.odftoolkit.odfdom.dom.element.style.StyleSectionPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.odftoolkit.odfdom.pkg.OdfNamespace;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *This class provides many helper functions for deaing with Text Sections
 * @author Ashok Hariharan
 */
public class BungeniOdfSectionHelper {

    public static final String ATTRIBUTE_NS_PREFIX = "anx:";
    public static final String BODY_NODE = "body";
    public static final String FILTER_BUNGENI_SECTION_META = "Bungeni";
    public static final String FILTER_SECTION_TYPE = ATTRIBUTE_NS_PREFIX + "BungeniSectionType";
    public static final String FILTER_SECTION_ID = ATTRIBUTE_NS_PREFIX + "BungeniSectionID";
    public static final String SECTION_ELEMENT = "text:section";
    public static final String SECTION_ELEMENT_NAME_ATTR = "text:name";
    public static final String STYLE_SECTION_PROPS = "style:section-properties";
    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(BungeniOdfSectionHelper.class.getName());
    private OdfDocument odfDocument = null;
    private BungeniOdfDocumentHelper m_bodfDoc = null;
    private XPath xPath = null;

    /**
     * Returns a section helper object from an ODF document
     * @param odfDoc
     */
    public BungeniOdfSectionHelper(OdfDocument odfDoc) {
        odfDocument = odfDoc;
        try {
            xPath = odfDocument.getContentDom().getXPath(); //xPath.setNamespaceContext(new OdfNamespace());
            //xPath.setNamespaceContext(new OdfNamespace());
        } catch (Exception ex) {
            log.error("Error while getting Xpath handle");
        }
    }

    /**
     * Returns a section helper object from a ODF document helper object
     * @param bodfDoc
     */
    public BungeniOdfSectionHelper(BungeniOdfDocumentHelper bodfDoc) {
        m_bodfDoc = bodfDoc;
        odfDocument = m_bodfDoc.getOdfDocument();
        try {
            xPath = odfDocument.getContentDom().getXPath();
        } catch (Exception ex) {
            log.error("Error while getting xpath handle");
        }
    }

    /**
     * Provides access to the ODFDOM odf document
     * @return
     */
    public OdfDocument getOdfDocument() {
        return odfDocument;
    }

    /**
     * Gets the immediate child sections for a section (not descendants)
     * @param nsection
     * @return
     */
    public ArrayList<TextSectionElement> getChildSections(TextSectionElement nsection) {
        ArrayList<TextSectionElement> foundChildren = new ArrayList<TextSectionElement>(0);

        try {
            NodeList nodeSet = (NodeList) xPath.evaluate(SECTION_ELEMENT, nsection, XPathConstants.NODESET);

            for (int i = 0; i < nodeSet.getLength(); i++) {
                Node foundNodeSection = nodeSet.item(i);

                foundChildren.add((TextSectionElement) foundNodeSection);
            }
        } catch (XPathExpressionException ex) {
            log.error("getChildSections : " + ex.getMessage());
        }

        return foundChildren;

    }

    /**
     * Gets all descendant sections 
     * @param nsection
     * @return
     */
    public List<TextSectionElement> getDescendantChildSections(TextSectionElement nsection) {
        List<TextSectionElement> foundChildren = new ArrayList<TextSectionElement>(0);

        try {
            NodeList nodeSet = (NodeList) xPath.evaluate("descendant::" + SECTION_ELEMENT, nsection, XPathConstants.NODESET);

            for (int i = 0; i < nodeSet.getLength(); i++) {
                Node foundNodeSection = nodeSet.item(i);

                foundChildren.add((TextSectionElement) foundNodeSection);
            }
        } catch (XPathExpressionException ex) {
            log.error("getDescendantChildSections : " + ex.getMessage());
        }
        return foundChildren;

    }

    /**
     * Returns the Section Type for the document this is a custom property in the anx: namespace ,
     * anx:BungeniSectionType
     * @param nsection
     * @return
     */
    public String getSectionType(TextSectionElement nsection) {
        return this.getSectionMetadataValue(nsection, "BungeniSectionType");
    }

    /**
     * Returns the Section Id for the document this is a custom property in the anx: namespace,
     * anx:BungeniSectionID
     * @param nsection
     * @return
     */
    public String getSectionID(TextSectionElement nsection) {
          return this.getSectionMetadataValue(nsection, "BungeniSectionID");
    }

    public String getFilterNamedItem(TextSectionElement nsection, NamedNodeMap nattr, String filterItem) {
        Node nitem = nattr.getNamedItem(filterItem);
        if (nitem != null) {
            return nitem.getNodeValue();
        } else {
            return nsection.getTextNameAttribute();
        }
    }

    public ArrayList<Node> getBungeniMetadataAttributes(TextSectionElement nsection) {
        ArrayList<Node> nodeLists = new ArrayList<Node>(0);
        NodeList listofMetadataNodes = this.getSectionMetadataAttributes(nsection);
        for (int i = 0; i < listofMetadataNodes.getLength(); i++) {
            Node foundNode = listofMetadataNodes.item(i);
            nodeLists.add(foundNode);
        }
        return nodeLists;
    }

    public OdfStyle getSectionStyle(TextSectionElement oSection) {
        OdfOfficeAutomaticStyles osb = oSection.getAutomaticStyles();
        OdfStyle secStyle = osb.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);

        return secStyle;
    }

    /**
     * Removes the background image for a section
     * @param oSection
     * @return
     */
    public boolean removeSectionBackgroundImage(TextSectionElement oSection) {
        boolean bState = false;

        try {
            OdfOfficeAutomaticStyles autoStyles = oSection.getAutomaticStyles();
            OdfStyle secStyle = autoStyles.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);
            Element sprops = getSectionStyleProperties(secStyle);
            NodeList nl = sprops.getElementsByTagName("style:background-image");

            if (nl.getLength() > 0) {
                StyleBackgroundImageElement img = (StyleBackgroundImageElement) nl.item(0);

                // odfPackage.getPackage().remove(img.getHref());
                sprops.removeChild(img);

                StyleBackgroundImageElement newimg = new StyleBackgroundImageElement(odfDocument.getContentDom());

                sprops.appendChild(newimg);
                bState = true;
            }
        } catch (Exception ex) {
            log.error("removeSectionBackgroundImage : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    /**
     * Removes the background color for a section
     * @param oSection
     * @return
     */
    public boolean removeSectionBackgroundColor(TextSectionElement oSection) {
        boolean bState = false;
        try {
            OdfOfficeAutomaticStyles autoStyles = oSection.getAutomaticStyles();
            OdfStyle secStyle = autoStyles.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);
            StyleSectionPropertiesElement sprops = (StyleSectionPropertiesElement) getSectionStyleProperties(secStyle);
            Attr nl = sprops.getAttributeNode("fo:background-color");
            if (nl != null) {
                nl.setValue("transparent");
                bState = true;
            }
        } catch (Exception ex) {
            log.error("removeSectionBackgroundColor : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    /*
     * Retrieves the style:section-properties for a section
     *
    <style:style style:name="Sect1" style:family="section">
    <style:section-properties fo:background-color="transparent" style:editable="false">
    <style:columns fo:column-count="1" fo:column-gap="0in"/>
    <style:background-image xlink:href="Pictures/100000000000009500000095F10913D8.jpg"
    xlink:type="simple" xlink:actuate="onLoad"/>
    </style:section-properties>
    </style:style>
     *
     */
    public Element getSectionStyleProperties(OdfStyle secStyle) {
        Node props = null;
        NodeList nsectList = secStyle.getChildNodes();

        for (int i = 0; i < nsectList.getLength(); i++) {
            Node nmatch = nsectList.item(i);

            if (nmatch.getNodeName().equals(STYLE_SECTION_PROPS)) {
                props = nmatch;
            }
        }

        return (Element) props;
        /**
         * returns this :
         * <style:section-properties fo:background-color="transparent" style:editable="false">
        <style:columns fo:column-count="1" fo:column-gap="0in"/>
        <style:background-image xlink:href="Pictures/100000000000009500000095F10913D8.jpg"
        xlink:type="simple" xlink:actuate="onLoad"/>
        </style:section-properties>
         */
    }



public static class BungeniODFNsContext {

   private static BungeniODFNsContext instance = null;
   public static final String RDF_NS_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
   public static final String ANX_NS_URI = "http://editor.bungeni.org/1.0/anx/";
   public static final String ANX_NS_PREF = "anx";


   private NamespaceContext ctx = null;

    public static BungeniODFNsContext getInstance() {
        if (null == instance) {
            instance = new BungeniODFNsContext();
        }
        return instance;
    }

    public NamespaceContext getContext() {
        return ctx;
    }

    private BungeniODFNsContext() {
        ctx = new NamespaceContext() {

                @Override
                public String getNamespaceURI(String prefix) {
                    String uri = "";
                    if (prefix.equals("rdf")) {
                        uri = RDF_NS_URI;
                    } else if (prefix.equals(ANX_NS_PREF)) {
                        uri = ANX_NS_URI;
                    } else if (prefix.startsWith("ns")) {
                        uri = ANX_NS_URI;
                    } else {
                        return null;
                    }
                    return uri;
                }

                @Override
                public String getPrefix(String namespaceURI) {
                     return null;
                }

                @Override
                public Iterator getPrefixes(String namespaceURI) {
                    return null;
                }

        };
    }


}

   public OdfFileDom getRDFDOM(){
       OdfFileDom fileDom = null;
       try {
            //the RDF Dom is located in meta/meta.rdf , we are hard coding this
            //presently , but the dynamic discovery of this needs to be implmenebted
            // !+FIX_THIS (implement dynamic discovery, ah, jan-2012)
            fileDom = this.getOdfDocument().getFileDom("meta/meta.rdf");
            // we need to do the below for the XPath to recognize the anx: namespace
            fileDom.setNamespace(BungeniODFNsContext.ANX_NS_PREF, BungeniODFNsContext.ANX_NS_URI);
        } catch (Exception ex) {
            log.error("Error while getting RDF metadata DOM");
        }
        return fileDom;
   }

   public XPath getRDFMetadataXPath(OdfFileDom fileDom) {
       XPath xfilePath = fileDom.getXPath();
       xfilePath.setNamespaceContext(fileDom);
       return xfilePath;
   }

   /**
     * Returns the metadata attributes for a section as  NamedNodeMap
     * @param nSection a TextSectionElement handle for a section
     * @return NodeList
     */
   public NodeList getSectionMetadataAttributes(TextSectionElement nSection)  {

       //get the ID of the section for which we want to get the metadata
       String  xmlId = nSection.getXmlIdAttribute();

       //get the rdf metadata hive from the rdf file in the ODF package

       OdfFileDom fileDom = this.getRDFDOM();

       //set the namespace context on the RDF metadom
       XPath xrdfPath = fileDom.getXPath();
       xrdfPath.setNamespaceContext(fileDom);

       //now query for the section metadata
       String xPression = "//rdf:Description[@rdf:about='../content.xml#"+ 
                        xmlId +
                     "']/node()[not(self::text())]" ;
       NodeList rdfMetadataNodes = null;
        try {

            rdfMetadataNodes = (NodeList) xrdfPath.evaluate(xPression, fileDom, XPathConstants.NODESET);

           

        } catch (XPathExpressionException ex) {
            log.error("XPath Error while accesing section metadata", ex);
        }
        return rdfMetadataNodes;
    }


    public String getSectionMetadataValue(TextSectionElement nSection, String key)  {

       String metadataValue = "";

       //get the ID of the section for which we want to get the metadata
       String  xmlId = nSection.getXmlIdAttribute();

       //get the rdf metadata hive from the rdf file in the ODF package

       OdfFileDom fileDom = this.getRDFDOM();

       XPath xrdfPath = this.getRDFMetadataXPath(fileDom);
       // sxrdfPath.setNamespaceContext(fileDom);

       String xPression = "//rdf:Description[@rdf:about='../content.xml#"+
                                            xmlId +
                                            "']/*[local-name()='"+ key + "']"   ;
       Node rdfMetadataNode = null;
       NodeList rdfMetadataNodes = null;
        try {
            rdfMetadataNode = (Node) xrdfPath.evaluate(xPression, fileDom, XPathConstants.NODE);

            metadataValue = rdfMetadataNode.getTextContent();

        } catch (XPathExpressionException ex) {
            log.error("XPath Error while accesing section metadata", ex);
        }
        return metadataValue;
    }



    /**
     * Retuns a node list of ALL the document sections
     * @return
     */
    public NodeList getDocumentSections() {
        NodeList lst = null;

        try {
            lst = odfDocument.getContentDom().getElementsByTagName(SECTION_ELEMENT);
        } catch (Exception ex) {
            log.error("getDocumentSections : " + ex.getMessage());
        } finally {
            return lst;
        }
    }




    /**
     * Gets the section type offset of the section
     * @param sectionType
     * @param aSection
     * @return
     */
    public Integer getDocumentSectionNumber(String sectionType, TextSectionElement aSection) {
        Integer foundIndex = 0;
        NodeList secList = this.getDocumentSections();
        for (int i = 0; i < secList.getLength(); i++) {
            Node aSecNode = secList.item(i);
            TextSectionElement foundSection = (TextSectionElement) aSecNode;
            String fSectionType = this.getSectionType(foundSection);
            String fSectionName = foundSection.getTextNameAttribute();
            if (fSectionType.equals(sectionType)) {
                //this is the same section type as the one we want to index
                ++foundIndex;
                //check if it is the same section, if yes, break
                if (fSectionName.equals(aSection.getTextNameAttribute())) {
                    break;
                }
            }

        }

        return foundIndex;
    }

    /**
     * Given the name of a section , returns the section object
     * @param sectionName
     * @return
     */
    public TextSectionElement getSection(String sectionName) {
        TextSectionElement oSection = null;

        try {
            OdfFileDom docDom = odfDocument.getContentDom();

            oSection = (TextSectionElement) xPath.evaluate("//text:section[@text:name='" + sectionName + "']", docDom,
                    XPathConstants.NODE);
        } catch (Exception ex) {
            log.error("Unable to get section !" , ex);
        } finally {
            return oSection;
        }
    }

   

    public void iterateSections(IBungeniOdfSectionObjectIterator sectionIterator) {
        NodeList nlist = getDocumentSections();

        for (int i = 0; i < nlist.getLength(); i++) {
            TextSectionElement odfSection = (TextSectionElement) nlist.item(i);

            if (sectionIterator.nextSection(this, odfSection) == false) {
                break;
            }
        }
    }

    public void iterateSections(IBungeniOdfSectionIterator sectionIterator) {
        NodeList nlist = getDocumentSections();

        for (int i = 0; i < nlist.getLength(); i++) {
            TextSectionElement odfSection = (TextSectionElement) nlist.item(i);

            if (sectionIterator.nextSection(this, odfSection) == false) {
                break;
            }
        }
    }

    private Node getBodyNode(NodeList nlist) {
        for (int i = 0; i < nlist.getLength(); i++) {
            Node nnode = nlist.item(i);

            if (nnode instanceof TextSectionElement) {
                TextSectionElement nsection = (TextSectionElement) nnode;

                // get section style name
                String sectionStyleName = nsection.getStyleName();

                if (sectionStyleName != null) {
                    if (sectionStyleName.length() > 0) {
                        OdfStyle sectStyle = getSectionStyle(nsection);
                        Element sProps = getSectionStyleProperties(sectStyle);

                        if (sProps.hasAttributes()) {
                            NamedNodeMap sectionProps = sProps.getAttributes();
                            Node nfoundNode = sectionProps.getNamedItem(FILTER_SECTION_TYPE);

                            if (nfoundNode != null) {
                                if (nfoundNode.getNodeValue().equals(BODY_NODE)) {
                                    return nnode;
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            OdfDocument odoc =
                    OdfDocument.loadDocument(new File("/home/undesa/Desktop/debate_file_02.odt"));
            BungeniOdfDocumentHelper helper = new BungeniOdfDocumentHelper(odoc);

            helper.removeBackgroundImage();
            odoc.save("/home/undesa/new.odt");

            // System.out.println(nl);
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfSectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
