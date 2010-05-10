package org.un.bungeni.translators.interfaces;

import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;
import org.un.bungeni.translators.odttoakn.steps.OAXSLTStep;
import org.un.bungeni.translators.odttoakn.steps.OAReplaceStep;

/**
 * This is the interface for the configuration reader. A configuration reader has a singleton
 * pattern and is used to read a specific configuration 
 */
public interface ConfigurationReader 
{	
	/**
	 * Used to get an HashMap containing all the INPUT XSLT Steps of the configuration with their position 
	 * as key. The input step are applied to the document before the resolution of its names according to the map 
	 * @return the HashMap containing all the Steps of the configuration
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAXSLTStep> getInputSteps() throws XPathExpressionException;

	/**
	 * Used to get an HashMap containing all the OUTPUT XSLT Steps of the configuration with their position 
	 * as key. The output step are applied to the document after the resolution of its names according to the map 
	 * @return the HashMap containing all the Steps of the configuration
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAXSLTStep> getOutputSteps() throws XPathExpressionException;

	/**
	 * Used to get an HashMap containing all the ReplaceStep of the configuration  
	 * @return the HashMap containing all the ReplaceSteps of the configuration
	 * @throws XPathExpressionException 
	 */
	public HashMap<Integer,OAReplaceStep> getReplaceSteps() throws XPathExpressionException;
}
