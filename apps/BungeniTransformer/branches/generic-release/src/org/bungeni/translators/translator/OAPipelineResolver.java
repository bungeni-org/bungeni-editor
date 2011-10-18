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
        //This is the <xsl:stylesheet > element 
        Element stylesheetElement = pipeline.getDocumentElement();
        //get all the <bp:template> elements in the pipeline 
        NodeList bpTemplateElements = (NodeList) XPathResolver.getInstance().evaluate(pipeline, "//bp:template",
                XPathConstants.NODESET);
        log.debug("bp:template nodes found : " + bpTemplateElements.getLength());
        // for each bp:template element get the URI, retrieve the pointed
        // XSLT and replace the content of the template into the pipeline
        for (int i = 0; i < bpTemplateElements.getLength(); i++) {
            // get the <bp:template> node
            Node bpTemplateNode = bpTemplateElements.item(i);
            // get the href attribute
            String xsltURI = bpTemplateNode.getAttributes().getNamedItem("href").getNodeValue();
            // get the name of the element
            String elementName = bpTemplateNode.getAttributes().getNamedItem("name").getNodeValue();
            log.debug(" node href= " + xsltURI +  " name="+ elementName);
            // get the XSLT file
            File XSLTFile = new File(GlobalConfigurations.getApplicationPathPrefix() + xsltURI);
            if (XSLTFile.exists()) {
                // open the pointed XSLT as a DOM document
                Document XSLTDoc = OADocumentBuilderFactory.getInstance().
                        getDBF().newDocumentBuilder().parse(
                        FileUtility.getInstance().FileAsInputSource(XSLTFile));
                // get the content of the template of the XSLT
                //!+PIPELINE_FORMAT_CHANGE(ah,oct-2011) Find the XSLT template matchign the bp:name attribute
                //multiple template matches are supported -- see comment below
                NodeList templateContentNodes  = (NodeList)XPathResolver.getInstance().
                     evaluate(
                      XSLTDoc,
                      "//*:template[@bp:name='" + elementName + "']",
                      XPathConstants.NODESET
                     );
                //+PIPELINE_FORMAT_CHANGE(ah,oct-2011)
                //Multiple template nodes for a pipe in the pipeline are supported,
                //they just need to have the same bp:name attribute value and must be in the 
                //same pipe file
                // e.g.
                //  <xs:template match="zelement" bp:name="zelement-pipe" >
                //     <!-- do something -->
                //     <xs:call-template name="local-template-only-relevant-here" />
                //  </xsl:template>
                //
                //  <xsl:template name="local-template-only-relevant-here" bp:name="zelement-pipe">
                //  </xsl:template>
                //
                //
                Node runningReplacementNode = null;
                for (int nNode=0 ; nNode < templateContentNodes.getLength() ; nNode++ ) {
                    //this loops through the individual matched xsl:templates which have the
                    //bp:name attribute and injects them into pipeline.xsl
                    Node replacementNode = pipeline.adoptNode(templateContentNodes.item(nNode).cloneNode(true));
                    if (nNode == 0 ) {
                        stylesheetElement.replaceChild(
                                replacementNode,
                                bpTemplateNode);
                        runningReplacementNode = replacementNode;
                    } else {
                        Node nextNode = runningReplacementNode.getNextSibling();
                        if (nextNode == null) {
                            replacementNode.appendChild(pipeline.createTextNode("\n\n"));
                            stylesheetElement.appendChild(replacementNode);

                        } else {
                            replacementNode.appendChild(pipeline.createTextNode("\n\n"));
                            stylesheetElement.insertBefore(replacementNode, nextNode);
                        }
                    }
                } 
               //stylesheetElement.replaceChild(pipeline.adoptNode(templateContentNode.cloneNode(true)), bpTemplateNode);
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
        //!+PIPELINE_FORMAT_CHANGE(ah,oct-2011) Use bp:stylesheets instead of ns-less stylesheets
        Node oldRoot = (Node) XPathResolver.getInstance().evaluate(
                pipeline,
                "//*:template[@match='/']/bp:stylesheets",
                XPathConstants.NODE
                );
        if (oldRoot == null) {
            log.error("The root template in the pipeline must have a bp:stylesheets container");
            throw new
               XPathExpressionException("The root template in the pipeline must have a bp:stylesheets container");
        }
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
