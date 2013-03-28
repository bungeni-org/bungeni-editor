package org.bungeni.translators.utility.streams;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.lang.RandomStringUtils;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.files.OutputXML;
import org.bungeni.translators.utility.runtime.CloseHandle;
import org.bungeni.translators.utility.runtime.TempFileManager;
import org.bungeni.translators.utility.transformer.GenericTransformer;
import org.w3c.dom.Document;

/**
 * This class supplies several method useful for the management of the Stream Source
 *
 */
public class StreamSourceUtility {

    /* The instance of this StreamSourceUtility object */
    private static StreamSourceUtility instance = null;

       /* This is the logger */
    private static org.apache.log4j.Logger log              =
        org.apache.log4j.Logger.getLogger(StreamSourceUtility.class.getName());

    /**
     * Private constructor used to create the StreamSourceUtility instance
     */
    private StreamSourceUtility(){
    }

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
     * @param aStreamSource the Stream Source to transform into string, this is closed after reading
     * @return a string representing the given Stream Source
     * @throws TransformerException
     */
    public String writeToString(StreamSource aStreamSource) throws TransformerException {
        
        String resultStringDocument = "";
        try {
            // create the writer for the transformation
            StringWriter resultString = new StringWriter();

            // perform the transformation
            Transformer trans = GenericTransformer.getInstance().getTransformer();
            trans.transform(
                    aStreamSource,
                    new StreamResult(
                        resultString
                        )
            );

            // copy the obtained string into the string to iterate
            resultStringDocument = resultString.toString();
        } catch (Exception ex) {
            log.error("Error while writing streamSource to string", ex);
        } finally {
            CloseHandle.closeQuietly(aStreamSource);
        }
        // return the string of the Stream Source
        return resultStringDocument;
    }

    /**
     * This method write a Stream Source to a File
     * The stream source cannot be used anymore
     * @param aStreamSource the Stream Source to write to File
     * @return a File containing the given Stream So
     * urce
     * @throws TransformerException
     * @throws IOException
     */
    public File writeToFile(StreamSource aStreamSource) throws TransformerException, IOException {

        // write the result stream to a string
        String resultDocumentString = this.writeToString(aStreamSource);
       // create a file for the result
        String randomFileName = RandomStringUtils.randomAlphanumeric(8);

        // use TempFileManager class that handles better than deleteonExit() ; 
        File resultFile = TempFileManager.createTempFile(randomFileName, ".xml");

        // write the result on the temporary file
        BufferedWriter outresult = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(resultFile),
                    "UTF-8"
                    )
                );

        try {
            outresult.write(resultDocumentString);
            outresult.flush();
        } catch (Exception ex) {
            log.error("Error while writing to file", ex);
        } finally {
            CloseHandle.closeQuietly(aStreamSource);
            CloseHandle.closeQuietly(outresult);
        }
        // return the string of the Stream Source
        return resultFile;
    }


    /**
     * This is a more sophisticated edition of _writeToFile_
     * This API writes the StreamSource to file and returns a handle to the File
     * and also a new StreamSource based on the file.
     * The name for the temporary file can be controlled using this API
     * @param aStreamSource
     * @param prefix
     * @param ext
     * @return
     * @throws TransformerException
     * @throws IOException
     */
     public OutputXML writeStreamSourceToFile(StreamSource aStreamSource, String prefix, String ext) throws TransformerException, IOException {
        // write the result stream to a string
        String resultDocumentString = this.writeToString(aStreamSource);
        File resultFile = TempFileManager.createTempFile(prefix, ext);
        // write the result on the temporary file
        BufferedWriter outresult = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(resultFile),
                    "UTF-8"
                    )
                );
        try {
            outresult.write(resultDocumentString);
            outresult.flush();
            CloseHandle.closeQuietly(outresult);
        } catch(Exception ex) {
            log.error("Error while writing to file " + resultFile, ex);
        }  finally {
            CloseHandle.closeQuietly(aStreamSource);
        }
       
        //Create a new StreamSource object based on the file and return the
        // StreamSource and the File in a combined OutputXML object
        StreamSource ss = FileUtility.getInstance().FileAsStreamSource(resultFile);
        OutputXML xml = new OutputXML(ss, resultFile);
        return xml;
    }


    /**
     * Write a StreamSource to DOM
     * Closes the input StreamSource
     * @param aStreamSource
     * @return
     * @throws TransformerException
     */
    public Document writeToDOM(StreamSource aStreamSource) throws TransformerException {
         // create an instance of TransformerFactory
        Document document = null;
        try {
            DOMResult aDOMResult = new DOMResult();
            GenericTransformer.getInstance().getTransformer().transform(aStreamSource, aDOMResult);
            document = (Document) aDOMResult.getNode();
        } catch (TransformerException ex) {
            throw ex;
        } finally {
            CloseHandle.closeQuietly(aStreamSource);
        }
        return document;
    }


    
    
}
