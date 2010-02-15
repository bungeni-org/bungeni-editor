package org.bungeni.odfdom.document;

//~--- non-JDK imports --------------------------------------------------------

import java.io.File;
import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.properties.BungeniOdfPropertiesHelper;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.office.OdfOfficeMasterStyles;
import org.odftoolkit.odfdom.doc.style.OdfStyleBackgroundImage;
import org.odftoolkit.odfdom.doc.style.OdfStyleMasterPage;
import org.odftoolkit.odfdom.doc.style.OdfStylePageLayout;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------


/**
 * Bungeni Odf Document Helper
 * This class provides a wrapper on the ODFDOM odf library.
 * It tries to provide a UNO like API on ODFDOM
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentHelper {
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfDocumentHelper.class.getName());
    private OdfDocument                    odfDocument;
    private BungeniOdfSectionHelper s_sectionHelper = null;
    private BungeniOdfPropertiesHelper s_propertiesHelper = null;

    /**
     * Init the object using the odfdocument handle
     * This can be created using OdfDocument.loadDocument()
     * @param doc
     */
    public BungeniOdfDocumentHelper(OdfDocument doc) {
        this.odfDocument = doc;
    }

    public BungeniOdfDocumentHelper(File fodfFile) throws Exception {
        this.odfDocument = OdfDocument.loadDocument(fodfFile);
    }

    public OdfDocument getOdfDocument() {
        return this.odfDocument;
    }

    public BungeniOdfSectionHelper getSectionHelper() {
        if (s_sectionHelper == null ) {
            s_sectionHelper = new BungeniOdfSectionHelper(this);
        }
        return s_sectionHelper;
    }

    public BungeniOdfPropertiesHelper getPropertiesHelper() {
        if (s_propertiesHelper == null ) {
           s_propertiesHelper =  new BungeniOdfPropertiesHelper(this);
        }
        return s_propertiesHelper;
    }

    /**
     * Get the standard page layout for the document
     * @return
     */
    public OdfStylePageLayout getStandardPageLayout() {
        OdfStylePageLayout standardLayout = null;

        try {
            OdfOfficeMasterStyles mastersStyles  = this.odfDocument.getOfficeMasterStyles();
            OdfStyleMasterPage   standardPage   = mastersStyles.getMasterPage("Standard");
            String          pageLayoutName = standardPage.getStylePageLayoutNameAttribute();
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
    public void removeBackgroundImage() {
        try {

            // get a list of background image elelemtnes
            NodeList bgImageNodes = odfDocument.getStylesDom().getElementsByTagName("style:background-image");

            for (int i = 0; i < bgImageNodes.getLength(); i++) {
                OdfStyleBackgroundImage bgImage    = (OdfStyleBackgroundImage) bgImageNodes.item(i);
                Node               parentNode = bgImage.getParentNode();

                if (parentNode != null) {
                    if (parentNode.getNodeName().equals("style:page-layout-properties")) {
                        bgImage.setXlinkHrefAttribute("");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("annulBackgroundImage : ", ex);
        }
    }
}
