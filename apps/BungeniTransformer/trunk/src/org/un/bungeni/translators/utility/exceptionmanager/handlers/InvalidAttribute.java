package org.un.bungeni.translators.utility.exceptionmanager.handlers;

/**
 *
 * @author murithi
 */
public class InvalidAttribute extends MissingAttribute{
    
    @Override
    public String getLocalizedMessage(String attribute, String element){
        String localizedMessage = null;
        
        localizedMessage = resourceBundle.getString("PROBLEM_DESCRIPTION_LEFT") +
                           resourceBundle.getString("INVALID_ATTRIBUTE_LEFT") +
                           element +
                           resourceBundle.getString("INVALID_ATTRIBUTE_CENTER") +
                           attribute;
        return localizedMessage;
    }
    
}
