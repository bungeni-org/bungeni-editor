package org.bungeni.translators.utility.exceptionmanager.handlers;

import java.util.regex.Pattern;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;

/**
 *
 * @author murithi
 */
public class InvalidAttribute extends BaseExceptionHandler{
    
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

    public void processException(ValidationError validationError, Pattern pattern) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSectionMessage(ValidationError validationError) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
