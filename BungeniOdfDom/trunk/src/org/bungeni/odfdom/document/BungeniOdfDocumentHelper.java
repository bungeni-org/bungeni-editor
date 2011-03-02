package org.bungeni.odfdom.document;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.properties.BungeniOdfPropertiesHelper;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeMasterStyles;
import org.odftoolkit.odfdom.dom.element.style.StyleBackgroundImageElement;
import org.odftoolkit.odfdom.pkg.OdfPackage;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStylePageLayout;

/**
 * <p>
 * This class provides a wrapper on the ODFDOM odf library. ODFDOM primarily provides access
 * to the DOM of an ODF file -- BungeniOdfDOM provides a helper API on that to access common
 * document elements like properties, sections and track changes
 * </p>
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentHelper {
    private static org.apache.log4j.Logger log                =
        Logger.getLogger(BungeniOdfDocumentHelper.class.getName());
    private BungeniOdfTrackedChangesHelper s_changesHelper    = null;
    private BungeniOdfPropertiesHelper     s_propertiesHelper = null;
    private BungeniOdfSectionHelper        s_sectionHelper    = null;
    private OdfDocument                    odfDocument;

    /**
     * <p>This API initializes the object using a File handle to a ODF document.
     * The OdfDocument handle is created internally by the object</p>
     * @param fodfFile
     * @throws java.lang.Exception
     */
    public BungeniOdfDocumentHelper(File fodfFile) throws Exception {
        this.odfDocument = OdfDocument.loadDocument(fodfFile);
    }

    /**
     * <p>Initialize the object using the ODFDOM OdfDocument handle
     * The OdfDocument object can be created using OdfDocument.loadDocument()</p>
     * @param doc OdfDocument handle
     */
    public BungeniOdfDocumentHelper(OdfDocument doc) {
        this.odfDocument = doc;
    }

    /**
     * <p>Returns the handle to the OdfDocument object</p>
     * @return OdfDocument
     */
    public OdfDocument getOdfDocument() {
        return this.odfDocument;
    }

    /**
     * Returns the path to the document
     * @return String containing path to the document
     */
    public String getDocumentPath() {
        String sPath = "";

        try {
            URI  fURI = new URI(odfDocument.getBaseURI());
            File fDoc = new File(fURI);

            sPath = fDoc.getPath();
        } catch (URISyntaxException ex) {
            log.error("getDocumentPath :" + ex.getMessage(), ex);
        }

        return sPath;
    }

    /**
     * <p>Returns a BungeniOdfSectionHelper object (non static singleton)</p>
     * @return
     */
    public BungeniOdfSectionHelper getSectionHelper() {
        if (s_sectionHelper == null) {
            s_sectionHelper = new BungeniOdfSectionHelper(this);
        }

        return s_sectionHelper;
    }

    /**
     * <p>Returns a BungeniOdfPropertiesHelper object</p>
     * @return
     */
    public BungeniOdfPropertiesHelper getPropertiesHelper() {
        if (s_propertiesHelper == null) {
            s_propertiesHelper = new BungeniOdfPropertiesHelper(this);
        }

        return s_propertiesHelper;
    }

    /**
     * <p>Returns a BungeniOdfTrackedChangesHelper object</p>
     * @return
     */
    public BungeniOdfTrackedChangesHelper getChangesHelper() {
        if (s_changesHelper == null) {
            s_changesHelper = new BungeniOdfTrackedChangesHelper(this);
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
            StyleMasterPageElement    standardPage   = mastersStyles.getMasterPage("Standard");
            String                pageLayoutName = standardPage.getStylePageLayoutNameAttribute();

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
                StyleBackgroundImageElement bgImage    = (StyleBackgroundImageElement) bgImageNodes.item(i);
                Node                    parentNode = bgImage.getParentNode();

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

    /**
     * Returns the checksum of the odf document
     * @return
     */
    public long getChecksum() {
        long crc = 0;
        try {
            FileInputStream     fs    = new FileInputStream(getDocumentPath());
            CheckedInputStream  check = new CheckedInputStream(fs, new CRC32());
            BufferedInputStream in    = new BufferedInputStream(check);
            while (in.read() != -1) {
                // Read file in completely
            }
            crc = check.getChecksum().getValue();
            in.close();
            check.close();
            fs.close();
        } catch (IOException ex) {
            log.error("getChecksum :" + ex.getMessage());
        }
        return crc;
    }

    /**
     * Saves the current document to its current path
     * @return
     */
    public boolean saveDocument() {
        return saveDocument(this.getDocumentPath());
    }

    /**
     * Saves the document to a parameter path
     *
     */
    public boolean saveDocument(String fpath) {
        boolean bState = false;

        try {
            OdfPackage docPackage = this.odfDocument.getPackage();

            this.odfDocument.save(fpath);
            bState = true;
        } catch (Exception ex) {
            log.error("saveDocument : " + ex);
        }

        return bState;
    }

    /**
     * saves the document to a output file handle
     * @param foutputfile
     * @return
     */
    public boolean saveDocument(File foutputfile) {
        boolean bState = false;

        try {
            OdfPackage docPackage = this.odfDocument.getPackage();

            this.odfDocument.save(foutputfile);
            bState = true;
        } catch (Exception ex) {
            log.error("saveDocument : " + ex);
        }

        return bState;
    }

    /**
     * It is neccessary to close a document if you want to process an odf document across different threads.
     * e.g. acrosss swingworker threads... you get an odfdocumenthelper from the first thread save the document,
     * close the handle ... and do the same from the other
     *
     */
    public void closeDocument() {
        OdfPackage docPackage = this.odfDocument.getPackage();

        this.odfDocument.close();
        docPackage.close();
    }
}
