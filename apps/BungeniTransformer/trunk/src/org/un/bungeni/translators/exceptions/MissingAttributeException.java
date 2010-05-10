package org.un.bungeni.translators.exceptions;

/**
 * This exception is raised whenever a SAXException 
 * is raised during a validation through a schema.
 * Specifically it is raised if an attribute was not found during the validation
 */
public class MissingAttributeException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	//get the resource bundle for the localized string
	private String message;

	/**
	 * The constructor of this exception
	 * @param aMessage the error message of the exception
	 * @param anElement the element in which the attribute is missing
	 */
	public MissingAttributeException(String aMessage)
	{
		//build the exception text
		super(aMessage);
		
		//save the exception text
		this.message = aMessage;
	}

	/**
	 * Return the error message of this exception
	 * @return the error message of this exception
	 */
	public String getMessage()
	{
	    return this.message;
	}

}
