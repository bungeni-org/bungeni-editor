package org.bungeni.translators.utility.exceptionmanager.handlers;

import java.util.regex.Pattern;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;

/**
 *
 * @author UNDESA/Africa i-Parliaments Action Plan
 */

public class InvalidContent extends BaseExceptionHandler{
    
    @Override
    public String getLocalizedMessage(String attribute, String element){
        String localizedMessage = "";
        localizedMessage = "There element  " +
                element +
                " has invalid content. Allowed content is :  " +
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
