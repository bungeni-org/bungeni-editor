package org.bungeni.translators.utility.dom;

//~--- non-JDK imports --------------------------------------------------------

import java.io.ByteArrayInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.saxon.dom.DocumentWrapper;
import org.bungeni.translators.utility.runtime.TempFileManager;
import org.bungeni.translators.utility.transformer.GenericTransformer;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class supplies several method useful for the management of the DOM documents
 *
 */
public class DOMUtility {

    /* The instance of this DOMUtility object */
    private static DOMUtility instance = null;

      private static org.apache.log4j.Logger log  =
        org.apache.log4j.Logger.getLogger(DOMUtility.class.getName());

    /**
     * Private constructor used to create the DOMUtility instance
     */
    private DOMUtility() {}

    /**
     * Get the current instance of the DOMUtility class
     * @return the Utility instance
     */
    public static DOMUtility getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            instance = new DOMUtility();
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * Write the given DOM document to a File and return the File
     * @param aDOMDocument to write to the FILE
     * @return The file that contains the dom document
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    public File writeToFile(Document aDOMDocument)
            throws IOException, TransformerFactoryConfigurationError, TransformerException {

        // Prepare the DOM document for writing
        Source source = new DOMSource(aDOMDocument);

        // Prepare the output file
        File   resultFile = TempFileManager.createTempFile("result", ".xml");
        Result result     = new StreamResult(resultFile);

        // Write the DOM document to the file
        Transformer xformer = GenericTransformer.getInstance().getTransformer();

        xformer.transform(source, result);

        // return the File
        return resultFile;
    }

    /**
     * Converts a DOM document to a StreamSource
     * @param domDocument
     * @return
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public StreamSource writeToStreamSource(Document domDocument) throws TransformerConfigurationException, TransformerException {
       DOMSource domSource = new DOMSource(domDocument);
       StringWriter swDomString = new StringWriter();

       StreamResult sr = new StreamResult(swDomString);
       Transformer transformer = GenericTransformer.getInstance().getTransformer();
       transformer.transform(domSource, sr);

       byte[] arrBytes = swDomString.toString().getBytes();
       InputStream is = new ByteArrayInputStream(arrBytes);
       StreamSource sOutput = new StreamSource(is);

       return sOutput;
    }

    /**
     * Creates a SAXON DocumentWrapper object around a DOM node
     * @param aNode
     * @return
     * @throws TransformerConfigurationException
     */
    public DocumentWrapper getSaxonDocumentWrapperForNode(Node aNode) throws TransformerConfigurationException {
        Document doc = null;
        try {
          doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                  parse(
                    new ByteArrayInputStream(getNodeAsString(aNode).getBytes())
                    );
        } catch (ParserConfigurationException ex) {
            log.error("Error while parsing config doc", ex);
        } catch (SAXException ex) {
            log.error("Error while parsing config doc", ex);
        } catch (IOException ex) {
            log.error("Error while parsing config doc", ex);
        }
        DocumentWrapper dwSaxon = new DocumentWrapper(doc, doc.getBaseURI(), GenericTransformer.getInstance().getTransformerFactoryImpl().getConfiguration());
        return dwSaxon;
    }

    /**
     * Returns a String representation for a NOde
     * @param aNode
     * @return
     */
    public String getNodeAsString(Node aNode) {
       StringWriter sw = new StringWriter();
       try {
           Transformer t = GenericTransformer.getInstance().getTransformer();
           t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
           t.transform(new DOMSource(aNode), new StreamResult(sw));
       } catch (TransformerException ex) {
           log.error("nodeToString Transformer Exception", ex);
       }
       return sw.toString();

    }
}
