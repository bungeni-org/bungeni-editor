package org.bungeni.odfdom.section;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.doc.style.OdfStyleBackgroundImage;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.odftoolkit.odfdom.OdfNamespace;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;

/**
 *This class provides many helper functions for deaing with Text Sections
 * @author Ashok Hariharan
 */
public class BungeniOdfSectionHelper {
    private static String                  ATTRIBUTE_NS_PREFIX         = "anx:";
    private static String                  BODY_NODE                   = "body";
    private static String                  FILTER_BUNGENI_SECTION_META = "Bungeni";
    private static String                  FILTER_SECTION_TYPE         = ATTRIBUTE_NS_PREFIX + "BungeniSectionType";
    private static String                  SECTION_ELEMENT             = "text:section";
    private static String                  SECTION_ELEMENT_NAME_ATTR   = "text:name";
    private static String                  STYLE_SECTION_PROPS         = "style:section-properties";
    private static org.apache.log4j.Logger log                         =
        org.apache.log4j.Logger.getLogger(BungeniOdfSectionHelper.class.getName());
    private OdfDocument odfDocument = null;
    private XPath       xPath       = null;

    /**
     *
     * @param odfDoc
     */
    public BungeniOdfSectionHelper(OdfDocument odfDoc) {
        odfDocument = odfDoc;
        xPath       = odfDocument.getXPath(); //XPathFactory.newInstance().newXPath();
        //xPath.setNamespaceContext(new OdfNamespace());
    }
    
    public OdfDocument getOdfDocument() {
        return odfDocument;
    }

    /**
     * Gets the immediate child sections for a section
     * @param nsection
     * @return
     */
    public ArrayList<OdfTextSection> getChildSections(OdfTextSection nsection) {
        ArrayList<OdfTextSection> foundChildren = new ArrayList<OdfTextSection>(0);

        try {
            NodeList nodeSet = (NodeList) xPath.evaluate(SECTION_ELEMENT, nsection, XPathConstants.NODESET);

            for (int i = 0; i < nodeSet.getLength(); i++) {
                Node foundNodeSection = nodeSet.item(i);

                foundChildren.add((OdfTextSection) foundNodeSection);
            }
        } catch (XPathExpressionException ex) {
            log.error("getChildSections : " + ex.getMessage());
        } finally {
            return foundChildren;
        }
    }

    /**
     * Returns the Section Type
     * @param nsection
     * @return
     */
    public String getSectionType(OdfTextSection nsection) {
        NamedNodeMap metaAttrs = getSectionMetadataAttributes(nsection);

        return getSectionType(nsection, metaAttrs);
    }


    public String getSectionType(OdfTextSection nsection, NamedNodeMap nattr) {
        Node nitem = nattr.getNamedItem(FILTER_SECTION_TYPE);

        if (nitem != null) {
            return nitem.getNodeValue();
        } else {
            return nsection.getTextNameAttribute();
        }
    }

    public ArrayList<Node> getBungeniMetadataAttributes(OdfTextSection nsection) {
        ArrayList<Node> nodeLists   = new ArrayList<Node>(0);
        NamedNodeMap    metaAttribs = getSectionMetadataAttributes(nsection);

        for (int i = 0; i < metaAttribs.getLength(); i++) {
            Node   foundNode     = metaAttribs.item(i);
            String metaLocalname = foundNode.getNodeName();

            if (metaLocalname.startsWith(FILTER_BUNGENI_SECTION_META)) {
                nodeLists.add(foundNode);
            }
        }

        return nodeLists;
    }

    public OdfStyle getSectionStyle(OdfTextSection oSection) {
        OdfOfficeAutomaticStyles osb      = oSection.getAutomaticStyles();
        OdfStyle           secStyle = osb.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);

        return secStyle;
    }

    public boolean removeSectionBackgroundImage(OdfTextSection oSection) {
        boolean bState = false;

        try {
            OdfOfficeAutomaticStyles   autoStyles = oSection.getAutomaticStyles();
            OdfStyle             secStyle   = autoStyles.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);
            Element sprops     = getSectionStyleProperties(secStyle);
            NodeList             nl         = sprops.getElementsByTagName("style:background-image");

            if (nl.getLength() > 0) {
                OdfStyleBackgroundImage img = (OdfStyleBackgroundImage) nl.item(0);

                // odfPackage.getPackage().remove(img.getHref());
                sprops.removeChild(img);

                OdfStyleBackgroundImage newimg = new OdfStyleBackgroundImage(odfDocument.getContentDom());

                sprops.appendChild(newimg);
                bState = true;
            }
        } catch (Exception ex) {
            log.error("removeSectionBackgroundImage : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    /*
     *
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
        Node props     = null;
        NodeList             nsectList = secStyle.getChildNodes();

        for (int i = 0; i < nsectList.getLength(); i++) {
            Node nmatch = nsectList.item(i);

            if (nmatch.getNodeName().equals(STYLE_SECTION_PROPS)) {
                props =  nmatch;
            }
        }

        return (Element)props;
        /**
         * returns this :
         * <style:section-properties fo:background-color="transparent" style:editable="false">
                <style:columns fo:column-count="1" fo:column-gap="0in"/>
                <style:background-image xlink:href="Pictures/100000000000009500000095F10913D8.jpg"
                    xlink:type="simple" xlink:actuate="onLoad"/>
            </style:section-properties>
         */
    }

    public NamedNodeMap getSectionMetadataAttributes(OdfTextSection nSection) {
        OdfStyle sectStyle = getSectionStyle(nSection);

        if (sectStyle != null) {
            Element sprops       = getSectionStyleProperties(sectStyle);
            NamedNodeMap         sectionProps = sprops.getAttributes();

            return sectionProps;
        } else {
            return null;
        }
    }

    /*
     * private void getChildSections(Node nNode, OdfJDomElement baseElement, int nDepth) {
     *   ++nDepth;
     *   //get the child elements of the root section
     *   NodeList nChildren = nNode.getChildNodes();
     *   for (int i=0; i < nChildren.getLength() ; i++ ) {
     *       Node nnChild = nChildren.item(i);
     *       //check if the child is an instance of OdfSection
     *       if (nnChild instanceof OdfSection) {
     *           OdfSection childSection = (OdfSection) nnChild;
     *           NamedNodeMap nattribs = getSectionMetadataAttributes(childSection);
     *           String sectionType = "";
     *           if (nattribs != null ) {
     *               sectionType = getSectionType(childSection, nattribs);
     *           }
     *           String sectionName = childSection.getName();
     *           OdfJDomElement newElement = new OdfJDomElement(childSection, sectionName, sectionType);
     *           baseElement.addContent(newElement);
     *           getChildSections(nnChild, newElement, nDepth);
     *       }
     *   }
     * }
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

    public OdfTextSection getSection(String sectionName) {
        OdfTextSection oSection = null;

        try {
            OdfFileDom docDom = odfDocument.getContentDom();

            oSection = (OdfTextSection) xPath.evaluate("//text:section[@text:name='" + sectionName + "']", docDom,
                    XPathConstants.NODE);
        } catch (Exception ex) {
            System.out.println(ex.getMessage())
;      ex.printStackTrace();  }
        finally {
            return oSection;
        }
    }

    public void iterateSections(IBungeniOdfSectionObjectIterator sectionIterator) {
        NodeList nlist = getDocumentSections();

        for (int i = 0; i < nlist.getLength(); i++) {
            OdfTextSection odfSection = (OdfTextSection) nlist.item(i);

            if (sectionIterator.nextSection(this, odfSection) == false) {
                break;
            }
        }
    }

    public void iterateSections(IBungeniOdfSectionIterator sectionIterator) {
        NodeList nlist = getDocumentSections();

        for (int i = 0; i < nlist.getLength(); i++) {
            OdfTextSection odfSection = (OdfTextSection) nlist.item(i);

            if (sectionIterator.nextSection(this, odfSection) == false) {
                break;
            }
        }
    }

    private Node getBodyNode(NodeList nlist) {
        for (int i = 0; i < nlist.getLength(); i++) {
            Node nnode = nlist.item(i);

            if (nnode instanceof OdfTextSection) {
                OdfTextSection nsection = (OdfTextSection) nnode;

                // get section style name
                String sectionStyleName = nsection.getStyleName();

                if (sectionStyleName != null) {
                    if (sectionStyleName.length() > 0) {
                        OdfStyle             sectStyle = getSectionStyle(nsection);
                        Element sProps    = getSectionStyleProperties(sectStyle);

                        if (sProps.hasAttributes()) {
                            NamedNodeMap sectionProps = sProps.getAttributes();
                            Node         nfoundNode   = sectionProps.getNamedItem(FILTER_SECTION_TYPE);

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
            OdfDocument              odoc   =
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
