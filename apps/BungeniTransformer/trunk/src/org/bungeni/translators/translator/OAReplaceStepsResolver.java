package org.bungeni.translators.translator;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.translators.configurations.OAConfiguration;
import org.bungeni.translators.configurations.steps.OAReplaceStep;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.streams.StreamSourceUtility;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;
import org.bungeni.translators.utility.runtime.TempFileManager;

/**
 * Used to resolve the REPLACE STEPS of a configuration file
 */
public final class OAReplaceStepsResolver {
  private static org.apache.log4j.Logger log                   = Logger.getLogger(OAReplaceStepsResolver.class.getName());

    /**
     * Return the StreamSource obtained after all the result steps of the given
     * configuration Document are applied to the given Stream source of the document
     * @param aDocument a Stream Source of an ODF DOM document
     * @param aConfiguration the configuration file that contains the MAP STEPS
     * @return a new StreamSource obtained applying all the steps of the configuration to the
     *                      given StreamSource
     * @throws XPathExpressionException
     * @throws TransformerException
     * @throws IOException
     */
    protected static StreamSource resolve(StreamSource anODFDocument, OAConfiguration aConfiguration)
            throws XPathExpressionException, TransformerException, IOException {
        BufferedWriter out = null;
        StreamSource tempStreamSource = null;
        File tempFile = null;
        try {
            // get the replacement step from the configuration
            TreeMap<Integer, OAReplaceStep> replaceSteps = aConfiguration.getReplaceSteps();

            // create an iterator on the hash map
            Iterator<OAReplaceStep> replaceIterator = replaceSteps.values().iterator();

            // get the Document String
            String iteratedStringDocument = StreamSourceUtility.getInstance().writeToString(anODFDocument);

            // while the Iterator has replacement steps apply the replacement
            while (replaceIterator.hasNext()) {

                // get the next step
                OAReplaceStep nextStep = (OAReplaceStep) replaceIterator.next();
                log.debug("executing replace step  :" + nextStep.getName()  );
                // get the pattern of the replace
                String pattern = nextStep.getPattern();

                // get the replacement of the step
                String replacement = nextStep.getReplacement();

                // apply the replacement
                iteratedStringDocument = iteratedStringDocument.replaceAll(pattern, replacement);
            }

            // create a file for the result
            tempFile = TempFileManager.createTempFile("temp", ".xml");

            // write the result on the temporary file
            out = new BufferedWriter(new FileWriter(tempFile));
            out.write(iteratedStringDocument);
            out.flush();
        } catch (Exception ex ) {
            log.error("Error in resolve", ex);
        } finally {
            try {
                if (out != null  ) {
                    out.close();
                    }
            } catch (IOException ex ){
                log.error("OAReplaceStepsResolver : Error WHILE_CLOSING_FILE ", ex);
            }
        }
        // create a new StremSource
        if (tempFile != null) {
            tempStreamSource = FileUtility.getInstance().FileAsStreamSource(tempFile);
        }
       // return the string of the new created document
        return tempStreamSource;
    }
}
