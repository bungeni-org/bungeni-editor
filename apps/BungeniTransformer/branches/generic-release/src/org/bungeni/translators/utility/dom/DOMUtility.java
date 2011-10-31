package org.bungeni.translators.utility.dom;

//~--- non-JDK imports --------------------------------------------------------

import java.io.ByteArrayInputStream;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.bungeni.translators.utility.transformer.GenericTransformer;

/**
 * This class supplies several method useful for the management of the DOM documents
 *
 */
public class DOMUtility {

    /* The instance of this DOMUtility object */
    private static DOMUtility instance = null;

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
        File   resultFile = File.createTempFile("result", ".xml");
        Result result     = new StreamResult(resultFile);

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer();

        xformer.transform(source, result);

        // return the File
        return resultFile;
    }

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
}
