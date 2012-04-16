package org.bungeni.translators.utility.exceptionmanager.handlers;

import java.util.regex.Pattern;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;

/**
 * Handles exceptions with incomplete elements. Similar implementation to
 * <code>MissingAttribute</code>.
 *
 * @author murithi
 */
public class IncompleteElement extends BaseExceptionHandler {

    @Override
    public String getLocalizedMessage(String attribute, String element){
        //construct the string to return
        String localizedMessage = "";

        localizedMessage = resourceBundle.getString("PROBLEM_DESCRIPTION_LEFT")
                         + resourceBundle.getString("MISSING_ATTRIBUTE_LEFT_TEXT") + attribute
                         + resourceBundle.getString("MISSING_ATTRIBUTE_CENTER_TEXT") + element
                         + resourceBundle.getString("MISSING_ATTRIBUTE_RIGHT_TEXT");

        return localizedMessage;
    }

    public void processException(ValidationError validationError, Pattern pattern) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSectionMessage(ValidationError validationError) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
