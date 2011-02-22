package org.bungeni.ooo.rdf;

import com.sun.star.container.ElementExistException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.rdf.Literal;
import com.sun.star.rdf.RepositoryException;
import com.sun.star.rdf.Statement;
import com.sun.star.rdf.URI;
import com.sun.star.rdf.XDocumentMetadataAccess;
import com.sun.star.rdf.XLiteral;
import com.sun.star.rdf.XMetadatable;
import com.sun.star.rdf.XNamedGraph;
import com.sun.star.rdf.XURI;
import com.sun.star.text.XTextSection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 * This class manages RDF Metadata for a ODT document
 * Most of the API is specific to the XTextSection object
 * @author Ashok Hariharan
 */
public class RDFMetadata {

    /**
     * This is the namespace used for RDF metadata.
     * Note: this is different from the style-metadata namespace
     */
    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(RDFMetadata.class.getName());
    /**
     * The local name of the RDF metadata file
     */
    public static String RDF_META_FILE = "meta.rdf";
    public static String RDF_META_ROOT = "anx";
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
    public XURI getMetadataRootURI() throws IllegalArgumentException {
        XURI nsURI = URI.create(openofficeHelper.getComponentContext(),
                makeElementNS("/", RDF_META_ROOT));
        return nsURI;
    }

    /**
     * Generates a namespace for an Element name with a prefix
     * 
     * @param elementName
     * @return
     */
    private String makeElementNS(String prefix, String elementName) {
        prefix = prefix.trim();
        if (!prefix.startsWith("/")) prefix = "/" + prefix;
        if (prefix.endsWith("/")) {
            return RDFConstants.RDF_META_NAMESPACE + prefix + elementName;
        } else {
            return RDFConstants.RDF_META_NAMESPACE + prefix + "/" + elementName;
        }
    }

    /**
     * Generates a namespace for an Element -- uses the root metadata URI as the prefix
     * @param metaName
     * @return
     * @throws IllegalArgumentException
     */
    private String makeMetaNS(String metaName) throws IllegalArgumentException {
        return makeElementNS(getMetadataRootURI().getLocalName(), metaName);
    }

    /**
     * Generates a XURI element for the metadata URI
     * @param metaName
     * @return
     * @throws IllegalArgumentException
     */
    public XURI getMetaURI(String metaName) throws IllegalArgumentException {
        XURI nsURI = URI.create(openofficeHelper.getComponentContext(),
                makeMetaNS(metaName));
        return nsURI;
    }

    /**
     * Escapes the string value and generates a Literal object out of it
     * @param literalValue
     * @return
     */
    private XLiteral makeEscapedLiteral(String literalValue) {
        XLiteral xLiteral = Literal.create(this.openofficeHelper.getComponentContext(), StringEscapeUtils.escapeXml(literalValue));
        return xLiteral;
    }

    /**
     * Returns the relative path to the metadata file
     * @return
     */
    private String getRDFFilePath() {
        return RDF_META_PATH + RDF_META_FILE;
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
     *      else returns null
     * @return
     * @throws IllegalArgumentException
     * @throws RepositoryException
     */
    public XNamedGraph getDocumentMetadataGraph() throws IllegalArgumentException, RepositoryException {
        XNamedGraph xGraph = null;
        XDocumentMetadataAccess metaAccess = getDocumentMetadataAccess();
        XURI nsURI = this.getMetadataRootURI();
        //first we look for a existing metadata file
        XURI[] graphNames = metaAccess.getMetadataGraphsWithType(nsURI);
        for (XURI graphURI : graphNames) {
            if (graphURI.getLocalName().equals(RDF_META_FILE)) {
                xGraph = metaAccess.getRDFRepository().getGraph(graphURI);
            }
        }
        return xGraph;

    }

    /**
     * Initializes the RDF metadata file and returns a graph to the metadata file
     * @return
     * @throws IllegalArgumentException
     * @throws RepositoryException
     */
    public XNamedGraph initDocumentMetadataGraph() throws IllegalArgumentException, RepositoryException {
        XNamedGraph xGraph = null;
        //get the root metadata URI
        XURI nsURI = this.getMetadataRootURI();
        XDocumentMetadataAccess metaAccess = getDocumentMetadataAccess();
        XURI[] xTypes = {nsURI};
        XURI metaURI = null;
        try {
            metaURI = metaAccess.addMetadataFile(getRDFFilePath(), xTypes);
            xGraph = metaAccess.getRDFRepository().getGraph(metaURI);
        } catch (ElementExistException ex) {
            log.error("Error while adding rdf file  " + ex.getMessage());
        }
        return xGraph;
    }


    /**
     * Adds section metadata ; checks if the subject and predicate exists ; if
     * they do ; it deletes it and adds a new tripe; finally returns the added
     * metadata as a statmement
     * @param aSection
     * @param sectionMetaName
     * @param sectionMetaValue
     * @return
     */
    public Statement addSectionMetadata(XTextSection aSection, String sectionMetaName, String sectionMetaValue) {
        Statement sectionStatement = null;
        XNamedGraph xGraph = null;

        //get the document metadata graph
        try {
            xGraph = getDocumentMetadataGraph();
        } catch (IllegalArgumentException ex) {
            log.error("error getting document metadata graph", ex);
        } catch (RepositoryException ex) {
            log.error("error getting document metadata graph", ex);
        }

        if (xGraph != null) {
            try {
                //prepare the URI for the metaName
                XURI uSectionMeta = getMetaURI(sectionMetaName);
                XLiteral uSectionValue = makeEscapedLiteral(sectionMetaValue);
                //get the metadatable interface of the section
                XMetadatable metaSection = ooQueryInterface.XMetadatable(aSection);

                //first remove section metadata
                boolean bRemoved = removeMetadataByPredicate(xGraph, metaSection, uSectionMeta);
                log.info("Removing section meta : " + sectionMetaName  + " returned = " + bRemoved);

                //now attempt to add the RDF statement
                boolean bError = true;
                try {
                    xGraph.addStatement(metaSection, uSectionMeta, uSectionValue);
                    bError = false;
                } catch (NoSuchElementException ex) {
                    log.error("error while adding section metadata", ex);
                } catch (RepositoryException ex) {
                    log.error("error while adding section metadata", ex);
                }

                //now refetch the RDF statmeent
                if (!bError) {
                    //if there were no exceptions
                    sectionStatement = getMetadataByPredicate(xGraph, metaSection, uSectionMeta);
                }
            } catch (IllegalArgumentException ex) {
                log.error("error while getting root namespace", ex);
            }
        }

        return sectionStatement;
    }

    /**
     * Returns section metadata by subject + predicate combination 
     * @param metadataGraph
     * @param xSubject
     * @param xPredicate
     * @return
     */

    public Statement getMetadataByPredicate(XNamedGraph metadataGraph, XMetadatable xSubject, XURI xPredicate) {
        XEnumeration xEnum = null;
        Statement foundStatement = null;
            try {
                xEnum = metadataGraph.getStatements(xSubject, xPredicate, null);
            } catch (NoSuchElementException ex) {
                log.info("Error while getting rdf statement" ,ex );
            } catch (RepositoryException ex) {
                log.info("Error while getting rdf statement" ,ex );
            }
           if (xEnum != null ) {
              while(xEnum.hasMoreElements()) {
                try {
                    Statement aStatement = (Statement) xEnum.nextElement();
                    if (aStatement.Predicate.getStringValue().equals(xPredicate.getStringValue())) {
                        foundStatement = aStatement;
                        break;
                    }
                } catch (NoSuchElementException ex) {
                    log.error("error while enumeration section rdf", ex);
                } catch (WrappedTargetException ex) {
                    log.error("error while enumeration section rdf", ex);
                }
              }
           }
        return foundStatement;
        }


    /**
     * Queries the section for the metadata ; if the metadata exists, returns a
     * statement ; otherwise returns null. This is a wrapper on getSectionMetadataByPredicate
     * @param aSection
     * @param sectionMetaName
     * @return
     */
    public Statement getSectionMetadataByName(XTextSection aSection, String sectionMetaName) {
        Statement sectionStatement = null;
        XNamedGraph xGraph = null;
        XEnumeration sectionEnumerator = null;
        try {
            xGraph = getDocumentMetadataGraph();
        } catch (IllegalArgumentException ex) {
            log.error("error getting document metadata graph", ex);
        } catch (RepositoryException ex) {
            log.error("error getting document metadata graph", ex);
        }
        if (xGraph != null) {
            try {
                //prepare the URI for the metaName
                XURI uSectionMeta = getMetaURI(sectionMetaName);
                XMetadatable sectionResource = ooQueryInterface.XMetadatable(aSection);

                //enumerate the section metadata
                sectionStatement =  getMetadataByPredicate(xGraph, sectionResource, uSectionMeta);

            } catch (IllegalArgumentException ex) {
                log.error("error while getting root namespace", ex);
            }
        }
        return sectionStatement;

    }

     /**
      * Removes a RDF metadata statmeent
      * @param metadataGraph
      * @param xSubject
      * @param xPredicate
      * @return
      */
     public boolean removeMetadataByPredicate(XNamedGraph metadataGraph, XMetadatable xSubject, XURI xPredicate) {
        boolean bRemoved = false;
        try {
            metadataGraph.removeStatements(xSubject, xPredicate, null);
            bRemoved = true;
        } catch (NoSuchElementException ex) {
            log.info("Element not removed as it does not exist" , ex);
            bRemoved = true;
        } catch (RepositoryException ex) {
            log.error("Attempt to remove raised an error" , ex);
        }
        return bRemoved;
     }


     public Statement[] getSectionMetadata(XTextSection aSection) {
       XNamedGraph xGraph = null;
       Statement[] metaStatements = {};
       try {
            xGraph = getDocumentMetadataGraph();
        } catch (IllegalArgumentException ex) {
            log.error("error getting document metadata graph", ex);
        } catch (RepositoryException ex) {
            log.error("error getting document metadata graph", ex);
        }
        if (xGraph != null) {
           metaStatements = getMetadata(xGraph, ooQueryInterface.XMetadatable(aSection));
        }
        return metaStatements;
     }

     /**
      * Returns all the metadata attached to a section as an array of
      * RDF Statement objects
      * @param aGraph
      * @param aSection
      * @return
      */
     public Statement[] getMetadata(XNamedGraph aGraph, XMetadatable xSubject) {
         List<Statement> statements = new ArrayList<Statement>(0);
         XEnumeration xEnum = null;
            try {
               xEnum = aGraph.getStatements(xSubject, null, null);
            } catch (NoSuchElementException ex) {
               log.error("error getting document metadata graph", ex);
            } catch (RepositoryException ex) {
               log.error("error getting document metadata graph", ex);
            }
          if (xEnum != null) {
            while (xEnum.hasMoreElements() ) {
                Statement aStatement  = null;
                try {
                     aStatement = (Statement) xEnum.nextElement();
                } catch (NoSuchElementException ex) {
                     log.error("error browsing elements", ex);
                } catch (WrappedTargetException ex) {
                    log.error("error browsing elements", ex);
                }
               if (aStatement != null ) {
                   statements.add(aStatement);
               }
            }
          }
         return statements.toArray(new Statement[statements.size()]);
     }

    }

