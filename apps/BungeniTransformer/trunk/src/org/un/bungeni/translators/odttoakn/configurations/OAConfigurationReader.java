package org.un.bungeni.translators.odttoakn.configurations;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.un.bungeni.translators.interfaces.ConfigurationReader;
import org.un.bungeni.translators.odttoakn.steps.OAXSLTStep;
import org.un.bungeni.translators.odttoakn.steps.OAReplaceStep;
import org.un.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is the reader of the configuration.
 * It supplies several methods to retrieve the Steps of a configuration 
 */
public class OAConfigurationReader implements ConfigurationReader 
{
	//the XML that contains the configurations
	private Document configXML; 
	
	/**
	 * Create a new Configuration reader object builded on the given Config XML file 
	 * @param aConfigXML the XML file that contains the configuration 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	public OAConfigurationReader(Document aConfigXML) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException
	{
		//save the config XML
		this.configXML = aConfigXML;
	}
	
	
	/**
	 * Used to get an HashMap containing all the Steps of the configuration with their position 
	 * as key 
	 * @return the HashMap containing all the Steps of the configuration
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAXSLTStep> getInputSteps() throws XPathExpressionException
	{
		//the HashMap to return 
		HashMap<Integer,OAXSLTStep> resultMap = new HashMap<Integer,OAXSLTStep>();
		
		//retreive the XPath resolver instance 
		XPathResolver xresolver = XPathResolver.getInstance();
		
		//get the step with the given nama in this configuration
		NodeList stepNodes = (NodeList)xresolver.evaluate(this.configXML, "//input/xslt", XPathConstants.NODESET);
		
		//get all the steps and creates a Step object for each one of them
		for (int i = 0; i < stepNodes.getLength(); i++) 
		{
			//get the step node
			Node stepNode = stepNodes.item(i);
			
			//create the Step 
			OAXSLTStep resultStep = new OAXSLTStep( stepNode.getAttributes().getNamedItem("name").getNodeValue(),
						  				stepNode.getAttributes().getNamedItem("href").getNodeValue(),
						  				Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()));
			
			//add the node to the hash map set its key as its position (step attribute)
			resultMap.put(Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()),resultStep);		
		}
		
		//return the hash map containing all the Steps
		return resultMap;
	}
	
	/**
	 * Used to get an HashMap containing all the OUTPUT XSLT Steps of the configuration with their position 
	 * as key. The output step are applied to the document after the resolution of its names according to the map 
	 * @return the HashMap containing all the Steps of the configuration
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAXSLTStep> getOutputSteps() throws XPathExpressionException
	{
		//the HashMap to return 
		HashMap<Integer,OAXSLTStep> resultMap = new HashMap<Integer,OAXSLTStep>();
		
		//retreive the XPath resolver instance 
		XPathResolver xresolver = XPathResolver.getInstance();
		
		//get the step with the given nama in this configuration
		NodeList stepNodes = (NodeList)xresolver.evaluate(this.configXML, "//output/xslt", XPathConstants.NODESET);
		
		//get all the steps and creates a Step object for each one of them
		for (int i = 0; i < stepNodes.getLength(); i++) 
		{
			//get the step node
			Node stepNode = stepNodes.item(i);
			
			//create the Step 
			OAXSLTStep resultStep = new OAXSLTStep( stepNode.getAttributes().getNamedItem("name").getNodeValue(),
						  				stepNode.getAttributes().getNamedItem("href").getNodeValue(),
						  				Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()));
			
			//add the node to the hash map set its key as its position (step attribute)
			resultMap.put(Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()),resultStep);		
		}
		
		//return the hash map containing all the Steps
		return resultMap;
		
	}

	/**
	 * Used to get an HashMap containing all the ReplaceStep of the configuration  
	 * @return the HashMap containing all the ReplaceSteps of the configuration
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAReplaceStep> getReplaceSteps() throws XPathExpressionException
	{
		//the HashMap to return 
		HashMap<Integer,OAReplaceStep> resultMap = new HashMap<Integer,OAReplaceStep>();
		
		//retrieve the XPath resolver instance 
		XPathResolver xresolver = XPathResolver.getInstance();
		
		//get the step with the given name in this configuration
		NodeList stepNodes = (NodeList)xresolver.evaluate(this.configXML, "//replacement", XPathConstants.NODESET);
		
		//get all the steps and creates a Step object for each one of them
		for (int i = 0; i < stepNodes.getLength(); i++) 
		{
			//get the replace step node
			Node stepNode = stepNodes.item(i);
			
			//the result Step
			OAReplaceStep resultStep;
			
			//create the replace Step 
			//if pattern attribute is not empty get the pattern from the attribute 
			if (stepNode.getAttributes().getNamedItem("pattern") != null)
			{
				resultStep = new OAReplaceStep( 	stepNode.getAttributes().getNamedItem("name").getNodeValue(),
												stepNode.getAttributes().getNamedItem("replacement").getNodeValue(),
						  					  	stepNode.getAttributes().getNamedItem("pattern").getNodeValue());
			}
			//otherwise get the value from the textValue of the node
			else
			{
				resultStep = new OAReplaceStep( 	stepNode.getAttributes().getNamedItem("name").getNodeValue(),
												stepNode.getAttributes().getNamedItem("replacement").getNodeValue(),
		  										stepNode.getFirstChild().getNodeValue());
			}
			
			//add the node to the hash map set its key as its position (step attribute)
			resultMap.put(Integer.parseInt(stepNode.getAttributes().getNamedItem("step").getNodeValue()),resultStep);		
		}
		
		//return the hash map containing all the Steps
		return resultMap;
	}
	
}
