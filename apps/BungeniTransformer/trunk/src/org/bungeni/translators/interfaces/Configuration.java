package org.bungeni.translators.interfaces;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.translators.configurations.steps.OAReplaceStep;
import org.bungeni.translators.configurations.steps.OAXSLTStep;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.xpath.XPathExpressionException;

/**
 * This is the interface for the configuration object of the ODTtoAKN translator. A configuration
 * stores all the steps needed to perform a specific translation.
 */
public interface Configuration {

    /**
     * Used to get an HashMap containing all the INPUT XSLT Steps of the configuration with their position
     * as key. The input step are applied to the document before the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getInputSteps() throws XPathExpressionException;

    /**
     * Used to get an HashMap containing all the OUTPUT XSLT Steps of the configuration with their position
     * as key. The output step are applied to the document after the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getOutputSteps() throws XPathExpressionException;

    /**
     * Used to get an HashMap containing all the Replace Steps of the configuration with their position
     * as key
     * @return the HashMap containing all the Replace Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAReplaceStep> getReplaceSteps() throws XPathExpressionException;
}
