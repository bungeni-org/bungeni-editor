package org.un.bungeni.translators.odttoakn.map;

import java.util.HashMap;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.un.bungeni.translators.interfaces.MapReader;
import org.un.bungeni.translators.odttoakn.steps.OAMapStep;
import org.un.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  This is the map reader Object, 
 *  A map reader object is used to read a map xml and retrieve all te Map Steps of the map
 */
public class OAMapReader implements MapReader
{
	//the XML that contains the map
	private Document mapXML; 
	
	/**
	 * Create a new Map reader object builded on the given Map XML file 
	 * @param aMapXML the XML file that contains the map 
	 */
	public OAMapReader(Document aMapXML)
	{
		//save the map XML
		this.mapXML = aMapXML;
	}
		
	/**
	 * Return an HashMap containing all the step of the map indexed by their id 
	 * @return the HashMap containing all the step of the map indexed by their id
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer, OAMapStep> getMapSteps() throws XPathExpressionException
	{
		//the HashMap to return 
		HashMap<Integer,OAMapStep> resultMap = new HashMap<Integer,OAMapStep>();
		
		//retreive the XPath resolver instance 
		XPathResolver xresolver = XPathResolver.getInstance();
		
		//get the MapSteps with the given type in this configuration
		NodeList stepNodes = (NodeList)xresolver.evaluate(this.mapXML, "//element", XPathConstants.NODESET);
		
		//get all the Map steps and creates a  Map Step object for each one of them
		for (int i = 0; i < stepNodes.getLength(); i++) 
		{
			//get the step node
			Node stepNode = stepNodes.item(i);
						
			//create the Map Step 
			OAMapStep resultStep = new OAMapStep(Integer.parseInt(stepNode.getAttributes().getNamedItem("id").getNodeValue()),
 					 								 stepNode.getAttributes().getNamedItem("bungeniSectionType").getNodeValue(),
 					 								 stepNode.getAttributes().getNamedItem("result").getNodeValue());

			//add the node to the hash map set its key as its position (step attribute)
			resultMap.put(Integer.parseInt(stepNode.getAttributes().getNamedItem("id").getNodeValue()),resultStep);		
		}
		
		//return the hash map containing all the Steps
		return resultMap;	
	}
	
	/**
	 * Returns a String containing the path of the map resolver 
	 * @return a String containing the path of the map resolver 
	 * @throws XPathExpressionException
	 */
	public String getMapResolver() throws XPathExpressionException
	{
		//retreive the XPath resolver instance 
		XPathResolver xresolver = XPathResolver.getInstance();
		
		//get the MapSteps with the given type in this configuration
		String resolverLocation = (String)xresolver.evaluate(this.mapXML, "//mapresolver/@href", XPathConstants.STRING);
		
		//return the resolver location
		return resolverLocation;
	}
}
