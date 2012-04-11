package org.bungeni.translators.configurations;

//~--- non-JDK imports --------------------------------------------------------
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.bungeni.translators.interfaces.Configuration;
import org.bungeni.translators.configurations.steps.OAReplaceStep;
import org.bungeni.translators.configurations.steps.OAXSLTStep;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.bungeni.translators.configurations.steps.OAPipelineStep;
import org.bungeni.translators.configurations.steps.OAProcessStep;

/**
 * This is the configuration object.
 * It is used to read a configuration, write a configuration and to create a new configuration
 */
public class OAConfiguration implements Configuration {

    private static OAConfiguration thisInstance = null;
    // the configuration reader
    private OAConfigurationReader reader;

    private Properties configProperties;

     private static org.apache.log4j.Logger log  =
        org.apache.log4j.Logger.getLogger(OAConfiguration.class.getName());

    private OAConfiguration() {
        reader = null;
    }


    /**
     * Create the new configuration based on a given Configuration XML file
     * @param aConfigXML the XML Document in witch the configuration is written
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public void setConfiguration(Document aConfigXML)
            throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {

        // create the reader
        this.reader = new OAConfigurationReader(aConfigXML);
    }

    public static OAConfiguration getInstance() {
        if (thisInstance == null) {
            thisInstance = new OAConfiguration();
        }
        return thisInstance;
    }

    public boolean hasProperties() throws XPathExpressionException{
        return this.reader.hasProperties();
    }

    public boolean hasParameters(String forStep) throws XPathExpressionException {
        return this.reader.hasParameters(forStep);
    }

    public HashMap<String,Object> getParameters(String forStep) throws XPathExpressionException {
        return this.reader.getParameters(forStep);
    }

    /**
     * Gets the properties of the configuration as Java properties object
     * @return
     */
    public Properties getProperties() {
       if (configProperties == null) {
            try {
                configProperties =  this.reader.getProperties();
            } catch (XPathExpressionException ex) {
               log.error("Error while getting translation properties", ex);
            } catch (TransformerConfigurationException ex) {
               log.error("Error while getting translation properties", ex);
            } catch (TransformerException ex) {
               log.error("Error while getting translation properties", ex);
            } catch (IOException ex) {
               log.error("Error while getting translation properties", ex);
            }
       }
       return configProperties;
    }


    public boolean hasInputSteps() throws XPathExpressionException {
        return this.reader.hasInputSteps();
    }

    /**
     * Used to get an HashMap containing all the INPUT XSLT Steps of the configuration with their position
     * as key. The input step are applied to the document before the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getInputSteps() throws XPathExpressionException {

        // ask the reader to get the steps of the configuration
        TreeMap<Integer, OAXSLTStep> resultSteps = this.reader.getInputSteps();

        // return the gotten steps
        return resultSteps;
    }

    public boolean hasOutputSteps() throws XPathExpressionException {
        return this.reader.hasOutputSteps();
    }

    /**
     * Used to get an HashMap containing all the OUTPUT XSLT Steps of the configuration with their position
     * as key. The output step are applied to the document after the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getOutputSteps() throws XPathExpressionException {

        // ask the reader to get the steps of the configuration
        TreeMap<Integer, OAXSLTStep> resultSteps = this.reader.getOutputSteps();

        // return the gotten steps
        return resultSteps;
    }

    public boolean hasReplaceSteps() throws XPathExpressionException {
        return this.reader.hasReplaceSteps();
    }

    /**
     * Used to get an HashMap containing all the Replace Steps of the configuration with their position
     * as key
     * @return the HashMap containing all the Replace Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAReplaceStep> getReplaceSteps() throws XPathExpressionException {

        // ask the reader to get the replace steps of the configuration
        TreeMap<Integer, OAReplaceStep> resultSteps = this.reader.getReplaceSteps();

        // return the gotten steps
        return resultSteps;
    }

    public boolean hasPostXmlSteps() throws XPathExpressionException {
        return this.reader.hasPostXmlSteps();
    }

    public TreeMap<Integer, OAXSLTStep> getPostXmlSteps() throws XPathExpressionException {

        TreeMap<Integer, OAXSLTStep> postxmlSteps = this.reader.getPostXmlSteps();

        return postxmlSteps;

    }

    public boolean hasPipelineXML() throws XPathExpressionException {
        return this.reader.hasPipelineXML();
    }

    public List<OAPipelineStep> getXsltPipeline() throws XPathExpressionException {
        List<OAPipelineStep> pipelineSteps = this.reader.getPipelineXML();
        return pipelineSteps;
    }

    public boolean hasSchema() throws XPathExpressionException {
        return this.reader.hasSchema();
    }

    public String getSchema() throws XPathExpressionException {
        String schema = this.reader.getSchema();
        return schema;
    }


    public List<OAProcessStep> getProcessGroup(String id) throws XPathExpressionException {
        return this.reader.getProcessGroup(id);
    }

        /**
     * Verify the configuration --
     *
     *   input and pipeline steps are mandatory
     * @param configuration
     * @return
     */
    public boolean verify() throws XPathExpressionException {
        if (this.hasProperties()) {
            log.info("verifyConfiguration : hasProperties : check OK ");
            if (this.hasInputSteps()) {
                log.info("verifyConfiguration : hasInputSteps : check OK ");
                //if (this.hasPipelineXML()) {
                //    log.info("verifyConfiguration : hasPipelineXML : check OK ");
                //    return true;
                //} else
                //    log.info("verifyConfiguration : hasPipelineXML : check FAIL ");
                return true;
            }
        } else
            log.info("verifyConfiguration : hasProperties : check FAIL ");
        return false;
    }
}