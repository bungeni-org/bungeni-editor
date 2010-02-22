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
 *<p>
 * This class provides a wrapper on the ODFDOM odf library. ODFDOM primarily provides access
 * to the DOM of an ODF file -- BungeniOdfDOM provides a helper API on that to access common
 * document elements like properties, sections and track changes
 * </p>
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentHelper {
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfDocumentHelper.class.getName());
    private OdfDocument                    odfDocument;
    private BungeniOdfSectionHelper s_sectionHelper = null;
    private BungeniOdfPropertiesHelper s_propertiesHelper = null;
    private BungeniOdfTrackedChangesHelper s_changesHelper = null;

    /**
     * <p>Initialize the object using the ODFDOM OdfDocument handle
     * The OdfDocument object can be created using OdfDocument.loadDocument()</p>
     * @param doc OdfDocument handle
     */
    public BungeniOdfDocumentHelper(OdfDocument doc) {
        this.odfDocument = doc;
    }

    /**
     *<p>This API initializes the object using a File handle to a ODF document.
     * The OdfDocument handle is created internally by the object</p>
     * @param fodfFile
     * @throws java.lang.Exception
     */
    public BungeniOdfDocumentHelper(File fodfFile) throws Exception {
        this.odfDocument = OdfDocument.loadDocument(fodfFile);
    }

    /**
     * <p>Returns the handle to the OdfDocument object</p>
     * @return OdfDocument
     */
    public OdfDocument getOdfDocument() {
        return this.odfDocument;
    }

    /**
     * <p>Returns a BungeniOdfSectionHelper object (non static singleton)</p>
     * @return
     */
    public BungeniOdfSectionHelper getSectionHelper() {
        if (s_sectionHelper == null ) {
            s_sectionHelper = new BungeniOdfSectionHelper(this);
        }
        return s_sectionHelper;
    }

     /**
     * <p>Returns a BungeniOdfPropertiesHelper object</p>
     * @return
     */
    public BungeniOdfPropertiesHelper getPropertiesHelper() {
        if (s_propertiesHelper == null ) {
           s_propertiesHelper =  new BungeniOdfPropertiesHelper(this);
        }
        return s_propertiesHelper;
    }

     /**
     * <p>Returns a BungeniOdfTrackedChangesHelper object</p>
     * @return
     */
    public BungeniOdfTrackedChangesHelper getChangesHelper() {
        if (s_changesHelper == null ) {
           s_changesHelper =  new BungeniOdfTrackedChangesHelper(this);
        }
        return s_changesHelper;
    }


    /**
     * <p>Get the standard page layout for the document</p>
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
     * <p>Removes the background image for the page if it has one</p>
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
