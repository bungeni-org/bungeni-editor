package org.un.bungeni.translators.interfaces;

/**
 * This is the interface for the map step object
 * A map step object is an object used to store each element of the mapping files
 */
public interface MapStep 
{
	/**
	 * This method is used to get the id attribute of a map Step
	 * @return the id attribute of a map Step
	 */
	public Integer getId();
	/**
	 * This method is used to get the bungeniSectionType attribute of a map Step
	 * @return the bungeniSectionType attribute of a map Step
	 */
	public String getBungeniSectionType();
	/**
	 * This method is used to get the result attribute of a map Step
	 * @return the result attribute of a map Step
	 */
	public String getResult();
}
