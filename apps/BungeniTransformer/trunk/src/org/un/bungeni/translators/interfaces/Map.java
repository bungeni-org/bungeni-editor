package org.un.bungeni.translators.interfaces;

import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

import org.un.bungeni.translators.odttoakn.steps.OAMapStep;

/**
 * This is the interface for the map object. 
 * A map object is used to read a map, write a map and create a new map.
 */
public interface Map 
{
	/**
	 * Get an hash map containing all the steps of the xml map indexed by their id 
	 * @return an hash map containing all the steps of the xml map indexed by their id
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAMapStep> getMapSteps() throws XPathExpressionException;
}
