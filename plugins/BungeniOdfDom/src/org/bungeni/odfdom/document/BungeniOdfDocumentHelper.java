package org.bungeni.odfdom.document;

//~--- non-JDK imports --------------------------------------------------------

import java.io.File;
import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
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
 * This class provides a wrapper on the ODFDOM odf library. ODFDOM primarily provides access
 * to the DOM of an ODF file -- BungeniOdfDOM provides a helper API on that to access common
 * document elements like properties, sections and track changes
 * 
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentHelper {
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfDocumentHelper.class.getName());
    private OdfDocument                    odfDocument;
    private BungeniOdfSectionHelper s_sectionHelper = null;
    private BungeniOdfPropertiesHelper s_propertiesHelper = null;
    private BungeniOdfTrackedChangesHelper s_changesHelper = null;

    /**
     * Initialize the object using the ODFDOM OdfDocument handle
     * The OdfDocument object can be created using OdfDocument.loadDocument()
     * @param doc OdfDocument handle
     */
    public BungeniOdfDocumentHelper(OdfDocument doc) {
        this.odfDocument = doc;
    }

    /**
     *This API initializes the object using a File handle to a ODF document.
     * The OdfDocument handle is created internally by the object
     * @param fodfFile
     * @throws java.lang.Exception
     */
    public BungeniOdfDocumentHelper(File fodfFile) throws Exception {
        this.odfDocument = OdfDocument.loadDocument(fodfFile);
    }

    /**
     * Returns the handle to the OdfDocument object
     * @return OdfDocument
     */
    public OdfDocument getOdfDocument() {
        return this.odfDocument;
    }

    /**
     * Returns a BungeniOdfSectionHelper object
     * @return
     */
    public BungeniOdfSectionHelper getSectionHelper() {
        if (s_sectionHelper == null ) {
            s_sectionHelper = new BungeniOdfSectionHelper(this);
        }
        return s_sectionHelper;
    }

     /**
     * Returns a BungeniOdfPropertiesHelper object
     * @return
     */
    public BungeniOdfPropertiesHelper getPropertiesHelper() {
        if (s_propertiesHelper == null ) {
           s_propertiesHelper =  new BungeniOdfPropertiesHelper(this);
        }
        return s_propertiesHelper;
    }

     /**
     * Returns a BungeniOdfTrackedChangesHelper object
     * @return
     */
    public BungeniOdfTrackedChangesHelper getChangesHelper() {
        if (s_changesHelper == null ) {
           s_changesHelper =  new BungeniOdfTrackedChangesHelper(this);
        }
        return s_changesHelper;
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
     * 
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
