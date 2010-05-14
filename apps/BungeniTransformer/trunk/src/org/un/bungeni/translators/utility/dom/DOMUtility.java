package org.un.bungeni.translators.utility.dom;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
}
