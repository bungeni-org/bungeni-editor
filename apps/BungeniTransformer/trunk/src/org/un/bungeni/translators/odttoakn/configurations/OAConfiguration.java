package org.un.bungeni.translators.odttoakn.configurations;

//~--- non-JDK imports --------------------------------------------------------

import org.un.bungeni.translators.interfaces.Configuration;
import org.un.bungeni.translators.odttoakn.steps.OAReplaceStep;
import org.un.bungeni.translators.odttoakn.steps.OAXSLTStep;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

/**
 * This is the configuration object.
 * It is used to read a configuration, write a configuration and to create a new configuration
 */
public class OAConfiguration implements Configuration {

    // the configuration reader
    private OAConfigurationReader reader;

    /**
     * Create the new configuration based on a given Configuration XML file
     * @param aConfigXML the XML Document in witch the configuration is written
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public OAConfiguration(Document aConfigXML)
            throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {

        // create the reader
        this.reader = new OAConfigurationReader(aConfigXML);
    }

    /**
     * Used to get an HashMap containing all the INPUT XSLT Steps of the configuration with their position
     * as key. The input step are applied to the document before the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public HashMap<Integer, OAXSLTStep> getInputSteps() throws XPathExpressionException {

        // ask the reader to get the steps of the configuration
        HashMap<Integer, OAXSLTStep> resultSteps = this.reader.getInputSteps();

        // return the gotten steps
        return resultSteps;
    }

    /**
     * Used to get an HashMap containing all the OUTPUT XSLT Steps of the configuration with their position
     * as key. The output step are applied to the document after the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public HashMap<Integer, OAXSLTStep> getOutputSteps() throws XPathExpressionException {

        // ask the reader to get the steps of the configuration
        HashMap<Integer, OAXSLTStep> resultSteps = this.reader.getOutputSteps();

        // return the gotten steps
        return resultSteps;
    }

    /**
     * Used to get an HashMap containing all the Replace Steps of the configuration with their position
     * as key
     * @return the HashMap containing all the Replace Steps of the configuration
     * @throws XPathExpressionException
     */
    public HashMap<Integer, OAReplaceStep> getReplaceSteps() throws XPathExpressionException {

        // ask the reader to get the replace steps of the configuration
        HashMap<Integer, OAReplaceStep> resultSteps = this.reader.getReplaceSteps();

        // return the gotten steps
        return resultSteps;
    }
}
