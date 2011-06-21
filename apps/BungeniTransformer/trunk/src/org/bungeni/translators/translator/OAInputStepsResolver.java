package org.bungeni.translators.translator;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.configurations.OAConfiguration;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.xslttransformer.XSLTTransformer;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;

/**
 * Used to resolve the XSLT INPUT STEPS of a configuration file
 */
public final class OAInputStepsResolver {
   private static org.apache.log4j.Logger log                   = Logger.getLogger(OAInputStepsResolver.class.getName());

    /**
     * Return the StreamSource obtained after all the INPUT XSLT steps of the given
     * configuration Document are applied to the given Stream source of the document
     * @param aDocument a Stream Source of an ODF DOM document
     * @param aConfiguration the configuration file that contains the XSLT STEPS
     * @return a new StreamSource Obtained applying all the steps of the configuration to the
     *                      given StreamSource
     * @throws XPathExpressionException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    protected static StreamSource resolve(StreamSource anODFDocument, OAConfiguration aConfiguration)
            throws XPathExpressionException, TransformerException, UnsupportedEncodingException {

        // get the steps from the configuration
        TreeMap<Integer, OAXSLTStep> stepsMap = aConfiguration.getInputSteps();

        // create an iterator on the hash map
        Iterator<OAXSLTStep> mapIterator = stepsMap.values().iterator();

        // copy the document to translate
        StreamSource iteratedDocument = anODFDocument;

        // while the Iterator has steps apply the transformation
        while (mapIterator.hasNext()) {

            // get the next step
            OAXSLTStep nextStep = (OAXSLTStep) mapIterator.next();

            // get the href from the step
            String stepHref = GlobalConfigurations.getApplicationPathPrefix() + nextStep.getHref();

            // create a stream source by the href of the XSLT
            StreamSource xsltStream = null;

            try {
                xsltStream = FileUtility.getInstance().FileAsStreamSource(stepHref);
            } catch (FileNotFoundException e) {
                    log.error("input step xslt file : " + stepHref + " not found." , e);
            }

            if (xsltStream != null) {

                // start the transformation
                iteratedDocument = XSLTTransformer.getInstance().transform(iteratedDocument, xsltStream);
                log.debug("executing input step = " + nextStep.getName() + ", " + nextStep.getHref());
                // /// ---ASHOK--- //////
                /*
                if (nextStep.getPosition() == 4) {
                 try {
                
                       java.io.File ftmp = org.bungeni.translators.utility.streams.StreamSourceUtility.getInstance().writeToFile(iteratedDocument);
                       FileUtility.getInstance().copyFile(ftmp, new java.io.File("/home/undesa/tmp/"+nextStep.getName()+"_out.xml"));
                   } catch (java.io.IOException ex) {
                      System.out.println(ex.getMessage());
                   }
                }
                */
                // /// ---ASHOK--- //////
            }
        }

        // return the StreamSource of the transformed document
        return iteratedDocument;
    }
}
