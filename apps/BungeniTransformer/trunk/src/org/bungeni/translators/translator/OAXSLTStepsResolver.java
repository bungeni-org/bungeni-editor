package org.bungeni.translators.translator;

/**
 *
 * @author Ashok Hariharan
 */
//~--- non-JDK imports --------------------------------------------------------
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;
import org.bungeni.translators.configurations.OAConfiguration;
import org.bungeni.translators.configurations.Parameter;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.bungeni.translators.process.actions.ProcessUnescape;
import org.bungeni.translators.utility.dom.DOMUtility;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.runtime.CloseHandle;
import org.bungeni.translators.utility.streams.StreamSourceUtility;
import org.bungeni.translators.utility.transformer.XSLTTransformer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *This class resplves the step pattern in the configuration files.
 * The step pattern is used to process, input, output and postxml steps
 *
 */
public class OAXSLTStepsResolver {

    private static org.apache.log4j.Logger log = Logger.getLogger(OAXSLTStepsResolver.class.getName());
    private HashMap<String,Parameter> pipelineInputParams ;

    private static OAXSLTStepsResolver thisInstance = null;

    private OAXSLTStepsResolver() {
        this.pipelineInputParams = new HashMap<String,Parameter>();
    }

    public static OAXSLTStepsResolver getInstance() {
        if (OAXSLTStepsResolver.thisInstance == null) {
            OAXSLTStepsResolver.thisInstance = new OAXSLTStepsResolver();
        }
        return OAXSLTStepsResolver.thisInstance;
    }

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
    public StreamSource resolve(StreamSource anODFDocument,
            TreeMap<Integer,
            OAXSLTStep> stepsMap)
            throws XPathExpressionException, TransformerException, UnsupportedEncodingException {

        // create an iterator on the hash map
        Iterator<OAXSLTStep> mapIterator = stepsMap.values().iterator();

        // copy the document to translate
        StreamSource iteratedDocument = anODFDocument;
        int stepCounter = 0;
        // while the Iterator has steps apply the transformation
        while (mapIterator.hasNext()) {

            // get the next step
            OAXSLTStep nextStep = (OAXSLTStep) mapIterator.next();

            // get the href from the step
            //!+REMOVE_DIRECT_PATH_PREFIXING(ah, 13-09-2012)
            String stepHref = /** GlobalConfigurations.getApplicationPathPrefix() + **/ nextStep.getHref();

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
            //the step href supports both relative and absolute paths 
            // and file URIs 
            try {
                File fstepFile = null;
                fstepFile = FileUtility.getInstance().resolveHref(stepHref);
                xsltStream = FileUtility.getInstance().FileAsStreamSource(fstepFile);
            } catch (FileNotFoundException e) {
                log.error(
                   "input step xslt file : " + stepHref + " not found.",
                   e
                );
            }

            if (xsltStream != null) {
                //log.debug("executing input step = Step " + nextStep.getPosition() +"," + nextStep.getName() + ", " + nextStep.getHref());

                //check if first step, check if input parameters
                if (0 == stepCounter) {

                    if (this.pipelineInputParams.size() > 0 ) {
                        try {
                            //get input parameters and call transformWithParam
                            //usually we call transform() but since we want to call with a parameter,
                            //we use transformWithParam()
                            iteratedDocument = XSLTTransformer.getInstance().transformWithParam(iteratedDocument, xsltStream, this.pipelineInputParams);
                            CloseHandle.closeQuietly(xsltStream);
                        } catch (Exception ex) {
                            log.error("Error while transformWithParam", ex);
                        } finally {
                            this.pipelineInputParams.clear();
                        }
                    } else {
                        iteratedDocument = XSLTTransformer.getInstance().transform(iteratedDocument, xsltStream);
                        CloseHandle.closeQuietly(xsltStream);
                    }
                } else {
                    // start the transformation
                    iteratedDocument = XSLTTransformer.getInstance().transform(iteratedDocument, xsltStream);
                    CloseHandle.closeQuietly(xsltStream);
                }
                
                
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
            stepCounter++;
        }

        // return the StreamSource of the transformed document
        return iteratedDocument;
    }

    /**
     * Alternate resolve() method, allows calling resolve() with input XSLT
     * parameters
     * @param anODFDocument
     * @param paramsMap
     * @param stepsMap
     * @return
     * @throws XPathExpressionException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public StreamSource resolve(
            StreamSource anODFDocument,
            HashMap<String, Parameter> paramsMap,
            TreeMap<Integer,OAXSLTStep> stepsMap
            )
            throws XPathExpressionException, TransformerException, UnsupportedEncodingException {
        this.pipelineInputParams = paramsMap;
        return this.resolve(anODFDocument, stepsMap);
    }

    /**
     * Apply a referenced Processor 
     * @param iteratedDocument - this will be closed in writeToDOM()
     * @param preProc
     * @return
     * @throws XPathExpressionException
     * @throws TransformerException
     * @throws SAXException
     * @throws IOException 
     */
    private StreamSource applyProcessorByRef(StreamSource iteratedDocument, String preProc)
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
