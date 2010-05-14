package org.un.bungeni.translators.utility.streams;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This class supplies several method useful for the management of the Stream Source
 *
 */
public class StreamSourceUtility {

    /* The instance of this StreamSourceUtility object */
    private static StreamSourceUtility instance = null;

    /**
     * Private constructor used to create the StreamSourceUtility instance
     */
    private StreamSourceUtility() {}

    /**
     * Get the current instance of the StreamSourceUtility class
     * @return the Utility instance
     */
    public static StreamSourceUtility getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            instance = new StreamSourceUtility();
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * This method write a Stream Source to a string
     * @param aStreamSource the Stream Source to transform into string
     * @return a string representing the given Stream Source
     * @throws TransformerException
     */
    public String writeToString(StreamSource aStreamSource) throws TransformerException {

        // get the Document String
        String resultStringDocument = new String();

        // create an instance of TransformerFactory
        TransformerFactory transFact = TransformerFactory.newInstance();

        // create a new transformer
        Transformer trans = transFact.newTransformer();

        // create the writer for the transformation
        StringWriter resultString = new StringWriter();

        // perform the transformation
        trans.transform(aStreamSource, new StreamResult(resultString));

        // copy the obtained string into the string to iterate
        resultStringDocument = resultString.toString();

        // return the string of the Stream Source
        return resultStringDocument;
    }

    /**
     * This method write a Stream Source to a File
     * @param aStreamSource the Stream Source to wrote to File
     * @return a File containing the given Stream Source
     * @throws TransformerException
     * @throws IOException
     */
    public File writeToFile(StreamSource aStreamSource) throws TransformerException, IOException {

        // write the result stream to a string
        String resultDocumentString = this.writeToString(aStreamSource);

        // create a file for the result
        File resultFile = File.createTempFile("result", ".xml");

        // write the result on the temporary file
        BufferedWriter outresult = new BufferedWriter(new FileWriter(resultFile));

        outresult.write(resultDocumentString);
        outresult.close();

        // delete the result file on exit
        resultFile.deleteOnExit();

        // return the string of the Stream Source
        return resultFile;
    }
}
