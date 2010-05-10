package org.un.bungeni.translators.odttoakn.steps;

import org.un.bungeni.translators.interfaces.XSLTStep;

/**
 * The Step Object is the basic unit of the translations. A configuration contains several steps
 * that are performed sequentially.
*/
public class OAXSLTStep implements XSLTStep 
{
	//the name of the step 
	private String name;
	//the href of the step 
	private String URI;
	//the position of the step 
	private Integer position;
	
	/**
	 * Create a new Step object with a given name, href and position.
	 * @param aName the name of the Step 
	 * @param aURI the href of the Step 
	 * @param aPosition the position of the Step 
	 */
	public OAXSLTStep(String aName, String aURI, Integer aPosition)
	{
		//set the name of the Step 
		this.name = aName;
		//set the URI of the Step
		this.URI = aURI;
		//set the Position of the step
		this.position = aPosition;
	}
	
	/**
	 * Used to get the name of the Step 
	 * @return the name of the Step
	 */
	public String getName()
	{
		//copy the name of the step and return it
		String aName = this.name;
		return aName;
	}

	/**
	 * Used to get the href of the Step 
	 * @return the href of the Step
	 */
	public String getHref()
	{
		//copy the href of the step and return it
		String aHref = this.URI;
		return aHref;
	}
	
	/**
	 * Used to get the position of the Step. The step of the configurations 
	 * are resolved performing all the Steps of the configurations. The step 
	 * with the lowest position number is performed firstly. 
	 * @return the position of the Step
	 */
	public Integer getPosition()
	{
		//copy the position of the step and return it
		Integer aPosition = this.position;
		return aPosition;
	}
}
