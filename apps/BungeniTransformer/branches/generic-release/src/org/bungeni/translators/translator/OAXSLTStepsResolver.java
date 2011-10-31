package org.bungeni.translators.translator;

/**
 *
 * @author Ashok Hariharan
 */
//~--- non-JDK imports --------------------------------------------------------
import java.io.IOException;
import java.util.logging.Level;
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.transformer.XSLTTransformer;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;
import org.bungeni.translators.configurations.OAConfiguration;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.bungeni.translators.process.actions.ProcessUnescape;
import org.bungeni.translators.utility.dom.DOMUtility;
import org.bungeni.translators.utility.streams.StreamSourceUtility;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Used to resolve the XSLT INPUT STEPS of a configuration file
 */
public class OAXSLTStepsResolver {

    private static org.apache.log4j.Logger log = Logger.getLogger(OAXSLTStepsResolver.class.getName());

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
    public static StreamSource resolve(StreamSource anODFDocument, TreeMap<Integer, OAXSLTStep> stepsMap)
            throws XPathExpressionException, TransformerException, UnsupportedEncodingException {

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

            if (nextStep.hasPreProc()) {
                try {
                    //apply the pre-processor
                    iteratedDocument = applyProcessorByRef(iteratedDocument, nextStep.getPreProc());
                } catch (SAXException ex) {
                    log.error("Error while applying pre Processor ", ex);
                } catch (IOException ex) {
                    log.error("Error while applying pre Processor ", ex);
                }
            }

            // create a stream source by the href of the XSLT
            StreamSource xsltStream = null;

            try {
                xsltStream = FileUtility.getInstance().FileAsStreamSource(stepHref);
            } catch (FileNotFoundException e) {
                log.error("input step xslt file : " + stepHref + " not found.", e);
            }

            if (xsltStream != null) {

                // start the transformation
                iteratedDocument = XSLTTransformer.getInstance().transform(iteratedDocument, xsltStream);
                log.debug("executing input step = " + nextStep.getName() + ", " + nextStep.getHref());
            }

            if (nextStep.hasPostProc()) {
                //apply the post processor 
                try {
                    //apply the pre-processor
                    iteratedDocument = applyProcessorByRef(iteratedDocument, nextStep.getPostProc());
                } catch (SAXException ex) {
                    log.error("Error while applying post Processor ", ex);
                } catch (IOException ex) {
                    log.error("Error while applying post Processor ", ex);
                }
            }

        }

        // return the StreamSource of the transformed document
        return iteratedDocument;
    }

    private static StreamSource applyProcessorByRef(StreamSource iteratedDocument, String preProc)
            throws XPathExpressionException, TransformerException, SAXException, IOException {
        String sIdAttrValue = preProc.replace("#", "").trim();
        List<OAProcessStep> processSteps = OAConfiguration.getInstance().getProcessGroup(sIdAttrValue);
        // !+PROCESS+ACTION (oct,2011) -- 
        // currently only the escape processor is support
        // factoring out and generalizing as a dynamicall loadable interface class to be done
        // convert StreamSource to Document
        Document domDocument = StreamSourceUtility.getInstance().writeToDOM(iteratedDocument);
        for (OAProcessStep oAProcessStep : processSteps) {
            if (oAProcessStep.getName().equals("unescapehtml")) {
                ProcessUnescape pu = new ProcessUnescape();
                domDocument = pu.process(domDocument, oAProcessStep);
            }
        }
        //convert back from dom to stream 
        StreamSource outputDocument = DOMUtility.getInstance().writeToStreamSource(domDocument);
        return outputDocument;
    }
}
