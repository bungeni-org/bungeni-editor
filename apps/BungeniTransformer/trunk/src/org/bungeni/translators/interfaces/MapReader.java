package org.bungeni.translators.interfaces;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.translators.configurations.steps.OAMapStep;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

/**
 *  This is the interface for the map reader Object.
 *  A map reader object is used to read a map xml.
 */
public interface MapReader {

    /**
     * Return an HashMap containing all the step of the map indexed by their id
     * @return the HashMap containing all the step of the map indexed by their id
     * @throws XPathExpressionException
     */
    public HashMap<Integer, OAMapStep> getMapSteps() throws XPathExpressionException;

    /**
     * Returns a String containing the path of the map resolver
     * @return a String containing the path of the map resolver
     * @throws XPathExpressionException
     */
    public String getMapResolver() throws XPathExpressionException;
}
