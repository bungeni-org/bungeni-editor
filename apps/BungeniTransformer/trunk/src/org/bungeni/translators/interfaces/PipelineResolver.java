package org.bungeni.translators.interfaces;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

/**
 * This is the interface for the pipeline resolvers.
 * Pipelines can be resolved in different ways by each translator. So the method "resolve"
 * must be implemented in different ways by each translator
 */
public interface PipelineResolver {

    /**
     * This method is used to create an XSLT starting from a pipeline.
     * @param aPipelinePath the path of the pipeline that must be resolved
     * @return a DOM document containing the XSLT obtained by resolving the pipeline
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public Document resolve(String aPipelinePath)
            throws SAXException, IOException, ParserConfigurationException, XPathExpressionException;
}
