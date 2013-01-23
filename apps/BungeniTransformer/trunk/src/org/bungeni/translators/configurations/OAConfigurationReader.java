package org.bungeni.translators.configurations;

//~--- non-JDK imports --------------------------------------------------------
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.bungeni.translators.configurations.steps.OAPipelineStep;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.bungeni.translators.configurations.steps.OAReplaceStep;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.bungeni.translators.interfaces.ConfigurationReader;
import org.bungeni.translators.utility.dom.DOMUtility;
import org.bungeni.translators.utility.runtime.CloseHandle;
import org.bungeni.translators.utility.transformer.GenericTransformer;
import org.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class reades the TranslatorConfig_xxxx.xml files for each content type
 *
 * The way it works is :
 *
 * (incoming doc) => input steps applied => (stream out) => replace steps =>
 *       (stream out) => output steps applied 
 *
 */
public class OAConfigurationReader implements ConfigurationReader {


    private static org.apache.log4j.Logger log  =
        org.apache.log4j.Logger.getLogger(OAConfigurationReader.class.getName());

    // the XML that contains the configurations
    private Document configXML;

    /**
     * Create a new Configuration reader object builded on the given Config XML file
     * @param aConfigXML the XML file that contains the configuration
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public OAConfigurationReader(Document aConfigXML)
            throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {

        // save the config XML
        this.configXML = aConfigXML;
    }

    public boolean hasProperties() throws XPathExpressionException{
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList propNodes = (NodeList) xresolver.evaluate(this.configXML, "//properties", XPathConstants.NODESET);
        return propNodes.getLength() > 0 ? true : false;
    }

    /**
     * Extracts the global configuration properties as properties object
     * @return
     * @throws XPathExpressionException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public Properties getProperties() throws XPathExpressionException,
            TransformerConfigurationException, TransformerException, IOException {
        Properties props = new Properties();
        ByteArrayInputStream propBytes = null;
        try {
            XPathResolver xresolver = XPathResolver.getInstance();
            // get the properties node in teh configuration
            Node propertiesNode = (Node) xresolver.evaluate(this.configXML,
                    "//properties", XPathConstants.NODE);
            Source xmlSource = new DOMSource(propertiesNode);
            // convert the properties node to a string
            StringWriter sw = new StringWriter();
            //we use a new transformer for the configuration instead of the cached one to ensure
            //sepeation of configuration from data
            Transformer tconfig = GenericTransformer.getInstance().getTransformerFactory().newTransformer();
            tconfig.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            tconfig.transform(xmlSource, new StreamResult(sw));
            //we need to append the properties DOCTYPE dtd for this to work
            byte[] bytes =
                    ("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">" +
                    sw.toString()).getBytes("UTF8");
            propBytes = new ByteArrayInputStream(bytes);
            props.loadFromXML(propBytes);
        } catch(Exception ex) {
            log.error("Error loading properties", ex);
        } finally {
            CloseHandle.closeQuietly(propBytes);
        }
        return props;
    }


    public boolean hasParameters(String forStep) throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the parameters in this configuration
        NodeList inputNodes = (NodeList) xresolver.evaluate(this.configXML,
                "//" + forStep + "/parameters/parameter", XPathConstants.NODESET);

        return inputNodes.getLength() > 0 ? true : false;
    }

    /**
     * This API retrieves the input parameter NAMES for an input or output steps
     * @param forStep
     * @return
     */
    public HashMap<String,Object> getParameters(String forStep) throws XPathExpressionException {
        HashMap<String,Object> map = new HashMap<String,Object>();
        XPathResolver xresolver = XPathResolver.getInstance();
        NodeList paramNodes = (NodeList) xresolver.evaluate(
                this.configXML,
                "//"+forStep+"/parameters/parameter" , XPathConstants.NODESET
        );
        for (int i = 0; i < paramNodes.getLength(); i++) {
            Element paramNode = (Element) paramNodes.item(i);
            String paramName = paramNode.getAttribute("name");
            /**
             * !+XSLT_PARAM_XML (ah, 12-04-2012) - XML value parameter is supported.
             *
             * The config file now supports an XML document in the value parameter.
             */
            if (paramNode.hasAttribute("value")) {
                map.put(paramName, paramNode.getAttribute("value"));
            } else if (paramNode.hasChildNodes()) {
                NodeList valueNodes = paramNode.getElementsByTagName("value");
                if (valueNodes != null ) {
                    if (valueNodes.getLength() > 0 ) {
                        //this is the <value> node, we get its child node
                        Node valueNode = valueNodes.item(0);
                        net.sf.saxon.dom.DocumentWrapper dw = null;
                        try {
                          dw =  DOMUtility.getInstance().getSaxonDocumentWrapperForNode(valueNode);
                        } catch (TransformerConfigurationException ex) {
                            log.error("Error while getting Saxon Document Wrapper !", ex);
                        }
                        if (null == dw) {
                           map.put(paramName, "");
                        } else {
                           map.put(paramName, dw);
                        }
                    }
                }
                if (!map.containsKey(paramName)) {
                    map.put(paramName, "");
                }
            } else {
                map.put(paramName, "");
            }
        }
        return map;
    }

    /**
     * Checks if the input steps exist in the translator configuration
     * @return
     * @throws XPathExpressionException
     */
    public boolean hasInputSteps() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList inputNodes = (NodeList) xresolver.evaluate(this.configXML, "//input", XPathConstants.NODESET);

        return inputNodes.getLength() > 0 ? true : false;

    }

    public boolean outputWrite () throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        Node outputNode = (Node) xresolver.evaluate(
                this.configXML,
                "//output",
                XPathConstants.NODE
                );
        return getPipeOutputState(outputNode);
    }

   public boolean inputWrite () throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        Node outputNode = (Node) xresolver.evaluate(
                this.configXML,
                "//input",
                XPathConstants.NODE
                );
        return getPipeOutputState(outputNode);
    }

      public boolean replacementsWrite () throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        Node outputNode = (Node) xresolver.evaluate(
                this.configXML,
                "//replacements",
                XPathConstants.NODE
                );
        return getPipeOutputState(outputNode);
    }

    private boolean getPipeOutputState(Node pipeNode){
         if (null != pipeNode) {
            NamedNodeMap attrs = pipeNode.getAttributes();
            if (attrs != null) {
               Node outputAttr = attrs.getNamedItem("output");
               //if no @output attribute set return false
               if (outputAttr == null ) {
                   return false;
               } else {
                   String soutput = outputAttr.getNodeValue();
                   boolean writeOutput = Boolean.parseBoolean(soutput);
                   return writeOutput ;
                }
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * Used to get an HashMap containing all the Steps of the configuration with their position
     * as key
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getInputSteps() throws XPathExpressionException {
        return this.getXSLTSteps("//input/xslt");
    }

    /**
     * Checks if output steps exist in the translator configuration
     * @return
     * @throws XPathExpressionException
     */
    public boolean hasOutputSteps() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList outputNodes = (NodeList) xresolver.evaluate(this.configXML, "//output", XPathConstants.NODESET);
        return outputNodes.getLength() > 0 ? true : false;

    }

    /**
     * Used to get an HashMap containing all the OUTPUT XSLT Steps of the configuration with their position
     * as key. The output step are applied to the document after the resolution of its names according to the map
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getOutputSteps() throws XPathExpressionException {
        return this.getXSLTSteps("//output/xslt");
    }

    /**
     * Checks if replace steps exist in the translator configuration
     * @return
     * @throws XPathExpressionException
     */
    public boolean hasReplaceSteps() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList replaceNodes = (NodeList) xresolver.evaluate(this.configXML, "//replacement", XPathConstants.NODESET);
        return replaceNodes.getLength() > 0 ? true : false;

    }

    /**
     * Used to get an HashMap containing all the ReplaceStep of the configuration
     * @return the HashMap containing all the ReplaceSteps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAReplaceStep> getReplaceSteps() throws XPathExpressionException {

        // the HashMap to return
        TreeMap<Integer, OAReplaceStep> resultMap = new TreeMap<Integer, OAReplaceStep>();

        // retrieve the XPath resolver instance
        XPathResolver xresolver = XPathResolver.getInstance();

        // get the step with the given name in this configuration
        NodeList stepNodes = (NodeList) xresolver.evaluate(this.configXML, "//replacement", XPathConstants.NODESET);

        // get all the steps and creates a Step object for each one of them
        for (int i = 0; i < stepNodes.getLength(); i++) {

            // get the replace step node
            Node stepNode = stepNodes.item(i);

            // the result Step
            OAReplaceStep resultStep;

            // create the replace Step
            // if pattern attribute is not empty get the pattern from the attribute
            if (stepNode.getAttributes().getNamedItem("pattern") != null) {
                resultStep = new OAReplaceStep(stepNode.getAttributes().getNamedItem("name").getNodeValue(),
                        stepNode.getAttributes().getNamedItem("replacement").getNodeValue(),
                        stepNode.getAttributes().getNamedItem("pattern").getNodeValue());
            } // otherwise get the value from the textValue of the node
            else {
                resultStep = new OAReplaceStep(stepNode.getAttributes().getNamedItem("name").getNodeValue(),
                        stepNode.getAttributes().getNamedItem("replacement").getNodeValue(),
                        stepNode.getFirstChild().getNodeValue());
            }

            // add the node to the hash map set its key as its position (step attribute)
            resultMap.put(Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()), resultStep);
        }

        // return the hash map containing all the Steps
        return resultMap;
    }

    /**
     * Checks if postxml steps exist in the translator configuration
     * @return
     * @throws XPathExpressionException
     */
    public boolean hasPostXmlSteps() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList postxmlNodes = (NodeList) xresolver.evaluate(this.configXML, "//postxml", XPathConstants.NODESET);
        return postxmlNodes.getLength() > 0 ? true : false;

    }

    /**
     * Used to get an HashMap containing all the Steps of the configuration with their position
     * as key
     * @return the HashMap containing all the Steps of the configuration
     * @throws XPathExpressionException
     */
    public TreeMap<Integer, OAXSLTStep> getPostXmlSteps() throws XPathExpressionException {
        return getXSLTSteps("//postxml/xslt");

    }

    /**
     * Checks if pipeline is defined in the translator configuration
     * @return
     * @throws XPathExpressionException
     */
    public boolean hasPipelineXML() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList pipexmlNodes = (NodeList) xresolver.evaluate(this.configXML, "//pipetoxml", XPathConstants.NODESET);
        return pipexmlNodes.getLength() > 0 ? true : false;

    }

    public List<OAPipelineStep> getPipelineXML() throws XPathExpressionException {

        List<OAPipelineStep> pipelineSteps = new ArrayList<OAPipelineStep>(0);

        XPathResolver xresolver = XPathResolver.getInstance();

        // get the step with the given nama in this configuration
        NodeList stepNodes = (NodeList) xresolver.evaluate(this.configXML, "//pipetoxml", XPathConstants.NODESET);
        for (int i = 0; i < stepNodes.getLength(); i++) {
            Node stepNode = stepNodes.item(i);
            OAPipelineStep pipelineStep = new OAPipelineStep(stepNode.getAttributes().getNamedItem("name").getNodeValue(),
                    stepNode.getAttributes().getNamedItem("href").getNodeValue());
            pipelineSteps.add(pipelineStep);
        }
        return pipelineSteps;
    }

    /**
     * Checks if a schema is defined in the translator configuration
     * @return
     * @throws XPathExpressionException
     */
    public boolean hasSchema() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        NodeList schemaNodes = (NodeList) xresolver.evaluate(this.configXML, "//schema", XPathConstants.NODESET);
        return schemaNodes.getLength() > 0 ? true : false;

    }

    public String getSchema() throws XPathExpressionException {
        XPathResolver xresolver = XPathResolver.getInstance();
        // get the step with the given nama in this configuration
        String schemaHref = (String) xresolver.evaluate(this.configXML, "//schema/@href", XPathConstants.STRING);

        return schemaHref;
    }


    public List<OAProcessStep> getProcessGroup(String processGroupId) throws XPathExpressionException {

        List<OAProcessStep> processList = new ArrayList<OAProcessStep>(0);

        XPathResolver xresolver = XPathResolver.getInstance();
        NodeList nl = (NodeList) xresolver.evaluate(this.configXML, "//processgroup[@id='" + processGroupId + "']/process", XPathConstants.NODESET);
        for (int i=0 ; i < nl.getLength() ; i++ ) {
            System.out.println(nl.item(i).getNodeName());
            processList.add(new OAProcessStep(nl.item(i).getAttributes()));
        }
        
        return processList;
    }

    private TreeMap<Integer, OAXSLTStep> getXSLTSteps(String forXpath) throws XPathExpressionException {
        // the TreeMap to return
        TreeMap<Integer, OAXSLTStep> resultMap = new TreeMap<Integer, OAXSLTStep>();

        // retreive the XPath resolver instance
        XPathResolver xresolver = XPathResolver.getInstance();

        // get the step with the given nama in this configuration
        NodeList stepNodes = (NodeList) xresolver.evaluate(this.configXML, forXpath, XPathConstants.NODESET);

        // get all the steps and creates a Step object for each one of them
        for (int i = 0; i < stepNodes.getLength(); i++) {

            // get the step node
            Node stepNode = stepNodes.item(i);
         
            // create the Step
            OAXSLTStep resultStep = new OAXSLTStep(stepNode.getAttributes());

            // add the node to the hash map set its key as its position (step attribute)
            resultMap.put(Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()), resultStep);
        }

        // return the hash map containing all the Steps
        return resultMap;

    }
}
