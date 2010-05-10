 package org.un.bungeni.translators.odttoakn.map;

import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

import org.un.bungeni.translators.interfaces.Map;
import org.un.bungeni.translators.odttoakn.steps.OAMapStep;
import org.w3c.dom.Document;

/**
 * This is the map object. It is used to read a map, write a map and to create a new map
 */
public class OAMap implements Map
{
	//the map reader
	private OAMapReader reader;
		
	/**
	 * Create a new map based on a given map XML file
	 * @param aMapXML the XML Document in witch the map is written 
	 */
	public OAMap(Document aMapXML)
	{
		//create the reader
		this.reader = new OAMapReader(aMapXML);
	}
	
	/**
	 * Get an hash map containing all the steps of the xml map indexed by their id 
	 * @return an hash map containing all the steps of the xml map indexed by their id
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAMapStep> getMapSteps() throws XPathExpressionException
	{
		//ask the reader to get all the map steps
		HashMap<Integer,OAMapStep> resultStep = this.reader.getMapSteps();
		
		//return the gotten step 
		return resultStep;
	}
	
}
