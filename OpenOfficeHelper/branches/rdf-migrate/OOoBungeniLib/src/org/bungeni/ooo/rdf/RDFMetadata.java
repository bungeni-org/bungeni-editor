package org.bungeni.ooo.rdf;

import com.sun.star.container.ElementExistException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.rdf.RepositoryException;
import com.sun.star.rdf.URI;
import com.sun.star.rdf.XDocumentMetadataAccess;
import com.sun.star.rdf.XNamedGraph;
import com.sun.star.rdf.XURI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 * This class manages RDF Metadata
 * @author Ashok Hariharan
 */
public class RDFMetadata {

    /**
     * This is the namespace used for RDF metadata.
     * Note: this is different from the style-metadata namespace
     */
    public static String RDF_META_NAMESPACE = "http://editor.bungeni.org/1.0/anx";
    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(RDFMetadata.class.getName());
    /**
     * The local name of the RDF metadata file
     */
    public static String RDF_META_FILE = "meta.rdf";
    /**
     * RDF_META_PATH + RDF_META_FILE gives you the relative path to the rdf metadata file within the ODF file
     */
    public static String RDF_META_PATH = "meta/";
    private OOComponentHelper openofficeHelper;

    /**
     * Requires a OOComponentHelper object since it operates on a per-document basis
     * @param ooHelper
     */
    public RDFMetadata(OOComponentHelper ooHelper) {
        this.openofficeHelper = ooHelper;
    }

    /**
     * Gets the root namespace URI for the rdf metadata
     * @return
     * @throws IllegalArgumentException
     */
    private XURI getNSUri() throws IllegalArgumentException {
        XURI nsURI = URI.create(openofficeHelper.getComponentContext(), RDF_META_NAMESPACE);
        return nsURI;
    }

    /**
     * Provides access to the Document metadata
     * @return XDocumentMetadataAccess object from the document model
     */
    private XDocumentMetadataAccess getDocumentMetadataAccess() {
        XDocumentMetadataAccess metaAccess = ooQueryInterface.XDocumentMetadataAccess(openofficeHelper.getDocumentModel());
        return metaAccess;
    }

    /**
     * Gets the RDF graph of the metadata file
     *   -- checks if the rdf graph exists ; if it exists returns the existing one
     *      if it does not exist, creates a new one
     * @return
     * @throws IllegalArgumentException
     * @throws RepositoryException
     */
    public XNamedGraph getDocumentMetadataFile() throws IllegalArgumentException, RepositoryException {
        XNamedGraph xGraph = null;
        XDocumentMetadataAccess metaAccess = getDocumentMetadataAccess();
        XURI nsURI = getNSUri();
        //first we look for a existing metadata file
        XURI[] graphNames = metaAccess.getMetadataGraphsWithType(nsURI);
        for (XURI graphURI : graphNames) {
            if (graphURI.getLocalName().equals(RDF_META_FILE)) {
                xGraph = metaAccess.getRDFRepository().getGraph(graphURI);
            }
        }
        if (xGraph == null) {
            //no metadata graph was found
            //so we add a new one
            XURI[] xTypes = {nsURI};
            XURI metaURI = null;
            try {
                metaURI = metaAccess.addMetadataFile(RDF_META_PATH + RDF_META_FILE, xTypes);
                xGraph = metaAccess.getRDFRepository().getGraph(metaURI);
            } catch (ElementExistException ex) {
                log.error("Errror while adding rdf file  " + ex.getMessage());
            }
        }
        return xGraph;
    }
}
