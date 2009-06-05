/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.odfdom.document;


import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.element.office.OdfMasterStyles;
import org.openoffice.odf.doc.element.style.OdfBackgroundImage;
import org.openoffice.odf.doc.element.style.OdfMasterPage;
import org.openoffice.odf.doc.element.style.OdfPageLayout;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Bungeni Odf Document Helper
 * @author Ashok
 */
public class BungeniOdfDocumentHelper {

    private OdfDocument odfDocument;
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfDocumentHelper.class.getName());

    /**
     * Init the object using the odfdocument handle
     * @param doc
     */
    public BungeniOdfDocumentHelper(OdfDocument doc) {
        this.odfDocument = doc;
    }

    /**
     * Get the standard page layout for the document
     * @return
     */
    public OdfPageLayout getStandardPageLayout()  {
        OdfPageLayout standardLayout = null;
        try {
            OdfMasterStyles mastersStyles = this.odfDocument.getOfficeMasterStyles();
            OdfMasterPage standardPage = mastersStyles.getMasterPage("Standard");
            String pageLayoutName = standardPage.getPageLayoutName();
            standardLayout = odfDocument.getStylesDom().getAutomaticStyles().getPageLayout(pageLayoutName);
        } catch (Exception ex) {
            log.error("getStandardPageLayout : ", ex);
        } finally {
            return standardLayout;
        }
    }

    /**
     * Removes the background image for the page if it has one
     */
    public void removeBackgroundImage(){
        try {
            //get a list of background image elelemtnes
            NodeList bgImageNodes = odfDocument.getStylesDom().getElementsByTagName("style:background-image");
            for (int i = 0; i < bgImageNodes.getLength(); i++) {
                OdfBackgroundImage bgImage = (OdfBackgroundImage) bgImageNodes.item(i);
                Node parentNode = bgImage.getParentNode();
                if (parentNode != null) {
                    if (parentNode.getNodeName().equals("style:page-layout-properties")) {
                        //we reset the background imag for the page
                        bgImage.setHref("");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("annulBackgroundImage : ", ex);
        }
    }
    
}
