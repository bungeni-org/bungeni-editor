package org.bungeni.translators.translator;

//~--- non-JDK imports --------------------------------------------------------
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.xpathresolver.XPathResolver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;

public final class OAPipelineResolver implements org.bungeni.translators.interfaces.PipelineResolver {

    private static org.apache.log4j.Logger log = Logger.getLogger(OAPipelineResolver.class.getName());

    /* The instance of this resolver */
    private static OAPipelineResolver instance = null;

    /**
     * Private constructor used to create the PipelineResolver instance
     */
    private OAPipelineResolver() {
    }

    /**
     * Get the current instance of the PipelineResolver
     * @return the translator instance
     */
    public static OAPipelineResolver getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            instance = new OAPipelineResolver();
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * Returns a stream source obtained after all the XSLT steps of the AHConfiguration are applied to the document
     * to translate
     * @param aPipelinePath the pipeline that must be resolved
     * @return a new StreamSource Obtained applying all the steps of the configuration to the
     *                      given StreamSource
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public Document resolve(String aPipelinePath)
            throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
        log.debug("resolving pipeline path = " + aPipelinePath);
        // open the XSLT file into a DOM document
        Document pipeline = OADocumentBuilderFactory.getInstance().getDBF().newDocumentBuilder().parse(
                FileUtility.getInstance().FileAsInputSource(aPipelinePath));    // new File(aPipelinePath));

        // get all the <xslt> elements in the pipeline
        NodeList xsltElements = (NodeList) XPathResolver.getInstance().evaluate(pipeline, "//xslt",
                XPathConstants.NODESET);

        System.out.println("XXXX node list size : " + xsltElements.getLength());
        // for each XSLT element get the URI, retrieve the pointed XSLT and replace the content of the template into the pipeline
        for (int i = 0; i < xsltElements.getLength(); i++) {
            // get the <xslt> node
            Node xsltNode = xsltElements.item(i);

            // get the URI attribute of the XSLT node
            String xsltURI = xsltNode.getAttributes().getNamedItem("href").getNodeValue();

            System.out.println("XXXX node  : " + xsltURI + " i = " + i);

            // get the name of the element
            String elementName = (String) XPathResolver.getInstance().evaluate(pipeline,
                    "//xslt[@href='" + xsltURI + "']/@name", XPathConstants.STRING);

            // get the XSLT file
            File XSLTFile = new File(GlobalConfigurations.getApplicationPathPrefix() + xsltURI);
            if (XSLTFile.exists()) {
                log.debug("\t processing element :: " + elementName + " , " + xsltURI);
                // open the pointed XSLT as a DOM document
                Document XSLTDoc = OADocumentBuilderFactory.getInstance().getDBF().newDocumentBuilder().parse(
                        FileUtility.getInstance().FileAsInputSource(XSLTFile));

                // get the content of the template of the XSLT
                Node templateContent = (Node) XPathResolver.getInstance().evaluate(XSLTDoc,
                        "//*:template[@match=\"*[@name='" + elementName + "']\"]/*",
                        XPathConstants.NODE);
                // get the parent template of the xslt node
                Node parentTemplate = (Node) XPathResolver.getInstance().evaluate(pipeline,
                        "//xslt[@href='" + xsltURI + "']/ancestor::*:template", XPathConstants.NODE);

                // remove the apply templates node
                if ((Node) XPathResolver.getInstance().evaluate(pipeline,
                        "//*:template[@match=\"*[@name='" + elementName + "']\"]/*:apply-templates",
                        XPathConstants.NODE) != null) {

                    // remove the apply templates node
                    parentTemplate.removeChild((Node) XPathResolver.getInstance().evaluate(pipeline,
                            "//*:template[@match=\"*[@name='" + elementName + "']\"]/*:apply-templates",
                            XPathConstants.NODE));
                }

                // appends the node to the pipeline
                xsltNode.getParentNode().replaceChild(pipeline.adoptNode(templateContent.cloneNode(true)), xsltNode);

                // destroy XSLT File
                XSLTFile = null;

                // destroy XSLT document
                XSLTDoc = null;
            } else {
                log.error("Missing file in pipeline : " + XSLTFile.getCanonicalPath());
                throw new IOException("Missing file in pipeline : " + XSLTFile.getCanonicalPath());
            }

        }

        // get the root element of the pipeline
        Node oldRoot = (Node) XPathResolver.getInstance().evaluate(pipeline, "//*:template[@match='/']/stylesheets",
                XPathConstants.NODE);

        // get the old root parent node
        Node oldRootParent = oldRoot.getParentNode();

        // delete the root
        oldRootParent.removeChild(oldRoot);

        // create a new apply templates element for the root.
        oldRootParent.appendChild(pipeline.createElement("xsl:apply-templates"));

        // get the output node of the produced XSLT
        Element outputNode = (Element) pipeline.getElementsByTagName("xsl:output").item(0);

        // change the method attribute of the output node
        outputNode.setAttribute("method", "xml");

        // returns the modified pipeline
        return pipeline;
    }
}
