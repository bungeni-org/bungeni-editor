package org.un.bungeni.translators.utility.exceptionmanager.handlers;

/**
 *
 * @author UNDESA/Africa i-Parliaments Action Plan
 */

public class InvalidContent extends MissingAttribute{
    
    @Override
    public String getLocalizedMessage(String attribute, String element){
        String localizedMessage = "";
        localizedMessage = "There element  " +
                element +
                " has invalid content. Allowed content is :  " +
                attribute;
        return localizedMessage;
    }
    
}
