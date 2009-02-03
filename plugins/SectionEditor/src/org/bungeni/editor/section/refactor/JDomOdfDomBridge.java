/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.OdfFileDom;
import org.openoffice.odf.doc.element.office.OdfAutomaticStyles;
import org.openoffice.odf.doc.element.style.OdfStyle;
import org.openoffice.odf.doc.element.text.OdfSection;
import org.openoffice.odf.dom.style.OdfStyleFamily;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class builds a JDom section tree from an OdfDom view of a ODT document
 * @author ashok
 */
public class JDomOdfDomBridge {
    private OdfDocument odfDocument  = null;
    private Document jdomDocument = null;

    private OdfFileDom odfStylesDom = null;
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JDomOdfDomBridge.class.getName());


    public JDomOdfDomBridge( OdfDocument odfDoc) {
        //filter odfdom node lists
         this.odfDocument = odfDoc;
         this.jdomDocument = new Document();
    }

    private static String FILTER_SECTION_TYPE = "BungeniSectionType";

    private String getSectionType(OdfSection nsection, NamedNodeMap nattr) {
        Node nitem = nattr.getNamedItem(FILTER_SECTION_TYPE);
        if (nitem != null) {
            return nitem.getNodeValue();
        } else
            return nsection.getName();
    }

    private OdfStyle getSectionStyle (OdfSection oSection) {
           OdfAutomaticStyles osb = oSection.getAutomaticStyles();
           OdfStyle secStyle = osb.getStyle(oSection.getStyleName(), OdfStyleFamily.Section);
           return secStyle;
    }

    private static String STYLE_SECTION_PROPS = "style:section-properties";

    private NamedNodeMap getSectionStyleProperties(OdfStyle sectStyle) {
        NamedNodeMap nmap = null;
        NodeList nsectList = sectStyle.getChildNodes();
        for (int i=0; i < nsectList.getLength(); i++) {
            Node nmatch = nsectList.item(i);
            if (nmatch.getNodeName().equals(STYLE_SECTION_PROPS)) {
                if (nmatch.hasAttributes()) {
                    NamedNodeMap nattrMap = nmatch.getAttributes();
                    return nattrMap;
                }
            }
        }
        return nmap;
}

    private NamedNodeMap getSectionMetadataAttributes(OdfSection nSection) {
             OdfStyle sectStyle = getSectionStyle(nSection);
             if (sectStyle != null) {
                NamedNodeMap sectionProps = getSectionStyleProperties (sectStyle);
                return sectionProps;
             } else
                 return null;
    }

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
                OdfJDomElement newElement = new OdfJDomElement(sectionName, sectionType);
                baseElement.addContent(newElement);
                /*
                if (nattribs != null) {
                    //if section has metadata
                    for (int d=0 ; d < nDepth ; d++) System.out.print(" ");
                    System.out.println(" - section type = " + getSectionType(childSection, nattribs));
                }*/
                getChildSections(nnChild, newElement, nDepth);
            }
        }
    }


    private static String BODY_NODE = "body";

       private Node getBodyNode(NodeList nlist) {
        for (int i=0; i < nlist.getLength(); i++) {
            Node nnode = nlist.item(i);
            if (nnode instanceof OdfSection) {
                OdfSection nsection = (OdfSection) nnode;
                //get section style name
                String sectionStyleName = nsection.getStyleName();
                if (sectionStyleName != null) {
                    if (sectionStyleName.length() > 0 ) {
                        //
                        OdfStyle sectStyle = getSectionStyle(nsection);
                        NamedNodeMap sectionProps = getSectionStyleProperties (sectStyle);
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
        return null;
    }

       private static String SECTION_ELEMENT = "text:section";
       private static String SECTION_ELEMENT_NAME_ATTR = "text:name";

    public void filterOdfDoc(){
        try {
            //get all text sections
            NodeList lst = this.odfDocument.getContentDom().getElementsByTagName(SECTION_ELEMENT);
            //get the first node with the body property
            Node nBodyNode = getBodyNode (lst);
            OdfJDomElement rootElement = new OdfJDomElement(nBodyNode.getAttributes().getNamedItem(SECTION_ELEMENT_NAME_ATTR).getNodeValue(), BODY_NODE);
            this.jdomDocument.setRootElement(rootElement);
            System.out.println(" body node = " + nBodyNode.getNodeName());
            //get child sections
            getChildSections(nBodyNode, (OdfJDomElement) jdomDocument.getRootElement(), 0);

        } catch (Exception ex) {
            log.info("filterOdfDoc" + ex.getMessage());
        }
    }

    public Document getJDomDocument(){
        return this.jdomDocument;
    }

    public static void main(String[] args) {
        try {
            OdfDocument oDoc = OdfDocument.loadDocument("/Users/ashok/Desktop/ken_bill_2009_1_10_eng_main.odt");
            JDomOdfDomBridge odfBridge = new JDomOdfDomBridge(oDoc);
            odfBridge.filterOdfDoc();
            Document jdoc = odfBridge.getJDomDocument();
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(jdoc , System.out);
        } catch (Exception ex) {
            Logger.getLogger(JDomOdfDomBridge.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
