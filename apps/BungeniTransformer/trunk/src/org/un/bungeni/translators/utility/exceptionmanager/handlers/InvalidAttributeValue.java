/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.un.bungeni.translators.utility.exceptionmanager.handlers;

import java.util.regex.Pattern;
import org.un.bungeni.translators.utility.exceptionmanager.ValidationError;

/**
 *
 * @author undesa
 */
public class InvalidAttributeValue extends BaseExceptionHandler{
    @Override
    public String getLocalizedMessage(String attribute, String element){
        String localizedMessage = null;
        localizedMessage = 
                resourceBundle.getString("INVALID_ATTRIBUTE_VALUE_LEFT") +
                attribute +
                resourceBundle.getString("INVALID_ATTRIBUTE_VALUE_CENTER") +
                element +
                resourceBundle.getString("INVALID_ATTRIBUTE_VALUE_RIGHT");
        return localizedMessage;
    }

    public void processException(ValidationError validationError, Pattern pattern) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSectionMessage(ValidationError validationError) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
