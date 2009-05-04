package org.bungeni.odfdom.section;

import java.util.ArrayList;
import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.element.office.OdfAutomaticStyles;
import org.openoffice.odf.doc.element.style.OdfBackgroundImage;
import org.openoffice.odf.doc.element.style.OdfSectionProperties;
import org.openoffice.odf.doc.element.style.OdfStyle;
import org.openoffice.odf.doc.element.text.OdfSection;
import org.openoffice.odf.dom.style.OdfStyleFamily;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniOdfSectionHelper {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniOdfSectionHelper.class.getName());

    private OdfDocument odfDocument = null;

    private static String FILTER_SECTION_TYPE = "BungeniSectionType";
    private static String FILTER_BUNGENI_SECTION_META="Bungeni";
    private static String STYLE_SECTION_PROPS = "style:section-properties";
    private static String BODY_NODE = "body";
    private static String SECTION_ELEMENT = "text:section";
    private static String SECTION_ELEMENT_NAME_ATTR = "text:name";


    public BungeniOdfSectionHelper(OdfDocument odfDoc) {
        odfDocument = odfDoc;
    }

    public String getSectionType(OdfSection nsection, NamedNodeMap nattr) {

        Node nitem = nattr.getNamedItem(FILTER_SECTION_TYPE);
        if (nitem != null) {
            return nitem.getNodeValue();
        } else
            return nsection.getName();
    }

    public ArrayList<Node> getBungeniMetadataAttributes (OdfSection nsection) {
        ArrayList<Node> nodeLists = new ArrayList<Node>(0);
        NamedNodeMap metaAttribs = getSectionMetadataAttributes(nsection);
        for (int i=0; i < metaAttribs.getLength() ; i++) {
            Node foundNode = metaAttribs.item(i);
            String metaLocalname = foundNode.getNodeName();
            if (metaLocalname.startsWith(FILTER_BUNGENI_SECTION_META)) {
                nodeLists.add(foundNode);
            }
        }
        return nodeLists;
    }

    public OdfStyle getSectionStyle (OdfSection oSection) {
           OdfAutomaticStyles osb = oSection.getAutomaticStyles();
           OdfStyle secStyle = osb.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);
           return secStyle;
    }

    public boolean removeSectionBackgroundImage(OdfSection oSection) {
            boolean bState = false;
            try {
             OdfAutomaticStyles autoStyles = oSection.getAutomaticStyles();
             OdfStyle secStyle = autoStyles.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);
             OdfSectionProperties sprops = getSectionStyleProperties(secStyle);
             NodeList nl = sprops.getElementsByTagName("style:background-image");
             if (nl.getLength() > 0 ) {
                OdfBackgroundImage img = (OdfBackgroundImage) nl.item(0);
                //  odfPackage.getPackage().remove(img.getHref());
                sprops.removeChild(img);
                OdfBackgroundImage newimg = new OdfBackgroundImage(odfDocument.getContentDom());
                sprops.appendChild(newimg);
                bState = true;
             }
            } catch (Exception ex) {
                log.error("removeSectionBackgroundImage : " + ex.getMessage());
            } finally {
                return bState;
            }
    }

    public OdfSectionProperties getSectionStyleProperties(OdfStyle secStyle) {
        OdfSectionProperties props = null;
        NodeList nsectList = secStyle.getChildNodes();
        for (int i=0; i < nsectList.getLength(); i++) {
            Node nmatch = nsectList.item(i);
            if (nmatch.getNodeName().equals(STYLE_SECTION_PROPS)) {
                props = (OdfSectionProperties) nmatch;
            }
        }
        return props;
    }

   
    public NamedNodeMap getSectionMetadataAttributes(OdfSection nSection) {
             OdfStyle sectStyle = getSectionStyle(nSection);
             if (sectStyle != null) {
                OdfSectionProperties sprops =  getSectionStyleProperties (sectStyle);
                NamedNodeMap sectionProps = sprops.getAttributes();
                return sectionProps;
             } else
                 return null;
    }

    /*
    private void getChildSections(Node nNode, OdfJDomElement baseElement, int nDepth) {
        ++nDepth;
        //get the child elements of the root section
        NodeList nChildren = nNode.getChildNodes();
        for (int i=0; i < nChildren.getLength() ; i++ ) {
            Node nnChild = nChildren.item(i);
            //check if the child is an instance of OdfSection
            if (nnChild instanceof OdfSection) {
                OdfSection childSection = (OdfSection) nnChild;
                NamedNodeMap nattribs = getSectionMetadataAttributes(childSection);
                String sectionType = "";
                if (nattribs != null ) {
                    sectionType = getSectionType(childSection, nattribs);
                }
                String sectionName = childSection.getName();
                OdfJDomElement newElement = new OdfJDomElement(childSection, sectionName, sectionType);
                baseElement.addContent(newElement);
                getChildSections(nnChild, newElement, nDepth);
            }
        }
    }
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

    public void iterateSections(IBungeniOdfSectionIterator sectionIterator) {
        NodeList nlist = getDocumentSections();
        for (int i = 0; i < nlist.getLength(); i++) {
            OdfSection odfSection = (OdfSection) nlist.item(i);
            if (sectionIterator.nextSection(odfSection) == false) {
                break;
            }
        }

    }



       private Node getBodyNode(NodeList nlist) {
        for (int i=0; i < nlist.getLength(); i++) {
            Node nnode = nlist.item(i);
            if (nnode instanceof OdfSection) {
                OdfSection nsection = (OdfSection) nnode;
                //get section style name
                String sectionStyleName = nsection.getStyleName();
                if (sectionStyleName != null) {
                    if (sectionStyleName.length() > 0 ) {
                        OdfStyle sectStyle = getSectionStyle(nsection);
                        OdfSectionProperties sProps = getSectionStyleProperties (sectStyle);
                        if (sProps.hasAttributes()) {
                            NamedNodeMap sectionProps = sProps.getAttributes();
                            Node nfoundNode = sectionProps.getNamedItem(FILTER_SECTION_TYPE);
                            if (nfoundNode != null ) {
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



}
